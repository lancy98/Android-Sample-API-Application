package com.example.apiapplication;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CurrencyList {

    public ArrayList<Currency> currencies;

    public CurrencyList(JSONObject jsonObject) {
        currencies = new ArrayList<Currency>();
        populateBaseCurrency(jsonObject);
        populateRates(jsonObject);
    }

    public void populateBaseCurrency(JSONObject jsonObject) {
        try {
            String baseCountryCode = jsonObject.getString("base");
            Currency currency = new Currency(baseCountryCode, 1.0);
            currencies.add(currency);
        } catch (Exception exception) {
            Log.d(MainActivity.TAG, "not able to get rate : " + exception.getLocalizedMessage());
        }
    }

    public void populateRates(JSONObject jsonObject) {
        try {
            String baseCountryCode = jsonObject.getString("base");
            JSONObject ratesJSON = jsonObject.getJSONObject("rates");
            Iterator<String> keys = ratesJSON.keys();

            while(keys.hasNext()) {
                String key = keys.next();

                if (!baseCountryCode.equals(key)) {
                    double value = ratesJSON.getDouble(key);
                    Currency currency = new Currency(key, value);
                    currencies.add(currency);
                }
            }
        } catch (Exception exception) {
            Log.d(MainActivity.TAG, "not able to get rate : " + exception.getLocalizedMessage());
        }
    }
}
