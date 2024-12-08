package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String key = "Y8du4%2B8y%2BfX2MHxWxQ63PKe5MiTdnI3Slf2J3tb0KgHK6uqDT5VzuIhae8pKxfm%2BR5gXUHatD4V7dQ0oDspWYg%3D%3D";
    String main = "http://apis.data.go.kr/1360000/TourStnInfoService1/getCityTourClmldx1";

    RequestQueue queue = Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UrlInfo info = new UrlInfo();

        info.pageNo = findViewById(R.id.pageNo).toString();
        info.numOfRows = findViewById(R.id.numOfRows).toString();
        info.curDate = findViewById(R.id.curDate).toString();
        info.hour = findViewById(R.id.hour).toString();
        info.id = findViewById(R.id.id).toString();

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest(info);
            }
        });
    }

    public void makeRequest(UrlInfo info) {
        String url = main +
                "?ServiceKey=" + key +
                "&PageNo=" + info.pageNo +
                "&numOfRows=" + info.numOfRows +
                "&dataType=JSON" +
                "&CURRENT_DATE=" + info.curDate +
                "&HOUR" + info.hour +
                "&COURSE_ID" + info.id;

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

    }
}