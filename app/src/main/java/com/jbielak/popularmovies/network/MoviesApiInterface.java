package com.jbielak.popularmovies.network;

import com.jbielak.popularmovies.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiInterface {

    @GET("/3/movie/{sort}")
    Call<MovieResponse> getMovies(@Path("sort") String sort, @Query("api_key") String apiKey);

}
