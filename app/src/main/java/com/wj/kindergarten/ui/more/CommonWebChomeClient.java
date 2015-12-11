//package com.wj.kindergarten.ui.more;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//
//public class CommonWebChomeClient extends WebChromeClient {
//
//    private Context context;
//
//    public CommonWebChomeClient(Context context) {
//        this.context = context;
//    }
//
//    public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                String acceptType, String capture) {
//        MainActivity.instance.myUploadMsg = uploadMsg;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        MainActivity.instance.startActivityForResult(
//                Intent.createChooser(intent, "完成操作需要使用"),
//                GloableUtils.FILECHOOSER_RESULTCODE);
//
//    }
//
//    // 3.0 + 调用这个方法
//    public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                String acceptType) {
//        MainActivity.instance.myUploadMsg = uploadMsg;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        MainActivity.instance.startActivityForResult(
//                Intent.createChooser(intent, "完成操作需要使用"),
//                GloableUtils.FILECHOOSER_RESULTCODE);
//    }
//
//    // Android < 3.0 调用这个方法
//    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//        MainActivity.instance.myUploadMsg = uploadMsg;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        MainActivity.instance.startActivityForResult(
//                Intent.createChooser(intent, "完成操作需要使用"),
//                GloableUtils.FILECHOOSER_RESULTCODE);
//    }
//
//    @Override
//    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
//    }
//}
//
