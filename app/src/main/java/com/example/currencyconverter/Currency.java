package com.example.currencyconverter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {
    private Bitmap flag;
    private String shortCurrencyName;
    private String fullCurrencyName;
    private double toCurrency;
    private double value;

    public Currency(Bitmap flag, String shortCurrencyName, String fullCurrencyName, double toCurrency, double value) {
        this.flag = flag;
        this.shortCurrencyName = shortCurrencyName;
        this.fullCurrencyName = fullCurrencyName;
        this.toCurrency = toCurrency;
        this.value = value;
    }

    protected Currency(Parcel in) {
        flag = in.readParcelable(Bitmap.class.getClassLoader());
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

    public Bitmap getFlag() {
        return flag;
    }

    public void setFlag(Bitmap flag) {
        this.flag = flag;
    }

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

    public void setValue(double value) {
        this.value = value * toCurrency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(flag, i);
        parcel.writeString(shortCurrencyName);
        parcel.writeString(fullCurrencyName);
        parcel.writeDouble(toCurrency);
        parcel.writeDouble(value);
    }
}
