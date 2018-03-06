package com.jbielak.popularmovies;

import android.os.AsyncTask;

import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.utilities.MovieDbJsonUtils;
import com.jbielak.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;


public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    private MoviesTaskCallback moviesTaskCallback;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (moviesTaskCallback != null) {
            moviesTaskCallback.onAsyncTaskPreExecute();
        }
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        /* If there's no sort type, there's nothing to look up. */
        if (params.length == 0) {
            return null;
        }

        String sortTypeStr = params[0];
        URL moviesRequestUrl = NetworkUtils.buildMoviesUrl(sortTypeStr);

        try {
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(moviesRequestUrl);

            List<Movie> moviesData = MovieDbJsonUtils
                    .getMovies(jsonMoviesResponse);

            return moviesData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (moviesTaskCallback != null) {
            moviesTaskCallback.onAsyncTaskComplete(movies);
        }
    }

    public void setMoviesTaskCallback(MoviesTaskCallback moviesTaskCallback) {
        this.moviesTaskCallback = moviesTaskCallback;
    }
}