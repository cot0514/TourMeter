package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import com.opencsv.CSVReader;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    String key = "Y8du4%2B8y%2BfX2MHxWxQ63PKe5MiTdnI3Slf2J3tb0KgHK6uqDT5VzuIhae8pKxfm%2BR5gXUHatD4V7dQ0oDspWYg%3D%3D";
    String main = "https://apis.data.go.kr/1360000/TourStnInfoService1/getCityTourClmldx1";

    EditText date;
    EditText day;
    EditText id;

    static RequestQueue queue;

    private List<SettingData> locations = new ArrayList<SettingData>();
    private List<String> cityList = new ArrayList<String>();
    private List<SettingData> selectedDistricts = new ArrayList<SettingData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UrlInfo info = new UrlInfo();

        date = findViewById(R.id.curDate);
        day = findViewById(R.id.day);

        Spinner spinnerCity = findViewById(R.id.city);
        Spinner spinnerDistrict = findViewById(R.id.district);
        TextView cityId = findViewById(R.id.id);

        loadCSVData();

        for (SettingData location : locations) {
            if(!cityList.contains(location.getCity()))
            {
                cityList.add(location.getCity());
            }
        }

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                cityList
        );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCity = cityList.get(position);

                selectedDistricts.clear();
                for (SettingData location : locations) {
                    if (location.getCity().equals(selectedCity)) {
                        selectedDistricts.add(location);
                    }
                }

                List<String> districtNames = new ArrayList<>();
                for (SettingData location : selectedDistricts) {
                    districtNames.add(location.getDistrict());
                }

                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        districtNames
                );
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDistrict.setAdapter(districtAdapter);

                spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {

                        String selectedId = selectedDistricts.get(position).getId();
                        cityId.setText(selectedId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setCurDate(date.getText().toString());
                info.setDay(day.getText().toString());
                info.setId(cityId.getText().toString());
                makeRequest(info);
            }
        });

        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void makeRequest(UrlInfo info) {
        String url = main +
                "?ServiceKey=" + key +
                "&pageNo=1" +
                "&numOfRows=" + info.getDay() +
                "&dataType=JSON" +
                "&CURRENT_DATE=" + info.getCurDate() +
                "&DAY=" + info.getDay() +
                "&CITY_AREA_ID=" + info.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("API_RESPONSE", response);
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("API_ERROR", error.toString());
                    }
                });

        queue.add(stringRequest);
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        WeatherList weatherList = gson.fromJson(response, WeatherList.class);


    }

    private void loadCSVData() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("data.csv")))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 4) { // city, district, id
                    locations.add(new SettingData(nextLine[0], nextLine[1], nextLine[2], nextLine[3]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}