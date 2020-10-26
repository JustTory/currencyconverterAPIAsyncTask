package com.example.currencyconverter;

import java.util.ArrayList;

public class TransferList {
    private static ArrayList<Currency> TransferCurrencyList = new ArrayList<>();
    TransferList() {};
    public static boolean mainToList;
    public static void addItem(Currency cur){
        cur.setValue(0);
        TransferCurrencyList.add(cur);
    }
    public static ArrayList<Currency> getTransferList() {return TransferCurrencyList;}
    public static void clearAll() {TransferCurrencyList.clear();}
}
