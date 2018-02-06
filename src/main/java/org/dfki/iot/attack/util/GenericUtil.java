package org.dfki.iot.attack.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
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

	public static void main(String[] args) throws UnknownHostException, SocketException {

		myLogger.info("To Get Temporary File Location : " + System.getProperty("java.io.tmpdir"));

		String current;
		try {
			current = new java.io.File(".").getCanonicalPath();
			myLogger.info("To Get Current dir :" + current);
		} catch (IOException e) {

			myLogger.info(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());

		}

		String currentDir = System.getProperty("user.dir");
		myLogger.info("To Current dir using System:" + currentDir);

		// To Create a folder(directory) in current working directory using java
		new File(System.getProperty("user.dir") + "/folder").mkdir();

		getCurrentMachineIpAddress();
		getCurrentMachineMACAddress();

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

		DatabaseReader dbReader = new DatabaseReader.Builder(cityDatabase).build();

		InetAddress inetAddress = InetAddress.getByName(ipAddress);
		CityResponse cityResponse = dbReader.city(inetAddress);

		String countryName = cityResponse.getCountry().getName();
		String continent = cityResponse.getContinent().getName();
		String cityName = cityResponse.getCity().getName();
		Double latitude = cityResponse.getLocation().getLatitude();
		Double longitude = cityResponse.getLocation().getLongitude();

		CountryModel countryModel = new CountryModel(countryName, continent, cityName, latitude, longitude);

		return countryModel;

	}

	public static void generateCharts() {

	}
	
	

}
