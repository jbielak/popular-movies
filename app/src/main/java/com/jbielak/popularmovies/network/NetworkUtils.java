package com.jbielak.popularmovies.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.jbielak.popularmovies.BuildConfig;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    public static final String BASE_URL ="http://api.themoviedb.org/";
    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY = BuildConfig.MOVIES_DB_API_KEY; // put your API key here

    public static URL buildMoviesUrl(String sortType) {
        Uri moviesUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        try {
            URL moviesUrl = new URL(moviesUri.toString());
            if (API_KEY.isEmpty()) {
                Log.w(TAG, "Missing API Key");
            }
            Log.v(TAG, "URL: " + moviesUrl);
            return moviesUrl;
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildPosterUrl(String posterPath) {
        Uri posterUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(posterPath)
                .build();
        try {
            URL posterUrl = new URL(posterUri.toString());
            Log.v(TAG, "URL: " + posterUrl);
            return posterUrl;
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isOnline = netInfo != null && netInfo.isConnectedOrConnecting();

        Log.i(TAG, "Connection provided: " + isOnline);

        return isOnline;
    }

}
