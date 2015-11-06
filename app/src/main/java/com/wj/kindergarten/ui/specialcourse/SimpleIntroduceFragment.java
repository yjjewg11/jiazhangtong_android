package com.wj.kindergarten.ui.specialcourse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
<<<<<<< HEAD
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SchoolDetail;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.SchoolDetailInfoActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;
=======
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wenjie.jiazhangtong.R;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0


public class SimpleIntroduceFragment extends Fragment {

    private View view;
<<<<<<< HEAD
    private SchoolDetail sd;

    boolean isFirst ;
    private WebView tv_html;
    private ImageView iv_img;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

<<<<<<< HEAD
        if(view != null){return view;}

        if(!isFirst){
            loadData();
            isFirst = true;
        }

            view = inflater.inflate(R.layout.fragment_simple_introduce,null);
            tv_html = (WebView)view.findViewById(R.id.tv_html);
//            iv_img = (ImageView)view.findViewById(R.id.iv_school);

        return view;
    }

    private void loadData() {
        SchoolDetailInfoActivity sif = (SchoolDetailInfoActivity)getActivity();
        UserRequest.getTrainSchoolDetail(sif, sif.getSchool().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SchoolDetailList sdl = (SchoolDetailList) domain;
                if(sdl!=null && sdl.getData() != null ){
                    sd = sdl.getData();
                    tv_html.loadDataWithBaseURL(null,sd.getDescription(),"text/html","utf-8",null);
                }else{

                    RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.introduce_rllll);
                    relativeLayout.removeAllViews();
                    relativeLayout.addView(View.inflate(getActivity(),R.layout.nothing_view,null));
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }
=======

        if(view != null){return view;}


        view = inflater.inflate(R.layout.fragment_simple_introduce,null);
        WebView webView = (WebView) view.findViewById(R.id.introduce_webview);
        WebSettings settings = webView.getSettings();
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.loadUrl("http://www.baidu.com");

        return view;
    }
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
}
