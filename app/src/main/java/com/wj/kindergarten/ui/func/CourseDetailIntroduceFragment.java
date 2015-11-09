package com.wj.kindergarten.ui.func;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.OnceSpecialCourse;
import com.wj.kindergarten.bean.OnceSpecialCourseList;
import com.wj.kindergarten.bean.SchoolDetail;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.ImageLoaderUtil;

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

	public void setCourse(OnceSpecialCourse course) {
		this.course = course;
		course_detail_train_class_name.setText("" + course.getSubtype());
		rating_bar.setStar(course.getCt_stars());
		teach_place.setText("" + course.getAddress());
		course_detail_info.loadDataWithBaseURL(null, course.getContext(), "text/html", "utf-8", null);
		nornal_course_price.setText("" + course.getFees());
		coupon_price.setText("" + course.getDiscountfees());
		if (course.getFees() < 1) {
			nornal_course_price.setVisibility(View.GONE);
		} else {
			nornal_course_price.setVisibility(View.VISIBLE);
		}
		if (course.getDiscountfees() < 1) {
			coupon_price.setVisibility(View.GONE);
		} else {
			coupon_price.setVisibility(View.VISIBLE);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		if (view != null) return view;

		view = inflater.inflate(R.layout.activity_special_course_info, null);
		rating_bar = (RatingBarView) view.findViewById(R.id.item_special_course_list_view__rating_bar);
		course_detail_train_class_name = (TextView) view.findViewById(R.id.course_detail_train_class_name);
		detail_course = (TextView) view.findViewById(R.id.detail_course);
		teach_place = (TextView) view.findViewById(R.id.teach_place);
		course_spend_time = (TextView) view.findViewById(R.id.course_spend_time);
		nornal_course_price = (TextView) view.findViewById(R.id.nornal_course_price);
		coupon_price = (TextView) view.findViewById(R.id.nornal_course_price);
		course_detail_info = (WebView) view.findViewById(R.id.course_detail_info);
		more_assess_linera = (LinearLayout) view.findViewById(R.id.more_assess_linera);
		iv_heading = (ImageView) view.findViewById(R.id.once_iv);
		parent_assess_linear = (LinearLayout) view.findViewById(R.id.parent_assess_linear);
		parent_assess_linear.setVisibility(View.GONE);
		tv_school_introduce = (WebView) view.findViewById(R.id.tv_school_introduce);
		tv_school_introduce.setVisibility(View.GONE);
		fl_title = (FrameLayout) view.findViewById(R.id.head_title);
		fl_title.setVisibility(View.GONE);
		fl_bottom = (FrameLayout) view.findViewById(R.id.btom_llll);
		fl_bottom.setVisibility(View.GONE);
		scroll_view = (PullToRefreshScrollView) view.findViewById(R.id.course_detail_scrollview);
		scroll_view.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		scroll_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				//下拉加载学校数据
				if (!isFirst) {
					loadData();
					isFirst = true;
				} else {
					scroll_view.onRefreshComplete();
				}
			}
		});


		return view;
	}

	private void loadData() {


		String uuid = null;
		MineCourseDetailActivity msc = (MineCourseDetailActivity) getActivity();

		uuid = msc.getCourses().getGroupuuid();
		UserRequest.getTrainSchoolDetail(getActivity(), uuid, new RequestResultI() {
			@Override
			public void result(BaseModel domain) {

				SchoolDetailList sdl = (SchoolDetailList) domain;
				if (sdl != null && sdl.getData() != null) {
					SchoolDetail sd = sdl.getData();
					String text = null;
					if (!TextUtils.isEmpty(sd.getDescription())) {
						text = sd.getDescription();
					} else {
						text = "当前暂无学校介绍!";
					}
					tv_school_introduce.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
					tv_school_introduce.setVisibility(View.VISIBLE);
				}
				if (scroll_view.isRefreshing()) scroll_view.onRefreshComplete();
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
