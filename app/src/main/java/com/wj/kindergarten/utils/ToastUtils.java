package com.wj.kindergarten.utils;


import android.widget.Toast;

import com.wj.kindergarten.CGApplication;

public class ToastUtils {
    public static void  showMessage(String message){
        Toast.makeText(CGApplication.context,message,Toast.LENGTH_LONG).show();

    }
}
