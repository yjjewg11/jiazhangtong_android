package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.PfPopPicAdapter;
import com.wj.kindergarten.ui.func.adapter.RecycleAdapter;
import com.wj.kindergarten.ui.imagescan.AutoDownLoadListener;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

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
    private static final int EDIT_SINGLE_PF_REQUEST_CODE = 4060;

    private TextView[] textViews;
    private PagerAdapter pagerAdapter;
    private static final int BEGIN_SCAN_PF = 4000;
    private static final int FINISH_SCAN = 4001;
    int page = 1;
    int pageCount = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BEGIN_SCAN_PF:
                    AllPfAlbumSunObject object = (AllPfAlbumSunObject) msg.obj;
                    String sql = " strftime('%Y-%m-%d',create_time) ='" + TimeUtil.getYMDTimeFromYMDHMS(object.getCreate_time()) + "' and family_uuid ='" + object.getFamily_uuid() + "';" ;
//                            "order by id limit 50 offset " + pageCount + ";";
                    List<AllPfAlbumSunObject> listt = family_object_db.findAllByWhere(AllPfAlbumSunObject.class, sql);
                    if (listt != null && listt.size() > 0) {
                        pageCount+=50;
                        list.addAll(listt);
                    }
                    mHandler.sendEmptyMessage(FINISH_SCAN);
                    pagerAdapter.notifyDataSetChanged();
                    break;
                case FINISH_SCAN:

                    break;
            }
        }
    };
    private FinalDb family_object_db;
    private View view;
    private GridView pf_pop_gridView;
    private PfPopPicAdapter pfAdapter;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.pf_gallery_activity;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void titleCenterButtonListener() {
        setPopChoosePic(titleCenterTextView);
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

        position = intent.getIntExtra("position", 0);
        setTitleRightImage(R.drawable.modification_pf, 0);
        family_uuid_object = FinalDb.create(this, GloablUtils.FAMILY_UUID_OBJECT, true);
        initViews();
        changeTitle();
        initBottomBt();
        showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_down);
        if (list != null && list.size() > 0) {
            Message message = new Message();
            message.obj = list.get(0);
            message.what = BEGIN_SCAN_PF;
            mHandler.sendMessage(message);
        }
    }

    public void setPopChoosePic(View click){
        if(view == null){
            view = View.inflate(this, R.layout.pf_pop_grid, null);
            pf_pop_gridView = (GridView) view.findViewById(R.id.pf_pop_gridView);
            pfAdapter = new PfPopPicAdapter(this, list);
            pf_pop_gridView.setAdapter(pfAdapter);
        }

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        Utils.setPopWindow(popupWindow);
        pf_pop_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                viewPager.setCurrentItem(position);
                pfAdapter.setChoosePosition(position);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(click, Gravity.BOTTOM,0,0);
    }

    private void initBottomBt() {
        textViews = new TextView[]{
                (TextView) findViewById(R.id.pf_bottom_collect),
                (TextView) findViewById(R.id.pf_bottom_assess),
                (TextView) findViewById(R.id.pf_bottom_share),
                (TextView) findViewById(R.id.pf_bottom_download),
                (TextView) findViewById(R.id.pf_bottom_edit),
        };

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunObject == null) return;
                switch (v.getId()) {
                    case R.id.pf_bottom_collect:

                        break;
                    case R.id.pf_bottom_assess:

                        break;
                    case R.id.pf_bottom_share:
                        AllPfAlbumSunObject object = list.get(viewPager.getCurrentItem());
                        String note = object.getNote();
                        ShareUtils.showShareDialog(PfGalleryActivity.this,view,note,note,object.getPath(),object.getPath(),false);
                        break;
                    case R.id.pf_bottom_download:

                        if (sunObject.getPath().contains("http")) {
                            ImageLoaderUtil.downLoadImageLoader(sunObject.getPath(), new AutoDownLoadListener(PfGalleryActivity.this));
                        } else {
                            ToastUtils.showMessage("图片已下载!");
                        }
                        break;
                    case R.id.pf_bottom_edit:
                        Intent intent = new Intent(PfGalleryActivity.this,SinglePfEditActivity.class);
                        intent.putExtra("object",list.get(viewPager.getCurrentItem()));
                        startActivityForResult(intent,EDIT_SINGLE_PF_REQUEST_CODE,null);
                        break;
                }
            }
        };

        for (TextView textView : textViews) {
            textView.setOnClickListener(listener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        if(requestCode == EDIT_SINGLE_PF_REQUEST_CODE){
            sunObject = (AllPfAlbumSunObject) data.getSerializableExtra("object");
            pagerAdapter.notifyDataSetChanged();
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
//        pf_gallery_recycleView = (RecyclerView) findViewById(R.id.pf_gallery_recycleView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        pf_gallery_recycleView.setLayoutManager(linearLayoutManager);
        viewPager = (ViewPager) findViewById(R.id.pf_edit_viewPager);
        pagerAdapter = new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = View.inflate(PfGalleryActivity.this, R.layout.pf_gallery_fragment, null);
                final ImageView pf_gallery_image = (ImageView) view.findViewById(R.id.pf_gallery_image);
                final FrameLayout pf_gallery_fl = (FrameLayout) view.findViewById(R.id.pf_gallery_fl);
                final TextView pf_info_careme_time = (TextView) view.findViewById(R.id.pf_info_careme_time);
                final TextView pf_info_location = (TextView) view.findViewById(R.id.pf_info_location);
                final TextView pf_info_device = (TextView) view.findViewById(R.id.pf_info_device);
                final TextView pf_info_upload_people = (TextView) view.findViewById(R.id.pf_info_upload_people);
                AllPfAlbumSunObject object = null;
                if (list != null && list.size() > 0) object = list.get(position);
                if (object != null) {
                    ImageLoaderUtil.downLoadImageLoader(object.getPath(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            pf_gallery_image.setImageBitmap(loadedImage);
                            //将图片按比例放大显示
                            int height =(WindowUtils.dm.widthPixels/loadedImage.getWidth())*loadedImage.getHeight();
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pf_gallery_image.getLayoutParams();
                            params.height = height;
                            pf_gallery_image.setLayoutParams(params);
                            //判断图片大小是否要显示上面的信息
                            if(height > WindowUtils.dm.heightPixels/3*2){
                                pf_gallery_fl.setVisibility(View.GONE);
                                setTitleRightImage(R.drawable.xinxi_photo,0);
                            }else{
                                setTitleRightImage(0,0);
                                AllPfAlbumSunObject object = list.get(position);
                                if(object != null){
                                    pf_info_careme_time.append("" + Utils.isNull(object.getCreate_time()));
                                    pf_info_location.append("" + Utils.isNull(object.getAddress()));
                                    pf_info_device.append("" + Utils.isNull(object.getCreate_useruuid()));
                                    pf_info_upload_people.append("" + Utils.isNull(object.getCreate_useruuid()));
                                }
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                } else {
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

    @Override
    protected void titleRightImageListener() {

        View view = View.inflate(this,R.layout.pf_info_four_message,null);
        TextView pf_info_careme_time = (TextView) view.findViewById(R.id.pf_info_careme_time);
        TextView pf_info_location = (TextView) view.findViewById(R.id.pf_info_location);
        TextView pf_info_device = (TextView) view.findViewById(R.id.pf_info_device);
        TextView pf_info_upload_people = (TextView) view.findViewById(R.id.pf_info_upload_people);
        AllPfAlbumSunObject object = list.get(viewPager.getCurrentItem());
        if(object != null){
            pf_info_careme_time.append("" + Utils.isNull(object.getCreate_time()));
            pf_info_location.append("" + Utils.isNull(object.getAddress()));
            pf_info_device.append("" + Utils.isNull(object.getCreate_useruuid()));
            pf_info_upload_people.append("" + Utils.isNull(object.getCreate_useruuid()));
        }
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Utils.setPopWindow(popupWindow);
        popupWindow.showAsDropDown(titleRightImageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}
