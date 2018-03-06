package com.jbielak.popularmovies;


import com.jbielak.popularmovies.model.Movie;

import java.util.List;

public interface MoviesTaskCallback {
    void onAsyncTaskPreExecute();
    void onAsyncTaskComplete(List<Movie> movies);
}
