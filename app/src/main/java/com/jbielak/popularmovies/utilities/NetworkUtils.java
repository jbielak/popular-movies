package com.jbielak.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = ""; // put your API key here

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

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
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
