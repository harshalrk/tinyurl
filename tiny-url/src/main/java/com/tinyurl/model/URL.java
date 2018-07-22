package com.tinyurl.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="url")

public class URL {

	@Id
	public String id;
	
	public String hashKey;
	private String originalUrl;
	private LocalDateTime creationDate;
	private LocalDateTime expirationDate;
	private int userId;
	
	public URL() {
		
	}
	
	public URL(String hashKey, String originalUrl, LocalDateTime creationDate, LocalDateTime expirationDate, int userId) {
		super();
		this.hashKey = hashKey;
		this.originalUrl = originalUrl;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
		this.userId = userId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHashKey() {
		return hashKey;
	}
	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	
	
}
