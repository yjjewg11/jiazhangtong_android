package com.wj.kindergarten.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.ui.mine.ChildActivity;
import com.wj.kindergarten.ui.mine.SettingActivity;

/**
 * MainFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class MineFragment extends Fragment {
    private View rootView;
    private LinearLayout childContent;
    private TextView collectTv;
    private TextView settingTv;
    private Login login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).clearCenterIcon();
        ((MainActivity) getActivity()).setTitleText("我的");
        login = ((CGApplication) CGApplication.getInstance()).getLogin();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, null, false);
            initViews(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        addChildren();
        return rootView;
    }

    private void initViews(View rootView) {
        childContent = (LinearLayout) rootView.findViewById(R.id.mine_content);
        collectTv = (TextView) rootView.findViewById(R.id.mine_collect);
        settingTv = (TextView) rootView.findViewById(R.id.mine_setting);
        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
    }

    private void addChildren() {
        childContent.removeAllViews();
        if (login != null && login.getList() != null) {
            int i = 0;
            for (final ChildInfo childInfo : login.getList()) {
                View view = View.inflate(getActivity(), R.layout.item_mine_list, null);
                if (i != login.getList().size() - 1) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.
                            LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.big_padding);
                    view.setLayoutParams(layoutParams);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ChildActivity.class);
                        intent.putExtra("child", childInfo);
                        startActivity(intent);
                    }
                });

                CircleImage headIv = (CircleImage) view.findViewById(R.id.item_mine_list_head);
                TextView nameTv = (TextView) view.findViewById(R.id.item_mine_list_name);
                TextView nickTv = (TextView) view.findViewById(R.id.item_mine_list_nike);
                TextView sexBTv = (TextView) view.findViewById(R.id.item_mine_list_sexb);

                headIv.setImageResource(R.drawable.touxiang);
                nameTv.setText(childInfo.getName());
                nickTv.setText("昵称:" + childInfo.getNickname());
                sexBTv.setText((childInfo.getSex() == 0 ? "男" : "女") + "        " + childInfo.getBirthday());

                childContent.addView(view);
                i++;
            }
        }
    }
}
