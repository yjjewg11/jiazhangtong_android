package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.IntervalUtil;
import com.wj.kindergarten.utils.Utils;

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
    private HintInfoDialog dialog = null;
    private int type = 0;

    public NormalReplyAdapter(int type, Context context) {
        this.context = context;
        this.type = type;
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
        ViewHolder holder = null;
        if (null == view) {
            view = View.inflate(context, R.layout.item_normal_reply, null);
            holder = new ViewHolder();
            holder.image = (CircleImage) view.findViewById(R.id.pl_img);
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.content = (TextView) view.findViewById(R.id.tv_content);
            holder.date = (TextView) view.findViewById(R.id.tv_date);
            holder.zan = (ImageView) view.findViewById(R.id.image_zan);
            holder.num = (TextView) view.findViewById(R.id.tv_num);
            holder.layout = (LinearLayout) view.findViewById(R.id.layout_zan);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Reply reply = replies.get(i);
        if (null != reply) {
            ImageLoaderUtil.displayImage(reply.getCreate_img(), holder.image);
            holder.title.setText(Utils.getText(reply.getCreate_user()));
            holder.content.setText(EmotUtil.getEmotionContent(context, Utils.getText(reply.getContent())));
            holder.date.setText(IntervalUtil.getInterval(reply.getCreate_time()));
            if (null != reply.getDianzan()) {
                if (reply.getDianzan().isCanDianzan()) {
                    holder.zan.setImageDrawable(context.getResources().getDrawable(R.drawable.interaction_mine_zan));
                } else {
                    holder.zan.setImageDrawable(context.getResources().getDrawable(R.drawable.interaction_mine_zan_on));
                }
                holder.layout.setOnClickListener(new ZanComment(reply, holder.zan, holder.num));
                holder.num.setText(Utils.getText(reply.getDianzan().getCount() + ""));
            }

        }
        return view;
    }

    private class ZanComment implements View.OnClickListener {
        private Reply reply = null;
        private ImageView imageView;
        private TextView textView;

        public ZanComment(Reply reply, ImageView imageView, TextView textView) {
            this.reply = reply;
            this.imageView = imageView;
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            if (reply.getDianzan().isCanDianzan()) {
                zan(reply, imageView, textView);
            } else {
                cacelZan(reply, imageView, textView);
            }
        }
    }

    private void zan(final Reply reply, final ImageView imageView, final TextView textView) {
        dialog = new HintInfoDialog(context, "点赞中，请稍后...");
        dialog.show();
        UserRequest.zan(context, reply.getUuid(), type, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(context, "点赞成功");
                reply.getDianzan().setCanDianzan(false);
                reply.getDianzan().setCount(reply.getDianzan().getCount() + 1);
                textView.setText(reply.getDianzan().getCount() + "");
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.interaction_mine_zan_on));
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(context, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void cacelZan(final Reply reply, final ImageView imageView, final TextView textView) {
        dialog = new HintInfoDialog(context, "取消点赞中，请稍后...");
        dialog.show();
        UserRequest.zanCancel(context, reply.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(context, "取消点赞成功");
                reply.getDianzan().setCanDianzan(true);
                if (reply.getDianzan().getCount() > 0) {
                    reply.getDianzan().setCount(reply.getDianzan().getCount() - 1);
                } else {
                    reply.getDianzan().setCount(0);
                }
                textView.setText(reply.getDianzan().getCount() + "");
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.interaction_mine_zan));
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(context, message);
                }
                dialog.dismiss();
            }
        });
    }

    class ViewHolder {
        CircleImage image;
        TextView title;
        TextView content;
        TextView date;
        ImageView zan;
        TextView num;
        LinearLayout layout;
    }
}
