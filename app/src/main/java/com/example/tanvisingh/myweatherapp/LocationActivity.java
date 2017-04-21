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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_location);
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
       // txtLabel = (TextView) findViewById(R.id.mylocation_weather);
        //btnTwo = (Button) findViewById(R.id.locationbutton);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(locationListener);
                //txtLabel.setText("Latitude:" + location.getLongitude() + ", Longitude:" + location.getLongitude());
                Intent intent = new Intent();
                intent.putExtra("Longitude", String.valueOf(location.getLongitude()));
                intent.putExtra("Latitude", String.valueOf(location.getLatitude()));
                //intent.putExtra("Longitude", "41.8781");  //this was for chicago!
                // intent.putExtra("Latitude", "-87.6298");
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
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                return;
            }
        }
        //configureButton();
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
                    //configureButton();
                    try {
                        locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                return;
        }
        return;
    }

 /*   private void configureButton() {
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });}
  */
}