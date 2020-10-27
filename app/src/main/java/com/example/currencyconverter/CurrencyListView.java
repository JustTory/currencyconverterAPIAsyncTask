package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CurrencyListView extends AppCompatActivity{

    private ListView lv_currency;
    private static ArrayList<Currency> currencyList;
    private ArrayCurrencyAdapter adapter;
    private Button backBtn;
    private static boolean firstTimeLoadData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (savedInstanceState != null) {
            currencyList = savedInstanceState.getParcelableArrayList("currencyList");
        }
        if(firstTimeLoadData == true) {
            currencyList = new ArrayList<>();
            new MyAsyncTask().execute(currencyList);
        }

        else initComponents(currencyList);
    }

    private class MyAsyncTask extends AsyncTask<ArrayList, String, String> {
        String base;
        JSONObject data;

        @Override
        protected String doInBackground(ArrayList... arrayLists) {
            final ArrayList<Currency> currencyList = arrayLists[0];

            final String url = "https://api.exchangeratesapi.io/latest";
            Log.d("@LOG", "doInBackground");
            final JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("@LOG", "onResponse");
                        base = response.getString("base");
                        Log.d("@LOG", base);
                        data = response.getJSONObject("rates");

                        ArrayList<String> shortCurrencyName = new ArrayList<>() ;
                                Iterator<String> iter = data.keys() ;
                                        while(iter.hasNext()){
                                            String key = iter.next();
                                            shortCurrencyName.add(key);
                                        }
                        for (int i = 0; i < shortCurrencyName.size(); i++) {
                            Currency currency = new Currency(shortCurrencyName.get(i), "", data);
                            currencyList.add(currency);
                            firstTimeLoadData = false;
                            adapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        Log.d("ERROR", e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue q = Volley.newRequestQueue(CurrencyListView.this);
            q.add(request);
            onPostExecute("doInBackground DONE");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("@LOG", "onPreExecute");
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String signal) {
            super.onPostExecute(signal);
            Log.d("@LOG", "onPostExecute");
            if(signal != null && signal.equals("doInBackground DONE")){
                initComponents(currencyList);
                ProgressBar progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        if(TransferList.mainToList == true) {
            initComponents(currencyList);
            currencyList.addAll(TransferList.getTransferList());
            adapter.notifyDataSetChanged();
            TransferList.clearAll();
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("currencyList", currencyList);
        super.onSaveInstanceState(outState);
    }

    private void initComponents(final ArrayList<Currency> currencyList) {
        lv_currency = findViewById(R.id.listView);
        backBtn = findViewById(R.id.backBtn);

        adapter = new ArrayCurrencyAdapter(getApplicationContext(), R.layout.listview_item, currencyList);
        lv_currency.setAdapter(adapter);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Currency currency = currencyList.get(position);
                TransferList.addItem(currency);
                currencyList.remove(position);
                adapter.notifyDataSetChanged();
                TransferList.mainToList = false;
            }
        };
        lv_currency.setOnItemClickListener(onItemClickListener);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}