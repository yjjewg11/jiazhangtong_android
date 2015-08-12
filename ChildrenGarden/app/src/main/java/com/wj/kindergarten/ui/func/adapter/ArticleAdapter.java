package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Article;

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
        viewHolder.nameTv.setText(article.getCreate_user());
        viewHolder.titleTv.setText(article.getTitle());
        viewHolder.contentTv.setText(article.getContent());
        viewHolder.timeTv.setText(article.getCreate_time());
        viewHolder.zanCountTv.setText(article.getCount() + "");

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
