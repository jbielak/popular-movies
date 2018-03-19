package com.jbielak.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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
    private DatabaseListener databaseListener;

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

    public boolean isInDatabase(Movie movie) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_ID + "=" + movie.getId(),
                null,
                null);
        cursor.close();
        if (cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public int removeMovie(Movie movie) {
        String stringId = Long.toString(movie.getId());
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        int moviesDeleted = context.getContentResolver().delete(uri,
                MovieContract.MovieEntry.COLUMN_ID + "=" + movie.getId(), null);
        if (moviesDeleted > 0) {
            if (databaseListener != null) {
                databaseListener.onRemoveSuccess();
            }
        } else {
            if (databaseListener != null) {
                databaseListener.onRemoveError();
            }
        }
        return moviesDeleted;
    }

    public void insertMovie(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(MovieEntry.COLUMN_VIDEOS, new Gson().toJson(movie.getVideos()));
        contentValues.put(MovieEntry.COLUMN_REVIEWS, new Gson().toJson(movie.getReviews()));

        Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            if (databaseListener != null) {
                databaseListener.onInsertSuccess();
            }
        } else {
            if (databaseListener != null) {
                databaseListener.onInsertError();
            }
        }
    }

    public void setDatabaseListener(DatabaseListener databaseListener) {
        this.databaseListener = databaseListener;
    }
}
