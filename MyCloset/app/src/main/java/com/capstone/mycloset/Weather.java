package com.capstone.mycloset;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather {
    private final double ABSOLUTE_ZERO = 273.15;
    private JSONObject data;

    private double latitude, longitude;
    private String minTemp, maxTemp;
    private String weather;

    public boolean canGetWeather = false;

    public Weather(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        updateWeather();
    }

    public String getMinTemp() {
        return this.minTemp;
    }

    public String getMaxTemp() {
        return this.maxTemp;
    }

    public String getWeather() {
        return this.weather;
    }

    public int getWeatherIcon() {
        int weatherID = new Integer(weather);
        int weatherIcon = R.drawable.wi_unknown;

        switch (weatherID) {
            case 1:
                weatherIcon = R.drawable.wi_sunny;
                break;
            case 2:
                weatherIcon = R.drawable.wi_mostlycloudy;
                break;
            case 3:
                weatherIcon = R.drawable.wi_cloudy;
                break;
            case 4:
                weatherIcon = R.drawable.wi_cloudy;
                break;
            case 9:
                weatherIcon = R.drawable.wi_rain;
                break;
            case 10:
                weatherIcon = R.drawable.wi_rain;
                break;
            case 11:
                weatherIcon = R.drawable.wi_tstorms;
                break;
            case 13:
                weatherIcon = R.drawable.wi_snow;
                break;
            case 50:
                weatherIcon = R.drawable.wi_fog;
                break;
        }
        return weatherIcon;
    }

    public void updateWeather() {
        new AsyncTask<Object, Object, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Object... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="
                            + latitude + "&lon=" + longitude + "&APPID=b8d06925570e44ddd1e5620c22aee88d");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    //weather 로 parsing
                    JSONArray weatherDataArray = (JSONArray) data.get("weather");

                    //weather 로 paring 한것을 JSON 객체로 재졍렬
                    JSONObject object1 = (JSONObject) weatherDataArray.get(0);

                    JSONObject object2 = (JSONObject) data.get("main");

                    minTemp = String.valueOf(object2.getDouble("temp_min")-ABSOLUTE_ZERO);
                    maxTemp = String.valueOf(object2.getDouble("temp_max")-ABSOLUTE_ZERO);
                    minTemp = minTemp.substring(0, 4);
                    maxTemp = maxTemp.substring(0, 4);

                    weather = (String) object1.get("icon");
                    weather = weather.substring(0, 2);

                } catch (Exception e) {
                    System.out.println("Exception "+ e.getMessage());
                }
                canGetWeather = true;
                return null;
            }

            @Override
            protected void onPostExecute(String Void) {
                if(data!=null){
                    Log.d("my weather received",data.toString());
                }
            }
        }.execute();
    }
}
