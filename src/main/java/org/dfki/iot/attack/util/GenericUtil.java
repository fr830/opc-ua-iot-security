package org.dfki.iot.attack.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.dfki.iot.attack.model.CountryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class GenericUtil {

	private static final Logger myLogger = LoggerFactory.getLogger(GenericUtil.class);
	private static final File cityDatabase = new File("./src/main/resources/GeoLite2-City.mmdb");

	public static void main(String[] args) throws UnknownHostException,
			SocketException {/*
								 * 
								 * myLogger.
								 * info("To Get Temporary File Location : " +
								 * System.getProperty("java.io.tmpdir"));
								 * 
								 * String current; try { current = new
								 * java.io.File(".").getCanonicalPath();
								 * myLogger.info("To Get Current dir :" +
								 * current); } catch (IOException e) {
								 * 
								 * myLogger.info( "\n LocalizedMessage : " +
								 * e.getLocalizedMessage() +
								 * "\n  		 Message :: " + e.getMessage() +
								 * "\n toString :: " + e.toString() +
								 * "\n:		 StackTrace :: " +
								 * e.getStackTrace());
								 * 
								 * }
								 * 
								 * String currentDir =
								 * System.getProperty("user.dir");
								 * myLogger.info("To Current dir using System:"
								 * + currentDir);
								 * 
								 * // To Create a folder(directory) in current
								 * working directory using java new
								 * File(System.getProperty("user.dir") +
								 * "/folder").mkdir();
								 * 
								 * getCurrentMachineIpAddress();
								 * getCurrentMachineMACAddress();
								 * 
								 */

		// HashMap<String, HashMap<String, Integer>> continentCountryMap =
		// extractContinentCountryMapFromLogs();
		// HashMap<String, Integer> ipRequestCountMap =
		// extractIpRequestCountMapFromLogs();
		generateCharts();

	}

	public static String readPropertyFile(String filename, String propertyName) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = GenericUtil.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				myLogger.debug("Sorry, unable to find " + filename);
				return null;
			}

			prop.load(input);
			myLogger.debug("Property [ " + propertyName + " ] retreieved successfully");

			return prop.getProperty(propertyName);

		} catch (IOException ex) {
			myLogger.debug(
					"\n LocalizedMessage : " + ex.getLocalizedMessage() + "\n  		 Message :: " + ex.getMessage()
							+ "\n toString :: " + ex.toString() + "\n:		 StackTrace :: " + ex.getStackTrace());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					myLogger.debug("\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: "
							+ e.getMessage() + "\n toString :: " + e.toString() + "\n:		 StackTrace :: "
							+ e.getStackTrace());
				}
			}
		}
		return null;

	}

	public static String readClientPropertyConfigFile(String propertyName) {
		String filename = "config.properties";
		return readPropertyFile(filename, propertyName);

	}

	public static String readServerPropertyConfigFile(String propertyName) {
		String filename = "serverconfig.properties";
		return readPropertyFile(filename, propertyName);
	}

	public static boolean enableAuditing(String operationName) {
		String operations = readServerPropertyConfigFile("monitor.operations");
		if (null != operations && operations.contains(operationName)) {
			return true;
		}
		return false;
	}

	public static String getCurrentMachineIpAddress() throws UnknownHostException {

		InetAddress ip = InetAddress.getLocalHost();

		myLogger.debug("Current machine IP address : " + ip.getHostAddress());

		return ip.getHostAddress();

	}

	public static String getCurrentMachineMACAddress() throws UnknownHostException, SocketException {

		InetAddress ip = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);

		byte[] mac = network.getHardwareAddress();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}

		myLogger.debug("Current machine MAC address : " + sb.toString());

		return sb.toString();
	}

	public static CountryModel getCountryDetails(String ipAddress) throws IOException, GeoIp2Exception {

		/*
		 * String isLocalEnvironment =
		 * GenericUtil.readClientPropertyConfigFile("isLocalEnvironment");
		 * 
		 * if ("true".equals(isLocalEnvironment)) {
		 * 
		 * return new CountryModel("Germany", "EU", "KL", 20.1, 20.2);
		 * 
		 * } else {
		 */

		DatabaseReader dbReader = new DatabaseReader.Builder(cityDatabase).build();

		InetAddress inetAddress = InetAddress.getByName(ipAddress);
		CityResponse cityResponse = dbReader.city(inetAddress);

		String countryName = cityResponse.getCountry().getName();
		String continent = cityResponse.getContinent().getName();
		String cityName = cityResponse.getCity().getName();
		Double latitude = cityResponse.getLocation().getLatitude();
		Double longitude = cityResponse.getLocation().getLongitude();

		return new CountryModel(countryName, continent, cityName, latitude, longitude);

		/* } */
	}

	public static HashMap<String, Integer> addToIpRequestCountMap(String ipAddress,
			HashMap<String, Integer> ipRequestCountMap) throws IOException, GeoIp2Exception {

		if (ipRequestCountMap.containsKey(ipAddress)) {
			Integer exisitingCountyCount = ipRequestCountMap.get(ipAddress);
			ipRequestCountMap.put(ipAddress, ++exisitingCountyCount);
			return ipRequestCountMap;
		} else {
			// when there is a new ip found
			ipRequestCountMap.put(ipAddress, 1);
			return ipRequestCountMap;
		}
	}

	public static HashMap<String, HashMap<String, Integer>> addToContinentCountryMap(String ipAddress,
			HashMap<String, HashMap<String, Integer>> continentCountryMap) throws IOException, GeoIp2Exception {

		CountryModel newCountryModel = GenericUtil.getCountryDetails(ipAddress);
		String countryName = newCountryModel.getCountryName();
		String continent = newCountryModel.getContinent();

		if (continentCountryMap.containsKey(continent)) {
			HashMap<String, Integer> exisitingCountryMap = continentCountryMap.get(continent);
			if (exisitingCountryMap.containsKey(countryName)) {
				Integer exisitingCountyCount = exisitingCountryMap.get(countryName);
				exisitingCountryMap.put(countryName, ++exisitingCountyCount);
				continentCountryMap.put(continent, exisitingCountryMap);
				return continentCountryMap;
			} else {
				// when there is a new country ip found
				exisitingCountryMap.put(countryName, 1);
				continentCountryMap.put(continent, exisitingCountryMap);
				return continentCountryMap;
			}
		} else {
			HashMap<String, Integer> newCountryMap = new HashMap<String, Integer>();
			newCountryMap.put(countryName, 1);
			continentCountryMap.put(continent, newCountryMap);
			return continentCountryMap;
		}
	}

	static HashMap<String, Integer> extractIpRequestCountMapFromLogs() {

		HashMap<String, Integer> ipRequestCountMap = new HashMap<String, Integer>();

		final File folder = new File("./src/main/resources/logs");

		try {

			for (final File fileEntry : folder.listFiles()) {

				if (fileEntry.getName() != null && fileEntry.getName().contains("log4j")) {

					FileReader fileReader = new FileReader(fileEntry);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						if (line.contains("connected")) {
							String ipAddress = null;
							if (line.contains(".HttpsServer")) {
								String[] value = line.split("<-> /");
								String[] ip = value[1].split(" context=");
								ipAddress = ip[0];
							} else {

								String[] value = line.split(": /");
								String[] ip = value[1].split(":");
								ipAddress = ip[0];
							}
							try {
								addToIpRequestCountMap(ipAddress, ipRequestCountMap);
							} catch (GeoIp2Exception e) {
								e.printStackTrace();
							}
						}
					}
					fileReader.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(ipRequestCountMap);
		return ipRequestCountMap;

	}

	public static HashMap<String, HashMap<String, Integer>> extractContinentCountryMapFromLogs() {

		HashMap<String, HashMap<String, Integer>> continentCountryMap = new HashMap<String, HashMap<String, Integer>>();

		final File folder = new File("./src/main/resources/logs");

		try {

			for (final File fileEntry : folder.listFiles()) {

				if (fileEntry.getName() != null && fileEntry.getName().contains("log4j")) {

					FileReader fileReader = new FileReader(fileEntry);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						if (line.contains("connected")) {
							String ipAddress = null;
							if (line.contains(".HttpsServer")) {
								String[] value = line.split("<-> /");
								String[] ip = value[1].split(" context=");
								ipAddress = ip[0];
							} else {

								String[] value = line.split(": /");
								String[] ip = value[1].split(":");
								ipAddress = ip[0];
							}
							try {
								addToContinentCountryMap(ipAddress, continentCountryMap);
							} catch (GeoIp2Exception e) {
								e.printStackTrace();
							}
						}
					}
					fileReader.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(continentCountryMap);
		return continentCountryMap;

	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Collections.reverse(list);

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void generateCharts() {

		/*ChartExample.PiChartExample piExample = new ChartExample.PiChartExample();
		piExample.main(null);

		ChartExample.ContinentBarChart continentBarChart = new ChartExample.ContinentBarChart();
		continentBarChart.main(null);*/

		ChartExample.IpRequestBarChart ipRequestBarChart = new ChartExample.IpRequestBarChart();
		ipRequestBarChart.main(null);

	}

}
