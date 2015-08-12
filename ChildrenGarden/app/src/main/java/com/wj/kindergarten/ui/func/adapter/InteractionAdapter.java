package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Interaction;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.ReplyList;
import com.wj.kindergarten.bean.ZanItem;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.InteractionSentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * InteractionAdapter
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 9:11
 */
public class InteractionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Interaction> dataList = new ArrayList<>();
    private HashMap<String, List<Reply>> replyMap = new HashMap<>();
    private HashMap<String, BaseModel> zanMap = new HashMap<>();

    public InteractionAdapter(Context context, List<Interaction> dataList) {
        mContext = context;
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_interaction, null);

            viewHolder.headCi = (CircleImage) view.findViewById(R.id.item_interaction_head);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.item_interaction_name);
            viewHolder.textTv = (TextView) view.findViewById(R.id.item_interaction_text);
            viewHolder.imageIv = (ImageView) view.findViewById(R.id.item_interaction_img);
            viewHolder.dateTv = (TextView) view.findViewById(R.id.item_interaction_date);
            viewHolder.zanIv = (ImageView) view.findViewById(R.id.item_interaction_zan);
            viewHolder.replyIv = (ImageView) view.findViewById(R.id.item_interaction_reply);
            viewHolder.mineZanTv = (TextView) view.findViewById(R.id.item_interaction_mine_zan);
            viewHolder.replyContent = (LinearLayout) view.findViewById(R.id.item_interaction_reply_content);
            viewHolder.iReplyEt = (EditText) view.findViewById(R.id.item_interaction_i_reply);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Interaction interaction = dataList.get(i);

        viewHolder.headCi.setImageResource(R.drawable.touxiang);
        viewHolder.nameTv.setText(interaction.getTitle());
        viewHolder.textTv.setText(Html.fromHtml(interaction.getContent()));
        viewHolder.imageIv.setVisibility(View.GONE);
        viewHolder.dateTv.setText(interaction.getCreate_time());
        viewHolder.iReplyEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InteractionSentActivity.class);
                intent.putExtra("uuid", interaction.getUuid());
                mContext.startActivity(intent);
            }
        });
        viewHolder.replyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                InputMethodManager imm = (InputMethodManager) viewHolder.iReplyEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//                viewHolder.iReplyEt.requestFocus();
//                viewHolder.iReplyEt.requestLayout();
                Intent intent = new Intent(mContext, InteractionSentActivity.class);
                intent.putExtra("uuid", interaction.getUuid());
                mContext.startActivity(intent);
            }
        });
        viewHolder.zanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zanMap.containsKey(interaction.getUuid())) {
                    ZanItem zanItem = (ZanItem) zanMap.get(interaction.getUuid());
                    if (zanItem.isCanDianzan()) {
                        return;
                    }
                }
                setZan(interaction.getUuid(), viewHolder.zanIv);
            }
        });

        if (!zanMap.containsKey(interaction.getUuid())) {
            getZanList(interaction.getUuid());
        } else {
            ZanItem zanItem = (ZanItem) zanMap.get(interaction.getUuid());
            if (zanItem != null) {
                viewHolder.mineZanTv.setText(zanItem.getNames() + "等" + zanItem.getCount() + "人觉得很赞");
            } else {
                viewHolder.mineZanTv.setText("");
            }
            if (zanItem.isCanDianzan()) {
                viewHolder.zanIv.setImageResource(R.drawable.interaction_zan_on);
            } else {
                viewHolder.zanIv.setImageResource(R.drawable.interaction_zan_off);
            }
        }
        if (!replyMap.containsKey(interaction.getUuid())) {
            getReplyList(interaction.getUuid());
        } else {
            addReplyView(viewHolder.replyContent, replyMap.get(interaction.getUuid()));
        }

        return view;
    }

    private void addReplyView(LinearLayout replyContent, List<Reply> replies) {
        if (replies != null && replies.size() > 0) {
            replyContent.removeAllViews();
            for (Reply reply : replies) {
                TextView textView = new TextView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText(reply.getCreate_user() + ":" + Html.fromHtml(reply.getContent()));
                replyContent.addView(textView);
            }
        }
    }

    private void getZanList(final String nUUID) {
        UserRequest.getZanList(mContext, nUUID, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                zanMap.put(nUUID, domain);
                notifyDataSetChanged();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                zanMap.remove(nUUID);
            }
        });
    }

    private void getReplyList(final String nUUID) {
        UserRequest.getReplyList(mContext, nUUID, 1, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ReplyList replyList = (ReplyList) domain;
                if (replyList != null && replyList.getList() != null) {
                    replyMap.put(nUUID, replyList.getList().getData());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                replyMap.remove(nUUID);
            }
        });
    }

    private void setZan(String uuid, final ImageView imageView) {
        UserRequest.zan(mContext, uuid, "0", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.interaction_zan_on);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    class ViewHolder {
        CircleImage headCi;
        TextView nameTv;
        TextView textTv;
        ImageView imageIv;
        TextView dateTv;
        ImageView zanIv;
        ImageView replyIv;
        TextView mineZanTv;
        LinearLayout replyContent;
        EditText iReplyEt;
    }
}
