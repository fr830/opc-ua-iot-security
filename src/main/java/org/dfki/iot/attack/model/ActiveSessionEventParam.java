package org.dfki.iot.attack.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author sriharsha
 *
 */
public class ActiveSessionEventParam {
	private static final Logger myLogger = LoggerFactory.getLogger(ActiveSessionEventParam.class);
	private String sessionId;
	private String authenToken;
	private String userName;

	
	
	public ActiveSessionEventParam() {
		super();
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ActiveSessionEventParam(String sessionId, String authenToken, String userName) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.userName = userName;
	}

}
