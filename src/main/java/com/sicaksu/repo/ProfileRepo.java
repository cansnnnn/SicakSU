package com.sicaksu.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sicaksu.models.Event;
import com.sicaksu.models.Profile;

@Repository
public interface ProfileRepo extends MongoRepository<Profile, String>{
	//it can be used for searching profiles
	//List<User> findByNameStartingWith(String regexp);
}
