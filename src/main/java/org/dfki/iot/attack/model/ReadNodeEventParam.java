package org.dfki.iot.attack.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadNodeEventParam {
	private static final Logger myLogger = LoggerFactory.getLogger(ReadNodeEventParam.class);
	private String sessionId;
	private String authenToken;
	private String auditId;

	public ReadNodeEventParam(String sessionId, String authenToken, String auditId) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.auditId = auditId;
	}

	public ReadNodeEventParam() {
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

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	

}
