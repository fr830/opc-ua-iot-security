package org.dfki.iot.attack.model;

import java.net.SocketAddress;

public class EventParamModel {
	private String sessionId;
	private String authenToken;
	private String auditId;
	private SocketAddress remoteAddress;

	public EventParamModel(String sessionId, String authenToken, String auditId, SocketAddress remoteAddress) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.auditId = auditId;
		this.remoteAddress = remoteAddress;
	}

	public EventParamModel() {
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

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(SocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditId == null) ? 0 : auditId.hashCode());
		result = prime * result + ((authenToken == null) ? 0 : authenToken.hashCode());
		result = prime * result + ((remoteAddress == null) ? 0 : remoteAddress.hashCode());
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventParamModel other = (EventParamModel) obj;
		if (auditId == null) {
			if (other.auditId != null)
				return false;
		} else if (!auditId.equals(other.auditId))
			return false;
		if (authenToken == null) {
			if (other.authenToken != null)
				return false;
		} else if (!authenToken.equals(other.authenToken))
			return false;
		if (remoteAddress == null) {
			if (other.remoteAddress != null)
				return false;
		} else if (!remoteAddress.equals(other.remoteAddress))
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventParamModel [sessionId=" + sessionId + ", authenToken=" + authenToken + ", auditId=" + auditId
				+ ", remoteAddress=" + remoteAddress + "]";
	}

}
