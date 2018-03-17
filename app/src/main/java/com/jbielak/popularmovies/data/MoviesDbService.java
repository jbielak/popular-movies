package com.jbielak.popularmovies.data;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jbielak.popularmovies.data.MovieContract.MovieEntry;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.model.Review;
import com.jbielak.popularmovies.model.Video;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Justyna on 2018-03-17.
 */

public class MoviesDbService {

    private Context context;

    public MoviesDbService(Context context) {
        this.context = context;
    }

    public List<Movie> getFavoriteMovies() {

        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        List<Movie> movies = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(MovieEntry.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
                Date releaseDate = new Date(cursor.getLong(cursor.getColumnIndex(
                        MovieEntry.COLUMN_RELEASE_DATE)));
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
                double voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
                String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
                Double popularity = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY));
                Type videoType = new TypeToken<List<Video>>() {}.getType();
                List<Video> videos = new Gson().fromJson(
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_VIDEOS)), videoType);
                Type reviewType = new TypeToken<List<Review>>() {}.getType();
                List<Review> reviews = new Gson().fromJson(
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_REVIEWS)), reviewType);

                movies.add(new Movie(id, title, releaseDate, posterPath, voteAverage, popularity, overview,
                        videos, reviews));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return movies;
    }
}
