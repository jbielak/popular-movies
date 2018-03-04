package com.jbielak.popularmovies.utilities;

import com.jbielak.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieDbJsonUtils {

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String POSTER_PATH = "poster_path";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String POPULARITY = "popularity";
    private static final String OVERVIEW = "overview";

    public static List<Movie> getMovies(String moviesJsonStr) throws JSONException{
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray moviesJsonArray = moviesJson.getJSONArray(RESULTS);
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                movies.add(getMovie(moviesJsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private static Movie getMovie(JSONObject movieJson) {
        Movie movie = null;
        if (movieJson != null) {

            movie = new Movie(
                    movieJson.optLong(ID),
                    movieJson.optString(TITLE),
                    DateUtils.getDate(movieJson.optString(RELEASE_DATE)),
                    movieJson.optString(POSTER_PATH),
                    movieJson.optDouble(VOTE_AVERAGE),
                    movieJson.optDouble(POPULARITY),
                    movieJson.optString(OVERVIEW));

        }
        return movie;
    }
}
