package com.wj.kindergarten.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.wenjie.jiazhangtong.R;

import static com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import static com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;

/**
 * Created by WW on 2014/6/3.
 */
public class ShareUtils {
    public static UMSocialService mController;
    private static Context context;
    private static final String appId = "wx6699cf8b21e12618";
    private static final String appSecret = "a2b1783f3ad9e6db4154d45d6e23ed4a";
    private static boolean flag = false;//防止弹窗多次
    private static boolean isShow = false;//分享图标是否显示
    public final static String PIC_SCALE = ".360x240.jpg";//用于提交服务器缩放图片

    private ShareUtils() {

    }

    private static ShareUtils shareUtils = new ShareUtils();

    public static ShareUtils getInstance() {
        return shareUtils;
    }

    /**
     * 显示分享对话框
     *
     * @param con
     * @param view
     *
     */
    public static void showShareDialog(final Context con, final View view, String title1, String content1, final String picurl, final String url,boolean isMessage) {
//        if (!isShow) {
        String title = null;
        String content = null;
            if(TextUtils.isEmpty(title1)){
                title = "问界互动家园";
            }else{
                title = title1;
            }

            if(TextUtils.isEmpty(content1)){
                content = title;
            }else{
                content = content1;
            }
//
//        if(picurl!=null && picurl.contains("@")){
//            pic = picurl.substring(0,picurl.indexOf("@"));
//        }else{
//            pic = picurl;
//        }
            isShow = true;
            context = con;
            flag = false;
            if (mController == null) {
                mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
                // 添加新浪微博SSO授权支持
                //   mController.getConfig().setSsoHandler(new SinaSsoHandler());
                //关闭自带的toast
                mController.getConfig().closeToast();
            }

            View popupView = View.inflate(con, R.layout.share_layout, null);
            LinearLayout lltop = (LinearLayout) popupView.findViewById(R.id.lltop);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lltop.getLayoutParams();
            if(isMessage){
                //如果是班级互动
                try{
                    int [] location = new int[2];
                    view.getLocationInWindow(location);
                    int w = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    int h = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    lltop.measure(w, h);
                    int height = lltop.getMeasuredHeight();
                    int margin = location[1] > height ? (location[1]-height+30) : (55*(int)WindowUtils.getDesnity());
                    layoutParams.topMargin = margin;
                    lltop.setLayoutParams(layoutParams);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }else{
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lltop.setLayoutParams(layoutParams);
            }

            final PopupWindow mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupView.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    isShow = false;
                }
            });



            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //弹出框取消时还原背景

                }
            });
            //微信
        final String finalTitle = title;
        final String finalContent = content;
        popupView.findViewById(R.id.share_wx_f).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                    UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
                    wxHandler.addToSocialSDK();
                    shareTo(SHARE_MEDIA.WEIXIN, finalTitle, finalContent, picurl, url);
                    Toast.makeText(context, "分享中，请稍后...", Toast.LENGTH_SHORT).show();
                }
            });

            //微信朋友圈
            popupView.findViewById(R.id.share_wx_c).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                    UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
                    wxCircleHandler.setToCircle(true);
                    wxCircleHandler.addToSocialSDK();
                    shareTo(SHARE_MEDIA.WEIXIN_CIRCLE, finalTitle, finalContent, picurl, url);
                    Toast.makeText(context, "分享中，请稍后...", Toast.LENGTH_SHORT).show();
                }
            });

            popupView.findViewById(R.id.share_sina).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                    String content = null;
                    if(finalTitle.equals(finalContent)){
                        content = "";
                    }else{
                        content = finalContent;
                    }
                    shareTo(SHARE_MEDIA.SINA, finalTitle, content, picurl, url);
                    Toast.makeText(context, "分享中，请稍后...", Toast.LENGTH_SHORT).show();
                }
            });

            popupView.findViewById(R.id.share_tencent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                    UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "100424468",
                            "SumAAk7jtaUSnZqd");
                    qqSsoHandler.addToSocialSDK();
                    shareTo(SHARE_MEDIA.QQ, finalTitle, finalContent, picurl, url);
                    Toast.makeText(context, "分享中，请稍后...", Toast.LENGTH_SHORT).show();
                }
            });

            mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                }
            });
            mPopupWindow.setAnimationStyle(R.style.ShareAnimBase);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.update();
            //判断是否是公告还是精品文章，如果是公告，隐藏取消按钮
            TextView cacleview = (TextView) popupView.findViewById(R.id.share_cancel);
            if(isMessage){
                //如果是公告则修改lltop在布局中的位置为中间
                cacleview.setVisibility(View.GONE);
            }else{
                cacleview.setVisibility(View.VISIBLE);

            }
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }

    /**
     * 分享到新浪微博
     */
    private static void shareTo(final SHARE_MEDIA share_media, final String title, final String content, final String pic, final String url) {
        if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE || share_media == SHARE_MEDIA.WEIXIN) {
            try {
                directShare(share_media, title, content, pic, url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            if (OauthHelper.isAuthenticated(context, share_media)) {
                directShare(share_media, title, content, pic, url);
            } else {
                mController.doOauthVerify(context, share_media,
                        new UMAuthListener() {

                            @Override
                            public void onStart(SHARE_MEDIA platform) {

                            }

                            @Override
                            public void onError(SocializeException e,
                                                SHARE_MEDIA platform) {
                                Toast.makeText(context, "抱歉，授权失败。", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                                directShare(share_media, title, content, pic, url);
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA platform) {
                                Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private static void directShare(SHARE_MEDIA share_media, String title, String content, String pic, String url) {
        if (Utils.stringIsNull(url)) {
            url = "http://www.wenjienet.com/";
        }
        try {
            UMImage localImage = null;
            if (!Utils.stringIsNull(pic)) {
                localImage = new UMImage(context, pic + PIC_SCALE);
            } else {
                localImage = new UMImage(context, R.drawable.ic_launcher);
            }
            if (share_media == SHARE_MEDIA.QQ) {
                if(title.equals(content)){
                    content = "";
                }
                QQShareContent qqShareContent = new QQShareContent(localImage);
                qqShareContent.setShareContent(title + content);
                qqShareContent.setTargetUrl(url);
                mController.setShareMedia(qqShareContent);
            } else if (share_media == SHARE_MEDIA.SINA) {
                SinaShareContent sinaShareContent = new SinaShareContent(localImage);
                sinaShareContent.setTargetUrl(url);
                sinaShareContent.setShareContent(title + url);
                mController.setShareMedia(sinaShareContent);
            } else if (share_media == SHARE_MEDIA.WEIXIN) {
                WeiXinShareContent weixinContent = new WeiXinShareContent();
                weixinContent.setShareContent(content);
                weixinContent.setTitle(title);
                weixinContent.setTargetUrl(url);
                weixinContent.setShareMedia(localImage);
                mController.setShareMedia(weixinContent);
            } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                CircleShareContent circleMedia = new CircleShareContent();
                circleMedia.setShareContent(content);
                circleMedia.setTitle(title);
                circleMedia.setShareImage(localImage);
                circleMedia.setTargetUrl(url);
                mController.setShareMedia(circleMedia);
            }

            mController.postShare(context, share_media,
                    new SnsPostListener() {


                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete(SHARE_MEDIA platform, int eCode,
                                               SocializeEntity entity) {
                            if (!flag) {
                                flag = true;
                                if (eCode == 200) {
                                    Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                                } else if (eCode == 40000) {
                                    Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear() {
        if (mController != null && mController.getConfig() != null) {
            mController.getConfig().removeSsoHandler(SHARE_MEDIA.SINA);
            mController.getConfig().removeSsoHandler(SHARE_MEDIA.TENCENT);
            mController = null;
        }
    }


}
