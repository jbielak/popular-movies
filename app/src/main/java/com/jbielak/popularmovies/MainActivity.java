package com.jbielak.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Context;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jbielak.popularmovies.adapter.MovieAdapter;
import com.jbielak.popularmovies.data.database.MovieDao;
import com.jbielak.popularmovies.data.model.Movie;
import com.jbielak.popularmovies.data.network.MoviesService;
import com.jbielak.popularmovies.data.network.NetworkUtils;
import com.jbielak.popularmovies.utilities.DisplayType;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SELECTED_DISPLAY_OPTION_KEY = "display_option";
    private static final String MOVIES_KEY = "movies_list";

    @BindView(R.id.recycler_view_movies)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    private static DisplayType sDisplayType = DisplayType.POPULAR;

    @Inject
    MovieDao mMovieDao;

    @Inject
    MoviesService mMoviesService;

    private MovieAdapter mMovieAdapter;
    private List<Movie> movies;

    private EndlessRecyclerViewScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupFetchMoviesDataCallback();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                calculateNoOfColumns(this));
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoviesData(sDisplayType, page);
            }
        };
        mMoviesRecyclerView.addOnScrollListener(mScrollListener);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(SELECTED_DISPLAY_OPTION_KEY)) {
            sDisplayType = DisplayType.getDisplayTypeByValue(savedInstanceState
                    .getString(SELECTED_DISPLAY_OPTION_KEY));
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_KEY)) {
            movies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            mMovieAdapter.setMovies(movies);
            showMoviesDataView();
        } else {
            loadMoviesData(sDisplayType, 1);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SELECTED_DISPLAY_OPTION_KEY, sDisplayType.getValue());
        if (mMovieAdapter.getItemCount() > 0) {
            outState.putParcelableArrayList(MOVIES_KEY, mMovieAdapter.getMoviesArrayList());
        }
        super.onSaveInstanceState(outState);
    }

    private void loadMoviesData(DisplayType displayType, int page) {
        switch (displayType) {
            case POPULAR:
                getMoviesFromNetwork(displayType, page);
                break;
            case RATING:
                getMoviesFromNetwork(displayType, page);
                break;
            case FAVORITES:
                getMoviesFromDb();
                break;
        }
    }

    private void getMoviesFromNetwork(DisplayType displayType, int page) {
        if (NetworkUtils.isOnline(getApplicationContext())) {
            mMoviesService.getMovies(displayType, page);
        } else {
            if (mMovieAdapter.getItemCount() == 0) {
                showErrorMessage(getString(R.string.error_fetch_movies_message));
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_fetch_movies_message),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getMoviesFromDb() {
        LiveData<List<Movie>> movies = mMovieDao.getAllMovies();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favoriteMovies) {
                Log.d(TAG, "Receiving movies database update from LiveData");
                if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
                    mMovieAdapter.setMovies(favoriteMovies);
                    showMoviesDataView();
                } else {
                    showErrorMessage(getString(R.string.favorite_movies_no_favorite_movies_in_db_message));
                }
            }
        });

    }

    private void setupFetchMoviesDataCallback() {
        mMoviesService.setFetchMoviesDataListener(new FetchDataListener<List<Movie>>() {
            @Override
            public void onPreExecute() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(List<Movie> movies) {
                mMovieAdapter.addMovies(movies);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItemId = item.getItemId();

        if (clickedItemId == R.id.action_sort_by_most_popular) {
            clearMoviesRecyclerView();
            loadMoviesData(DisplayType.POPULAR, 1);
            sDisplayType = DisplayType.POPULAR;
        }
        if (clickedItemId == R.id.action_sort_by_most_highest_rated) {
            clearMoviesRecyclerView();
            loadMoviesData(DisplayType.RATING, 1);
            sDisplayType = DisplayType.RATING;
        }
        if (clickedItemId == R.id.action_favorites) {
            clearMoviesRecyclerView();
            loadMoviesData(DisplayType.FAVORITES, 0);
            sDisplayType = DisplayType.FAVORITES;
        }
        return false;
    }

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void clearMoviesRecyclerView() {
        mMovieAdapter.clearMovies();
        mMovieAdapter.notifyDataSetChanged();
        mScrollListener.resetState();
    }
}
