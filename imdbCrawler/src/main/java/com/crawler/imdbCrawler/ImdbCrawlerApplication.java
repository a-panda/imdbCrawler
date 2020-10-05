package com.crawler.imdbCrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImdbCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImdbCrawlerApplication.class, args);
		System.out.println("Server started ...");
	}

}
