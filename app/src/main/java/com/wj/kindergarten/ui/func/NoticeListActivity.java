package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.Notice;
import com.wj.kindergarten.bean.NoticeList;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.IntervalUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * NoticeActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/20 23:04
 */
public class NoticeListActivity extends BaseActivity {
    private PullToRefreshListView mListView;

    private List<Notice> notices = new ArrayList<>();
    private int nowPage = 1;
    private NoticeAdapter noticeAdapter;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_notice_list;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("公告");
        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, NoticeActivity.class);
                intent.putExtra("uuid", notices.get(i - 1).getUuid());
                startActivity(intent);
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                nowPage = 1;
                getNoticeList(nowPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getNoticeList(nowPage + 1);
            }
        });
        noticeAdapter = new NoticeAdapter();
        mListView.setAdapter(noticeAdapter);

        mHandler.sendEmptyMessageDelayed(0, 300);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!mListView.isRefreshing()) {
                        mListView.setRefreshing();
                    }
                    break;
            }
        }
    };

    private void getNoticeList(final int page) {
        UserRequest.getNoticeList(mContext, "", page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                NoticeList noticeList = (NoticeList) domain;
                if (noticeList != null && noticeList.getList() != null &&
                        noticeList.getList().getData() != null && noticeList.getList().getData().size() > 0) {
                    if (page == 1) {
                        notices.clear();
                    }
                    notices.addAll(noticeList.getList().getData());
                    noticeAdapter.notifyDataSetChanged();
                    nowPage = page;
                } else {
                    if(page != 1){
                        commonClosePullToRefreshListGridView(mListView);
                    }
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                Utils.showToast(mContext, message);
            }
        });
    }

    class NoticeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return notices.size();
        }

        @Override
        public Object getItem(int i) {
            return notices.get(i);
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
                view = View.inflate(mContext, R.layout.item_notice, null);
                viewHolder.head = (CircleImage) view.findViewById(R.id.item_notice_head);
                viewHolder.title = (TextView) view.findViewById(R.id.item_notice_title);
                viewHolder.content = (TextView) view.findViewById(R.id.item_notice_content);
                viewHolder.place = (TextView) view.findViewById(R.id.item_notice_place);
                viewHolder.date = (TextView) view.findViewById(R.id.item_notice_time);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Notice notice = notices.get(i);

            //  viewHolder.head.setImageResource(R.drawable.group_img);
            getImage(notice.getGroupuuid(), viewHolder.head);
            viewHolder.title.setText(notice.getTitle());
            viewHolder.content.setText(notice.getMessage());
            viewHolder.date.setText(IntervalUtil.getInterval(notice.getCreate_time()));

            return view;
        }
    }

    private void getImage(String groupUuid, CircleImage imageView) {
        try {
            for (Group group : CGApplication.getInstance().getLogin().getGroup_list()) {
                if (groupUuid.equals(group.getUuid())) {
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.drawable.group_img)
                            .showImageForEmptyUri(R.drawable.group_img)
                            .showImageOnFail(R.drawable.group_img)
                            .cacheInMemory(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .cacheOnDisk(true)
                            .displayer(new RoundedBitmapDisplayer(0)).build();
                    ImageLoaderUtil.displayImage(group.getImg(), imageView, options, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder {
        CircleImage head;
        TextView title;
        TextView content;
        TextView place;
        TextView date;
    }
}
