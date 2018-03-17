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
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.network.MoviesService;
import com.jbielak.popularmovies.network.NetworkUtils;
import com.jbielak.popularmovies.utilities.SortType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COLUMNS = 2;
    private static final SortType DEFAULT_SORT_TYPE = SortType.POPULAR;

    @BindView(R.id.recycler_view_movies)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    private MovieAdapter mMovieAdapter;
    private MoviesService moviesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        moviesService = new MoviesService();
        setupFetchMoviesDataCallback();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        loadMoviesData(DEFAULT_SORT_TYPE);
    }

    private void loadMoviesData(SortType sortType) {

        if (NetworkUtils.isOnline(getApplicationContext())) {
            moviesService.getMovies(sortType);
        } else {
            showErrorMessage();
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
                showErrorMessage();
            }
        });
    }

    private void showMoviesDataView() {
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.GONE);
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
            loadMoviesData(SortType.POPULAR);
        }
        if (clickedItemId == R.id.action_sort_by_most_highest_rated) {
            loadMoviesData(SortType.RATING);
        }
        if (clickedItemId == R.id.action_favorites) {
        }
        return false;
    }
}
