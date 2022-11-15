package com.example.movie.services;

import com.example.movie.models.CatalogItem;
import com.example.movie.models.Movie;
import com.example.movie.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie =  restTemplate.getForObject("http://movie-info-service/movies/"+ rating.getMovieId(), Movie.class);
        //put them all together
        return new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating());
    }
    public CatalogItem getFallbackCatalogItem(Rating rating) {

        return new CatalogItem("Movie name not found", "", rating.getRating());
    }
}
