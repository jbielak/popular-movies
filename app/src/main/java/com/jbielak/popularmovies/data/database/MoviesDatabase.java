package com.jbielak.popularmovies.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.jbielak.popularmovies.data.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MoviesDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "movies";

    public abstract MovieDao movieDao();
}
