package com.jbielak.popularmovies.data.network;

import com.jbielak.popularmovies.data.model.MovieResponse;
import com.jbielak.popularmovies.data.model.ReviewResponse;
import com.jbielak.popularmovies.data.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiInterface {

    @GET("/3/movie/{sort}")
    Call<MovieResponse> getMovies(@Path("sort") String sort, @Query("api_key") String apiKey,
                                  @Query("page") int page);

    @GET("/3/movie/{id}/videos")
    Call<VideoResponse> getMovieVideos(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") String id, @Query("api_key") String apiKey);
}
