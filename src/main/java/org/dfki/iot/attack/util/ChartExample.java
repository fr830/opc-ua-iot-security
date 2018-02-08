package org.dfki.iot.attack.util;

import java.util.HashMap;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ChartExample {

	public static class PiChartExample extends Application {

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
				if (i < 10) {
					pieChartData.add(new PieChart.Data(countryName, countryMap.get(countryName)));
					i++;
				} else {
					break;
				}
			}

			PieChart chart = new PieChart(pieChartData);
			chart.setTitle("Top 10 country");
			// chart.setLabelLineLength(150);
			chart.setLabelsVisible(false);
			chart.setLegendSide(Side.LEFT);

			Scene scene = new Scene(new Group());
			((Group) scene.getRoot()).getChildren().add(chart);

			stage.setTitle("Top 10 country hits");
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
			stage.setTitle("# Reqests based on continent");
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);

			bc.setTitle("# Reqests based on continent");
			xAxis.setLabel("Country");
			yAxis.setLabel("# Number of Requests");

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
				bc.getData().add(series);

			}

			Scene scene = new Scene(bc);
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
			stage.setTitle("Maximum # Reqests based by user (per IP address)");
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);

			bc.setTitle("Maximum # Reqests based by user (per IP address)");
			xAxis.setLabel("IP Address");
			yAxis.setLabel("# Number of Requests/IPAddress");

			HashMap<String, Integer> ipRequestCountMap = GenericUtil.extractIpRequestCountMapFromLogs();
			ipRequestCountMap = (HashMap<String, Integer>) GenericUtil.sortByValue(ipRequestCountMap);

			XYChart.Series series = new XYChart.Series();

			Set<String> ipList = ipRequestCountMap.keySet();
			int i = 0;
			for (String ip : ipList) {
				if (i < 10) {
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
}
