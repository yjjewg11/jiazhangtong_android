package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Article;
import com.wj.kindergarten.utils.IntervalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ArticleAdapter
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/4 23:22
 */
public class ArticleAdapter extends BaseAdapter {
    private Context mContext;
    private List<Article> articles = new ArrayList<>();

    public ArticleAdapter(Context context) {
        mContext = context;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int i) {
        return articles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_article_list, null);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.item_article_name);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.item_article_title);
            viewHolder.contentTv = (TextView) view.findViewById(R.id.item_article_content);
            viewHolder.timeTv = (TextView) view.findViewById(R.id.item_article_time);
            viewHolder.zanCountTv = (TextView) view.findViewById(R.id.item_article_zan);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Article article = articles.get(i);
        if (null != article) {
            viewHolder.nameTv.setText(article.getCreate_user());
            viewHolder.titleTv.setText(article.getTitle());
            viewHolder.contentTv.setText(article.getContent());
            viewHolder.timeTv.setText(IntervalUtil.getInterval(article.getCreate_time()));
            if (null != article.getDianzan()) {
                viewHolder.zanCountTv.setText(article.getDianzan().getCount() + "");
                if (article.getDianzan().getCount() > 0) {
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.interaction_mine_zan_on);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    viewHolder.zanCountTv.setCompoundDrawables(drawable, null, null, null);
                } else {
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.interaction_mine_zan);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    viewHolder.zanCountTv.setCompoundDrawables(drawable, null, null, null);
                }
            }
        }

        return view;
    }

    class ViewHolder {
        TextView titleTv;
        TextView nameTv;
        TextView contentTv;
        TextView zanCountTv;
        TextView timeTv;
    }
}
