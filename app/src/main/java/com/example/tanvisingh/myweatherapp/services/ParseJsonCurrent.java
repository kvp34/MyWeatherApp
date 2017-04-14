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
    public static String getWeatherStringsCurrent(Context context, String weatherJson)
            throws JSONException {
        final String W_CODE = "cod";
        final String W_TEMPERATURE = "temp";
        final String W_MAX = "temp_max";
        final String W_MIN = "temp_min";
        final String W_MAIN = "main";
        String parsedData ="";

        if (weatherJson!=null) {
            Long max;
            Long min;
            Long current;

            JSONObject weatherObject = new JSONObject(weatherJson);
            JSONObject main = weatherObject.getJSONObject(W_MAIN);

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

            current = main.getLong(W_TEMPERATURE);
            max = main.getLong(W_MAX);
            min = main.getLong(W_MIN);

            parsedData += "Today: " + current + ", Max: " + max + ", Min: " + min + "\r\n";


        }
        else
        {
            Log.e(TAG, "Couldn't get response from server.");}
        return parsedData;
    }
}
