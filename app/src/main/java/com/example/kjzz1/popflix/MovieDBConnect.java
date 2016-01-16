package com.example.kjzz1.popflix;

import android.util.Log;

import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.MalformedURLException;

/**
 * Created by cfsuman on 31/05/2015.
 */
public class MovieDBConnect {

    static String stream = null;

    public MovieDBConnect(){
    }

    public String GetHTTPData(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Checking for good connection - if 200, then ok
            if(urlConnection.getResponseCode() == 200)
            {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                // Read "in".
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                stream = stringBuilder.toString();

                urlConnection.disconnect();
            }
            else
            {
                Log.v("MovieDBConnect","Failed");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {

        }
        // Return the data from MovieDB
        return stream;
    }
}