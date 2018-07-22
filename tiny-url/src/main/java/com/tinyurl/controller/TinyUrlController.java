package com.tinyurl.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.tinyurl.model.URL;
import com.tinyurl.model.User;
import com.tinyurl.model.repository.URLRepository;
import com.tinyurl.model.repository.UserRepository;

@RestController
public class TinyUrlController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	URLRepository urlRepository;

	@PostMapping(value="/api/shortenUrl")
	public String shortenUrl(@RequestBody URL request) {
		User user = userRepository.findByUserId(request.getUserId());
		if (user == null) {
			return "User doesn't exist";
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(
					request.getOriginalUrl().getBytes(StandardCharsets.UTF_8));
			String encodedHash = Base64.getUrlEncoder().encodeToString(encodedhash);
			String shaString = new String(encodedHash.substring(0, 10));
			request.setHashKey(shaString);
			request.setCreationDate(LocalDateTime.now());
			urlRepository.save(request);
			return "Shortedn URL created=>"+shaString;
		}catch(Exception e) {
			e.printStackTrace();
			return "Failed to generate shortened URL";
		}

	}
	
	@PostMapping(value="/api/deleteUrl")
	public String deleteUrl(@RequestBody URL request) {
		if (StringUtils.isEmpty(request.hashKey)) {
			return "Please specify valid hashKey";
		}
		User user = userRepository.findByUserId(request.getUserId());
		if (user == null) {
			return "User doesn't exist";
		}
		int count = urlRepository.deleteByHashKey(request.getHashKey());
		if (count > 0)
			return "Shortened URL deleted";
		else 
			return "Shortened URL not found";
		
	}
	
	
	
	
	@GetMapping(value= "{id}")
	public RedirectView redirect(@PathVariable String id) {
		RedirectView redirectView = new RedirectView();
		URL url = urlRepository.findByHashKey(id);
	    redirectView.setUrl(url.getOriginalUrl());
	    return redirectView;
		
	}



}
