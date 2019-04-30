package com.example.apiapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements NetworkRequest.NetworkResponse {

    private NetworkRequest request;
    private CurrencyList currencyList;
    private ListView listView;
    private Button button;
    private EditText editText;

    public static final String TAG = "API-APP-LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.loadButton);

        editTextFocusChangeListener();
        addListenerToButton();
        invokeAPI(null);

    }

    public void invokeAPI(String baseCurrencyCode) {
        String url = "https://api.exchangeratesapi.io/latest";

        if (baseCurrencyCode != null && baseCurrencyCode.length() > 0) {
            url += "?base=" + baseCurrencyCode.toUpperCase();
        }

        request = new NetworkRequest(this, url, this);
    }

    public void editTextFocusChangeListener() {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void addListenerToButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currencyCode = editText.getText().toString();
                invokeAPI((currencyCode.length() > 0) ? currencyCode : null);
                hideKeyboard(getCurrentFocus());
            }
        });
    }

    public void networkResponse(JSONObject jsonObject ,VolleyError error) {
        if (jsonObject != null) {
            currencyList = new CurrencyList(jsonObject);
            updateListView();
        } else {
            Toast.makeText(this, "Response Error", Toast.LENGTH_SHORT).show();
        }

        request = null;
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
