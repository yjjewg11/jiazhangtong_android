package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wj.kindergarten.ui.func.adapter.PfPopPicAdapter;
import com.wj.kindergarten.ui.imagescan.AutoDownLoadListener;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.SinglePfEditActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

public class PfSingleInfoFragment extends Fragment {
    View fragmentView;
    private List<AllPfAlbumSunObject> list = new ArrayList<>();
    private ViewPager viewPager;
    private AllPfAlbumSunObject sunObject;
    private int position;
    private FinalDb family_uuid_object;
    private static final int EDIT_SINGLE_PF_REQUEST_CODE = 4060;

    private TextView[] textViews;
    private PagerAdapter pagerAdapter;

    public PfSingleInfoFragment(int position, List<AllPfAlbumSunObject> list) {
        this.position = position;
        this.list.clear();
        this.list.addAll(list);
    }

    private View view;
    private GridView pf_pop_gridView;
    private PfPopPicAdapter pfAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };


    public void changeData(int position,List<AllPfAlbumSunObject> list){
        this.position = position;
        this.list.clear();
        this.list.addAll(list);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(fragmentView != null) return fragmentView;
        ((PfGalleryActivity)getActivity()).hideActionbar();
        fragmentView = inflater.inflate(R.layout.pf_gallery_activity,null);
        initViews();
        initBottomBt();
        changeTitle();
        return fragmentView;
    }
    private void initViews() {
        viewPager = (ViewPager) fragmentView.findViewById(R.id.pf_edit_viewPager);
        pagerAdapter = new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = View.inflate(getActivity(), R.layout.pf_gallery_fragment, null);
                final ImageView pf_gallery_image = (ImageView) view.findViewById(R.id.pf_gallery_image);
                final FrameLayout pf_gallery_fl = (FrameLayout) view.findViewById(R.id.pf_gallery_fl);
                final TextView pf_info_careme_time = (TextView) view.findViewById(R.id.pf_info_careme_time);
                final TextView pf_info_location = (TextView) view.findViewById(R.id.pf_info_location);
                final TextView pf_info_device = (TextView) view.findViewById(R.id.pf_info_device);
                final TextView pf_info_upload_people = (TextView) view.findViewById(R.id.pf_info_upload_people);
                AllPfAlbumSunObject object = null;
                if (list != null && list.size() > 0) object = list.get(position);
                if (object != null) {
                    String path = object.getPath();
                    if(!TextUtils.isEmpty(path)){
                        if(path.contains("@")){
                            path = path.substring(0,path.indexOf("@"));
                        }
                    }
                    ImageLoaderUtil.downLoadImageLoader(path, new ImageLoadingListener() {
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
                            int height = (WindowUtils.dm.widthPixels / loadedImage.getWidth()) * loadedImage.getHeight();
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pf_gallery_image.getLayoutParams();
                            params.height = height;
                            pf_gallery_image.setLayoutParams(params);
                            //判断图片大小是否要显示上面的信息
                            if (height > WindowUtils.dm.heightPixels / 3 * 2) {
                                pf_gallery_fl.setVisibility(View.GONE);
                                //TODO 显示上部分的标题
//                                setTitleRightImage(R.drawable.xinxi_photo, 0);
                            } else {
//                                setTitleRightImage(0, 0);
                                AllPfAlbumSunObject object = list.get(position);
                                if (object != null) {
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

    public void showRightInfo(){
        View view = View.inflate(getActivity(),R.layout.pf_info_four_message,null);
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
        popupWindow.showAsDropDown(fragmentView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void queryItemNewInfo(String uuid) {
        UserRequest.getSinglePfInfo(getActivity(), uuid, new RequestResultI() {
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

    private void initBottomBt() {
        textViews = new TextView[]{
                (TextView) fragmentView.findViewById(R.id.pf_bottom_collect),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_assess),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_share),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_download),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_edit),
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
                        ShareUtils.showShareDialog(getActivity(), view, note, note, object.getPath(), object.getPath(), false);
                        break;
                    case R.id.pf_bottom_download:

                        if (sunObject.getPath().contains("http")) {
                            ImageLoaderUtil.downLoadImageLoader(sunObject.getPath(), new AutoDownLoadListener(getActivity()));
                        } else {
                            ToastUtils.showMessage("图片已下载!");
                        }
                        break;
                    case R.id.pf_bottom_edit:
                        Intent intent = new Intent(getActivity(),SinglePfEditActivity.class);
                        intent.putExtra("object", list.get(viewPager.getCurrentItem()));
                        getActivity().startActivityForResult(intent, EDIT_SINGLE_PF_REQUEST_CODE,null);
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
//                setTitleText(TimeUtil.getYMDTimeFromYMDHMS(sunObject.getCreate_time()), "");
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

}
