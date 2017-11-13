package org.dfki.iot.attack.util;

import java.io.IOException;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.dfki.iot.attack.model.RoverClientA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtil {

	// Create Logger
	private static final Logger myLogger = LoggerFactory.getLogger(JSONUtil.class);

	public static void main(String[] args) {

		RoverClientA roverClientA1 = RoverClientA.getRandomRoverClientA();

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);

		try {
			System.out.println(mapper.writeValueAsString(roverClientA1));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			myLogger.info(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
