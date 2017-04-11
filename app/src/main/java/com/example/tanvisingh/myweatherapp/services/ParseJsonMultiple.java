package com.example.tanvisingh.myweatherapp.services;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by KaranPatel on 4/10/2017.
 */

public class ParseJsonMultiple {
    private static String TAG= ParseJsonMultiple.class.getSimpleName();
    public static String getWeatherStringsMultiple(Context context, String weatherJson)
            throws JSONException {

        final String W_MAX = "temp_max";
        final String W_MIN = "temp_min";
        final String W_LIST = "list";
        final String W_MAIN = "main";
        final String W_CODE = "cod";
        String parsedData ="";
        if (weatherJson != null) {
            JSONObject weatherObject = new JSONObject(weatherJson);
            JSONArray weatherArray = weatherObject.getJSONArray(W_LIST);

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

            for (int i = 0; i < weatherArray.length(); i = i + 8) {
                double max;
                double min;

                JSONObject w = weatherArray.getJSONObject(i);
                JSONObject main = w.getJSONObject(W_MAIN);

                max = main.getDouble(W_MAX);
                min = main.getDouble(W_MIN);

                parsedData += "Max: " + max + " , Min: " + min + "\r\n";

            }

        }
        else
        {Log.e(TAG, "Couldn't get response from server.");}
        return parsedData;
    }
}
