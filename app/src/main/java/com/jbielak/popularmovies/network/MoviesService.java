package com.jbielak.popularmovies.network;

import android.util.Log;

import com.jbielak.popularmovies.FetchDataListener;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.model.MovieResponse;
import com.jbielak.popularmovies.utilities.SortType;

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
    private FetchDataListener<List<Movie>> fetchDataListener;

    public MoviesService() {
        moviesApiInterface = ApiClientGenerator.createClient(MoviesApiInterface.class, NetworkUtils.BASE_URL);
    }

    public void getMovies(SortType sortType){
        fetchDataListener.onPreExecute();

        Call<MovieResponse> call = moviesApiInterface.getMovies(sortType.getValue(), NetworkUtils.API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size());
                fetchDataListener.onResponse(movies);
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                fetchDataListener.onError();
            }
        });
    }

    public void setFetchDataListener(FetchDataListener<List<Movie>> fetchDataListener) {
        this.fetchDataListener = fetchDataListener;
    }
}
