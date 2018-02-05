package org.dfki.iot.attack.model;

public class EventModel {
	private String eventType;
	private Object eventParam;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Object getEventParam() {
		return eventParam;
	}

	public void setEventParam(Object eventParam) {
		this.eventParam = eventParam;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventParam == null) ? 0 : eventParam.hashCode());
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
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
		EventModel other = (EventModel) obj;
		if (eventParam == null) {
			if (other.eventParam != null)
				return false;
		} else if (!eventParam.equals(other.eventParam))
			return false;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.equals(other.eventType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventModel [eventType=" + eventType + ", eventParam=" + eventParam + "]";
	}

	/**
	 * @param eventType
	 * @param eventParam
	 */
	public EventModel(String eventType, Object eventParam) {
		super();
		this.eventType = eventType;
		this.eventParam = eventParam;
	}

	public EventModel() {
		super();
	}
}
