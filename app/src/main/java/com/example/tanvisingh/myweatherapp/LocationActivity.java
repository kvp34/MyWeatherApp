package com.example.tanvisingh.myweatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;



public class LocationActivity extends AppCompatActivity {


    private LocationManager locationManager;
    private LocationListener locationListener;
    private String latitude;
    private String longitude;
    //Since this is a weather application and not a location provider, we do not keep a listener for updates.
    //We merely start a new service if we need an update, and get a current location.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);  //use the android location manager api for location
        locationListener = new LocationListener() {  //upon location updates, this listener will be called.
            @Override
            public void onLocationChanged(Location location) {  //when device location changes, send values to main to get the current weather
                locationManager.removeUpdates(locationListener);
                Intent intent = new Intent();
                intent.putExtra("Longitude", String.valueOf(location.getLongitude()));
                intent.putExtra("Latitude", String.valueOf(location.getLatitude()));
                setResult(RESULT_OK, intent);
                finish();
                return;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {  //if no permission to access gps on this device, have user provide permission
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                return;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                return;
            }
        }
        try {
            locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }; //send back the default current weather
        return;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    try {
                        locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("Longitude", String.valueOf(longitude));
                    intent.putExtra("Latitude", String.valueOf(latitude));
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
                return;
        }
        return;
    }
}