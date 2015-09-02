package com.wj.kindergarten.ui.emot;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.Emot;
import com.wj.kindergarten.ui.addressbook.EmotManager;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EmotUtil
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-13 18:15
 */
public class EmotUtil {

    /**
     * 显示表情符号
     *
     * @param context
     * @param source  包含表情的字符串 例如 ddd[大笑][笑]
     * @return
     */
    public static SpannableString getEmotionContent(final Context context, String source) {
        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();
        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            Bitmap scaleBitmap = getImage(source.substring(start, start + key.length()));
            if (null != scaleBitmap) {
                ImageSpan span = new ImageSpan(context, scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    /**
     * 获得缓存的消息图片
     *
     * @param name
     * @return
     */
    private static Bitmap getImage(String name) {
        if (Utils.stringIsNull(name)) {
            return null;
        }
        String url = getImageUrlByName(name);
        if (!Utils.stringIsNull(url)) {
            int width;
            int height;
            width = height = CGApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.big_text);
            if (ImageLoaderUtil.getImageFromCache(url) != null) {
                return Bitmap.createScaledBitmap(ImageLoaderUtil.getImageFromCache(url), width, height, true);
            }
        }
        return null;
    }

    /**
     * 获得表情图片的url
     *
     * @param name
     * @return
     */
    private static String getImageUrlByName(String name) {
        for (Emot emot : EmotManager.getEmots()) {
            if (name.contains(emot.getDatavalue())) {
                return emot.getDescription();
            }
        }
        return "";
    }

    /**
     * 封装消息内容
     *
     * @param name
     * @return
     */
    public static String getHtmlImg(String name) {
        ArrayList<Emot> emots = EmotManager.getEmots();
        for (Emot emot : emots) {
            if (null != emot) {
                if (name.equals(emot.getDatavalue())) {
                    String str = "<img alt=" + "\"" + emot.getDatavalue() + "\"" + " src=" + "\"" + emot.getDescription() + "\"" + "/>";
                    return str;
                }
            }
        }
        return "";
    }

    private static boolean isEmot = false;

    //封装要发送的消息内容
    public static String getEmotNames(String str) {
        String msg = "";
        int start = 0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            String s = String.valueOf(item);
            if ("[".equals(s)) {
                isEmot = true;
                start = i;
            }

            if (!isEmot) {
                msg += s;
            }

            if ("]".equals(s)) {
                msg += EmotUtil.getHtmlImg(str.substring(start + 1, i));
                isEmot = false;
            }

        }
        return msg;
    }
}
