package com.example.tanvisingh.myweatherapp.services;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.example.tanvisingh.myweatherapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by KaranPatel on 4/8/2017.
 */

public class ConnectURL {

    private static final String TAG = ConnectURL.class.getSimpleName();
    final static String BASE_URL_FORECAST =
            "http://api.openweathermap.org/data/2.5/forecast";
    final static String BASE_URL_CURRENT =
            "http://api.openweathermap.org/data/2.5/weather";

    final static String PARAM_QUERY = "q";
    final static String PARAM_UNIT="units";
    final static String UNIT_VALUE="imperial";
    public static URL buildUrlForecast(String githubSearchQuery) {
        Uri builtUri = Uri.parse(BASE_URL_FORECAST).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_UNIT,UNIT_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildUrlCurrent(String githubSearchQuery) {
        Uri builtUri = Uri.parse(BASE_URL_CURRENT).buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_UNIT,UNIT_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url, Context context) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


        urlConnection.setRequestProperty("x-api-key",context.getString(R.string.open_weather_maps_app_id));
                //"b1d40666bebb4e2de82aee9785f2bbb2");
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }




}
