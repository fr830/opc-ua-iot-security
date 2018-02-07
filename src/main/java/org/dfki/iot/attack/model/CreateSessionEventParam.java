package org.dfki.iot.attack.model;

import java.security.Timestamp;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

/**
 * 
 * @author sriharsha
 *
 */
public class CreateSessionEventParam {
	private String sessionId;
	private String authenToken;
	private String sessionName;
	private String clientApplicationName;
	private UnsignedInteger clientMaxResponseTime;
	private Double clientRequestSessionTimeOut;
	private Timestamp resTimeStamp;
	private String sessionResult;
	private Double resRevisedTimeOut;
	private String ipAddress;
	private int port;

	public CreateSessionEventParam() {
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
		result = prime * result + ((clientApplicationName == null) ? 0 : clientApplicationName.hashCode());
		result = prime * result + ((clientMaxResponseTime == null) ? 0 : clientMaxResponseTime.hashCode());
		result = prime * result + ((clientRequestSessionTimeOut == null) ? 0 : clientRequestSessionTimeOut.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + port;
		result = prime * result + ((resRevisedTimeOut == null) ? 0 : resRevisedTimeOut.hashCode());
		result = prime * result + ((resTimeStamp == null) ? 0 : resTimeStamp.hashCode());
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		result = prime * result + ((sessionName == null) ? 0 : sessionName.hashCode());
		result = prime * result + ((sessionResult == null) ? 0 : sessionResult.hashCode());
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
		CreateSessionEventParam other = (CreateSessionEventParam) obj;
		if (authenToken == null) {
			if (other.authenToken != null)
				return false;
		} else if (!authenToken.equals(other.authenToken))
			return false;
		if (clientApplicationName == null) {
			if (other.clientApplicationName != null)
				return false;
		} else if (!clientApplicationName.equals(other.clientApplicationName))
			return false;
		if (clientMaxResponseTime == null) {
			if (other.clientMaxResponseTime != null)
				return false;
		} else if (!clientMaxResponseTime.equals(other.clientMaxResponseTime))
			return false;
		if (clientRequestSessionTimeOut == null) {
			if (other.clientRequestSessionTimeOut != null)
				return false;
		} else if (!clientRequestSessionTimeOut.equals(other.clientRequestSessionTimeOut))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (port != other.port)
			return false;
		if (resRevisedTimeOut == null) {
			if (other.resRevisedTimeOut != null)
				return false;
		} else if (!resRevisedTimeOut.equals(other.resRevisedTimeOut))
			return false;
		if (resTimeStamp == null) {
			if (other.resTimeStamp != null)
				return false;
		} else if (!resTimeStamp.equals(other.resTimeStamp))
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		if (sessionName == null) {
			if (other.sessionName != null)
				return false;
		} else if (!sessionName.equals(other.sessionName))
			return false;
		if (sessionResult == null) {
			if (other.sessionResult != null)
				return false;
		} else if (!sessionResult.equals(other.sessionResult))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CreateSessionEventParam [sessionId=" + sessionId + ", authenToken=" + authenToken + ", sessionName="
				+ sessionName + ", clientApplicationName=" + clientApplicationName + ", clientMaxResponseTime="
				+ clientMaxResponseTime + ", clientRequestSessionTimeOut=" + clientRequestSessionTimeOut
				+ ", resTimeStamp=" + resTimeStamp + ", sessionResult=" + sessionResult + ", resRevisedTimeOut="
				+ resRevisedTimeOut + ", ipAddress=" + ipAddress + ", port=" + port + "]";
	}

	/**
	 * @param sessionId
	 * @param authenToken
	 * @param sessionName
	 * @param clientApplicationName
	 * @param clientMaxResponseTime
	 * @param clientRequestSessionTimeOut
	 * @param resTimeStamp
	 * @param sessionResult
	 * @param resRevisedTimeOut
	 * @param ipAddress
	 * @param port
	 */
	public CreateSessionEventParam(String sessionId, String authenToken, String sessionName,
			String clientApplicationName, UnsignedInteger clientMaxResponseTime, Double clientRequestSessionTimeOut,
			Timestamp resTimeStamp, String sessionResult, Double resRevisedTimeOut, String ipAddress, int port) {
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
		this.ipAddress = ipAddress;
		this.port = port;
	}

}
