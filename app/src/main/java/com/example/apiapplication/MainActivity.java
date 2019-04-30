package com.example.apiapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private CurrencyList currencyList;
    private ListView listView;
    public static final String TAG = "API-APP-LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.exchangeratesapi.io/latest";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        currencyList = new CurrencyList(response);
                        updateListView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(MainActivity.TAG, "response error");
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void updateListView() {

        ArrayAdapter adapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_2, android.R.id.text1, currencyList.currencies) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText("Currency Code : " + currencyList.currencies.get(position).countryName);
                text2.setText("Price : " + currencyList.currencies.get(position).rate);
                return view;
            }
        };

        listView.setAdapter(adapter);
    }

}
