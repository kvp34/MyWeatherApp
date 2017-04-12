package com.example.tanvisingh.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.app.Activity;

import com.example.tanvisingh.myweatherapp.services.ConnectURL;
import com.example.tanvisingh.myweatherapp.services.ParseJsonCurrent;
import com.example.tanvisingh.myweatherapp.services.ParseJsonMultiple;

import org.json.JSONException;

import java.net.*;
import java.io.*;
import java.lang.*;


public class MainActivity extends AppCompatActivity {
    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mCurrentWeatherTextView;
    private TextView mSearchResultsTextView;
    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey=getString(R.string.open_weather_maps_app_id);
        String apikey1=apiKey;
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mCurrentWeatherTextView = (TextView) findViewById(R.id.tv_current_weather);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_search_results_five_days);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


    }

    private void searchWeather() {
        String searchQuery = mSearchBoxEditText.getText().toString();
        URL weatherUrlForecast = ConnectURL.buildUrlForecast(searchQuery);
        URL weatherUrlCurrent = ConnectURL.buildUrlCurrent(searchQuery);
        mUrlDisplayTextView.setText(weatherUrlForecast.toString());
        new GithubQueryTask().execute(weatherUrlForecast,weatherUrlCurrent);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... params) {
            URL searchUrlForecast = params[0];
            URL searchUrlCurrent = params[1];
            String weatherResultsForecast = null;
            String weatherResultsCurrent = null;
            String[] weatherResults=new String[2];
            try {
                weatherResultsForecast = ConnectURL.getResponseFromHttpUrl(searchUrlForecast,MainActivity.this);
                weatherResultsCurrent = ConnectURL.getResponseFromHttpUrl(searchUrlCurrent,MainActivity.this);
                weatherResults[0]=weatherResultsForecast;
                weatherResults[1]=weatherResultsCurrent;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weatherResults;
        }


        @Override
        protected void onPostExecute(String[] weatherSearchResults) {
            String result0=null;
            String result1=null;
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherSearchResults[0] != null && !weatherSearchResults[0].equals("")
                    && weatherSearchResults[1] != null && !weatherSearchResults[1].equals("")) {
                ParseJsonMultiple parseJson = new ParseJsonMultiple();
                try {
                    result0 = ParseJsonMultiple.getWeatherStringsMultiple(MainActivity.this, weatherSearchResults[0]);
                    result1 = ParseJsonCurrent.getWeatherStringsCurrent(MainActivity.this, weatherSearchResults[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mCurrentWeatherTextView.setText(result1);
                mSearchResultsTextView.setText(result0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            searchWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
