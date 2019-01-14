package com.anakromeiro.urlShortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UrlShortenerApplication {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	String index() {
		return "RESTful Server for URL Shortener\n Developed by: anakromeiro@gmail.com";
	}

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}
}

