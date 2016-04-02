package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Sign;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SignAdapter
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 9:50
 */
public class SignAdapter extends BaseAdapter {
    private List<Sign> datas = new ArrayList<>();
    private Context context;

    public SignAdapter(Context context, List<Sign> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_sign_list, null);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_student_name);
            viewHolder.headIv = (CircleImage) view.findViewById(R.id.item_sign_list_head);
            viewHolder.timeTv = (TextView) view.findViewById(R.id.item_sign_list_time);
            viewHolder.placeTv = (TextView) view.findViewById(R.id.item_sign_list_place);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.item_sign_list_name);
            viewHolder.typeTv = (TextView) view.findViewById(R.id.item_sign_list_type);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Sign sign = datas.get(i);
        ChildInfo info = getImageUrl(sign.getStudentuuid());
        if (null != info) {
            ImageLoaderUtil.displayImage(info.getHeadimg(), viewHolder.headIv);
            viewHolder.name.setText(Utils.getText(info.getName()));
        } else {
            ImageLoaderUtil.displayImage("", viewHolder.headIv);
        }
        viewHolder.timeTv.setText("时间：" + sign.getSign_time());
        viewHolder.placeTv.setText("地点：" + sign.getGroupname());
        viewHolder.nameTv.setText("打卡人：" + sign.getSign_name());
        if (sign.getType() == 1) {
            viewHolder.typeTv.setText("打卡类型：家长打卡");
        } else if (sign.getType() == 2) {
            viewHolder.typeTv.setText("打卡类型：老师签到");
        } else if (sign.getType() == 3) {
            viewHolder.typeTv.setText("打卡类型：家长签到");
        } else {
            viewHolder.typeTv.setText("打卡类型：");
        }

        return view;
    }

    private ChildInfo getImageUrl(String uuid) {
        if (CGApplication.getInstance().getLogin() != null) {
            for (ChildInfo childInfo : CGApplication.getInstance().getLogin().getList()) {
                if (null != childInfo && childInfo.getUuid().equals(uuid)) {
                    return childInfo;
                }
            }
        }
        return null;
    }

    class ViewHolder {
        TextView name;
        CircleImage headIv;
        TextView timeTv;
        TextView placeTv;
        TextView nameTv;
        TextView typeTv;
    }
}
