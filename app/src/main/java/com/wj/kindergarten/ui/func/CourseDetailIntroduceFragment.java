package com.wj.kindergarten.ui.func;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.MoreDiscussList;
import com.wj.kindergarten.bean.OnceSpecialCourse;
import com.wj.kindergarten.bean.OnceSpecialCourseList;
import com.wj.kindergarten.bean.SchoolDetail;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.coursefragments.CourseDetailFragmentThree;
import com.wj.kindergarten.ui.coursefragments.FamilyAssessFragmentThree;
import com.wj.kindergarten.ui.coursefragments.SchoolFragmentThree;
import com.wj.kindergarten.ui.other.ManDrawLine;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.ui.webview.LoadHtmlActivity;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import java.util.List;


public class CourseDetailIntroduceFragment extends Fragment {
	private View view;
	private RatingBarView rating_bar;
	private TextView course_detail_train_class_name;
	private TextView detail_course;
	private TextView teach_place;
	private TextView course_spend_time;
	private TextView nornal_course_price;
	private TextView coupon_price;
	private WebView course_detail_info;
	private LinearLayout more_assess_linera;
	private ImageView iv_heading;


	boolean isFirst;
	private LinearLayout parent_assess_linear;
	private WebView tv_school_introduce;
	private FrameLayout fl_title;
	private FrameLayout fl_bottom;

	private OnceSpecialCourse course;
	private PullToRefreshScrollView scroll_view;
	private RelativeLayout rl_price;
	private RelativeLayout rl_free_price;
	private MineCourseDetailActivity activity;
	private TextView pull_detail_text;
	private LinearLayout draw_line_ll;
	private ManDrawLine line_text;
	private RadioButton rb_three_corse;
	private RadioButton rb_three_school;
	private RadioButton rb_three_assess;
	private CourseDetailFragmentThree courseFragment;
	private SchoolFragmentThree schoolFragment;
	private FamilyAssessFragmentThree familyFragment;
	private FragmentTransaction transion;
	private View.OnClickListener listener;
	private boolean isAgain;
	private boolean isAssessOne;
	private TextView[] three;
	private HintInfoDialog dialog;
	private OnceSpecialCourseList oscs;
	private WebView webView;
	private View school_head;

	public void setOscs(OnceSpecialCourseList oscs){
		this.oscs = oscs;
//		courseFragment.setText(oscs);
		webView.loadUrl(oscs.getShare_url());
	}

	public OnceSpecialCourseList getOscs() {
		return oscs;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (view != null) return view;

		view = inflater.inflate(R.layout.activity_load_school_html, null);
		school_head = view.findViewById(R.id.school_head);
		school_head.setVisibility(View.GONE);
		webView = (WebView) view.findViewById(R.id.group_webView);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);

		return view;
	}


	public void getSchoolInfo(String schooluuid){
		if(dialog.isShowing()){
			dialog.cancel();
		}
		UserRequest.getTrainSchoolDetail(getActivity(), schooluuid, new RequestResultI() {
			@Override
			public void result(BaseModel domain) {
				SchoolDetailList sdl = (SchoolDetailList) domain;
				if (sdl != null) {
					SchoolDetail sd = sdl.getData();
					String text = null;
					schoolFragment.setUrl(sdl.getObj_url());
					if (!TextUtils.isEmpty(sd.getDescription())) {
						text = sd.getDescription();
					} else {
						text = "暂时没有学校内容介绍!";
					}
//                    tv_school_introduce.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
//                    tv_school_introduce.setVisibility(View.VISIBLE);
//                    scrollView.onRefreshComplete();
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
	public void getAssess(final int page) {
		//获取该对象的评价
		dialog.show();
		UserRequest.getMoreDiscuss(getActivity(), oscs.getData().getUuid(), page, new RequestResultI() {
			@Override
			public void result(BaseModel domain) {
				if (dialog.isShowing()) {
					dialog.cancel();
				}
				MoreDiscussList mdl = (MoreDiscussList) domain;
				if (mdl.getList() != null && mdl.getList().getData() != null
						&& mdl.getList().getData().size() > 0) {
					familyFragment.addList(mdl.getList().getData());
				} else {
					if (page == 1) {
						familyFragment.noData();
					}
					familyFragment.setNoRequest();
					ToastUtils.noMoreContentShow();
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

}
