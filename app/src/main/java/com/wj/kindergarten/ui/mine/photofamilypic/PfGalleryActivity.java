package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.imagescan.AutoDownLoadListener;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PfGalleryActivity extends BaseActivity {
    private List<AllPfAlbumSunObject> list;
    private ViewPager viewPager;
    private AllPfAlbumSunObject sunObject;
    private int position;
    private FinalDb family_uuid_object;

    private TextView[] textViews;
    private PagerAdapter pagerAdapter;
    private static final int BEGIN_SCAN_PF = 4000;
    private static final int FINISH_SCAN = 4001;
    int page = 1;
    int pageCount = 0;
    private List<AllPfAlbumSunObject> listObj = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BEGIN_SCAN_PF:
                    AllPfAlbumSunObject object = (AllPfAlbumSunObject) msg.obj;
                    String sql = " strftime('%Y-%m-%d',create_time) ='" + object.getCreate_time() + "' and family_uuid ='" + object.getFamily_uuid() + "'" + "order by id limit 50 offset " + pageCount + ";";
                    List<AllPfAlbumSunObject> listt = family_object_db.findAllByWhere(AllPfAlbumSunObject.class, sql);
                    if (listt != null && listt.size() > 0) {
                        pageCount+=50;
                        list.addAll(listt);
                    }
                    pagerAdapter.notifyDataSetChanged();
                    break;
                case FINISH_SCAN:

                    break;
            }
        }
    };
    private FinalDb family_object_db;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.pf_gallery_activity;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void titleRightButtonListener() {
        Intent intent = new Intent(this, PfEditInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate() {
        family_object_db = FinalDb.create(this, GloablUtils.FAMILY_UUID_OBJECT, true);
        Intent intent = getIntent();
        list = (ArrayList) intent.getSerializableExtra("list");
        if (list != null && list.size() > 0) {
            Message message = new Message();
            message.obj = list.get(0);
            message.what = BEGIN_SCAN_PF;
            mHandler.sendMessage(message);
        }
        position = intent.getIntExtra("position", 0);
        setTitleRightImage(R.drawable.modification_pf, 0);
        family_uuid_object = FinalDb.create(this, GloablUtils.FAMILY_UUID_OBJECT, true);
        initViews();
        changeTitle();
        initBottomBt();
    }

    private void initBottomBt() {
        textViews = new TextView[]{
                (TextView) findViewById(R.id.pf_bottom_collect),
                (TextView) findViewById(R.id.pf_bottom_assess),
                (TextView) findViewById(R.id.pf_bottom_share),
                (TextView) findViewById(R.id.pf_bottom_download),
                (TextView) findViewById(R.id.pf_bottom_delete),
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunObject == null) return;
                switch (v.getId()) {
                    case R.id.pf_bottom_collect:

                        break;
                    case R.id.pf_bottom_assess:

                        break;
                    case R.id.pf_bottom_share:

                        break;
                    case R.id.pf_bottom_download:

                        if (sunObject.getPath().contains("http")) {
                            ImageLoaderUtil.downLoadImageLoader(sunObject.getPath(), new AutoDownLoadListener(PfGalleryActivity.this));
                        } else {
                            ToastUtils.showMessage("图片已下载!");
                        }
                        break;
                    case R.id.pf_bottom_delete:
                        ToastUtils.showDialog(PfGalleryActivity.this, "提示 !", "你确定要删除吗?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(sunObject);
                                pagerAdapter.notifyDataSetChanged();
                                family_uuid_object.delete(sunObject);
                                dialog.cancel();
                            }
                        });
                        break;
                }
            }
        };

        for (TextView textView : textViews) {
            textView.setOnClickListener(listener);
        }
    }

    private void changeTitle() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (list == null || list.size() == 0) return;
                sunObject = list.get(position);
                setTitleText(TimeUtil.getYMDTimeFromYMDHMS(sunObject.getCreate_time()), "");
                queryItemNewInfo(sunObject.getUuid());
                //做调试，屏蔽
//                if(sunObject.getStatus() != 0){
//                    queryItemNewInfo(sunObject.getUuid());
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void queryItemNewInfo(String uuid) {
        UserRequest.getSinglePfInfo(this, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SingleNewInfo singleNewInfo = (SingleNewInfo) domain;
                if (singleNewInfo != null) {
                    sunObject = singleNewInfo.getData();
                    sunObject.setStatus(0);
                    family_uuid_object.update(sunObject);
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.pf_edit_viewPager);
        pagerAdapter = new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(PfGalleryActivity.this, R.layout.pf_gallery_fragment, null);
                ImageView pf_gallery_image = (ImageView) view.findViewById(R.id.pf_gallery_image);
                TextView pf_gallery_annotation = (TextView) view.findViewById(R.id.pf_gallery_annotation);
                TextView pf_gallery_location = (TextView) view.findViewById(R.id.pf_gallery_location);
                TextView pf_gallery_people = (TextView) view.findViewById(R.id.pf_gallery_people);
                AllPfAlbumSunObject object = null;
                if (list != null && list.size() > 0) object = list.get(position);
                if (object != null) {
                    ImageLoaderUtil.displayMyImage(object.getPath(), pf_gallery_image);
                    pf_gallery_annotation.setText("" + Utils.isNull(object.getNote()));
                    pf_gallery_location.setText("" + Utils.isNull(object.getAddress()));
                    pf_gallery_people.setText("" + Utils.isNull(object.getPhoto_time()));
                } else {
                    pf_gallery_image.setImageResource(R.drawable.new_album);
                }
                container.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return view;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
    }
}
