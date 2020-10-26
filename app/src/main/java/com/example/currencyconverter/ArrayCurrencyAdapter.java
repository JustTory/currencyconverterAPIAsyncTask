package com.example.currencyconverter;

import android.content.ContentUris;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArrayCurrencyAdapter extends ArrayAdapter<Currency> {
    private Context context;
    private int layoutID;
    private ArrayList<Currency> currencyList;

    public ArrayCurrencyAdapter(@NonNull Context context, int resource, @NonNull List<Currency> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutID = resource;
        this.currencyList = (ArrayList<Currency>) objects;
    }

    @Override
    public int getCount() {
        return currencyList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layoutID,null,false);
        }

        ImageView imageView = convertView.findViewById(R.id.iv_flag);
        TextView tvShortCurrencyName = convertView.findViewById(R.id.tv_shortCurrencyName);
        TextView tvFullCurrencyName = convertView.findViewById(R.id.tv_fullCurrencyName);
        TextView tvValue = convertView.findViewById(R.id.tv_value);

        Currency currency = currencyList.get(position);

        imageView.setImageBitmap(currency.getFlag());
        tvShortCurrencyName.setText(currency.getShortCurrencyName());
        tvFullCurrencyName.setText(currency.getFullCurrencyName());
        if(currency.getValue() == 0)
        {
            tvValue.setText("0");
        }
        else tvValue.setText(String.format("%.2f", currency.getValue()));

        return convertView;
    }
}
