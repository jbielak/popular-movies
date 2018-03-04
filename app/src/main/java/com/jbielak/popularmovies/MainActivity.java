package com.jbielak.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jbielak.popularmovies.adapter.MovieAdapter;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.utilities.MovieDbJsonUtils;
import com.jbielak.popularmovies.utilities.NetworkUtils;
import com.jbielak.popularmovies.utilities.SortType;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COLUMNS = 2;
    private static final SortType DEFAULT_SORT_TYPE = SortType.POPULAR;

    @BindView(R.id.recycler_view_movies)
     RecyclerView mMoviesRecyclerView;

    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        if (NetworkUtils.isOnline(getApplicationContext())) {
            loadMoviesData(DEFAULT_SORT_TYPE);
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadMoviesData(SortType sortType) {
        new FetchMoviesTask().execute(sortType.getValue());
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
            loadMoviesData(SortType.POPULAR);
        }
        if (clickedItemId == R.id.action_sort_by_most_highest_rated) {
            loadMoviesData(SortType.RATING);
        }
        return false;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            /* If there's no sort type, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String sortTypeStr = params[0];
            URL moviesRequestUrl = NetworkUtils.buildMoviesUrl(sortTypeStr);

            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                List<Movie> moviesData = MovieDbJsonUtils
                        .getMovies(jsonMoviesResponse);

                return moviesData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                mMovieAdapter.setMovies(movies);
            }
        }
    }

}
