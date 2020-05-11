package com.mobilecomputing.currency_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String endPoint = "https://data.fixer.io/api/latest?access_key=9e27d8a2e992e35dc0e5c141bf361169&base=";
    EditText editTextCurrency;
    Button buttonSearch;
    ArrayList currencyList;
    ListView currenciesListView;
    CurrencyAdapter currencyAdapter;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextCurrency = findViewById(R.id.currency_ip_txt);
        buttonSearch = findViewById(R.id.search_btn);
        currenciesListView = findViewById(R.id.currency_list_view);
        currencyList = new ArrayList<>();
        currencyAdapter = new CurrencyAdapter(this, R.layout.list_item, currencyList);
        currenciesListView.setAdapter(currencyAdapter);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currencyStr = editTextCurrency.getText().toString();
                currencyStr = currencyStr.isEmpty() ? "USD" : currencyStr;
                endPoint = endPoint.concat(currencyStr.toUpperCase());
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, endPoint, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("Response: " + response.toString());
                                        try {
                                            response = response.getJSONObject("rates");
                                            currencyList.clear();
                                            for (int i = 0; i < response.names().length(); i++) {
                                                String key = response.names().getString(i);
                                                double value = Double.parseDouble(response.get(response.names().getString(i)).toString());
                                                currencyList.add(new CurrencyItem(key, value));
                                            }
                                            currencyAdapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                    }
                                });
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                };
                Thread thread = new Thread(null, runnable, "background");
                thread.start();
            }
        });
    }
}