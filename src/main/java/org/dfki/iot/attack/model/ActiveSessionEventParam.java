package org.dfki.iot.attack.model;

/**
 * 
 * @author sriharsha
 *
 */
public class ActiveSessionEventParam {
	private String sessionId;
	private String authenToken;
	private String userName;
	private String password;
	private String ipAddress;
	private int port;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		result = prime * result + ((authenToken == null) ? 0 : authenToken.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + port;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		ActiveSessionEventParam other = (ActiveSessionEventParam) obj;
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (port != other.port)
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActiveSessionEventParam [sessionId=" + sessionId + ", authenToken=" + authenToken + ", userName="
				+ userName + ", password=" + password + ", ipAddress=" + ipAddress + ", port=" + port + "]";
	}

	/**
	 * @param sessionId
	 * @param authenToken
	 * @param userName
	 * @param password
	 * @param ipAddress
	 * @param port
	 */
	public ActiveSessionEventParam(String sessionId, String authenToken, String userName, String password,
			String ipAddress, int port) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.userName = userName;
		this.password = password;
		this.ipAddress = ipAddress;
		this.port = port;
	}

}
