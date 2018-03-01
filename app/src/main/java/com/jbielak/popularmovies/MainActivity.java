package com.jbielak.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jbielak.popularmovies.adapter.MovieAdapter;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.utilities.MovieDeserializer;
import com.jbielak.popularmovies.utilities.NetworkUtils;
import com.jbielak.popularmovies.utilities.PathProvider;
import com.jbielak.popularmovies.utilities.ResponseListener;
import com.jbielak.popularmovies.utilities.SortType;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COLUMNS = 2;

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager gridLayoutManager;

    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        NetworkUtils.getInstance(getApplicationContext());

        if (NetworkUtils.isOnline(getApplicationContext())) {
            getMovies(SortType.POPULAR);
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                    .show();
        }

        mMoviesRecyclerView = findViewById(R.id.recyclerView);
        mMoviesRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mian, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItemId = item.getItemId();

        if (clickedItemId == R.id.action_sort_by_most_popular) {
            getMovies(SortType.POPULAR);
        }
        if (clickedItemId == R.id.action_sort_by_most_highest_rated) {
            getMovies(SortType.RATING);
        }
        return false;
    }

    private void getMovies(SortType sortMethod) {
        URL url = PathProvider.getMoviesUrl(sortMethod);
        NetworkUtils.getInstance()
                .get(url, new ResponseListener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movies = MovieDeserializer.deserializeMovies(response);
                        mMovieAdapter = new MovieAdapter(MainActivity.this, movies);
                        mMoviesRecyclerView.setAdapter(mMovieAdapter);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_fetch_movies_data), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
