package org.dfki.iot.attack.util;

import java.util.HashMap;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class PiChartExample extends Application {

	@Override
	public void start(Stage stage) {

		HashMap<String, HashMap<String, Integer>> continentCountryMap = GenericUtil.extractIpDetailsFromLogs();

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