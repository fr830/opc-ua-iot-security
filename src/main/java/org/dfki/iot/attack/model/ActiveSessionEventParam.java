package org.dfki.iot.attack.model;

import java.net.SocketAddress;

/**
 * 
 * @author sriharsha
 *
 */
public class ActiveSessionEventParam {
	private String sessionId;
	private String authenToken;
	private String userName;
	private SocketAddress remoteAddress;

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

	public ActiveSessionEventParam(String sessionId, String authenToken, String userName, SocketAddress remoteAddress) {
		super();
		this.sessionId = sessionId;
		this.authenToken = authenToken;
		this.userName = userName;
		this.remoteAddress = remoteAddress;
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
		result = prime * result + ((authenToken == null) ? 0 : authenToken.hashCode());
		result = prime * result + ((remoteAddress == null) ? 0 : remoteAddress.hashCode());
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
				+ userName + ", remoteAddress=" + remoteAddress + "]";
	}

}
