package com.example.zhangwenxian.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.parameter.PlaceParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private EditText Street;
    private TextView s;
    Spinner spinner;
    //String state = "Select";
    //TextView text = null;
    EditText street = null;
    EditText city = null;
    RadioGroup degree = null;


    RadioButton radio = null;

    public void forecast_io(View view) {
        Intent foreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forecast.io"));
        startActivity(foreIntent);
    }

    public void AboutAct(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void SearchAct(View view) {
        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        degree = (RadioGroup) findViewById(R.id.degree);

        int selected_id = degree.getCheckedRadioButtonId();
        radio =  (RadioButton) findViewById(selected_id);

        TextView error = (TextView) findViewById(R.id.error);

        Spinner mySpinner=(Spinner) findViewById(R.id.statespinner);
        String state = mySpinner.getSelectedItem().toString();

        if (street.getText().toString().matches("")) {
            error.setText("Pleas enter a Street.");
            error.setVisibility(View.VISIBLE);
        } else if (city.getText().toString().matches("")) {
            error.setText("Pleas enter a City.");
            error.setVisibility(View.VISIBLE);
        } else if (state.matches("Select")) {
            error.setText("Pleas select a State.");
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.INVISIBLE);
            String regex = " ";
            String streetOut = street.getText().toString().replaceAll(regex, "%20");
            String cityOut = city.getText().toString().replaceAll(regex, "%20");

            String url="http://amycsci571-env.elasticbeanstalk.com/?street=" + streetOut + "&city=" + cityOut + "&state=" + state.substring(state.length()-2,state.length()) + "&degree=" + degree;
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("street", street.getText().toString());
            intent.putExtra("city", city.getText().toString());
            intent.putExtra("state", state.substring(state.length()-2,state.length()));
            intent.putExtra("degree", radio.getText().toString());
            PlaceParameter place = new PlaceParameter(city+", "+state.substring(state.length()-2,state.length()));
            startActivity(intent);
        }


    }

    public void ClearAct(View view) {
        TextView street_text = (TextView) findViewById(R.id.street);
        TextView city_text = (TextView) findViewById(R.id.city);
        Spinner state_spinner = (Spinner) findViewById(R.id.statespinner);
        RadioButton fah = (RadioButton) findViewById(R.id.fahrenheit);

        street_text.setText("");
        city_text.setText("");
        state_spinner.setSelection(0);
        fah.setChecked(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.statespinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = (TextView) view;
        state = text.getText().toString();
        if (!"Select".equals(text.getText())) {
            Toast.makeText(this, text.getText() + " is selected", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
*/
}
