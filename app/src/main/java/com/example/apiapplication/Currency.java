package com.example.apiapplication;

import android.util.Log;

import org.json.JSONObject;


public class Currency {
    public String countryName;
    public double rate;

    public Currency(String countryName, double rate) {
        this.countryName = countryName;
        this.rate = rate;
    }
}
