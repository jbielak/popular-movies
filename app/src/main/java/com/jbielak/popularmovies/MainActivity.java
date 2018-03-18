package com.jbielak.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jbielak.popularmovies.adapter.MovieAdapter;
import com.jbielak.popularmovies.data.MoviesDbService;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.network.MoviesService;
import com.jbielak.popularmovies.network.NetworkUtils;
import com.jbielak.popularmovies.utilities.DisplayType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COLUMNS = 2;
    private static final String SELECTED_DISPLAY_OPTION_KEY = "display_option";

    @BindView(R.id.recycler_view_movies)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    private static DisplayType sDisplayType = DisplayType.POPULAR;

    private MovieAdapter mMovieAdapter;
    private MoviesService moviesService;
    private MoviesDbService moviesDbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            sDisplayType = DisplayType.valueOf(savedInstanceState
                    .getString(SELECTED_DISPLAY_OPTION_KEY));
        }

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        moviesDbService = new MoviesDbService(getApplicationContext());
        moviesService = new MoviesService();
        setupFetchMoviesDataCallback();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        loadMoviesData(sDisplayType);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SELECTED_DISPLAY_OPTION_KEY, sDisplayType.getValue());
        super.onSaveInstanceState(outState);
    }

    private void loadMoviesData(DisplayType displayType) {
        switch (displayType) {
            case POPULAR:
                getMoviesFromNetwork(displayType);
                break;
            case RATING:
                getMoviesFromNetwork(displayType);
                break;
            case FAVORITES:
                getMoviesFromDb();
                break;
        }
    }

    private void getMoviesFromNetwork(DisplayType displayType) {
        if (NetworkUtils.isOnline(getApplicationContext())) {
            moviesService.getMovies(displayType);
        } else {
            showErrorMessage(getString(R.string.error_fetch_movies_message));
        }
    }
    private void getMoviesFromDb() {
        List<Movie> favoriteMovies = moviesDbService.getFavoriteMovies();
        if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
            mMovieAdapter.setMovies(favoriteMovies);
            showMoviesDataView();
        } else {
            showErrorMessage(getString(R.string.favorite_movies_no_favorite_movies_in_db_message));
        }
    }


    private void setupFetchMoviesDataCallback() {
        moviesService.setFetchMoviesDataListener(new FetchDataListener<List<Movie>>() {
            @Override
            public void onPreExecute() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(List<Movie> movies) {
                mMovieAdapter.setMovies(movies);
                showMoviesDataView();
            }

            @Override
            public void onError() {
                showErrorMessage(getString(R.string.error_fetch_movies_data));
            }
        });
    }

    private void showMoviesDataView() {
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String message) {
        mLoadingIndicator.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.GONE);
        mErrorMessageTextView.setText(message);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
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
            loadMoviesData(DisplayType.POPULAR);
            sDisplayType = DisplayType.POPULAR;
        }
        if (clickedItemId == R.id.action_sort_by_most_highest_rated) {
            loadMoviesData(DisplayType.RATING);
            sDisplayType = DisplayType.RATING;
        }
        if (clickedItemId == R.id.action_favorites) {
            loadMoviesData(DisplayType.FAVORITES);
            sDisplayType = DisplayType.FAVORITES;
        }
        return false;
    }
}
