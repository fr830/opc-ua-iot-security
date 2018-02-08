package org.dfki.iot.attack.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.dfki.iot.attack.model.CreateSessionEventParam;
import org.dfki.iot.attack.model.EventModel;
import org.opcfoundation.ua.core.CreateSessionRequest;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogUtil {

	private static final Logger myLogger = LoggerFactory.getLogger(EventLogUtil.class);
	private static final File serverEventLogfile = new File("./src/main/resources/logs/event.log");
	private static final File clientEventLogfile = new File("./src/main/resources/logs/clientEvents.log ");

	public static void main(String[] args) {

		try {
			FileInputStream excelFile = new FileInputStream(new File("./src/main/resources/event.log"));
		} catch (FileNotFoundException e) {
			try {
				FileOutputStream outputStream = new FileOutputStream(new File("./src/main/resources/event.log"));

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
			myLogger.debug(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
		}

	}

	/**
	 * Method to convert Object to Json and write to a File.
	 * 
	 * @param eventModel
	 * 
	 */
	public static void writeToServerEventLog(EventModel eventModel) {

		try {

			if (!serverEventLogfile.exists()) {
				serverEventLogfile.createNewFile();
			}

			String text = JSONUtil.getJSONString(eventModel) + "\n";

			FileWriter writer = new FileWriter(serverEventLogfile, true);
			writer.write(text);
			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			try {

				serverEventLogfile.createNewFile();

				String text = JSONUtil.getJSONString(eventModel) + "\n";

				FileWriter writer = new FileWriter(serverEventLogfile, true);
				writer.write(text);
				writer.flush();
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sessionId
	 * @param authToken
	 * @param request
	 * @param response
	 * @return
	 */

	public static CreateSessionEventParam populateCreateSessionObject(Object sessionId, Object authToken,
			Object request, Object response) {
		CreateSessionEventParam createSessionEventParam = new CreateSessionEventParam();
		createSessionEventParam.setSessionId(sessionId.toString());
		createSessionEventParam.setAuthenToken(authToken.toString());
		if (request instanceof CreateSessionRequest) {
			createSessionEventParam.setClientApplicationName(
					((CreateSessionRequest) request).getClientDescription().getApplicationName().toString());
			createSessionEventParam
					.setClientMaxResponseTime(((CreateSessionRequest) request).getMaxResponseMessageSize());
			createSessionEventParam.setResRevisedTimeOut(((CreateSessionRequest) request).getRequestedSessionTimeout());
		}

		if (response instanceof CreateSessionResponse) {
			createSessionEventParam.setSessionResult(
					((CreateSessionResponse) response).getResponseHeader().getServiceResult().getDescription());

		}
		return createSessionEventParam;
	}

	public static void writeToClientEventLog(String clientName, String authenticationToken) {

		try {

			if (!clientEventLogfile.exists()) {
				clientEventLogfile.createNewFile();
			}

			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.createObjectNode();
			((ObjectNode) rootNode).put("clientName", clientName);
			((ObjectNode) rootNode).put("authenticationToken", authenticationToken);
			String text = JSONUtil.getJSONString(rootNode) + "\n";

			FileWriter writer = new FileWriter(clientEventLogfile,true);
			writer.write(text);
			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			try {

				clientEventLogfile.createNewFile();

				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.createObjectNode();
				((ObjectNode) rootNode).put("clientName", clientName);
				((ObjectNode) rootNode).put("authenticationToken", authenticationToken);
				String text = JSONUtil.getJSONString(rootNode) + "\n";

				FileWriter writer = new FileWriter(clientEventLogfile,true);
				writer.write(text);
				writer.flush();
				writer.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
