package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.URLEncoder;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String key = "Y8du4+8y+fX2MHxWxQ63PKe5MiTdnI3Slf2J3tb0KgHK6uqDT5VzuIhae8pKxfm+R5gXUHatD4V7dQ0oDspWYg==";
    String main = "https://apis.data.go.kr/1360000/TourStnInfoService1/getCityTourClmIdx1";

    EditText date;
    EditText day;
    EditText id;

    static RequestQueue queue;

    WeatherAdapter adapter;
    RecyclerView recyclerView;

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

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new WeatherAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void makeRequest(UrlInfo info) {
        try {
            String url = main +
            "?serviceKey=" + URLEncoder.encode(key, "UTF-8") +
            "&pageNo=1" +
            "&numOfRows=" + URLEncoder.encode(info.getDay(), "UTF-8") +
            "&dataType=JSON" +
            "&CURRENT_DATE=" + URLEncoder.encode(info.getCurDate(), "UTF-8") +
            "&DAY=" + URLEncoder.encode(info.getDay(), "UTF-8") +
            "&CITY_AREA_ID=" + URLEncoder.encode(info.getId(), "UTF-8");

            Log.d("API_URL", url);

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
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                String responseBody = new String(error.networkResponse.data);
                                Log.e("API_ERROR", "Error Response: " + responseBody);
                            } else {
                                Log.e("API_ERROR", error.toString());
                            }
                        }
                    }) {
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<String,String>();

                    return params;
                }
            };

            stringRequest.setShouldCache(false);
            queue.add(stringRequest);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        Weather weatherList = gson.fromJson(response, Weather.class);

        adapter.clearItem();

        Weather.Body body = weatherList.response.body;

        for (Weather.Item item : body.items.item) {

            adapter.addItem(item);
        }

        adapter.notifyDataSetChanged();
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