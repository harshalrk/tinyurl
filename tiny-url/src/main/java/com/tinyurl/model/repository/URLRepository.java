package com.tinyurl.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tinyurl.model.URL;

public interface URLRepository extends MongoRepository<URL, String>{
	public URL findByHashKey(String hashKey);
	
	public int deleteByHashKey(String hashKey);
	

}
