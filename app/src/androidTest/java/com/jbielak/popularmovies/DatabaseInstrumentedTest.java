package com.jbielak.popularmovies;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.jbielak.popularmovies.database.MoviesDatabase;
import com.jbielak.popularmovies.model.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {

    MoviesDatabase mMoviesDatabase;
    Movie MOVIE = new Movie(1L, "Godfather", new Date(), "path",
            1.5, 1.5, "Overview");

    @Before
    public void initDb() throws Exception {
        mMoviesDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                MoviesDatabase.class)
                .build();
    }

    @After
    public void closeDb() throws Exception {
        mMoviesDatabase.close();
    }

    @Test
    public void insertAndGetMovie() {
        mMoviesDatabase.movieDao().insertMovie(MOVIE);

        List<Movie> movies = mMoviesDatabase.movieDao().getAllMovies();
        assertThat(movies.size(), is(1));
        Movie dbMovie = movies.get(0);
        assertEquals(dbMovie.getId(), MOVIE.getId());
        assertEquals(dbMovie.getTitle(), MOVIE.getTitle());
    }

}
