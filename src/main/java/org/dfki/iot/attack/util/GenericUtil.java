package org.dfki.iot.attack.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericUtil {

	private static final Logger myLogger = LoggerFactory.getLogger(GenericUtil.class);

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
				myLogger.info("Sorry, unable to find " + filename);
				return null;
			}

			prop.load(input);
			myLogger.info("Property [ " + propertyName + " ] retreieved successfully");

			return prop.getProperty(propertyName);

		} catch (IOException ex) {
			myLogger.info(
					"\n LocalizedMessage : " + ex.getLocalizedMessage() + "\n  		 Message :: " + ex.getMessage()
							+ "\n toString :: " + ex.toString() + "\n:		 StackTrace :: " + ex.getStackTrace());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					myLogger.info("\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: "
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

	public static String getCurrentMachineIpAddress() throws UnknownHostException {

		InetAddress ip = InetAddress.getLocalHost();

		myLogger.info("Current machine IP address : " + ip.getHostAddress());

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

		myLogger.info("Current machine MAC address : " + sb.toString());

		return sb.toString();
	}

}
