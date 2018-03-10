package com.jbielak.popularmovies.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jbielak.popularmovies.utilities.DateDeserializer;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Justyna on 2018-03-10.
 */

public class ApiClientGenerator {

    public static String defaultApiBaseUrl = NetworkUtils.BASE_URL;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(defaultApiBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        defaultApiBaseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(defaultApiBaseUrl);
    }

    public static <S> S createClient(Class<S> serviceClass, String baseUrl) {
        Retrofit retrofit = builder
                .baseUrl(baseUrl == null ? defaultApiBaseUrl : baseUrl)
                .client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
