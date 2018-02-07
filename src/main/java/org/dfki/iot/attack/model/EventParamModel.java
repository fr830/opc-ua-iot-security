package org.dfki.iot.attack.model;

public class EventParamModel {
	private String sessionId;
	private String authenToken;
	private String auditId;
	private String ipAddress;
	private int port;

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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditId == null) ? 0 : auditId.hashCode());
		result = prime * result + ((authenToken == null) ? 0 : authenToken.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + port;
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
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (port != other.port)
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
				+ ", ipAddress=" + ipAddress + ", port=" + port + "]";
	}

	/**
	 * @param sessionId
	 * @param authenToken
	 * @param auditId
	 * @param ipAddress
	 * @param port
	 */
	public EventParamModel(String sessionId, String authenToken, String auditId, String ipAddress, int port) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.auditId = auditId;
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public EventParamModel() {
		super();
	}

}
