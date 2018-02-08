package org.dfki.iot.attack.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.dfki.iot.attack.model.RoverAModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

public class JSONUtil {

	// Create Logger
	private static final Logger myLogger = LoggerFactory.getLogger(JSONUtil.class);

	public static String getJSONString(Object object) {

		ObjectMapper mapper = new ObjectMapper();

		if ("true".equals(GenericUtil.readClientPropertyConfigFile("jsonPrettyPrint"))) {
			mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		}

		String text = null;
		try {
			text = mapper.writeValueAsString(object);
			// myLogger.info("JSON String : " + text);
		} catch (JsonGenerationException e) {
			myLogger.debug(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;

	}

	public static String getJSONString(Object object,boolean jsonPrettyPrint) {

		ObjectMapper mapper = new ObjectMapper();

		if (jsonPrettyPrint) {
			mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		}

		String text = null;
		try {
			text = mapper.writeValueAsString(object);
			// myLogger.info("JSON String : " + text);
		} catch (JsonGenerationException e) {
			myLogger.debug(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;

	}
	
	public static Object getObjFromJSONString(String jsonString, Object objClass) {
		ObjectMapper mapper = new ObjectMapper();

		Object myObject = null;
		try {
			myObject = mapper.readValue(jsonString, objClass.getClass());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			/*
			 * myLogger.info( "\n LocalizedMessage : " + e.getLocalizedMessage()
			 * + "\n  		 Message :: " + e.getMessage() + "\n toString :: " +
			 * e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
			 */
		}
		return myObject;

	}

	public static Map<String, HashMap<String, Integer>> getContinentCountryMapFromJSONString(String jsonString) {

		Map<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>();

		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

		try {
			map = mapper.readValue(jsonString, new TypeReference<HashMap<String, HashMap<String, Integer>>>() {
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			/*
			 * myLogger.info( "\n LocalizedMessage : " + e.getLocalizedMessage()
			 * + "\n  		 Message :: " + e.getMessage() + "\n toString :: " +
			 * e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
			 */
		}
		return map;

	}

	public static void main(String[] args) {

		RoverAModel roverAmodel = RoverAModel.getRandomRoverClientA();
		RoverAModel resRoverAModel = (RoverAModel) getObjFromJSONString(getJSONString(roverAmodel), new RoverAModel());
		// System.out.println(resRoverAModel);

	}

}
