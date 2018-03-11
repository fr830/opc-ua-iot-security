package org.dfki.iot.attack.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.maxmind.geoip2.exception.GeoIp2Exception;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StatisticsTableExample extends Application {

	private TableView<KeyValue> table = new TableView<KeyValue>();
	private static int httpsRequestsCount = 0, tcpRequestsCount = 0;
	private final static ObservableList<KeyValue> data = FXCollections.observableArrayList();

	public static void main(String[] args) {

		HashMap<String, HashMap<String, Integer>> continentCountryMap = GenericUtil
				.extractContinentCountryMapFromLogs();

		HashMap<String, Integer> countryMap = new HashMap<String, Integer>();

		for (String continent : continentCountryMap.keySet()) {
			HashMap<String, Integer> exisitingCountry = continentCountryMap.get(continent);
			countryMap.putAll(exisitingCountry);
		}

		HashMap<String, Integer> ipRequestCountMap = GenericUtil.extractIpRequestCountMapFromLogs();

		int connectionCount = 0;
		for (String ip : ipRequestCountMap.keySet()) {
			connectionCount += ipRequestCountMap.get(ip);
		}
		getTCPHTTPSRequestCountsFromLog();
		
		data.add(new KeyValue("# unique IP Address ", "" + ipRequestCountMap.keySet().size()));
		data.add(new KeyValue("# total connections  ", "" + connectionCount));
		data.add(new KeyValue("# unique country ", "" + countryMap.keySet().size()));
		data.add(new KeyValue("# continent", "" + continentCountryMap.keySet().size()));
		data.add(new KeyValue("# tcp request", "" + tcpRequestsCount));
		data.add(new KeyValue("# https request", "" + httpsRequestsCount));

		launch(args);
	}

	public static void getTCPHTTPSRequestCountsFromLog() {

		final File folder = new File("./src/main/resources/logs");

		try {

			for (final File fileEntry : folder.listFiles()) {

				if (fileEntry.getName() != null && fileEntry.getName().contains("log4j")) {

					FileReader fileReader = new FileReader(fileEntry);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						if (line.contains("connected")) {
							if (line.contains(".HttpsServer")) {
								httpsRequestsCount++;
							} else {
								tcpRequestsCount++;
							}
						}
					}
					fileReader.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("Statistics");
		stage.setWidth(450);
		stage.setHeight(400);

		final Label label = new Label("Statistics");
		label.setFont(new Font("Arial", 20));

		TableColumn firstNameCol = new TableColumn("Details");
		firstNameCol.setMinWidth(150);
		firstNameCol.setCellValueFactory(new PropertyValueFactory<KeyValue, String>("key"));

		TableColumn lastNameCol = new TableColumn("Count");
		lastNameCol.setMinWidth(100);
		lastNameCol.setCellValueFactory(new PropertyValueFactory<KeyValue, String>("value"));

		table.setEditable(true);
		table.setMaxHeight(170.0);
		table.setItems(data);
		table.getColumns().addAll(firstNameCol, lastNameCol);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 40));
		vbox.getChildren().addAll(label, table);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.show();
	}

	public static class KeyValue {

		private final SimpleStringProperty key;
		private final SimpleStringProperty value;

		private KeyValue(String fName, String lName) {
			this.key = new SimpleStringProperty(fName);
			this.value = new SimpleStringProperty(lName);
		}

		public String getKey() {
			return key.get();
		}

		public String getValue() {
			return value.get();
		}

		public void setKey(String key) {
			this.key.set(key);
		}

		public void setValue(String value) {
			this.value.set(value);
		}

	}
}
