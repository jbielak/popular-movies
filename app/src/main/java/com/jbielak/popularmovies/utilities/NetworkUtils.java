package com.jbielak.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.net.URL;

/**
 * Created by Justyna on 2018-02-26.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static NetworkUtils instance = null;
    public RequestQueue requestQueue;

    private NetworkUtils (Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkUtils getInstance(Context context) {
        if (null == instance)
            instance = new NetworkUtils(context);
        return instance;
    }

    public static synchronized NetworkUtils getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NetworkUtils.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void get(URL url, final ResponseListener<JSONObject> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Response : " + response.toString());
                        if (null != response)
                            listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d(TAG + ": ", "Error Response code: "
                                    + error.networkResponse.statusCode);
                            listener.onError(error.getMessage());
                        }
                    }
                });
        requestQueue.add(request);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
