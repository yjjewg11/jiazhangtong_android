package com.wj.kindergarten.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Msg;
import com.wj.kindergarten.bean.MsgDataModel;
import com.wj.kindergarten.bean.Teacher;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.addressbook.LeaderMessageActivty;
import com.wj.kindergarten.ui.addressbook.TeacherMessageActivty;
import com.wj.kindergarten.ui.coursefragments.MessageAddressFragment;
import com.wj.kindergarten.ui.func.ArticleActivity;
import com.wj.kindergarten.ui.func.CourseListActivity;
import com.wj.kindergarten.ui.func.FoodListActivity;
import com.wj.kindergarten.ui.func.InteractionListActivity;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.ui.func.NoticeActivity;
import com.wj.kindergarten.ui.func.SignListActivity;
import com.wj.kindergarten.ui.message.MessageAdapter;
import com.wj.kindergarten.ui.messagepag.MessageSunFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueSingleInfoActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.TransportListener;
import com.wj.kindergarten.ui.mine.photofamilypic.dbupdate.DbUtils;
import com.wj.kindergarten.ui.more.HtmlActivity;
import com.wj.kindergarten.ui.more.PfRefreshLinearLayout;
import com.wj.kindergarten.ui.webview.SchoolIntroduceActivity;
import com.wj.kindergarten.ui.webview.WebviewActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 * MainFragment
 *
 * @Description: 消息
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class MessageFragment extends Fragment {

    private View view;
    private ViewPager message_viewPager;
    private RadioGroup message_view_radioGroup;
    private ImageView message_view_add_constact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((MainActivity)getActivity()).hideActionbar();
        if(view != null) return view;
        view = inflater.inflate(R.layout.message_view_pager,null);
        message_view_add_constact = (ImageView) view.findViewById(R.id.message_view_add_constact);
        message_view_add_constact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        message_viewPager = (ViewPager) view.findViewById(R.id.message_viewPager);
        message_view_radioGroup = (RadioGroup) view.findViewById(R.id.message_view_radioGroup);
        message_view_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.message_view_chooose) {
                    message_viewPager.setCurrentItem(0);
                } else if (checkedId == R.id.address_view_chooose) {
                    message_viewPager.setCurrentItem(1);
                }
            }
        });
        message_viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new MessageSunFragment();
                } else {
                    return new MessageAddressFragment();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        message_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    message_view_radioGroup.check(R.id.message_view_chooose);
                }else if (position == 1) {
                    message_view_radioGroup.check(R.id.address_view_chooose);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }
}
