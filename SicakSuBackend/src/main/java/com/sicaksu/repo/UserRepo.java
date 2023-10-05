package com.sicaksu.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sicaksu.models.Profile;
import com.sicaksu.models.User;

@Repository
public interface UserRepo extends MongoRepository<User, String>{
	public User findByUsername(String username);
	public User findByUsernameAndPassword(String username, String password);


}
