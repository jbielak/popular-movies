package com.jbielak.popularmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.jbielak.popularmovies.database.MovieDao;
import com.jbielak.popularmovies.database.MoviesDatabase;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class MoviesDatabaseModule {

    @Singleton
    @Provides
    public static MoviesDatabase provideMovieDatabase(Context context){
        return Room.databaseBuilder(context, MoviesDatabase.class, MoviesDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    public static MovieDao provideMovieDao(MoviesDatabase moviesDatabase){
        return moviesDatabase.movieDao();
    }
}