package com.tinyurl.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tinyurl.model.User;

public interface UserRepository extends MongoRepository<User, Integer> {

	public User findByEmail(String email);
	
	public User findByUserId(int userId);
	
}
