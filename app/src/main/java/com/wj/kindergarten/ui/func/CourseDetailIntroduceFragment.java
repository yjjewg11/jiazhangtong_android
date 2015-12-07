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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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
import com.nineoldandroids.animation.ObjectAnimator;
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
import com.wj.kindergarten.ui.other.TopWebView;
import com.wj.kindergarten.ui.webview.LoadHtmlActivity;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import java.util.List;


public class CourseDetailIntroduceFragment extends Fragment implements View.OnTouchListener,GestureDetector.OnGestureListener{
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
	private TopWebView webView;
	private View school_head;
	private GestureDetector gd;
	private float downY;
	private int moveGloal;
	private int upInstance = 60;
	private MineCourseDetailActivity activity;
	private ObjectAnimator resetAnim;

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

		activity = (MineCourseDetailActivity)getActivity();
		view = inflater.inflate(R.layout.activity_load_school_html, null);
		school_head = view.findViewById(R.id.school_head);
		school_head.setVisibility(View.GONE);
		webView = (TopWebView) view.findViewById(R.id.group_webView);
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
		webView.setOnTouchListener(this);
		gd = new GestureDetector(getActivity(), this);

		//创建移除动画
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
//					familyFragment.addList(mdl.getList().getData());
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(event.getAction() == MotionEvent.ACTION_DOWN){
			downY = event.getY();
		}

		if(event.getAction() == MotionEvent.ACTION_MOVE){

			if(event.getY() - downY < 0 && event.getY() - downY < -50 && !activity.isLocationTop()){
				//向上滑动
				activity.getAnim().start();
				activity.setIsLocationTop(true);
			}

			//判断webview是否位于顶部，
			if(webView.isTop() && activity.isLocationTop()){

				moveGloal = (int)(event.getY() - downY);
				if(moveGloal < 0) {moveGloal = 0;}
				if(moveGloal > 100 ){ moveGloal = 100;}
				//对实际移动距离做缩小，造成用力拉的感觉
				((RelativeLayout.LayoutParams)webView.getLayoutParams()).topMargin = moveGloal/2;
				 webView.requestLayout();
			}
		}

		if(event.getAction() == MotionEvent.ACTION_UP){
			if(webView.isTop() && activity.isLocationTop()) {
				if (moveGloal > 60) {
					//滑动距离足够，可以进行刷新操作。
					activity.getAnim().reverse();
					activity.setIsLocationTop(false);
				}
			}
				resetAnim = ObjectAnimator.ofInt(new Wrapper(webView),"topMagin",0);
				resetAnim.setDuration(500);
				resetAnim.setInterpolator(new AccelerateDecelerateInterpolator());
				resetAnim.start();

		}


		return false;
	}

	private boolean judgeIsTop() {
		return webView.isTop();
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	class Wrapper{
		private WebView webView;
		private int topMagin;

		public Wrapper(WebView webView) {
			this.webView = webView;
		}

		public int getTopMagin() {
			return ((RelativeLayout.LayoutParams)webView.getLayoutParams()).topMargin;
		}

		public void setTopMagin(int topMagin) {
			((RelativeLayout.LayoutParams)webView.getLayoutParams()).topMargin = topMagin;
			webView.requestLayout();
		}
	}


}
