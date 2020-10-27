package com.example.currencyconverter;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView expressionTV;
    private TextView baseCurrency;
    private Button btnPlus;
    private Button btnMinus;
    private Button btnMulti;
    private Button btnDivide;
    private Calculator calculator;
    private String operator = "";
    private boolean dotPerOperand = false;
    private double result;
    private ListView lv_currency;
    private ArrayList<Currency> currencyList;
    private ArrayCurrencyAdapter adapter;
    private Button fragmentBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("@LOG", "onCreate Main");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initComponents();

        if (savedInstanceState != null) {
            currencyList = savedInstanceState.getParcelableArrayList("currencyList");
            expressionTV.setText(savedInstanceState.getString("expressionTV"));
            baseCurrency.setText(savedInstanceState.getString("baseCurrency"));
            result = savedInstanceState.getDouble("result");
        }

        adapter = new ArrayCurrencyAdapter(getBaseContext(), R.layout.listview_item, currencyList);
        lv_currency.setAdapter(adapter);



        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Currency currency = currencyList.get(position);
                TransferList.addItem(currency);
                currencyList.remove(position);
                Log.d("@LOG", "currency removed");
                adapter.notifyDataSetChanged();
                TransferList.mainToList = true;

            }
        };
        lv_currency.setOnItemClickListener(onItemClickListener);

        fragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CurrencyListView.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        if(TransferList.mainToList == false) {
            //Log.d("@LOG", "onResume Main");
            currencyList.addAll(TransferList.getTransferList());
            toNewCurrency(currencyList, result);
            adapter.notifyDataSetChanged();
            TransferList.clearAll();
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Log.d("@LOG", "onSave Main");
        outState.putParcelableArrayList("currencyList", currencyList);
        outState.putString("expressionTV", expressionTV.getText().toString());
        outState.putString("baseCurrency", baseCurrency.getText().toString());
        outState.putDouble("result", result);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {}

    private void initComponents() {
        calculator = new Calculator();
        expressionTV = findViewById(R.id.expressionTV);
        baseCurrency = findViewById(R.id.baseCurrency);
        btnPlus = findViewById(R.id.plus);
        btnMinus = findViewById(R.id.minus);
        btnMulti = findViewById(R.id.multi);
        btnDivide = findViewById(R.id.divide);
        lv_currency = findViewById(R.id.listView);
        fragmentBtn = findViewById(R.id.fragmentBtn);
        currencyList = new ArrayList<>();
    }

    public void onClick(View view) {
        if (expressionTV.getText().toString().equals("Error")) {
            expressionTV.setText("");
            expressionTV.setTextColor(Color.WHITE);
            operator = "";
            dotPerOperand = false;
        }
        switch (view.getId()) {
            case (R.id.num0):
                expressionTV.setText(expressionTV.getText() + "0");
                break;
            case (R.id.num1):
                expressionTV.setText(expressionTV.getText() + "1");
                break;
            case (R.id.num2):
                expressionTV.setText(expressionTV.getText() + "2");
                break;
            case (R.id.num3):
                expressionTV.setText(expressionTV.getText() + "3");
                break;
            case (R.id.num4):
                expressionTV.setText(expressionTV.getText() + "4");
                break;
            case (R.id.num5):
                expressionTV.setText(expressionTV.getText() + "5");
                break;
            case (R.id.num6):
                expressionTV.setText(expressionTV.getText() + "6");
                break;
            case (R.id.num7):
                expressionTV.setText(expressionTV.getText() + "7");
                break;
            case (R.id.num8):
                expressionTV.setText(expressionTV.getText() + "8");
                break;
            case (R.id.num9):
                expressionTV.setText(expressionTV.getText() + "9");
                break;

            case (R.id.dot):
                if (dotPerOperand == false) {
                    expressionTV.setText(expressionTV.getText() + ".");
                    dotPerOperand = true;
                }
                break;

            case (R.id.plus):
                if (operator.equals("")) {
                    expressionTV.setText(expressionTV.getText() + " + ");
                    operator = "+";
                    dotPerOperand = false;
                    toSelected(btnPlus);
                }
                break;

            case (R.id.minus):
                if (operator.equals("")) {
                    expressionTV.setText(expressionTV.getText() + " - ");
                    operator = "-";
                    dotPerOperand = false;
                    toSelected(btnMinus);
                }
                break;

            case (R.id.multi):
                if (operator.equals("")) {
                    expressionTV.setText(expressionTV.getText() + " x ");
                    operator = "x";
                    dotPerOperand = false;
                    toSelected(btnMulti);
                }
                break;

            case (R.id.divide):
                if (operator.equals("")) {
                    expressionTV.setText(expressionTV.getText() + " ÷ ");
                    operator = ":";
                    dotPerOperand = false;
                    toSelected(btnDivide);
                }
                break;

            case (R.id.equal):
                if (operator.equals("")) {
                    try {

                        result = Double.parseDouble(expressionTV.getText().toString());
                        baseCurrency.setText(formatString(result));
                        toNewCurrency(currencyList, result);
                        adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        expressionTV.setText("Error");
                        expressionTV.setTextColor(Color.RED);
                        baseCurrency.setText("0");
                    }
                }
                if (operator.equals("+")) {
                    toNotSelected(btnPlus);
                    try {
                        result = calculator.plus(expressionTV);
                        baseCurrency.setText(formatString(result));
                        toNewCurrency(currencyList, result);
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        expressionTV.setText("Error");
                        expressionTV.setTextColor(Color.RED);
                    }
                }
                if (operator.equals("-")) {
                    toNotSelected(btnMinus);
                    try {
                        result = calculator.minus(expressionTV);
                        baseCurrency.setText(formatString(result));
                        toNewCurrency(currencyList, result);
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        expressionTV.setText("Error");
                        expressionTV.setTextColor(Color.RED);
                    }
                }
                if (operator.equals("x")) {
                    toNotSelected(btnMulti);
                    try {
                        result = calculator.multiply(expressionTV);
                        baseCurrency.setText(formatString(result));
                        toNewCurrency(currencyList, result);
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        expressionTV.setText("Error");
                        expressionTV.setTextColor(Color.RED);
                    }
                }
                if (operator.equals(":")) {
                    toNotSelected(btnDivide);
                    try {
                        result = calculator.divide(expressionTV);
                        baseCurrency.setText(formatString(result));
                        toNewCurrency(currencyList, result);

                        adapter.notifyDataSetChanged();
                    } catch (IllegalArgumentException iae) {
                        expressionTV.setText("Error");
                        expressionTV.setTextColor(Color.RED);
                    }
                }
                break;

            case (R.id.delete):
                String inp3 = expressionTV.getText().toString();

                if (!inp3.equals("")) {
                    if (inp3.equals("Error")) {
                        expressionTV.setText("");
                        expressionTV.setTextColor(Color.WHITE);
                        operator = "";
                    } else if (inp3.charAt(inp3.length() - 1) == ' ') {
                        inp3 = inp3.substring(0, inp3.length() - 3);
                        operator = "";
                        dotPerOperand = true;
                        expressionTV.setText(inp3);
                        toNotSelected(btnPlus);
                        toNotSelected(btnMinus);
                        toNotSelected(btnMulti);
                        toNotSelected(btnDivide);
                    } else {
                        if (inp3.charAt(inp3.length() - 1) == '.') {
                            dotPerOperand = false;
                        }
                        inp3 = inp3.substring(0, inp3.length() - 1);
                        expressionTV.setText(inp3);
                    }
                }
                if (inp3.equals("")) {
                    baseCurrency.setText("0");
                    dotPerOperand = false;
                }
                break;

            case (R.id.ac):
                expressionTV.setText("");
                baseCurrency.setText("0");
                operator = "";
                dotPerOperand = false;

                toNewCurrency(currencyList, 0);
                adapter.notifyDataSetChanged();

                toNotSelected(btnPlus);
                toNotSelected(btnMinus);
                toNotSelected(btnMulti);
                toNotSelected(btnDivide);
                break;
        }
    }

    public void toNewCurrency(ArrayList<Currency> arrayList, double result) {

        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setValue(result);
        }
    }

    public String formatString(double res) {
        return String.format("%.2f", res);
    }

    public void toNotSelected(Button btn) {
        btn.setTextColor(Color.parseColor("#FFFFFF"));
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
    }

    public void toSelected(Button btn) {
        btn.setTextColor(Color.parseColor("#FF9800"));
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
    }
}