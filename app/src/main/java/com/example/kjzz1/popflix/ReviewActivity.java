package com.example.kjzz1.popflix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by kjzz1 on 1/16/2016.
 */
//Simple layout for reviews
public class ReviewActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);

        String review = getIntent().getStringExtra("review");

        TextView reviewView = (TextView) findViewById(R.id.reviewView);
        reviewView.setText(review);
    }
}
