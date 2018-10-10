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
    public void insertAndGetMovies() throws InterruptedException {
        mMoviesDatabase.movieDao().insertMovie(MOVIE);

        List<Movie> movies = LiveDataTestUtil.getValue(mMoviesDatabase.movieDao().getAllMovies());
        assertThat(movies.size(), is(1));
        Movie dbMovie = movies.get(0);
        assertEquals(dbMovie.getId(), MOVIE.getId());
        assertEquals(dbMovie.getTitle(), MOVIE.getTitle());
    }

    @Test
    public void getMovieById() throws InterruptedException {
        mMoviesDatabase.movieDao().insertMovie(MOVIE);

        Movie dbMovie = LiveDataTestUtil.getValue(mMoviesDatabase.movieDao().getMovie(MOVIE.getId()));
        assertEquals(dbMovie.getId(), MOVIE.getId());
        assertEquals(dbMovie.getTitle(), MOVIE.getTitle());
    }

    @Test
    public void updateMovie() throws InterruptedException {
        mMoviesDatabase.movieDao().insertMovie(MOVIE);
        MOVIE.setTitle("New Title");
        mMoviesDatabase.movieDao().updateMovie(MOVIE);

        Movie dbMovie = LiveDataTestUtil.getValue(mMoviesDatabase.movieDao().getMovie(MOVIE.getId()));
        assertEquals(dbMovie.getId(), MOVIE.getId());
        assertEquals(dbMovie.getTitle(), "New Title");
    }

    @Test
    public void deleteMovie() throws InterruptedException {
        mMoviesDatabase.movieDao().insertMovie(MOVIE);
        mMoviesDatabase.movieDao().deleteMovie(MOVIE);
        List<Movie> movies = LiveDataTestUtil.getValue(mMoviesDatabase.movieDao().getAllMovies());
        mMoviesDatabase.movieDao().deleteMovie(MOVIE);
        assertThat(movies.size(), is(0));
    }

}
