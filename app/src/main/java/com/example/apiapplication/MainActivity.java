package com.example.apiapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements NetworkRequest.NetworkResponse {

    private NetworkRequest request;
    private CurrencyList currencyList;
    private ListView listView;
    private Button button;
    private EditText editText;
    private ProgressBar progressBar;

    public static final String TAG = "API-APP-LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.loadButton);
        progressBar = findViewById(R.id.progressBar);

        editTextFocusChangeListener();
        addListenerToButton();
        invokeAPI("USD");

    }

    public void invokeAPI(String baseCurrencyCode) {
        String url = "https://api.exchangeratesapi.io/latest";

        if (baseCurrencyCode != null && baseCurrencyCode.length() > 0) {
            url += "?base=" + baseCurrencyCode.toUpperCase();
        }

        progressBar.setVisibility(View.VISIBLE);
        disableUserInteraction();
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
        enableUserInteraction();
        progressBar.setVisibility(View.GONE);

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

                Currency currency = currencyList.currencies.get(position);

                text1.setText("Currency Code : " + currency.countryName);
                text2.setText("Price : " + currency.rate);
                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void disableUserInteraction() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    public void enableUserInteraction() {
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

}
