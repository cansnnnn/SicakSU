package com.sicaksu.models;


import java.util.ArrayList;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "events")
public class Event {

	@Id private String id;
	private String content;
	private String headline;
	private int limit;
	private int joinCount;
	private LocalDateTime requestDate;      
	
	@DBRef
	private ArrayList<Profile> joinedPeople;
	
	@DBRef 
	private Profile createdBy;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return Objects.equals(id, other.id);
	}

	

	public Event() {}
	
	public Event(String content, String headline, int limit, LocalDateTime requestDate,Profile createdBy) {
		super();
		this.content = content;
		this.headline = headline;
		this.limit = limit;
		this.requestDate = requestDate;
		this.joinedPeople = new ArrayList<Profile>();
		this.createdBy = createdBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Event(String id, String content, String headline, int limit, int joinCount, LocalDateTime requestDate, ArrayList<Profile> joinedPeople, Profile createdBy) {
		super();
		this.id = id;
		this.content = content;
		this.headline = headline;
		this.limit = limit;
		this.joinCount = joinCount;
		this.requestDate = requestDate;
		this.joinedPeople = joinedPeople;
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "Event [content=" + content + ", headline=" + headline + ", limit=" + limit + ", joinCount=" + joinCount
				+ ", requestDate=" + requestDate + ", joinedPeople=" + joinedPeople
				+ ", user=" + createdBy + "]";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(int joinCount) {
		this.joinCount = joinCount;
	}

	public ArrayList<Profile> getJoinedPeople() {
		if(this.joinedPeople == null) {
			this.joinedPeople = new ArrayList<Profile>();
		}
		return joinedPeople;
	}

	public void setJoinedPeople(ArrayList<Profile> joinedPeople) {
		this.joinedPeople = joinedPeople;
	}

	public Profile getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Profile createdBy) {
		this.createdBy = createdBy;
	}
	
	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setResponseDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}
}