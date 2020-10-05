package com.crawler.imdbCrawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    private static Set<String> links;
    private static Map<String, Set<String>> dataMap;

    @Override
    public List<String> getMovies(String searchTerm) {
        List<String> movieList = new ArrayList<>();
        if(dataMap==null) loadData();
        if(searchTerm==null || searchTerm.isEmpty()) return movieList;
        searchTerm = searchTerm.toLowerCase();
        String[] searchKeys = searchTerm.split(" ");
        if(searchKeys==null || searchKeys.length==0) return movieList;
        Set<String> movieSet = new HashSet<>();
        if(!dataMap.containsKey(searchKeys[0])) return movieList;
        movieSet.addAll(dataMap.get(searchKeys[0]));
        for(int i=1; i<searchKeys.length; i++) {
            if(!dataMap.containsKey(searchKeys[i])) return movieList;
            movieSet.retainAll(dataMap.get(searchKeys[i]));
            if(movieSet.size()==0) return movieList;
        }
        movieList.addAll(movieSet);
        return movieList;
    }

    private void loadData() {
        System.out.println("Loading data");
        dataMap = new HashMap<>();
        links = new HashSet<>();
        getLinks("https://www.imdb.com/search/title/?groups=top_1000&sort=user_rating,desc&count=100&view=advanced");
//        System.out.println(links.toString());
        getData();
    }

    private void getLinks(String url) {
        try {
            if(!links.contains(url)) {
                links.add(url);
                Document document = Jsoup.connect(url).get();
                String nextLink = document.getElementsByClass("lister-page-next").attr("abs:href");
                if(nextLink!=null && !nextLink.isEmpty()) {
                    getLinks(nextLink);
                }
            }
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    private void getData() {
        try {
            for (String link : links.toArray(new String[0])) {
                Document document = Jsoup.connect(link).get();
                Elements movies = document.getElementsByClass("lister-item");
                for(Element movie : movies) {
                    Elements movieHeader = movie.getElementsByClass("lister-item-header");
                    String movieName = "";
                    for(Element header : movieHeader) {
                        movieName = header.getElementsByAttribute("href").text();
                        String year = movie.getElementsByClass("lister-item-year").text();
                        year = year.substring(1, year.length() - 1);
                        Set<String> nameSet =  dataMap.getOrDefault(year, new HashSet<>());
                        nameSet.add(movieName);
                        dataMap.put(year, nameSet);
                    }
                    Elements moviePeopleNames = movie.getElementsByAttributeValueContaining("href", "/name/");
                    for(Element moviePeople : moviePeopleNames) {
                        String peopleName = moviePeople.text();
                        String[] peopleNames = peopleName.split(" ");
                        for(String name : peopleNames) {
                            name = name.toLowerCase();
                            Set<String> nameSet =  dataMap.getOrDefault(name, new HashSet<>());
                            nameSet.add(movieName);
                            dataMap.put(name, nameSet);
                        }
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
