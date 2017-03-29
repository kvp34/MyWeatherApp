package com.example.tanvisingh.myweatherapp.gettingURL;

/**
 * Created by tanvisingh on 3/25/17.
 */
import java.net.*;
import java.io.*;
import java.lang.*;

public class connectURL
{
   static String bufferdata=null;

    public String getHttData(String urlstring)
    {
        try{
            URL u=new URL(urlstring);
            HttpURLConnection con= ((HttpURLConnection)u.openConnection() );
            if(con.getResponseCode()==200)
            {
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String st;
                StringBuilder sb = new StringBuilder();
                while ((st=bf.readLine() )!= null)
                    sb.append(st);
                 bufferdata=sb.toString();
                con.disconnect();

            }
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return bufferdata;

    }

}
