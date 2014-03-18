package de.aaa.al_shajara.data;

public class BiographicEvent {

	private String eventText;
	private PartiallyDefinedDate startDate;
	private PartiallyDefinedDate endDate;
	
	public BiographicEvent(PartiallyDefinedDate startDate,PartiallyDefinedDate endDate, String eventText) {
		this.eventText = eventText;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public BiographicEvent(PartiallyDefinedDate startDate, String eventText) {
		this.eventText = eventText;
		this.startDate = startDate;
		this.endDate = null;
	}

	public String getEventText() {
		return eventText;
	}
	public void setEventText(String eventText) {
		this.eventText = eventText;
	}
	public PartiallyDefinedDate getStartDate() {
		return startDate;
	}
	public void setStartDate(PartiallyDefinedDate startDate) {
		this.startDate = startDate;
	}
	public PartiallyDefinedDate getEndDate() {
		return endDate;
	}
	public void setEndDate(PartiallyDefinedDate endDate) {
		this.endDate = endDate;
	}
	
}
