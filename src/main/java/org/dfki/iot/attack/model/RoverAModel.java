package org.dfki.iot.attack.model;

import java.io.Serializable;
import java.util.Random;

public class RoverAModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private double speedSensorData;
	private double tempSensorData;
	private double percentagePowerAvilable;
	private boolean isClientActive;

	private String requesterIPAddress;
	private String requesterMACAddress;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSpeedSensorData() {
		return speedSensorData;
	}

	public void setSpeedSensorData(double speedSensorData) {
		this.speedSensorData = speedSensorData;
	}

	public double getTempSensorData() {
		return tempSensorData;
	}

	public void setTempSensorData(double tempSensorData) {
		this.tempSensorData = tempSensorData;
	}

	public double getPercentagePowerAvilable() {
		return percentagePowerAvilable;
	}

	public void setPercentagePowerAvilable(double percentagePowerAvilable) {
		this.percentagePowerAvilable = percentagePowerAvilable;
	}

	public boolean isClientActive() {
		return isClientActive;
	}

	public void setClientActive(boolean isClientActive) {
		this.isClientActive = isClientActive;
	}

	public String getRequesterIPAddress() {
		return requesterIPAddress;
	}

	public void setRequesterIPAddress(String requesterIPAddress) {
		this.requesterIPAddress = requesterIPAddress;
	}

	public String getRequesterMACAddress() {
		return requesterMACAddress;
	}

	public void setRequesterMACAddress(String requesterMACAddress) {
		this.requesterMACAddress = requesterMACAddress;
	}

	@Override
	public String toString() {
		return "RoverAModel [id=" + id + ", speedSensorData=" + speedSensorData + ", tempSensorData=" + tempSensorData
				+ ", percentagePowerAvilable=" + percentagePowerAvilable + ", isClientActive=" + isClientActive
				+ ", requesterIPAddress=" + requesterIPAddress + ", requesterMACAddress=" + requesterMACAddress + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + (isClientActive ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(percentagePowerAvilable);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((requesterIPAddress == null) ? 0 : requesterIPAddress.hashCode());
		result = prime * result + ((requesterMACAddress == null) ? 0 : requesterMACAddress.hashCode());
		temp = Double.doubleToLongBits(speedSensorData);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(tempSensorData);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		RoverAModel other = (RoverAModel) obj;
		if (id != other.id)
			return false;
		if (isClientActive != other.isClientActive)
			return false;
		if (Double.doubleToLongBits(percentagePowerAvilable) != Double.doubleToLongBits(other.percentagePowerAvilable))
			return false;
		if (requesterIPAddress == null) {
			if (other.requesterIPAddress != null)
				return false;
		} else if (!requesterIPAddress.equals(other.requesterIPAddress))
			return false;
		if (requesterMACAddress == null) {
			if (other.requesterMACAddress != null)
				return false;
		} else if (!requesterMACAddress.equals(other.requesterMACAddress))
			return false;
		if (Double.doubleToLongBits(speedSensorData) != Double.doubleToLongBits(other.speedSensorData))
			return false;
		if (Double.doubleToLongBits(tempSensorData) != Double.doubleToLongBits(other.tempSensorData))
			return false;
		return true;
	}

	/**
	 * @param id
	 * @param speedSensorData
	 * @param tempSensorData
	 * @param percentagePowerAvilable
	 * @param isClientActive
	 * @param requesterIPAddress
	 * @param requesterMACAddress
	 */
	public RoverAModel(int id, double speedSensorData, double tempSensorData, double percentagePowerAvilable,
			boolean isClientActive, String requesterIPAddress, String requesterMACAddress) {
		super();
		this.id = id;
		this.speedSensorData = speedSensorData;
		this.tempSensorData = tempSensorData;
		this.percentagePowerAvilable = percentagePowerAvilable;
		this.isClientActive = isClientActive;
		this.requesterIPAddress = requesterIPAddress;
		this.requesterMACAddress = requesterMACAddress;
	}

	public RoverAModel() {
		super();
	}

	public static RoverAModel getRandomRoverClientA() {
		Random rand = new Random();

		RoverAModel roverClientA = new RoverAModel();
		roverClientA.setClientActive(true);
		roverClientA.setId(rand.nextInt());
		roverClientA.setPercentagePowerAvilable(rand.nextInt() % 100);
		roverClientA.setSpeedSensorData(rand.nextInt());
		roverClientA.setTempSensorData(rand.nextInt());

		return roverClientA;

	}

}
