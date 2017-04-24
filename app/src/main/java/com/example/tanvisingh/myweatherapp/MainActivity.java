package com.example.tanvisingh.myweatherapp;
import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.widget.*;
import android.view.*;

import com.example.tanvisingh.myweatherapp.services.ConnectURL;
import com.example.tanvisingh.myweatherapp.services.ParseJsonCurrent;
import com.example.tanvisingh.myweatherapp.services.ParseJsonMultiple;
import com.example.tanvisingh.myweatherapp.services.WeatherDetails;
import com.example.tanvisingh.myweatherapp.services.WeatherDetailsMultiple;

import org.json.JSONException;

import java.net.*;
import java.io.*;
import java.lang.*;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ONE = 100;
    private EditText mSearchBoxEditText;
    private TextView mCurrentWeatherTextView;
    private TextView mSearchResultsTextView;
    private ImageView imWeatherIcon;
    private ProgressBar mLoadingIndicator;
    private Switch mWeatherUnit;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location myLocation;
    String latitude="40.497604";//our default values are for Somerset, NJ
    String longitude="-74.488487";
    String tempUnit="Imperial";
    boolean localweathertoggle=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        imWeatherIcon = (ImageView) findViewById(R.id.iv_weather_icons);
        mCurrentWeatherTextView = (TextView) findViewById(R.id.tv_current_weather);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_search_results_five_days);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mWeatherUnit=(Switch)findViewById(R.id.s_weather_unit);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);  //use the android location manager api for location
        locationListener = new myLocationListener();

        startUpdatesOrGetLastLocation();

        mWeatherUnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    buttonView.setText("Celcius");
                    tempUnit="Metric";
                    if (!localweathertoggle)
                        searchWeather(tempUnit);
                    else
                        defaultWeather(tempUnit,longitude,latitude);
                }
                else {
                    buttonView.setText("Fahrenheit");
                    tempUnit="Imperial";
                    if (!localweathertoggle)
                        searchWeather(tempUnit);
                    else
                        defaultWeather(tempUnit,longitude,latitude);
                }
            }
        });
        return;
    }
    private void defaultWeather(String unitValue,String longitude, String latitude) {
        localweathertoggle=true;
        URL weatherUrlForecast = ConnectURL.buildUrlForecastLatLong(latitude,longitude,unitValue);
        URL weatherUrlCurrent = ConnectURL.buildUrlCurrentLatLong(latitude,longitude,unitValue);
        new weatherQueryTask().execute(weatherUrlForecast,weatherUrlCurrent);
        mSearchBoxEditText.setText("");
    }
    private void searchWeather(String unitValue) {
        localweathertoggle=false;
        String searchQuery = mSearchBoxEditText.getText().toString();
        URL weatherUrlForecast = ConnectURL.buildUrlForecast(searchQuery,unitValue);
        URL weatherUrlCurrent = ConnectURL.buildUrlCurrent(searchQuery,unitValue);
        new weatherQueryTask().execute(weatherUrlForecast,weatherUrlCurrent);
    }


    public class weatherQueryTask extends AsyncTask<URL, Void, String[]> {

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

                        result0+= wdm.getMultipleDays().get(i).getDate()
                                +"    Max: " + wdm.getMultipleDays().get(i).getMaxTemp() + "°"
                                +" Min: " + wdm.getMultipleDays().get(i).getMinTemp() + "°"
                                +"   Desc: "+wdm.getMultipleDays().get(i).getWeatherMain()+ "\r\n";
                    }
                    WeatherDetails wd=ParseJsonCurrent.getWeatherStringsCurrent(MainActivity.this, weatherSearchResults[1]);
                    if(tempUnit == "Metric"){
                        result1= result1
                                + "City: "+wd.getCityName()
                                +"\r\nCurrent: " +wd.getCurrentTemp()+ "°C"
                                +" \r\nMax: "+wd.getMaxTemp()+"°C"
                                +" \r\nMin: "+wd.getMinTemp()+"°C"
                                +" \r\nHumidity: "+wd.getHumidity()+ "%"
                                +" \r\nDescription: "+wd.getWeatherMain()
                                +"\r\nWindSpeed: "+wd.getWindSpeed()+"m/s";
                    }else{
                        result1= result1
                                +"City: "+wd.getCityName()
                                +"\r\nCurrent: "+wd.getCurrentTemp()+ "°F"
                                +" \r\nMax: "+wd.getMaxTemp()+"°F"
                                +" \r\nMin: "+wd.getMinTemp()+"°F"
                                +"\r\nHumidity: "+wd.getHumidity()+ "%"
                                +"\r\nDescription: "+wd.getWeatherMain()
                                +"\r\nWindSpeed: "+wd.getWindSpeed()+"m/h";
                    }
                    switch (wd.getWeatherMain()) {
                        case "Clear": imWeatherIcon.setImageResource(R.drawable.clear); break;
                        case "Rain": imWeatherIcon.setImageResource(R.drawable.rain); break;
                        case "Snow": imWeatherIcon.setImageResource(R.drawable.snow); break;
                        case "Mist": imWeatherIcon.setImageResource(R.drawable.mist); break;
                        case "Clouds": imWeatherIcon.setImageResource(R.drawable.clouds); break;
                        case "Extreme": imWeatherIcon.setImageResource(R.drawable.extreme); break;
                        default: imWeatherIcon.setImageResource(R.drawable.defweathericon); break;
                    }
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
            searchWeather(tempUnit);
            //defaultWeather("Imperial");
            return true;
        }
        if (itemThatWasClickedId == R.id.current_location) {
            startUpdatesOrGetLastLocation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startUpdatesOrGetLastLocation();
        }
        return;
    }
    public class myLocationListener extends Service implements LocationListener {  //upon location updates, this listener will be called.
        @Override
        public void onLocationChanged(Location location) {  //when device location changes, send values to main to get the current weather
            locationManager.removeUpdates(locationListener);
            myLocation = location;
            longitude=String.valueOf(location.getLongitude());
            latitude=String.valueOf(location.getLatitude());
            defaultWeather(tempUnit, String.valueOf(myLocation.getLongitude()), String.valueOf(myLocation.getLatitude()));
            location.reset();
            return;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return;
        }
        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }
    }

    public void startUpdatesOrGetLastLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                return;
            }
        }
        try {
            locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (locationManager!=null) { //see if we can display last location before updates happen
            try {
                Location newLocation=locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if (newLocation==null) return; //no last location, need to wait for update
                if (isLocationBetter(newLocation,myLocation)==true) {
                    myLocation=newLocation;
                    longitude=String.valueOf(myLocation.getLongitude());
                    latitude=String.valueOf(myLocation.getLatitude());
                    defaultWeather(tempUnit, String.valueOf(myLocation.getLongitude()), String.valueOf(myLocation.getLatitude()));
                } else {
                    longitude=String.valueOf(myLocation.getLongitude());
                    latitude=String.valueOf(myLocation.getLatitude());
                    defaultWeather(tempUnit, String.valueOf(myLocation.getLongitude()), String.valueOf(myLocation.getLatitude()));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            return;
        }

    }
    protected boolean isLocationBetter(Location location, Location lastLocation) {
        if (lastLocation == null)
            return true;//so must be better
        if ((location.getTime()-lastLocation.getTime())>1000*60*2)
            return true; //is cached one more than two minutes old?
        if ((location.getTime()-lastLocation.getTime())<1000*60*2)
            return false; //is cached one less than two minutes old?
        return false; //default is to use the cached one
    }

}
