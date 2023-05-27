package com.sicaksu.payload;

import com.sicaksu.models.Event;

public class createEventPayload {
	String createdById;
	public createEventPayload(String createdById, Event event) {
		super();
		this.createdById = createdById;
		this.event = event;
	}
	Event event;
	public String getCreatedById() {
		return createdById;
	}
	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
}
