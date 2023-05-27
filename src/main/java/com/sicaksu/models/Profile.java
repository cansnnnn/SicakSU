package com.sicaksu.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.ArrayList;
import java.util.Objects;

@Document(collection = "profiles")
public class Profile {

	@Id private String id;
	private String name;
	private String lastname;
	private String imageUrl;
	
	public Profile() {
		super();
	}

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
		Profile other = (Profile) obj;
		return Objects.equals(id, other.id);
	}

	public Profile(String name, String lastname, String imageUrl) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.imageUrl = imageUrl;
	}

	public Profile(String id, String name, String lastname, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.imageUrl = imageUrl;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
}