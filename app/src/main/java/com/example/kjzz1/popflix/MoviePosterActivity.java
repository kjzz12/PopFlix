package com.example.kjzz1.popflix;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MoviePosterActivity extends AppCompatActivity
        implements MoviePosterActivityFragment.OnMovieSelectedListener{

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);

        if (findViewById(R.id.movie_detail_container) != null) {
                mTwoPane = true;

            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
//                        .addToBackStack(null)
//                        .commit();

                android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                DetailFragment fragment = new DetailFragment();

                fragmentTransaction.add(R.id.movie_detail_container, fragment);
                fragmentTransaction.commit();
            }
        } else {
            mTwoPane = false;
        }
    }
    @Override
    public void OnMovieSelected(MovieData item) {

        if (mTwoPane == false) {

            Intent intent = new Intent(this, DetailsActivity.class);

            intent.putExtra("movie", item);

            startActivity(intent);

        } else {
            Bundle arguments = new Bundle();

            arguments.putParcelable("movie", item);
            DetailFragment fragment = new DetailFragment();

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}