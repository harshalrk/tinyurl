package com.tinyurl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tinyurl.model.URL;
import com.tinyurl.model.User;
import com.tinyurl.model.repository.URLRepository;
import com.tinyurl.model.repository.UserRepository;

@SpringBootApplication
public class TinyUrlApplication implements CommandLineRunner {

	@Autowired
	URLRepository urlRepository;
	@Autowired
	UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(TinyUrlApplication.class, args);
	}
	
	@Override
	public void run(String... strings) {
		urlRepository.deleteAll();
		userRepository.deleteAll();
		
		userRepository.save(new User(1, "Harshal", "harshal@home.com", LocalDateTime.now()));
		
		urlRepository.save(new URL("EZDwE23y1q", 
				"https://spring.io/projects/spring-session-data-mongodb", 
				LocalDateTime.now(), LocalDateTime.now().plusYears(1), 1));
		
		
	}
}
