package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.adsmogo.adview.AdsMogoLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.DianZan;
import com.wj.kindergarten.bean.Interaction;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.func.InteractionFragment;

import com.wj.kindergarten.ui.func.InteractionListActivity;

import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.ui.more.HtmlActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.IntervalUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import java.util.ArrayList;
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
    private InteractionFragment interactionFragment;
    private boolean zanLock = false;

    public InteractionAdapter(Context context, InteractionFragment interactionFragment, List<Interaction> dataList) {
        mContext = context;
        this.dataList = dataList;
        this.interactionFragment = interactionFragment;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_interaction, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interactionFragment.hideBottomLayout();
                }
            });
            viewHolder.tv_show_link_title = (TextView)view.findViewById(R.id.tv_show_link_title);
            viewHolder.interation_link_video_ll = (LinearLayout)view.findViewById(R.id.interation_link_video_ll);
            viewHolder.iv_hudong_share = (ImageView) view.findViewById(R.id.iv_hudong_share);
            viewHolder.headCi = (CircleImage) view.findViewById(R.id.item_interaction_head);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.item_interaction_name);
            viewHolder.textTv = (TextView) view.findViewById(R.id.item_interaction_text);
            viewHolder.imageIv = (ImageView) view.findViewById(R.id.item_interaction_img);
            viewHolder.nestedGridView = (NestedGridView) view.findViewById(R.id.item_interaction_grid);
            viewHolder.dateTv = (TextView) view.findViewById(R.id.item_interaction_date);
            viewHolder.zanIv = (ImageView) view.findViewById(R.id.item_interaction_zan);
            viewHolder.replyIv = (ImageView) view.findViewById(R.id.item_interaction_reply);
            viewHolder.mineZanTv = (TextView) view.findViewById(R.id.item_interaction_mine_zan);
            viewHolder.replyContent = (LinearLayout) view.findViewById(R.id.item_interaction_reply_content);
            viewHolder.moreReplyTv = (TextView) view.findViewById(R.id.item_interaction_reply_more);
            viewHolder.iReplyEt = (TextView) view.findViewById(R.id.item_interaction_i_reply);
            viewHolder.sendReply = (TextView) view.findViewById(R.id.item_interaction_i_reply_send);
            viewHolder.ads_ll = (LinearLayout)view.findViewById(R.id.ll_ads);
            viewHolder.iv_interaction_link = (ImageView)view.findViewById(R.id.iv_interaction_link);


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if(i == 0){
            viewHolder.ads_ll.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ads_ll.setVisibility(View.GONE);
        }

        final Interaction interaction = dataList.get(i);

        ImageLoaderUtil.displayImage(interaction.getCreate_img(), viewHolder.headCi);
        viewHolder.tv_show_link_title.setText(EmotUtil.getEmotionContent(mContext, interaction.getContent()));
        if(TextUtils.isEmpty(interaction.getUrl())){
            viewHolder.iv_interaction_link.setVisibility(View.GONE);
            viewHolder.interation_link_video_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            viewHolder.interation_link_video_ll.setBackgroundColor(Color.parseColor("#e0e0e0"));
            viewHolder.iv_interaction_link.setVisibility(View.VISIBLE);
        }



        click(viewHolder, interaction);

        if(TextUtils.isEmpty(interaction.getContent())){
            viewHolder.interation_link_video_ll.setVisibility(View.GONE);
        }else{
            viewHolder.interation_link_video_ll.setVisibility(View.VISIBLE);
        }
        viewHolder.iv_hudong_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击启动分享页
                String content = interaction.getContent();

                ShareUtils.showShareDialog(mContext, v, interaction.getTitle(), content, interaction.getCreate_img(), interaction.getShare_url(), true);

            }
        });
        viewHolder.nameTv.setText(interaction.getCreate_user());
//        viewHolder.textTv.setText();
//        if(TextUtils.isEmpty(EmotUtil.getEmotionContent(mContext, interaction.getContent()))){
//            viewHolder.textTv.setVisibility(View.GONE);
//        }else{
//            viewHolder.textTv.setVisibility(View.VISIBLE);
//        }
        viewHolder.imageIv.setVisibility(View.GONE);
        viewHolder.dateTv.setText(IntervalUtil.getInterval(interaction.getCreate_time()));
        viewHolder.replyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = 0;
                message.obj = interaction;
                mHandler.sendMessageDelayed(message, 300);
            }
        });
        viewHolder.iReplyEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = 0;
                message.obj = interaction;
                mHandler.sendMessageDelayed(message, 300);
            }
        });
        final DianZan dianZan = interaction.getDianzan();
        viewHolder.zanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dianZan.isCanDianzan()) {
                    setZan(interaction, viewHolder.zanIv);
                } else {
                    cancelZan(interaction, viewHolder.zanIv);
                }
            }
        });

        viewHolder.moreReplyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                intent.putExtra("replyId", interaction.getUuid());
                intent.putExtra("type", NormalReplyListActivity.REPLY_TYPE_INTERACTION);
                mContext.startActivity(intent);
            }
        });

        if (interaction.getDianzan() != null) {
            if (interaction.getDianzan().getCount() <= 0) {
                viewHolder.mineZanTv.setText("0人觉得很赞");
            } else {
                String temp = "<font  color='#ff4966'>"
                        + interaction.getDianzan().getNames() + "</font>"
                        + interaction.getDianzan().getCount() + "人觉得很赞";
                viewHolder.mineZanTv.setText(Html.fromHtml(temp));
            }
            if (interaction.getDianzan().isCanDianzan()) {
                viewHolder.zanIv.setImageResource(R.drawable.anzan_hudong_new);
            } else {
                viewHolder.zanIv.setImageResource(R.drawable.hongzan_big);
            }
        } else {
            viewHolder.zanIv.setImageResource(R.drawable.anzan_hudong_new);
            viewHolder.mineZanTv.setText("0人觉得很赞");
        }

        if (interaction.getReplyPage() != null) {
            addReplyView(viewHolder.replyContent, interaction.getReplyPage().getData());
            if (interaction.getReplyPage().getData() != null &&
                    interaction.getReplyPage().getData().size() >= 5) {
                viewHolder.moreReplyTv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.moreReplyTv.setVisibility(View.GONE);
            }
        } else {
            viewHolder.moreReplyTv.setVisibility(View.GONE);
            viewHolder.replyContent.removeAllViews();
        }

        NstGridPicAdapter nGAdapter = null;
        if (interaction.getImgsList() != null && interaction.getImgsList().size() > 0) {
            nGAdapter = new NstGridPicAdapter(interaction.getImgsList(), mContext);
            viewHolder.nestedGridView.setVisibility(View.VISIBLE);
            int width = (int) Math.floor(WindowUtils.getDesnity());
//            viewHolder.nestedGridView.setVerticalSpacing(10/width);
//            viewHolder.nestedGridView.setHorizontalSpacing(5);
        } else {
            nGAdapter = new NstGridPicAdapter(new ArrayList<String>(), mContext);
            viewHolder.nestedGridView.setVisibility(View.GONE);
        }
        viewHolder.nestedGridView.setAdapter(nGAdapter);
        viewHolder.nestedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return view;
    }

    private void click(ViewHolder viewHolder, final Interaction myinteraction) {
        viewHolder.interation_link_video_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iscontain = true;
                String url = myinteraction.getUrl();
                if(TextUtils.isEmpty(url) || url.equals("null")) return;
                iscontain = myinteraction.getUrl().contains("http://") || myinteraction.getUrl().contains("https://");
                if(iscontain) {
                Intent intent = new Intent(mContext, HtmlActivity.class);
                intent.putExtra("url",myinteraction.getUrl());
                intent.putExtra("center_title","链接详情");
                mContext.startActivity(intent);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Interaction interaction = (Interaction) msg.obj;
                    if (interaction != null && !Utils.stringIsNull(interaction.getUuid())) {
                        interactionFragment.showReplyLayout(interaction.getUuid(), interaction);
                    }
                    break;
            }
        }
    };

    private void addReplyView(LinearLayout replyContent, List<Reply> replies) {
        replyContent.removeAllViews();
        if (replies != null && replies.size() > 0) {
            for (Reply reply : replies) {
                if (replyContent.getChildCount() < 5) {
                    View view = View.inflate(mContext, R.layout.item_layout_reply_text, null);
                    TextView nameTv = (TextView) view.findViewById(R.id.item_reply_text_name);
                    String temp = reply.getCreate_user() + ":";
                    SpannableString spanString = new SpannableString(temp);
                    ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff4966"));
                    spanString.setSpan(span, 0, temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    nameTv.setText(spanString);
                    TextView contentTv = (TextView) view.findViewById(R.id.item_reply_text_content);
                    contentTv.setText(EmotUtil.getEmotionContent(mContext, reply.getContent()));
                    replyContent.addView(view);
                } else {
                    break;
                }
            }
        }
    }

    public void addReply(Interaction interaction, Reply reply) {
        interaction.getReplyPage().getData().add(0, reply);
        interaction.getReplyPage().setTotalCount(interaction.getReplyPage().getTotalCount() + 1);
        notifyDataSetChanged();
    }

    private void addReplyView(LinearLayout replyContent, Reply reply) {
        View view = View.inflate(mContext, R.layout.item_layout_reply_text, null);
        TextView nameTv = (TextView) view.findViewById(R.id.item_reply_text_name);
        String temp = reply.getCreate_user() + ":";
        SpannableString spanString = new SpannableString(temp);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff4966"));
        spanString.setSpan(span, 0, temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameTv.setText(spanString);
        TextView contentTv = (TextView) view.findViewById(R.id.item_reply_text_content);
        contentTv.setText(EmotUtil.getEmotionContent(mContext, reply.getContent()));
        replyContent.addView(view);

        if (replyContent.getChildCount() > 5) {
            replyContent.removeViewAt(5);
        }
    }


    private void setZan(final Interaction interaction, final ImageView imageView) {
        UserRequest.zan(mContext, interaction.getUuid(), NormalReplyListActivity.REPLY_TYPE_INTERACTION, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.hongzan_big);
                if (Utils.stringIsNull(interaction.getDianzan().getNames())) {
                    interaction.getDianzan().setNames(CGApplication.getInstance().getLogin().getUserinfo().getName());
                } else {
                    interaction.getDianzan().setNames(interaction.getDianzan().getNames()
                            + "," + CGApplication.getInstance().getLogin().getUserinfo().getName());
                }
                interaction.getDianzan().setCount(interaction.getDianzan().getCount() + 1);
                interaction.getDianzan().setCanDianzan(false);
                notifyDataSetChanged();
                zanLock = false;
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                zanLock = false;
                Utils.showToast(mContext, "点赞失败");
            }
        });
    }

    private void cancelZan(final Interaction interaction, final ImageView imageView) {
        UserRequest.zanCancel(mContext, interaction.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.anzan_hudong_new);
                if (interaction.getDianzan().getNames().contains("," +
                        CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    interaction.getDianzan().setNames(interaction.getDianzan().getNames().replace(","
                            + CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                } else if (interaction.getDianzan().getNames().contains(
                        CGApplication.getInstance().getLogin().getUserinfo().getName() + ",")) {
                    interaction.getDianzan().setNames(interaction.getDianzan().getNames().replace(
                            CGApplication.getInstance().getLogin().getUserinfo().getName() + ",", ""));
                } else if (interaction.getDianzan().getNames().contains(CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    interaction.getDianzan().setNames(interaction.getDianzan().getNames()
                            .replace(CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                }
                interaction.getDianzan().setCount(interaction.getDianzan().getCount() - 1);
                interaction.getDianzan().setCanDianzan(true);
                notifyDataSetChanged();
                zanLock = false;
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                zanLock = false;
                Utils.showToast(mContext, "取消点赞失败");
            }
        });
    }

    class ViewHolder {
        ImageView iv_hudong_share;
        CircleImage headCi;
        TextView nameTv;
        TextView textTv;
        ImageView imageIv;
        TextView dateTv;
        ImageView zanIv;
        ImageView replyIv;
        TextView mineZanTv;
        LinearLayout replyContent;
        TextView moreReplyTv;
        TextView iReplyEt;
        TextView sendReply;
        NestedGridView nestedGridView;
        LinearLayout ads_ll;
        LinearLayout interation_link_video_ll;

        TextView tv_show_link_title;
        ImageView iv_interaction_link;
    }
}
