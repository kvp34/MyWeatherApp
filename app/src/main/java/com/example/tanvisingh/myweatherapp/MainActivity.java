package com.example.tanvisingh.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.app.Activity;

import com.example.tanvisingh.myweatherapp.services.ConnectURL;
import com.example.tanvisingh.myweatherapp.services.ParseJsonCurrent;
import com.example.tanvisingh.myweatherapp.services.ParseJsonMultiple;
import com.example.tanvisingh.myweatherapp.services.WeatherDetails;
import com.example.tanvisingh.myweatherapp.services.WeatherDetailsMultiple;

import org.json.JSONException;

import java.net.*;
import java.io.*;
import java.lang.*;

//Cecilia was here
public class MainActivity extends AppCompatActivity {
    private EditText mSearchBoxEditText;
    private TextView mCurrentWeatherTextView;
    private TextView mSearchResultsTextView;
    private ProgressBar mLoadingIndicator;
    private  Switch mWeatherUnit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mCurrentWeatherTextView = (TextView) findViewById(R.id.tv_current_weather);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_search_results_five_days);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mWeatherUnit=(Switch)findViewById(R.id.s_weather_unit);

        mWeatherUnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    searchWeather("Metric");
                }
                else {
                    searchWeather("Imperial");
                }
            }
        });

    }

    private void searchWeather(String unitValue) {
        String searchQuery = mSearchBoxEditText.getText().toString();
        URL weatherUrlForecast = ConnectURL.buildUrlForecast(searchQuery,unitValue);
        URL weatherUrlCurrent = ConnectURL.buildUrlCurrent(searchQuery,unitValue);
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
            String result0="";
            String result1="";
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherSearchResults[0] != null && !weatherSearchResults[0].equals("")
                    && weatherSearchResults[1] != null && !weatherSearchResults[1].equals("")) {
                ParseJsonMultiple parseJson = new ParseJsonMultiple();
                try {
                    WeatherDetailsMultiple wdm = ParseJsonMultiple.getWeatherStringsMultiple(MainActivity.this, weatherSearchResults[0]);
                    for (int i=0;i<wdm.getMultipleDays().size();i++){
                        result0+=wdm.getMultipleDays().get(i).getMaxTemp()+" "+wdm.getMultipleDays().get(i).getMinTemp()+"\r\n";
                    }
                    WeatherDetails wd=ParseJsonCurrent.getWeatherStringsCurrent(MainActivity.this, weatherSearchResults[1]);
                    result1= result1+"Current: "+wd.getCurrentTemp()+ "\r\nMax: "+wd.getMaxTemp()+"\r\nMin: "+wd.getMinTemp()
                            +"\r\nHumidity: "+wd.getHumidity()+"\r\nDescription: "+wd.getWeatherMain()+"\r\nIcon: "+wd.getWeatherIconId()
                            +"\r\nWindSpeed: "+wd.getWindSpeed();
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
            searchWeather("Imperial");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
