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
	private MessageDigest digest = null;

	public TinyUrlController() {
		try {
			digest = MessageDigest.getInstance("SHA-256");
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
	}


	@PostMapping(value="/api/v1/shortenUrl")
	public String shortenUrl(@RequestBody URL request) {
		User user = userRepository.findByUserId(request.getUserId());
		if (user == null) {
			return "User doesn't exist";
		}
		try {
			String origUrl = request.getOriginalUrl();
			String hashKey = findShortenedUrl(origUrl, request.getUserId());

			request.setHashKey(hashKey);
			request.setCreationDate(LocalDateTime.now());
			urlRepository.save(request);
			return "Shortedn URL created=>"+hashKey;
		}catch(Exception e) {
			e.printStackTrace();
			return "Failed to generate shortened URL";
		}

	}

	private String findShortenedUrl(String origUrl, int userId) {
		String hashKey = null;
		while(hashKey == null) {
			byte[] encodedhash = digest.digest(
					origUrl.getBytes(StandardCharsets.UTF_8));
			String encodedHash = Base64.getUrlEncoder().encodeToString(encodedhash);
			String shaString = new String(encodedHash.substring(0, 10));
			URL url = urlRepository.findByHashKey(shaString);
			if (url == null || (userId == url.getUserId() && origUrl.equals(url.getOriginalUrl()))) {
				hashKey = shaString;
			}else {
				origUrl = origUrl+""+Math.random();
			}
		}
		return hashKey;
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
		if (url != null) {
			redirectView.setUrl(url.getOriginalUrl());
			return redirectView;
		}else {
			return  null;
		}

	}



}
