package com.jbielak.popularmovies.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jbielak.popularmovies.data.network.MoviesApiInterface;
import com.jbielak.popularmovies.data.network.MoviesService;
import com.jbielak.popularmovies.data.network.NetworkUtils;
import com.jbielak.popularmovies.utilities.DateDeserializer;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class NetworkModule {

    @Provides
    @Singleton
    public static Gson provideGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

    @Provides
    @Singleton
    public static OkHttpClient provideOkhttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(NetworkUtils.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    public static MoviesApiInterface getMoviesApiInterface(Retrofit retrofit) {
        return retrofit.create(MoviesApiInterface.class);
    }

    @Provides
    @Singleton
    public static MoviesService getMovesService(MoviesApiInterface moviesApiInterface) {
        return new MoviesService(moviesApiInterface);
    }
}
