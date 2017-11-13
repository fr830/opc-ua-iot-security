package org.dfki.iot.attack.model;

import java.io.Serializable;
import java.util.Random;

public class RoverClientA implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int speedSensorData;
	private double percentagePowerAvilable;
	private boolean isClientActive;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSpeedSensorData() {
		return speedSensorData;
	}

	public void setSpeedSensorData(int speedSensorData) {
		this.speedSensorData = speedSensorData;
	}

	public boolean isClientActive() {
		return isClientActive;
	}

	public void setClientActive(boolean isClientActive) {
		this.isClientActive = isClientActive;
	}

	public double getPercentagePowerAvilable() {
		return percentagePowerAvilable;
	}

	public void setPercentagePowerAvilable(double percentagePowerAvilable) {
		this.percentagePowerAvilable = percentagePowerAvilable;
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
		result = prime * result + speedSensorData;
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
		RoverClientA other = (RoverClientA) obj;
		if (id != other.id)
			return false;
		if (isClientActive != other.isClientActive)
			return false;
		if (Double.doubleToLongBits(percentagePowerAvilable) != Double.doubleToLongBits(other.percentagePowerAvilable))
			return false;
		if (speedSensorData != other.speedSensorData)
			return false;
		return true;
	}

	/**
	 * @param id
	 * @param speedSensorData
	 * @param percentagePowerAvilable
	 * @param isClientActive
	 */
	public RoverClientA(int id, int speedSensorData, double percentagePowerAvilable, boolean isClientActive) {
		super();
		this.id = id;
		this.speedSensorData = speedSensorData;
		this.percentagePowerAvilable = percentagePowerAvilable;
		this.isClientActive = isClientActive;
	}

	@Override
	public String toString() {
		return "RoverClientA [id=" + id + ", speedSensorData=" + speedSensorData + ", percentagePowerAvilable="
				+ percentagePowerAvilable + ", isClientActive=" + isClientActive + "]";
	}

	public RoverClientA() {
		super();
	}

	public static RoverClientA getRandomRoverClientA() {
		Random rand = new Random();

		RoverClientA roverClientA = new RoverClientA();
		roverClientA.setClientActive(true);
		roverClientA.setId(rand.nextInt());
		roverClientA.setPercentagePowerAvilable(rand.nextInt()%100);
		roverClientA.setSpeedSensorData(rand.nextInt());
		return roverClientA;
		
	}

}
