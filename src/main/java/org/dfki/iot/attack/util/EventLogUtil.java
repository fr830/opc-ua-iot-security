package org.dfki.iot.attack.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.codehaus.jackson.map.util.JSONPObject;
import org.dfki.iot.attack.model.ActiveSessionEventParam;
import org.dfki.iot.attack.model.AddNodesEventParam;
import org.dfki.iot.attack.model.CreateSessionEventParam;
import org.dfki.iot.attack.model.ReadNodeEventParam;
import org.dfki.iot.attack.model.RoverAModel;
import org.hamcrest.core.IsInstanceOf;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opcfoundation.ua.core.CreateSessionRequest;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogUtil {
	private static final Logger myLogger = LoggerFactory.getLogger(EventLogUtil.class);
 private static final	File serverEventLogfile = new File("./src/main/resources/event.log");
	public static void main(String[] args) {

		try {
			FileInputStream excelFile = new FileInputStream(new File("./src/main/resources/event.log"));
		} catch (FileNotFoundException e) {
			try {
				FileOutputStream outputStream = new FileOutputStream(new File("./src/main/resources/event.log"));

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param sessionId
	 * @param authToken
	 * @param request
	 * @param response
	 *            Method to write parameters sessionId, AuthenticationTokenId
	 *            and extract parameters from CreateSession Request and Response
	 *            for security purpose Logging.
	 */
	public static void writeSessionLog(Object sessionId, Object authToken, Object request, Object response) {

		CreateSessionEventParam populateCreateSessionObject = populateCreateSessionObject(sessionId, authToken, request,
				response);
		writeToEventFile("createSession", populateCreateSessionObject);

	}

	/**
	 * Method to convert Object to Json and write to a File.
	 * 
	 * @param auditString
	 * @param populateCreateSessionObject
	 */
	private static void writeToEventFile(String auditString, Object populateCreateSessionObject) {
		JSONObject jsonObject = new JSONObject();
		try {

		//	File file = new File("./src/main/resources/event.log");
			if (serverEventLogfile.exists()) {
				FileWriter writer = new FileWriter(serverEventLogfile, true);

				jsonObject.put(auditString, (JSONUtil.getJSONString(populateCreateSessionObject)));
				writer.write(jsonObject.toString());
				writer.flush();
				writer.close();
			} else {
				serverEventLogfile.createNewFile();
				FileWriter writer = new FileWriter(serverEventLogfile);
				jsonObject.put(auditString, JSONUtil.getJSONString(populateCreateSessionObject));
				writer.write(jsonObject.toString());
				writer.flush();
				writer.close();

			}
		} catch (FileNotFoundException e) {
			try {
			//	File file = new File("./src/main/resources/event.log");
				serverEventLogfile.createNewFile();
				FileWriter writer = new FileWriter(serverEventLogfile);
				jsonObject.put(auditString, JSONUtil.getJSONString(populateCreateSessionObject));
				writer.write(jsonObject.toString());
				writer.flush();
				writer.close();
			} catch (IOException e1) {

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

	private static CreateSessionEventParam populateCreateSessionObject(Object sessionId, Object authToken,
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

	/**
	 * Method to write onActiveSession Event parameters such as userName,
	 * Session and Authentication Token to Event log.
	 * 
	 * @param activeSessionEventParam
	 */
	public static void writeActiveSessionLog(ActiveSessionEventParam activeSessionEventParam) {
		writeToEventFile("activeSession", activeSessionEventParam);
	}
	/**
	 * 
	 * @param addNodesEventParam
	 */

	public static void writeAddNodesLog(AddNodesEventParam addNodesEventParam , String str) {
		writeToEventFile(str, addNodesEventParam);
		
	}

	public static void writeReadLog(ReadNodeEventParam readEventParam) {
		writeToEventFile("ReadNode", readEventParam);
		
	}

	public static void writeClientEventLog(String clientName, Object obj){
		JSONObject jsonObject = new JSONObject();
		String fileName="./src/main/resources/"+clientName+".log" ; 
		File file = new File(fileName);
		try {
              
			if (file.exists()) {
				FileWriter writer = new FileWriter(file, true);

				jsonObject.put(clientName, (JSONUtil.getJSONString(obj)));
				writer.write(jsonObject.toString());
				writer.flush();
				writer.close();
			} else {
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				jsonObject.put(clientName, JSONUtil.getJSONString(obj));
				writer.write(jsonObject.toString());
				writer.flush();
				writer.close();

			}
		} catch (FileNotFoundException e) {
			try {
			//	File file = new File("./src/main/resources/event.log");
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				jsonObject.put(clientName, JSONUtil.getJSONString(obj));
				writer.write(jsonObject.toString());
				writer.flush();
				writer.close();
			} catch (IOException e1) {

			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
