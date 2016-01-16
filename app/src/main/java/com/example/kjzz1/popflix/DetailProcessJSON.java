package com.example.kjzz1.popflix;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for retrieving backdrop image for DetailsActivity.
 */

public class DetailProcessJSON extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... strings) {
        String stream;
        String backdrop = null;
        String urlString = strings[0];

        //reuse earlier class
        MovieDBConnect hh = new MovieDBConnect();

        stream = hh.GetHTTPData(urlString);

        if (stream != null) {
            try {
                JSONObject reader = new JSONObject(stream);
                JSONArray results = reader.optJSONArray("backdrops");

                //getting random number for array of backdrops so that each time DetailsActivity is
                // loaded there is a diff image.
                Double d = (Math.floor(Math.random() * results.length()));
                Integer i = (int) d.doubleValue();

                JSONObject jsonObject = results.getJSONObject(i);
                backdrop = "http://image.tmdb.org/t/p/w500" + jsonObject.getString("file_path");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return backdrop;
    }

    @Override
    protected void onPostExecute(String backdrop) {
        if (backdrop != null) {
            super.onPostExecute(backdrop);
        }
    }
}
