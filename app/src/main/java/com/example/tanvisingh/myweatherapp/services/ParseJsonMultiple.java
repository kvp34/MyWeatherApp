package com.example.tanvisingh.myweatherapp.services;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KaranPatel on 4/10/2017.
 */

public class ParseJsonMultiple {
    private static String TAG= ParseJsonMultiple.class.getSimpleName();
    public static WeatherDetailsMultiple getWeatherStringsMultiple(Context context, String weatherJson)
            throws JSONException {

        final String W_MAX = "max";
        final String W_MIN = "min";
        final String W_LIST = "list";
        final String W_MAIN = "main";
        final String W_CODE = "cod";
        final String W_TEMP = "temp";
        final String W_DATE="dt";
        WeatherDetailsMultiple wdm=new WeatherDetailsMultiple();
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

            List<WeatherDetails> list = new ArrayList<>();
            for (int i = 0; i < weatherArray.length(); i++) {
                Long max;
                Long min;
                WeatherDetails wd=new WeatherDetails();
                JSONObject w = weatherArray.getJSONObject(i);
                JSONObject temp=w.getJSONObject(W_TEMP);
                wd.setDate(String.valueOf(w.getLong(W_DATE)));
                wd.setMaxTemp(String.valueOf(temp.getLong(W_MAX)));
                wd.setMinTemp(String.valueOf(temp.getLong(W_MIN)));

                list.add(wd);
            }
            wdm.setMultipleDays(list);

        }
        else
        {Log.e(TAG, "Couldn't get response from server.");}
        return wdm;
    }
}
