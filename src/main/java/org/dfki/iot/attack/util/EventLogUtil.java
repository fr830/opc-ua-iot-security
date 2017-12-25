package org.dfki.iot.attack.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.codehaus.jackson.map.util.JSONPObject;
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
	 * Method to write parameters sessionId, AuthenticationTokenId and extract
	 * parameters from CreateSession Request and Response for security purpose Logging.
	 */
	public static void writeSessionLog(Object sessionId, Object authToken, Object request,Object response ) {
		JSONObject jsonObject=new JSONObject();
		try {
			
			File file =new File("./src/main/resources/event.log"); 
			if (file.exists()) {
			FileWriter writer = new FileWriter(file,true);
             CreateSessionEventParam populateCreateSessionObject = populateCreateSessionObject(sessionId, authToken, request, response);
             jsonObject.put("createSession", (JSONUtil.getJSONString(populateCreateSessionObject)));
		      writer.write(jsonObject.toString());
			writer.flush();
			 writer.close();
			}else{
				//File file = new File("./src/main/resources/event.log");
			      file.createNewFile();
			      FileWriter writer = new FileWriter(file); 
			      CreateSessionEventParam populateCreateSessionObject = populateCreateSessionObject(sessionId, authToken, request, response);
			      jsonObject.put("createSession", JSONUtil.getJSONString(populateCreateSessionObject));
			      writer.write(jsonObject.toString());
			      writer.flush();
			      writer.close();
				
			}
		} catch (FileNotFoundException e) {
			try {
				 File file = new File("./src/main/resources/event.log");
			      file.createNewFile();
			      FileWriter writer = new FileWriter(file); 
			      CreateSessionEventParam populateCreateSessionObject = populateCreateSessionObject(sessionId, authToken, request, response);
			      jsonObject.put("createSession", JSONUtil.getJSONString(populateCreateSessionObject));
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
	
	private static CreateSessionEventParam populateCreateSessionObject(Object sessionId, Object authToken, Object request,
			Object response) {
		CreateSessionEventParam createSessionEventParam=new CreateSessionEventParam();
		 createSessionEventParam.setSessionId(sessionId.toString());
		 createSessionEventParam.setAuthenToken(authToken.toString());
		 if (request instanceof CreateSessionRequest){
		 createSessionEventParam.setClientApplicationName(((CreateSessionRequest)request).getClientDescription().getApplicationName().toString());
		 createSessionEventParam.setClientMaxResponseTime(((CreateSessionRequest)request).getMaxResponseMessageSize());
		 createSessionEventParam.setResRevisedTimeOut(((CreateSessionRequest)request).getRequestedSessionTimeout());
		 }
		 
		 if(response instanceof CreateSessionResponse){
			 createSessionEventParam.setSessionResult(((CreateSessionResponse)response).getResponseHeader().getServiceResult().getDescription());
			 
		 }
		 return createSessionEventParam;
	}

}
