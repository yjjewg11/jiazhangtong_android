package com.wj.kindergarten.ui.imagescan;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.services.PicUploadService;
import com.wj.kindergarten.ui.func.EditPfActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.PfUpGalleryActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * GalleryImagesAdapter
 *
 * @author weiwu.song
 * @data: 2015/1/4 16:09
 * @version: v1.0
 */
public class PfGalleryImagesAdapter extends BaseAdapter {
    private final List<AlreadySavePath> imageAlreadyList;
    private FinalDb db;
    //封装ImageView的宽和高的对象
    private Point mPoint = new Point(0, 0);
    //存储图片的选中情况
    private HashMap<String, Boolean> mSelectMap = new HashMap<>();
    private GridView mGridView;
    //数据源
    private List<String> list;
    //第一个Item是否为特殊按钮(拍照)
    private boolean isFirstSpecial = true;
    //还可以选择的图片数
    private int canSelect = 100;
    private int type;


    public PfGalleryImagesAdapter(List<String> list, int canSelect, HashMap<String, Boolean> selectMap, GridView mGridView,int type) {
        this.list = list;
        this.canSelect = canSelect;
        this.mGridView = mGridView;
        this.mSelectMap = selectMap;
        db = FinalDb.create(mGridView.getContext());
        imageAlreadyList =   db.findAll(AlreadySavePath.class);
        this.type = type;
        CGLog.v("打印数据集合 ; "+imageAlreadyList);
    }

    public void setFirstSpecial(boolean isFirstSpecial) {
        this.isFirstSpecial = isFirstSpecial;
    }

    public boolean isFirstSpecial() {
        return isFirstSpecial;
    }

    @Override
    public int getCount() {
        if (null != list) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != list && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        final String path = list.get(position);

        CGLog.d("PATH:" + path);

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        FuniLog.i("ImageScan", "path:" + path);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.imagescan_images_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (CustomImageView) convertView.findViewById(R.id.child_image);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
            viewHolder.tv_show_already = (TextView) convertView.findViewById(R.id.tv_show_already);

            //监听ImageView的宽和高
            viewHolder.mImageView.setOnMeasureListener(new CustomImageView.OnMeasureListener() {

                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }

//        if (position == 0 && isFirstSpecial) {
//            viewHolder.mCheckBox.setSelected(false);
//            viewHolder.mCheckBox.setVisibility(View.GONE);
//            //viewHolder.mImageView.setImageURI(Uri.parse(path));
//            viewHolder.mImageView.setImageResource(R.drawable.photo);
//            viewHolder.mImageView.setScaleType(ImageView.ScaleType.CENTER);
//        } else {


            if (!Utils.stringIsNull(path) && !path.contains("CGImage")) {
                viewHolder.mImageView.setTag(path);
                viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && mSelectMap.size() >= canSelect && !mSelectMap.containsKey(path)) {
                            viewHolder.mCheckBox.setChecked(false);
                            Utils.showToast(viewHolder.mCheckBox.getContext(), viewHolder.mCheckBox.getContext()
                                    .getString(R.string.can_select_photo_max, canSelect + ""));
                            return;
                        }

                        //如果是未选中的CheckBox,则添加动画
                        if (!mSelectMap.containsKey(path) || !mSelectMap.get(path)) {
                            addAnimation(viewHolder.mCheckBox);
                        }
                        if (isChecked) {
                            mSelectMap.put(path, isChecked);
                        } else {
                            mSelectMap.remove(path);
                        }
                        ((PfUpGalleryActivity) parent.getContext()).selectChange(mSelectMap);
                    }
                });

                viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(path) ? mSelectMap.get(path) : false);

                viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.mCheckBox.setVisibility(View.VISIBLE);
                //利用NativeImageLoader类加载本地图片
                Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {

                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
                        if (bitmap != null && mImageView != null) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                });

                if (bitmap != null) {
                    viewHolder.mImageView.setImageBitmap(bitmap);
                }
            }
        //如果是新建相册，则不需要显示已导入，
        if(type == EditPfActivity.CHOOSE_NEW){

            //如果是在已有相册的基础上添加照片，则需要标明是否已导入。
        }else if (type == PhotoFamilyFragment.ADD_PIC){
            judgeIsShow(position, viewHolder);
        }
        return convertView;
    }

    private void judgeIsShow(int position, ViewHolder viewHolder) {

        AlreadySavePath alreadySavePath = new AlreadySavePath(list.get(position));
        if(imageAlreadyList.contains(alreadySavePath)){
            viewHolder.mCheckBox.setVisibility(View.GONE);
            viewHolder.tv_show_already.setVisibility(View.VISIBLE);
            viewHolder.mImageView.setAlpha(0.2f);
        }else{
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
            viewHolder.tv_show_already.setVisibility(View.GONE);
            viewHolder.mImageView.setAlpha(1.0f);
        }
    }

    /**
     * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
     *
     * @param view
     */
    private void addAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};

        if (Build.VERSION.SDK_INT < 11) {
            com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
            set.playTogether(com.nineoldandroids.animation.ObjectAnimator.ofFloat(view, "scaleX", vaules),
                    com.nineoldandroids.animation.ObjectAnimator.ofFloat(view, "scaleY", vaules));
            set.setDuration(150);
            set.start();
        } else {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                    ObjectAnimator.ofFloat(view, "scaleY", vaules));
            set.setDuration(150);
            set.start();
        }
    }

    public class ViewHolder {
        public CustomImageView mImageView;
        public CheckBox mCheckBox;
        TextView tv_show_already;
    }
}
