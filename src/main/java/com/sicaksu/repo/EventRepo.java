package com.sicaksu.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sicaksu.models.Profile;
import com.sicaksu.models.Event;


@Repository
public interface EventRepo extends MongoRepository<Event, String>{
	public List<Event> findByContent(String content);
	public List<Event> findByCreatedById(String userId);
    public List<Event> findByJoinedPeopleId(String userId);

}
