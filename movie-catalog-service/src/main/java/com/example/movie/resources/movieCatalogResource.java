package com.example.movie.resources;

import com.example.movie.models.CatalogItem;
import com.example.movie.models.Movie;
import com.example.movie.models.Rating;
import com.example.movie.models.UserRating;
import com.example.movie.services.MovieInfo;
import com.example.movie.services.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class movieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;
    /*
    @Autowired
    private WebClient.Builder webClientBuilder;
     */
    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}")
   // @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        //get all rated movies IDs
       UserRating ratings = userRatingInfo.getUserRating(userId);

       return ratings.getUserRating().stream().map(rating -> {
           //for each movie ID, call movie info service and get details
                   return movieInfo.getCatalogItem(rating);
               })
                .collect(Collectors.toList());
    }



//    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
//        return Arrays.asList(new CatalogItem("No movie","",0));
//    }

}
 /*
        //to use webClient instead:
        //add reactive web dependency
        //set webclient as a been just like restTemplate
        //AutoWire
        Movie movie = webClientBuilder.build().get()
        .uri("http://localhost:8082/movies/"+rating.getMovieId())
        .retrieve().bodyToMono(Movie.class).block();
         */