package com.wj.kindergarten.ui.specialcourse;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllTeacher;
import com.wj.kindergarten.bean.AllTeacherList;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.GetAssessState;
import com.wj.kindergarten.bean.GetAssessStateList;
import com.wj.kindergarten.bean.OnceSpecialCourse;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.MineCourseDetailActivity;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MineDiscussFragment extends Fragment {


	private static final int HAVA_ASSESS = 1001;
	private static final int NO_ASSESS = 1002;
	private List<AllTeacher> teacher_list = new ArrayList<>();
	View view = null;
	private List<GetAssessState> stateList = new ArrayList<>();
	private static final int[] arrays = new int[]{81, 82, 83};
	//    private Button bt;
	private EditText et;
	private int count = 0;
	boolean isFirst;
	private LinearLayout teacher_linear;
	private MineCourseDetailActivity mcd;
	private HashMap<String, RatingBarView> map = new HashMap<>();
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case 100:
					break;
				case HAVA_ASSESS:
					et.setVisibility(View.GONE);
					input_content.setVisibility(View.VISIBLE);
					//显示状态
					//设置星星的评价
					setHavedAssess();
					break;
				case NO_ASSESS:
					et.setVisibility(View.VISIBLE);
					input_content.setVisibility(View.GONE);

					break;

			}
		}
	};
	private OnceSpecialCourse course;
	private RatingBarView[] ratingBars;
	private LinearLayout root_assess_ll;
	private LinearLayout discuss_assess_soft_state;
	private HintInfoDialog dialog;

	private void setHavedAssess() {
		Iterator iterator = stateList.iterator();
		while (iterator.hasNext()) {
			GetAssessState getAssessState = (GetAssessState) iterator.next();
			if (getAssessState.getType() == NormalReplyListActivity.TRAIN_COURSE) {
				ratingBars[0].setFloatStar(Integer.valueOf(getAssessState.getScore()), true);
				ratingBars[0].setmClickable(false);
				input_content.setText("" + (TextUtils.isEmpty(getAssessState.getContent()) == true ? "" : getAssessState.getContent()));
			} else if (getAssessState.getType() == NormalReplyListActivity.TRAIN_SCHOOL) {
				ratingBars[1].setFloatStar((Integer.valueOf(getAssessState.getScore())), true);
				ratingBars[1].setmClickable(false);
			} else if (teacher_list.size() > 0) {
				Iterator<Map.Entry<String, RatingBarView>> iteratorMap = map.entrySet().iterator();
				while(iteratorMap.hasNext()){
					Map.Entry<String, RatingBarView> bean = iteratorMap.next();
					if (getAssessState.getExt_uuid().equals(bean.getKey())) {
						   bean.getValue().setmClickable(false);
                           bean.getValue().setFloatStar(Utils.stringToFloat(getAssessState.getScore()),true);
					}
				}

			}
		}

		if(map.size() > 0){
			Iterator<RatingBarView> bianLi = map.values().iterator();
			while(bianLi.hasNext()){
				bianLi.next().setmClickable(false);
			}
		}

		teacher_submit.setText("已评价");
		teacher_submit.setBackgroundColor(Color.parseColor("#e5e5e5"));
		teacher_submit.setClickable(false);

	}

	private TextView input_content;

	private TextView teacher_submit;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//请求老师列表
		if (view != null) return view;
		mcd = (MineCourseDetailActivity) getActivity();
		course = mcd.getCourses();
		view = inflater.inflate(R.layout.fragment_mine_discuss, null);
		ratingBars = new RatingBarView[]{
				(RatingBarView) view.findViewById(R.id.fragment_course_rating_bar),
				(RatingBarView) view.findViewById(R.id.fragment_school_rating_bar),
		};
		ratingBars[0].setStar(5);
		ratingBars[1].setStar(5);
		ratingBars[0].setmClickable(true);
		ratingBars[1].setmClickable(true);
		et = (EditText) view.findViewById(R.id.fragment_mine_discuss_edit_text);
		teacher_linear = (LinearLayout) view.findViewById(R.id.all_teacher_linear);
//            bt = (Button)view.findViewById(R.id.discuss_submit);
		input_content = (TextView) view.findViewById(R.id.input_content);
		teacher_submit = (TextView) view.findViewById(R.id.teacher_submit);
		root_assess_ll = (LinearLayout) view.findViewById(R.id.root_assess_ll);

		return view;
	}

	boolean isOnce;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!isOnce) {
			isOnce = true;
			setListener();
			requestTeacher();
			getAssess();
		}

	}

	private void setListener() {
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.teacher_submit:
						dialog = new HintInfoDialog(getActivity(),"保存评价中!");
						dialog.show();
						saveAssess(course.getGroupuuid(), 81, ratingBars[1].getClickedCount(), et.getText().toString(), input_content, teacher_submit, ratingBars[1]);
						saveAssess(course.getUuid(), 82, ratingBars[0].getClickedCount(), et.getText().toString(), input_content, teacher_submit, ratingBars[0]);
						if (!teacher_list.isEmpty()) {
							RatingBarView[] array = new RatingBarView[teacher_list.size()];
							for (AllTeacher allTeacher : teacher_list) {
								//TODO 查看评价老师的类型
								saveAssess(allTeacher.getUuid(), 83, map.get(allTeacher.getUuid()).getClickedCount(), et.getText().toString()
										, input_content, teacher_submit, map.values().toArray(array));
							}
						}
						break;
				}
			}
		};
		teacher_submit.setOnClickListener(listener);
	}

	private void getAssess() {
		UserRequest.getAssessState(getActivity(), mcd.getCourseuuid(), new RequestResultI() {
			@Override
			public void result(BaseModel domain) {
				GetAssessStateList gas = (GetAssessStateList) domain;
				if (gas != null & gas.getList() != null && gas.getList().getData() != null
						&& gas.getList().getData().size() > 0) {
					stateList.addAll(gas.getList().getData());
					//有评价
					mHandler.sendEmptyMessage(HAVA_ASSESS);
				} else {

					mHandler.sendEmptyMessage(NO_ASSESS);
				}

			}

			@Override
			public void result(List<BaseModel> domains, int total) {

			}

			@Override
			public void failure(String message) {
				mcd.noView(root_assess_ll);
			}
		});
	}

	private void requestTeacher() {

		UserRequest.getAllAssessTeacher(getActivity(), mcd.getCourseuuid(), new RequestResultI() {
			@Override
			public void result(BaseModel domain) {
				AllTeacherList atl = (AllTeacherList) domain;
				if (atl != null & atl.getList() != null && atl.getList().size() > 0) {
					teacher_list.addAll(atl.getList());
					for (AllTeacher at : teacher_list) {
						try {
							View rlView = View.inflate(getActivity(), R.layout.teacher_assess_item, null);
							TextView tv = (TextView) rlView.findViewById(R.id.signal_teacher);
							RatingBarView ratingBarView = (RatingBarView) rlView.findViewById(R.id.fragment_teacher_rating_bar);
							ratingBarView.setmClickable(true);
							ratingBarView.setStar(5);
							map.put(at.getUuid(), ratingBarView);
							tv.setText("" + at.getName());
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							params.bottomMargin = 5;
							teacher_linear.addView(rlView, params);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
//					teacher_submit.setVisibility(View.VISIBLE);
				} else {
//					teacher_submit.setVisibility(View.GONE);
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

	private void saveAssess(String extend_uuid, final int type, int score, String content, final TextView input_content,
							final TextView submit, final RatingBarView... methondRatingBars) {


		UserRequest.sendSpecialCourseAssess(getActivity(), extend_uuid, mcd.getCourseuuid(), type, score, content, new RequestResultI() {
			@Override
			public void result(BaseModel domain) {
				CGSharedPreference.setMineCourseIsSendSContent();
				if(type == NormalReplyListActivity.TRAIN_COURSE){
					input_content.setVisibility(View.VISIBLE);
					input_content.setText("" + et.getText().toString());
					et.setVisibility(View.GONE);
				}

				submit.setClickable(false);
				submit.setGravity(Gravity.CENTER_HORIZONTAL);
				submit.setText("已评价");
				submit.setBackgroundColor(Color.parseColor("#e5e5e5"));
				if (methondRatingBars.length != 0 && methondRatingBars.length == 1) {
					methondRatingBars[0].setmClickable(false);
				} else {
					for (RatingBarView ratingBarView : methondRatingBars) {
						ratingBarView.setmClickable(false);
					}
				}

				if(dialog.isShowing()){
					dialog.cancel();
					ToastUtils.showMessage("评价保存成功");
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

