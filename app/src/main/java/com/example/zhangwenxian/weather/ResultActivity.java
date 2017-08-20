package com.example.zhangwenxian.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ResultActivity extends AppCompatActivity {

    String street;
    String city;
    String state;
    String degree;
    String JSONString;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        LoginManager.getInstance().logOut();
        FacebookSdk.sdkInitialize(getApplicationContext());


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager,
                new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        if(result.getPostId()!=null){
                            success();
                        }
                        else{
                            cancel();
                        }
                    }

                    @Override
                    public void onCancel() {
                        cancel();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        error();
                    }
                });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            street = extras.getString("street");
            city = extras.getString("city");
            state = extras.getString("state");
            degree = extras.getString("degree");
        }

        String regex = " ";
        String streetOut = street.replaceAll(regex, "%20");
        String cityOut = city.replaceAll(regex, "%20");
        String url="http://amycsci571-env.elasticbeanstalk.com/?street=" + streetOut + "&city=" + cityOut + "&state=" + state + "&degree=" + degree;
        new JSONTask().execute(url);

    }

    public void mapAct(View view) throws JSONException {
        JSONObject JSON = new JSONObject(JSONString);
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("lat", String.valueOf(JSON.getDouble("latitude")));
        intent.putExtra("lng", String.valueOf(JSON.getDouble("longitude")));
        startActivity(intent);
    }

    public  void facebookAct(View view) throws JSONException {

        JSONObject JSONO = null;
        try {
            Log.d("string",JSONString);
            JSONO = new JSONObject(JSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String icon = (String) JSONO.getJSONObject("currently").getString("icon");
        String url_string = "";
        switch (icon) {
            case "clear-day":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/clear.png";
                break;
            case "clear-night":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/clear_night.png";
                break;
            case "rain":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/rain.png";
                break;
            case "snow":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/snow.png";
                break;
            case "sleet":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/sleet.png";
                break;
            case "wind":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/clear.png";
                break;
            case "fog":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/fog.png";
                break;
            case "cloudy":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/cloudy.png";
                break;
            case "partly-cloudy-day":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/cloud_day.png";
                break;
            case "partly-cloudy-night":
                url_string = "http://www-scf.usc.edu/~wenxianz/HW8/css/cloud_night.png";
                break;
        }

        Uri imgUri=Uri.parse(url_string);
        //imageView.setImageURI(null);
        //imageView.setImageURI(imgUri);
        if (shareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Current Weather in " + city + ", " + state)
                    .setContentDescription(JSONO.getJSONObject("currently").getString("summary") + ", " + String.format("%.0f", JSONO.getJSONObject("currently").getDouble("temperature")) + (degree.equals("Fahrenheit") ? " °F" : " °C"))
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .setImageUrl(imgUri)
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void cancel(){
        Toast.makeText(this, "Posted Cancelled", Toast.LENGTH_SHORT).show();
    }
    public void success(){
        Toast.makeText(this, "Facebook Post Successful", Toast.LENGTH_SHORT).show();
    }
    public void error(){
        Toast.makeText(this, "Errors for Post", Toast.LENGTH_SHORT).show();
    }

    public void DetailsAct(View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("JSON", JSONString);
        intent.putExtra("city", city);
        intent.putExtra("state", state);
        intent.putExtra("degree", degree);
        startActivity(intent);
    }


    class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader br = null;
            StringBuffer buffer = new StringBuffer();

            try {
                Log.d("url", params[0]);
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();


                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();

                InputStream stream = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            JSONString = buffer.toString();
            Log.d("json",JSONString);
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonObject = null;
            JSONObject currently;
            String condition = "", temp = "", tempu = "", htemp = "", ltemp = "", icon = "", pre = "", rain = "", wind = "", due = "";
            String humidity = "", visibility = "", preValue = "", formattedSunrise = "", formattedSunset = "";

            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            try {
                currently = jsonObject.getJSONObject("currently");

                condition = (String) currently.getString("summary") + " in " + city + ", " + state;
                temp = String.format("%.0f", currently.getDouble("temperature"));
                tempu = degree.equals("Fahrenheit") ? "F" : "C";
                htemp = String.format("%.0f", jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMax"));
                ltemp = String.format("%.0f", jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMin"));
                icon = (String) currently.getString("icon");
                pre = String.valueOf(currently.getDouble("precipIntensity"));
                rain = (String) (String.format("%.0f", currently.getDouble("precipProbability") * 100)) + "%";
                wind = (String) String.format("%.2f", currently.getDouble("windSpeed")) + " mph";
                due = (String) String.format("%.2f", currently.getDouble("dewPoint"));
                humidity = (String) String.format("%.0f", currently.getDouble("humidity") * 100) + "%";
                visibility = (String) String.format("%.2f", currently.getDouble("visibility")) + " mi";
                Date sunrise = new Date(jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getInt("sunriseTime") * 1000L);
                Date sunset = new Date(jsonObject.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getInt("sunsetTime") * 1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:MM a");
                formattedSunrise = sdf.format(sunrise);
                formattedSunset = sdf.format(sunset);


                switch (pre) {
                    case "0.0":
                        preValue = "None";
                        break;
                    case "0.002":
                        preValue = "Light";
                        break;
                    case "0.017":
                        preValue = "Very Light";
                        break;
                    case "0.01":
                        preValue = "Moderate";
                        break;
                    case "0.04":
                        preValue = "Heavy";
                        break;
                }

                ImageView findImg = (ImageView) findViewById(R.id.img);

                switch (icon) {
                    case "clear-day":
                        findImg.setBackgroundResource(R.drawable.clear);
                        break;
                    case "clear-night":
                        findImg.setBackgroundResource(R.drawable.clear_night);
                        break;
                    case "rain":
                        findImg.setBackgroundResource(R.drawable.rain);
                        break;
                    case "snow":
                        findImg.setBackgroundResource(R.drawable.snow);
                        break;
                    case "sleet":
                        findImg.setBackgroundResource(R.drawable.sleet);
                        break;
                    case "wind":
                        findImg.setBackgroundResource(R.drawable.clear);
                        break;
                    case "fog":
                        findImg.setBackgroundResource(R.drawable.fog);
                        break;
                    case "cloudy":
                        findImg.setBackgroundResource(R.drawable.cloudy);
                        break;
                    case "partly-cloudy-day":
                        findImg.setBackgroundResource(R.drawable.cloud_day);
                        break;
                    case "partly-cloudy-night":
                        findImg.setBackgroundResource(R.drawable.cloud_night);
                        break;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            TextView finder;
            finder = (TextView) findViewById(R.id.weather_title);
            finder.setText(condition);
            finder = (TextView) findViewById(R.id.degree);
            finder.setText(temp);
            finder = (TextView) findViewById(R.id.degree_sym);
            finder.setText(tempu);
            finder = (TextView) findViewById(R.id.high_temp);
            finder.setText(htemp);
            finder = (TextView) findViewById(R.id.low_temp);
            finder.setText(ltemp);
            finder = (TextView) findViewById(R.id.pre);
            finder.setText(preValue);
            finder = (TextView) findViewById(R.id.rain);
            finder.setText(rain);
            finder = (TextView) findViewById(R.id.wind);
            finder.setText(wind);
            finder = (TextView) findViewById(R.id.dew);
            finder.setText(due);
            finder = (TextView) findViewById(R.id.hum);
            finder.setText(humidity);
            finder = (TextView) findViewById(R.id.vis);
            finder.setText(visibility);
            finder = (TextView) findViewById(R.id.sunrise);
            finder.setText(formattedSunrise);
            finder = (TextView) findViewById(R.id.sunset);
            finder.setText(formattedSunset);

        }

    }
}

