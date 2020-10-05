package com.crawler.imdbCrawler.service;

import java.util.List;

public interface SearchService {
    List<String> getMovies(String searchTerm);
}
