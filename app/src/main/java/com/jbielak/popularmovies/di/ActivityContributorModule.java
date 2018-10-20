package com.jbielak.popularmovies.di;

import com.jbielak.popularmovies.DetailActivity;
import com.jbielak.popularmovies.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityContributorModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity contributeDetailActivity();

}