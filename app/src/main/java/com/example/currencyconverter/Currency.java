package com.example.currencyconverter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Currency implements Parcelable {
    private String shortCurrencyName;
    private String fullCurrencyName;
    private double toCurrency;
    private double value;

    public Currency(String shortCurrencyName, String fullCurrencyName, JSONObject data) throws JSONException {
        this.shortCurrencyName = shortCurrencyName;
        this.fullCurrencyName = fullCurrencyName;
        this.toCurrency = data.getDouble(shortCurrencyName);
    }

    protected Currency(Parcel in) {
        shortCurrencyName = in.readString();
        fullCurrencyName = in.readString();
        toCurrency = in.readDouble();
        value = in.readDouble();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    public String getShortCurrencyName() {
        return shortCurrencyName;
    }

    public void setShortCurrencyName(String shortCurrencyName) {
        this.shortCurrencyName = shortCurrencyName;
    }

    public String getFullCurrencyName() {
        return fullCurrencyName;
    }

    public void setFullCurrencyName(String fullCurrencyName) {
        this.fullCurrencyName = fullCurrencyName;
    }

    public double getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(double toCurrency) {
        this.toCurrency = toCurrency;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value){
        this.value = value * toCurrency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortCurrencyName);
        parcel.writeString(fullCurrencyName);
        parcel.writeDouble(toCurrency);
        parcel.writeDouble(value);
    }
}
