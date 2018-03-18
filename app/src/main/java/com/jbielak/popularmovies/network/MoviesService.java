package com.jbielak.popularmovies.network;

import android.util.Log;

import com.jbielak.popularmovies.FetchDataListener;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.model.MovieResponse;
import com.jbielak.popularmovies.model.Review;
import com.jbielak.popularmovies.model.ReviewResponse;
import com.jbielak.popularmovies.model.Video;
import com.jbielak.popularmovies.model.VideoResponse;
import com.jbielak.popularmovies.utilities.DisplayType;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Justyna on 2018-03-10.
 */

public class MoviesService {

    private static final String TAG = MoviesService.class.getSimpleName();

    private MoviesApiInterface moviesApiInterface;
    private FetchDataListener<List<Movie>> fetchMoviesDataListener;
    private FetchDataListener<List<Video>> fetchVideosDataListener;
    private FetchDataListener<List<Review>> fetchReviewsDataListener;

    public MoviesService() {
        moviesApiInterface = ApiClientGenerator.createClient(MoviesApiInterface.class,
                NetworkUtils.BASE_URL);
    }

    public void getMovies(DisplayType displayType){
        if (fetchMoviesDataListener != null) {
            fetchMoviesDataListener.onPreExecute();
        }


        Call<MovieResponse> call = moviesApiInterface.getMovies(displayType.getValue(), NetworkUtils.API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = null;
                if(response.body() != null) {
                    movies = response.body().getResults();
                    Log.d(TAG, "Number of movies received: " + movies.size());
                }
                if (fetchMoviesDataListener != null) {
                    fetchMoviesDataListener.onResponse(movies);
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                if (fetchMoviesDataListener != null) {
                    fetchMoviesDataListener.onError();
                }
            }
        });
    }

    public void getMovieVideos(String movieId){
        if(fetchVideosDataListener != null) {
            fetchVideosDataListener.onPreExecute();
        }

        Call<VideoResponse> call = moviesApiInterface.getMovieVideos(movieId, NetworkUtils.API_KEY);

        call.enqueue(new Callback<VideoResponse>() {
            List<Video> videos = null;
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() != null) {
                    videos = response.body().getResults();
                    Log.d(TAG, "Number of videos received: " + videos.size());
                }
                if (fetchVideosDataListener != null) {
                    fetchVideosDataListener.onResponse(videos);
                }

            }
            @Override
            public void onFailure(Call<VideoResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                if (fetchVideosDataListener != null) {
                    fetchVideosDataListener.onError();
                }
            }
        });
    }

    public void getMovieReviews(String movieId){
        if(fetchReviewsDataListener != null) {
            fetchReviewsDataListener.onPreExecute();
        }

        Call<ReviewResponse> call = moviesApiInterface.getMovieReviews(movieId, NetworkUtils.API_KEY);

        call.enqueue(new Callback<ReviewResponse>() {
            List<Review> reviews = null;
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.body() != null) {
                    reviews = response.body().getResults();
                    Log.d(TAG, "Number of reviews received: " + reviews.size());
                }
                if (fetchReviewsDataListener != null) {
                    fetchReviewsDataListener.onResponse(reviews);
                }

            }
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                if (fetchReviewsDataListener != null) {
                    fetchReviewsDataListener.onError();
                }
            }
        });
    }

    public void setFetchMoviesDataListener(FetchDataListener<List<Movie>> fetchMoviesDataListener) {
        this.fetchMoviesDataListener = fetchMoviesDataListener;
    }

    public void setFetchVideosDataListener(FetchDataListener<List<Video>> fetchVideosDataListener) {
        this.fetchVideosDataListener = fetchVideosDataListener;
    }

    public void setFetchReviewsDataListener(FetchDataListener<List<Review>> fetchReviewsDataListener) {
        this.fetchReviewsDataListener = fetchReviewsDataListener;
    }
}
