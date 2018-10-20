package com.jbielak.popularmovies;

import com.jbielak.popularmovies.di.AppComponent;
import com.jbielak.popularmovies.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class PopularMoviesApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        return appComponent;
    }
}
