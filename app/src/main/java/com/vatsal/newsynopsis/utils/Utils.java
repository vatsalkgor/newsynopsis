package com.vatsal.newsynopsis.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

public class Utils {
    private static final String URL_v1 = "http://172.28.107.221:8080/v1/";
    private JSONObject resp;
    public static void makeToast(Context c, String msg){
        Toast.makeText(c,msg, Toast.LENGTH_LONG).show();
    }
}