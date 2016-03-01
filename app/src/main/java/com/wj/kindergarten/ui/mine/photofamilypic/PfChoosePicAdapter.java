package com.wj.kindergarten.ui.mine.photofamilypic;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
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
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.imagescan.CustomImageView;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GalleryImagesAdapter
 *
 * @author weiwu.song
 * @data: 2015/1/4 16:09
 * @version: v1.0
 */

public class PfChoosePicAdapter extends BaseAdapter {
    //封装ImageView的宽和高的对象
    private Point mPoint = new Point(0, 0);
    //存储图片的选中情况
    private HashMap<String, Boolean> mSelectMap = new HashMap<>();
    private GridView mGridView;
    //数据源
    private List<AllPfAlbumSunObject> list;
    //第一个Item是否为特殊按钮(拍照)
    private boolean isFirstSpecial = true;
    //还可以选择的图片数
    private int canSelect = BoutiqueGalleryActivity.IMAGE_MAX;
    private List<AllPfAlbumSunObject> selectList = new ArrayList<>();
    private List<AllPfAlbumSunObject> singleObject = new ArrayList<>();

    public List<AllPfAlbumSunObject> getSelectList() {
        return selectList;
    }

    public void clearSingleList(int position){
        if(list == null) return;
        singleObject.clear();
        singleObject.add(list.get(position));
        notifyDataSetChanged();
    }
    public AllPfAlbumSunObject getCurrentObj(int position){
        return list.get(position);
    }

    public PfChoosePicAdapter(List<AllPfAlbumSunObject> list, int canSelect, HashMap<String, Boolean> selectMap, GridView mGridView) {
        this.list = list;
        this.canSelect = canSelect;
        this.mGridView = mGridView;
        this.mSelectMap = selectMap;
        this.list.add(new AllPfAlbumSunObject());
        if(list.size() > 0){
            singleObject.add(list.get(0));
        }
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
        final AllPfAlbumSunObject object = list.get(position);

        final String path = object.getPath();
        CGLog.d("PATH:" + object.getPath());

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

        viewHolder.tv_show_already.setText("封面");
        viewHolder.tv_show_already.setVisibility(View.GONE);
        if (position == list.size() - 1) {
            viewHolder.mCheckBox.setSelected(false);
            viewHolder.mCheckBox.setVisibility(View.GONE);
            //viewHolder.mImageView.setImageURI(Uri.parse(path));
            viewHolder.mImageView.setImageResource(R.drawable.tianjia_jpxc);
            viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
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
                            if (!selectList.contains(object)) {
                                selectList.add(object);
                            }
                        } else {
                            mSelectMap.remove(path);
                            selectList.remove(object);
                        }
                        ((PfChoosedPicActivity) parent.getContext()).selectChange(mSelectMap);
                    }
                });

                viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(path) ? mSelectMap.get(path) : false);

                viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.mCheckBox.setVisibility(View.VISIBLE);
                ImageLoaderUtil.displayMyImage(path, viewHolder.mImageView);
                if(singleObject.get(0).getPath().equals(path)){
                    viewHolder.tv_show_already.setVisibility(View.VISIBLE);
                }
            }

        }

        return convertView;
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
