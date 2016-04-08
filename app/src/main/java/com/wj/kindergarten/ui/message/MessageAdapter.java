package com.wj.kindergarten.ui.message;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.MainItem;
import com.wj.kindergarten.bean.MsgDataModel;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.utils.IntervalUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * MessageAdapter
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 10:31
 */
public class MessageAdapter extends BaseAdapter {
    private ArrayList<MsgDataModel> msgs;
    private Context context;

    public MessageAdapter(Context context, ArrayList<MsgDataModel> list) {
        this.msgs = list;
        this.context = context;
        sparseIntArray = new SparseIntArray();
        sparseIntArray.append(1,R.drawable.gonggao);
        sparseIntArray.append(3,R.drawable.main_item_jingpin);
        sparseIntArray.append(4,R.drawable.baobaoruxue);
        sparseIntArray.append(5,R.drawable.techangkechen_school);
        sparseIntArray.append(6,R.drawable.shipu);
        sparseIntArray.append(7,R.drawable.kechenbiao);
        sparseIntArray.append(8,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(10,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(11,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(12,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(13,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(20,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(21,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(22,R.drawable.tongzhi_xiaoxi_btn);
        sparseIntArray.append(99,R.drawable.banjihudong);

    }
//
//    int [] picTags = new int[]{
//            R.drawable.banjihudong,// "班级互动"
//            R.drawable.baobaoruxue, //"宝宝入学"
//            R.drawable.gonggao,// "校园公告"
//            R.drawable.qiandao, //"签到记录"
//            R.drawable.kechenbiao,// "课程表"
//            R.drawable.shipu, //"每日食谱"
//            R.drawable.main_item_jingpin,//精品文章
//            R.drawable.techangkechen_school,//特长课程
//            R.drawable.youhuihuodong90,//"优惠活动",
//            R.drawable.pingjialaoshi, //"评价老师"
//            R.drawable.gengduo,// "更多"
//            R.drawable.tongxunlu,//"通讯录"
//            R.drawable.baobaoruxue//宝宝入学
//            //幼儿园介绍暂用宝宝入学
//    };
    SparseIntArray sparseIntArray;


    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.view_message_item, null);
            viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_picture);
            viewHolder.ivQuan = (ImageView) convertView.findViewById(R.id.iv_read);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.message_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.message_content);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.message_date);
            convertView.setTag(viewHolder);
            convertView.setBackgroundResource(R.drawable.seclector_meesage_back_ground);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MsgDataModel msg = msgs.get(position);
        if (null != msg) {
            if (msg.getIsread() == 0) {
                viewHolder.ivQuan.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivQuan.setVisibility(View.GONE);
            }
            int type = 0;
            if(msg.getType() == 0) {
                type = 1;
            }else {
                type = msg.getType();
            }
            int drawable = sparseIntArray.get(type);
            if(drawable <= 0){
                viewHolder.ivHead.setImageResource(R.drawable.message_image);
            }else {
                viewHolder.ivHead.setImageResource(drawable);
            }

            viewHolder.tvTitle.setText(Utils.getText(msg.getTitle()));
            viewHolder.tvContent.setText(Utils.getText(msg.getMessage()));
            viewHolder.tvDate.setText(IntervalUtil.getInterval(msg.getCreate_time()));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView ivHead;
        ImageView ivQuan;
        TextView tvTitle;
        TextView tvContent;
        TextView tvSchool;
        TextView tvDate;
    }
}
