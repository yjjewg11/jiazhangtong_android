package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.CallTransfer;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;

import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public abstract class CallUtils {

    public static void showCall(final Context context,String [] arrays, final CallTransfer callTransfer){
        View view = ViewGroup.inflate(context,R.layout.call_layout,null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.call_count_ll);
        for(final String array : arrays){
            View oneView = View.inflate(context,R.layout.one_item,null);
            TextView textView = (TextView) oneView.findViewById(R.id.tv_call);
            textView.setText(""+array);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            oneView.setBackgroundResource(R.drawable.setting_item_click_selector);
            linearLayout.addView(oneView,layoutParams);
            oneView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendCallState(context,callTransfer);
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + array));
                    context.startActivity(phoneIntent);
                }
            });
        }

        TextView tv_call = (TextView) view.findViewById(R.id.special_call_cancel);
        final PopupWindow mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.ShareAnimBase);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        };
        tv_call.setOnClickListener(listener);
        view.setOnClickListener(listener);


        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);


    }

    private static void sendCallState(Context context,CallTransfer callTransfer) {
        UserRequest.sendCallMessage(context,callTransfer.getExt_uuid(),callTransfer.getType(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }
}
