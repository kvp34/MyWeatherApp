package com.example.tanvisingh.myweatherapp.services;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by KaranPatel on 4/10/2017.
 */

public class ParseJsonCurrent {
    private static String TAG= ParseJsonCurrent.class.getSimpleName();
    public static WeatherDetails getWeatherStringsCurrent(Context context, String weatherJson)
            throws JSONException {
        final String W_CODE = "cod";
        final String W_TEMPERATURE = "temp";
        final String W_MAX = "temp_max";
        final String W_MIN = "temp_min";
        final String W_MAIN = "main";
        final String W_HUMIDITY = "humidity";
        final String W_WEATHER_OBJECT="weather";
        final String W_WIND_OBJECT="wind";
        final String W_ICONID="icon";
        final String W_SPEED="speed";

        WeatherDetails wd = null;
        if (weatherJson != null) {

            JSONObject weatherObject = new JSONObject(weatherJson);
            JSONObject main = weatherObject.getJSONObject(W_MAIN);
            JSONArray description=weatherObject.getJSONArray(W_WEATHER_OBJECT);
            JSONObject descriptionMain= description.getJSONObject(0);
            JSONObject wind =weatherObject.getJSONObject(W_WIND_OBJECT);

            if (weatherObject.has(W_CODE)) {
                int errorCode = weatherObject.getInt(W_CODE);

                switch (errorCode) {
                    case HttpURLConnection.HTTP_OK:
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Log.e(TAG, "Location Not Found. ErrorCode: " + errorCode);

                    default:
                        Log.e(TAG, "Server Down");

                }
            }

            wd = new WeatherDetails();
            wd.setMaxTemp(String.valueOf(main.getLong(W_MAX)));
            wd.setMinTemp(String.valueOf(main.getLong(W_MIN)));
            wd.setCurrentTemp(String.valueOf(main.getLong(W_TEMPERATURE)));
            wd.setHumidity(String.valueOf(main.getLong(W_HUMIDITY)));
            wd.setWeatherMain(descriptionMain.getString(W_MAIN));
            wd.setWeatherIconId(descriptionMain.getString(W_ICONID));
            wd.setWindSpeed(String.valueOf(wind.getLong(W_SPEED)));
        } else {
            Log.e(TAG, "Couldn't get response from server.");
        }
        return wd;
    }
}
