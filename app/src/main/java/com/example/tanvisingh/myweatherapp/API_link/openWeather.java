package com.example.tanvisingh.myweatherapp.API_link;

/**
 * Created by tanvisingh on 3/24/17.
 */

public class openWeather
{
    public static String API_KEY="a56060851e25e429476157ff65d0e5e1";
    public static String API_LINK="http://api.openweathermap.org/data/2.5/weather";

    public static String apiRequest(String cityname, String countrycode)
    {
        StringBuilder sb=new StringBuilder(API_LINK);
        sb.append(String.format("?cityname=%s&countrycode=%s",cityname,countrycode,API_KEY));
        return sb.toString();
    }

}
