package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.utils.URLImageParser;

import java.util.ArrayList;
import java.util.List;

/**
 * NormalReplyAdaper
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/14 23:25
 */
public class NormalReplyAdapter extends BaseAdapter {
    private Context context;
    private List<Reply> replies = new ArrayList<>();

    public NormalReplyAdapter(Context context) {
        this.context = context;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return replies.size();
    }

    @Override
    public Object getItem(int i) {
        return replies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rView = View.inflate(context, R.layout.item_normal_reply, null);
        Reply reply = replies.get(i);
        TextView textView = (TextView) rView.findViewById(R.id.normal_reply_title);
        String info = "<font  color='#ff4966'>" + reply.getCreate_user() + ":</font>"
                + reply.getContent().trim();
        textView.setText(Html.fromHtml(info, new URLImageParser(textView, context), null), TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return rView;
    }
}
