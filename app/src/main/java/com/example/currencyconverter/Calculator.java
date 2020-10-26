package com.example.currencyconverter;

import android.util.Log;
import android.widget.TextView;

public class Calculator {
    private double operand1;
    private double operand2;

    public double plus(TextView tv) {
        String inp = tv.getText().toString();
        String num1 = "";
        String num2 = "";

        inp = inp.replaceAll("\\s", "");
        int inpLength = inp.length();
        int operatorPos = inp.indexOf("+");
        if (operatorPos != -1) {
            num1 = inp.substring(0, operatorPos);
            num2 = inp.substring(operatorPos + 1, inpLength);
        }
        operand1 = Double.parseDouble(num1);
        operand2 = Double.parseDouble(num2);
        return operand1 + operand2;
    }

    public double minus(TextView tv) {
        String inp = tv.getText().toString();
        String num1 = "";
        String num2 = "";

        inp = inp.replaceAll("\\s", "");
        int inpLength = inp.length();
        int operatorPos = inp.indexOf("-");
        if (operatorPos != -1) {
            num1 = inp.substring(0, operatorPos);
            num2 = inp.substring(operatorPos + 1, inpLength);
        }
        operand1 = Double.parseDouble(num1);
        operand2 = Double.parseDouble(num2);
        return operand1 - operand2;
    }

    public double multiply(TextView tv) {
        String inp = tv.getText().toString();
        String num1 = "";
        String num2 = "";

        inp = inp.replaceAll("\\s", "");
        int inpLength = inp.length();
        int operatorPos = inp.indexOf("x");
        if (operatorPos != -1) {
            num1 = inp.substring(0, operatorPos);
            num2 = inp.substring(operatorPos + 1, inpLength);
        }
        operand1 = Double.parseDouble(num1);
        operand2 = Double.parseDouble(num2);
        return operand1 * operand2;
    }

    public double divide(TextView tv) {
        String inp = tv.getText().toString();
        String num1 = "";
        String num2 = "";

        inp = inp.replaceAll("\\s", "");
        int inpLength = inp.length();
        int operatorPos = inp.indexOf("÷");
        if (operatorPos != -1) {
            num1 = inp.substring(0, operatorPos);
            num2 = inp.substring(operatorPos + 1, inpLength);
        }
        operand1 = Double.parseDouble(num1);
        operand2 = Double.parseDouble(num2);

        if (operand2 == 0) {
            throw new IllegalArgumentException();
        }
        return operand1 / operand2;
    }
}
