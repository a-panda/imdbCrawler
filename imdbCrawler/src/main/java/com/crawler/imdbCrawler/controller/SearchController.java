package com.crawler.imdbCrawler.controller;

import com.crawler.imdbCrawler.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    @RequestMapping(value="/search/{searchTerm}", method={RequestMethod.GET})
    public List<String> getMovies(@PathVariable("searchTerm") String searchTerm) {
        return searchService.getMovies(searchTerm);
    }

}
