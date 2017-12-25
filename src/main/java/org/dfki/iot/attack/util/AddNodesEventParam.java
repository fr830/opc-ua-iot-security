package org.dfki.iot.attack.util;

import java.security.Timestamp;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddNodesEventParam {
	private static final Logger myLogger = LoggerFactory.getLogger(AddNodesEventParam.class);
	private String sessionId;
	private String authenToken;
	private String reqAuditId;

	public AddNodesEventParam(String sessionId, String authenToken, String reqAuditId) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.reqAuditId = reqAuditId;
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

	public String getReqAuditId() {
		return reqAuditId;
	}

	public void setReqAuditId(String reqAuditId) {
		this.reqAuditId = reqAuditId;
	}

}
