package com.example.kjzz1.popflix;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for retrieving backdrop image for DetailsActivity.
 */

public class ReviewJSON extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... strings) {
        String stream;
        String review = null;
        String urlString = strings[0];

        //reuse earlier class
        MovieDBConnect hh = new MovieDBConnect();

        stream = hh.GetHTTPData(urlString);

        review = "";

        if (stream != null) {
            try {
                JSONObject reader = new JSONObject(stream);
                JSONArray results = reader.optJSONArray("results");

                for (int i=0;i<=results.length();i++){
                    JSONObject jsonObject = results.getJSONObject(i);
                    String author = jsonObject.getString("author");
                    String content = jsonObject.getString("content");


                    review = review + "Review #" + Integer.toString(i+1) + ":" + "\r\n" +
                            "Reviewed by " + author + "\r\n\r\n" +
                            content + "\r\n\r\n";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (review != null) {
        return review;
        } else {
            review = "No reviews found.";
            return review;
        }
    }

    @Override
    protected void onPostExecute(String review) {
        if (review != null) {
            super.onPostExecute(review);
        }
    }
}
