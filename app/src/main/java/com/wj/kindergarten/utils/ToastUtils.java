package com.wj.kindergarten.utils;


import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;

public class ToastUtils {
    public static void  showMessage(String message){
        Toast.makeText(CGApplication.context,message,Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View view,String left,String right,int duration ,View.OnClickListener onClickListener){
        if(duration == 0){
            duration = Snackbar.LENGTH_SHORT;
        }
        Snackbar.make(view,left,duration).setAction(right,onClickListener).show();
    }

    public static void showSnackBar(View view,String left,String right){
        showSnackBar(view,left,right,0,null);
    }


    public static void noMoreContentShow(){
        Toast toast = Toast.makeText(CGApplication.context,"没有更多内容了!",Toast.LENGTH_SHORT);
        toast.show();
    }
}
