package com.example.zhangwenxian.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    String JSON;
    String City;
    String State;
    String Degree;

    public void NEXT_24_HOURS(View view) {
        View next24 = findViewById(R.id.next24hours);
        View next7 = findViewById(R.id.next7dyas);
        View plus = findViewById(R.id.plus);
        View secondPart = findViewById(R.id.SecondPart);
        next24.setVisibility(View.VISIBLE);
        plus.setVisibility(View.VISIBLE);
        next7.setVisibility(View.GONE);
        secondPart.setVisibility(View.GONE);
        Button button = (Button) findViewById(R.id.next24_button);
        button.setBackgroundResource(R.drawable.button_on);
        button = (Button) findViewById(R.id.next7_button);
        button.setBackgroundResource(android.R.drawable.btn_default_small);
    }

    public void NEXT_7_DAYS(View view) {
        View next24 = findViewById(R.id.next24hours);
        View next7 = findViewById(R.id.next7dyas);
        next24.setVisibility(View.GONE);
        next7.setVisibility(View.VISIBLE);
        Button button = (Button) findViewById(R.id.next7_button);
        button.setBackgroundResource(R.drawable.button_on);
        button = (Button) findViewById(R.id.next24_button);
        button.setBackgroundResource(android.R.drawable.btn_default_small);
    }

    public void Plus(View view){
        View plus = findViewById(R.id.plus);
        View secondPart = findViewById(R.id.SecondPart);
        secondPart.setVisibility(View.VISIBLE);
        plus.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();

        JSONObject jsonObject = null;

        if (extras != null) {
            JSON = extras.getString("JSON");
            State = extras.getString("state");
            City = extras.getString("city");
            Degree = extras.getString("degree");
        }

        try {
            jsonObject = new JSONObject(JSON);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        TextView finder;
        finder = (TextView) findViewById(R.id.detail_title);
        finder.setText("More Details for " + City + ", " + State);
        finder = (TextView) findViewById(R.id.temp_unit);
        finder.setText(Degree.equals("Fahrenheit") ? "Temp(°F)" : "Temp(°C)");
        SimpleDateFormat hourly = new SimpleDateFormat("hh:00 a");
        SimpleDateFormat daily = new SimpleDateFormat("EEEE, MMM d");
        String icon_part1 = "", icon_part2="", icon_day="";
        for (int i = 1; i < 8; i++) {
            int j = i-1;
            int date_id = getResources().getIdentifier("date" + j, "id", getPackageName());
            TextView date = (TextView) findViewById(date_id);
            int img_day_id = getResources().getIdentifier("dayimg" + j, "id", getPackageName());
            ImageView img_day = (ImageView) findViewById(img_day_id);
            int minmax_id = getResources().getIdentifier("minmax" + j, "id", getPackageName());
            TextView minmax = (TextView) findViewById(minmax_id);

            try{
                date.setText(daily.format(new Date(jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getInt("time") * 1000L)));
                icon_day=jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getString("icon");
                Log.d("icon",icon_day);

                switch (icon_day) {
                    case "clear-day":
                        img_day.setBackgroundResource(R.drawable.clear);
                        break;
                    case "clear-night":
                        img_day.setBackgroundResource(R.drawable.clear_night);
                        break;
                    case "rain":
                        img_day.setBackgroundResource(R.drawable.rain);
                        break;
                    case "snow":
                        img_day.setBackgroundResource(R.drawable.snow);
                        break;
                    case "sleet":
                        img_day.setBackgroundResource(R.drawable.sleet);
                        break;
                    case "wind":
                        img_day.setBackgroundResource(R.drawable.clear);
                        break;
                    case "fog":
                        img_day.setBackgroundResource(R.drawable.fog);
                        break;
                    case "cloudy":
                        img_day.setBackgroundResource(R.drawable.cloudy);
                        break;
                    case "partly-cloudy-day":
                        img_day.setBackgroundResource(R.drawable.cloud_day);
                        break;
                    case "partly-cloudy-night":
                        img_day.setBackgroundResource(R.drawable.cloud_night);
                        break;
                }
                String min = String.format("%.0f", jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureMin"));
                String max = String.format("%.0f", jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(i).getDouble("temperatureMax"));

                minmax.setText("Min: "+min+(Degree.equals("Fahrenheit") ? "°F" : "°C")+" | Max: "+max+(Degree.equals("Fahrenheit") ? "°F" : "°C"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 24; i++) {
            int j = i + 100;
            int k = i + 24;
            int timeId_part1 = getResources().getIdentifier("time" + i, "id", getPackageName());
            TextView time_part1 = (TextView) findViewById(timeId_part1);
            int imgId_part1 = getResources().getIdentifier("img" + i, "id", getPackageName());
            ImageView img_part1 = (ImageView) findViewById(imgId_part1);
            int tempId_part1 = getResources().getIdentifier("temp" + i, "id", getPackageName());
            TextView tempnum_part1 = (TextView) findViewById(tempId_part1);
            int timeId_part2 = getResources().getIdentifier("time" + j, "id", getPackageName());
            TextView time_part2 = (TextView) findViewById(timeId_part2);
            int imgId_part2 = getResources().getIdentifier("img" + j, "id", getPackageName());
            ImageView img_part2 = (ImageView) findViewById(imgId_part2);
            int tempId_part2 = getResources().getIdentifier("temp" + j, "id", getPackageName());
            TextView tempnum_part2 = (TextView) findViewById(tempId_part2);
            try {
                time_part1.setText(hourly.format(new Date(jsonObject.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getInt("time") * 1000L)));
                time_part2.setText(hourly.format(new Date(jsonObject.getJSONObject("hourly").getJSONArray("data").getJSONObject(k).getInt("time") * 1000L)));

                icon_part1=jsonObject.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getString("icon");
                icon_part2=jsonObject.getJSONObject("hourly").getJSONArray("data").getJSONObject(k).getString("icon");
                tempnum_part1.setText(String.format("%.0f", jsonObject.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getDouble("temperature")));
                tempnum_part2.setText(String.format("%.0f", jsonObject.getJSONObject("hourly").getJSONArray("data").getJSONObject(k).getDouble("temperature")));
                switch (icon_part1) {
                    case "clear-day":
                        img_part1.setBackgroundResource(R.drawable.clear);
                        break;
                    case "clear-night":
                        img_part1.setBackgroundResource(R.drawable.clear_night);
                        break;
                    case "rain":
                        img_part1.setBackgroundResource(R.drawable.rain);
                        break;
                    case "snow":
                        img_part1.setBackgroundResource(R.drawable.snow);
                        break;
                    case "sleet":
                        img_part1.setBackgroundResource(R.drawable.sleet);
                        break;
                    case "wind":
                        img_part1.setBackgroundResource(R.drawable.clear);
                        break;
                    case "fog":
                        img_part1.setBackgroundResource(R.drawable.fog);
                        break;
                    case "cloudy":
                        img_part1.setBackgroundResource(R.drawable.cloudy);
                        break;
                    case "partly-cloudy-day":
                        img_part1.setBackgroundResource(R.drawable.cloud_day);
                        break;
                    case "partly-cloudy-night":
                        img_part1.setBackgroundResource(R.drawable.cloud_night);
                        break;
                }

                switch (icon_part2) {
                    case "clear-day":
                        img_part2.setBackgroundResource(R.drawable.clear);
                        break;
                    case "clear-night":
                        img_part2.setBackgroundResource(R.drawable.clear_night);
                        break;
                    case "rain":
                        img_part2.setBackgroundResource(R.drawable.rain);
                        break;
                    case "snow":
                        img_part2.setBackgroundResource(R.drawable.snow);
                        break;
                    case "sleet":
                        img_part2.setBackgroundResource(R.drawable.sleet);
                        break;
                    case "wind":
                        img_part2.setBackgroundResource(R.drawable.clear);
                        break;
                    case "fog":
                        img_part2.setBackgroundResource(R.drawable.fog);
                        break;
                    case "cloudy":
                        img_part2.setBackgroundResource(R.drawable.cloudy);
                        break;
                    case "partly-cloudy-day":
                        img_part2.setBackgroundResource(R.drawable.cloud_day);
                        break;
                    case "partly-cloudy-night":
                        img_part2.setBackgroundResource(R.drawable.cloud_night);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
