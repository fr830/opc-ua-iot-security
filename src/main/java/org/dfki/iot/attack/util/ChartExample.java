package org.dfki.iot.attack.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ChartExample {

	private static final Logger myLogger = LoggerFactory.getLogger(ChartExample.class);

	public static class CountryPiChart extends Application {

		@Override
		public void start(Stage stage) {

			HashMap<String, HashMap<String, Integer>> continentCountryMap = GenericUtil
					.extractContinentCountryMapFromLogs();

			HashMap<String, Integer> countryMap = new HashMap<String, Integer>();

			for (String continent : continentCountryMap.keySet()) {
				HashMap<String, Integer> exisitingCountry = continentCountryMap.get(continent);
				countryMap.putAll(exisitingCountry);
			}

			countryMap = (HashMap<String, Integer>) GenericUtil.sortByValue(countryMap);

			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

			Set<String> countryList = countryMap.keySet();
			int i = 0;
			for (String countryName : countryList) {
				if (i < 15) {
					// System.out.println(countryName + "," +
					// countryMap.get(countryName));
					pieChartData.add(new PieChart.Data(countryName, countryMap.get(countryName)));
					i++;
				} else {
					break;
				}
			}

			PieChart chart = new PieChart(pieChartData);
			chart.setTitle("Top 15 countries");
			chart.setLabelLineLength(50);
			chart.setLabelsVisible(true);
			chart.setLegendSide(Side.LEFT);
			// chart.autosize();
			chart.setPrefHeight(700.0);
			chart.setPrefWidth(700.0);
			chart.setPadding(new Insets(10, 0, 0, 40));

			Scene scene = new Scene(new Group());
			((Group) scene.getRoot()).getChildren().add(chart);

			stage.setTitle("Top 15 countries");
			// stage.setWidth(500);
			// stage.setHeight(500);
			// stage.setFullScreen(true);
			stage.setScene(scene);
			stage.show();

		}

		public static void main(String[] args) {
			launch(args);
		}
	}

	public static class ContinentBarChart extends Application {

		@Override
		public void start(Stage stage) {
			stage.setTitle("Numebr of reqests based on continent");

			Font font = Font.font(null, FontWeight.BOLD, 15);

			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setTickLabelFont(font);
			xAxis.setLabel("Countries");

			NumberAxis yAxis = new NumberAxis();
			yAxis.setTickLabelFont(font);
			yAxis.setLabel("Number of requests");

			final BarChart<String, Number> chart = new BarChart<String, Number>(xAxis, yAxis);
			chart.setBarGap(-30);
			chart.setTitle("Number of reqests based on continent");

			HashMap<String, HashMap<String, Integer>> continentCountryMap = GenericUtil
					.extractContinentCountryMapFromLogs();

			for (String continent : continentCountryMap.keySet()) {
				HashMap<String, Integer> exisitingCountry = continentCountryMap.get(continent);
				exisitingCountry = (HashMap<String, Integer>) GenericUtil.sortByValue(exisitingCountry);

				XYChart.Series series = new XYChart.Series();
				series.setName(continent);

				Set<String> countryList = exisitingCountry.keySet();
				int i = 0;
				for (String countryName : countryList) {
					if (i < 10) {
						series.getData().add(new XYChart.Data(countryName, exisitingCountry.get(countryName)));
						i++;
					} else {
						break;
					}
				}
				chart.getData().add(series);

			}

			Scene scene = new Scene(chart);
			stage.setScene(scene);
			stage.show();
		}

		public static void main(String[] args) {
			launch(args);
		}
	}

	public static class IpRequestBarChart extends Application {

		@Override
		public void start(Stage stage) {
			stage.setTitle("Number requests per user (or per IP address)");
			Font font = Font.font(null, FontWeight.BOLD, 15);

			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setTickLabelFont(font);
			// xAxis.setLabel("IP Address");

			final NumberAxis yAxis = new NumberAxis();
			yAxis.setTickLabelFont(font);
			yAxis.setLabel("Number of requests");

			final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
			bc.setTitle("Number requests per user (or per IP address)");

			HashMap<String, Integer> ipRequestCountMap = GenericUtil.extractIpRequestCountMapFromLogs();
			ipRequestCountMap = (HashMap<String, Integer>) GenericUtil.sortByValue(ipRequestCountMap);

			XYChart.Series series = new XYChart.Series();
			series.setName("IP Address");

			Set<String> ipList = ipRequestCountMap.keySet();
			int i = 0;
			for (String ip : ipList) {
				if (i < 15) {
					series.getData().add(new XYChart.Data(ip, ipRequestCountMap.get(ip)));
					i++;
				} else {
					break;
				}
			}
			bc.getData().addAll(series);

			Scene scene = new Scene(bc);
			stage.setScene(scene);
			stage.show();
		}

		public static void main(String[] args) {
			launch(args);
		}
	}

	public static class RequestPerDayLineChart extends Application {

		@Override
		public void start(Stage stage) throws ParseException {
			stage.setTitle("Number of requests per day");
			// defining the axes
			final CategoryAxis xAxis = new CategoryAxis();
			xAxis.setTickLabelRotation(90);
			final NumberAxis yAxis = new NumberAxis();
			// xAxis.setLabel("Days of Month");
			// creating the chart
			final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

			lineChart.setTitle("Number of requests per day");
			// defining a series
			XYChart.Series series = new XYChart.Series();
			series.setName("Days of Month");
			// populating the series with data

			DateFormat df = new SimpleDateFormat("dd-MMM-yy");

			HashMap<Date, Integer> value = GenericUtil.getNumberOfRequestPerDayFromLogs();
			value = (HashMap<Date, Integer>) GenericUtil.sortByDateKey(value);

			for (Date date : value.keySet()) {

				String dateStringParse = df.format(date);
				series.getData().add(new XYChart.Data(dateStringParse, value.get(date)));
			}

			lineChart.getData().add(series);

			Scene scene = new Scene(lineChart, 800, 600);
			stage.setScene(scene);
			stage.show();
		}

		public void main(String[] args) {
			launch(args);
		}
	}

	public static void generateCharts(String chartName) {

		if (chartName.equalsIgnoreCase("CountryPiChart")) {
			ChartExample.CountryPiChart piExample = new ChartExample.CountryPiChart();
			piExample.main(null);
		} else if (chartName.equalsIgnoreCase("ContinentBarChart")) {
			ChartExample.ContinentBarChart continentBarChart = new ChartExample.ContinentBarChart();
			continentBarChart.main(null);
		} else if (chartName.equalsIgnoreCase("IpRequestBarChart")) {
			ChartExample.IpRequestBarChart ipRequestBarChart = new ChartExample.IpRequestBarChart();
			ipRequestBarChart.main(null);
		} else if (chartName.equalsIgnoreCase("RequestPerDayLineChart")) {
			ChartExample.RequestPerDayLineChart RequestPerDayLineChart = new ChartExample.RequestPerDayLineChart();
			RequestPerDayLineChart.main(null);
		} else {
			myLogger.error(
					"Usage: ChartExample [ Please provide one of the argument (CountryPiChart, ContinentBarChart, IpRequestBarChart, RequestPerDayLineChart)  ]");
		}

	}

	public static void main(String[] args) {
		if (args.length > 0 && null != args[0]) {
			generateCharts(args[0]);
		} else {
			myLogger.error(
					"Usage: ChartExample [ Please provide the argument (CountryPiChart, ContinentBarChart, IpRequestBarChart)  ]");
		}
	}
}
