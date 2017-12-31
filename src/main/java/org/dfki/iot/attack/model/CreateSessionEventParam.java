package org.dfki.iot.attack.model;

import java.security.Timestamp;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author sriharsha
 *
 */
public class CreateSessionEventParam {
	private static final Logger myLogger = LoggerFactory.getLogger(CreateSessionEventParam.class);
	private String sessionId;
	private String authenToken;
	private String sessionName;
	private String clientApplicationName;
	private UnsignedInteger clientMaxResponseTime;
	private Double clientRequestSessionTimeOut;
	private Timestamp resTimeStamp;
	private String sessionResult;
	private Double resRevisedTimeOut;

	public CreateSessionEventParam(String sessionId, String authenToken, String sessionName,
			String clientApplicationName, UnsignedInteger clientMaxResponseTime, Double clientRequestSessionTimeOut,
			Timestamp resTimeStamp, String sessionResult, Double resRevisedTimeOut) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.sessionName = sessionName;
		this.clientApplicationName = clientApplicationName;
		this.clientMaxResponseTime = clientMaxResponseTime;
		this.clientRequestSessionTimeOut = clientRequestSessionTimeOut;
		this.resTimeStamp = resTimeStamp;
		this.sessionResult = sessionResult;
		this.resRevisedTimeOut = resRevisedTimeOut;
	}

	public CreateSessionEventParam() {
		
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAuthenToken() {
		return authenToken;
	}

	public void setAuthenToken(String authenToken) {
		this.authenToken = authenToken;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getClientApplicationName() {
		return clientApplicationName;
	}

	public void setClientApplicationName(String clientApplicationName) {
		this.clientApplicationName = clientApplicationName;
	}

	public UnsignedInteger getClientMaxResponseTime() {
		return clientMaxResponseTime;
	}

	public void setClientMaxResponseTime(UnsignedInteger clientMaxResponseTime) {
		this.clientMaxResponseTime = clientMaxResponseTime;
	}

	public Double getClientRequestSessionTimeOut() {
		return clientRequestSessionTimeOut;
	}

	public void setClientRequestSessionTimeOut(Double clientRequestSessionTimeOut) {
		this.clientRequestSessionTimeOut = clientRequestSessionTimeOut;
	}

	public Timestamp getResTimeStamp() {
		return resTimeStamp;
	}

	public void setResTimeStamp(Timestamp resTimeStamp) {
		this.resTimeStamp = resTimeStamp;
	}

	public String getSessionResult() {
		return sessionResult;
	}

	public void setSessionResult(String sessionResult) {
		this.sessionResult = sessionResult;
	}

	public Double getResRevisedTimeOut() {
		return resRevisedTimeOut;
	}

	public void setResRevisedTimeOut(Double resRevisedTimeOut) {
		this.resRevisedTimeOut = resRevisedTimeOut;
	}

	

}
