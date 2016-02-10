package com.example.kjzz1.popflix;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for retrieving backdrop image for DetailsActivity.
 */

public class YouTubeJSON extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... strings) {
        String stream;
        String youTubeURL = null;
        String urlString = strings[0];

        //reuse earlier class
        MovieDBConnect hh = new MovieDBConnect();

        stream = hh.GetHTTPData(urlString);

        if (stream != null) {
            try {
                JSONObject reader = new JSONObject(stream);
                JSONArray results = reader.optJSONArray("results");

                //it's too bad more reviews aren't included, or I'd choose a random one here as well
                JSONObject jsonObject = results.getJSONObject(0);
                youTubeURL = "https://www.youtube.com/watch?v=" + jsonObject.getString("key");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return youTubeURL;
    }

    @Override
    protected void onPostExecute(String youTubeURL) {
        if (youTubeURL != null) {
            super.onPostExecute(youTubeURL);
        }
    }
}
