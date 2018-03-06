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
import com.jbielak.popularmovies.utilities.NetworkUtils;
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
    private FetchMoviesTask fetchMoviesTask;

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

        loadMoviesData(DEFAULT_SORT_TYPE);
    }

    private void loadMoviesData(SortType sortType) {
        setupFetchMoviesTask();
        showMoviesDataView();

        if (NetworkUtils.isOnline(getApplicationContext())) {
            fetchMoviesTask.execute(sortType.getValue());
        } else {
            mErrorMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showMoviesDataView() {
        mErrorMessageTextView.setVisibility(View.GONE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMoviesRecyclerView.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void setupFetchMoviesTask() {
        fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.setMoviesTaskCallback(new MoviesTaskCallback() {
            @Override
            public void onAsyncTaskPreExecute() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAsyncTaskComplete(List<Movie> movies) {
                mLoadingIndicator.setVisibility(View.GONE);
                if (movies != null) {
                    showMoviesDataView();
                    mMovieAdapter.setMovies(movies);
                } else {
                    showErrorMessage();
                }
            }
        });
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
}
