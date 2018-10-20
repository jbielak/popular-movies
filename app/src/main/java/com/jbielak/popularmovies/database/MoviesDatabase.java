package com.jbielak.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.jbielak.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MoviesDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "movies";

    public abstract MovieDao movieDao();
}
