package com.wj.kindergarten.ui.func.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.imagescan.CustomImageView;
import com.wj.kindergarten.ui.imagescan.GalleryImagesActivity;
import com.wj.kindergarten.ui.imagescan.NativeImageLoader;
import com.wj.kindergarten.ui.mine.photofamilypic.PfUpGalleryActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by tangt on 2016/1/19.
 */
public class PfUpGalleryAdapter extends BaseAdapter {
    private Context context;
    private Point point = new Point(0,0);
    private LayoutInflater inflater;

    private List<String> alreadySubmit;

    private GridView mGridView;
    //数据源
    private List<String> list;
    //第一个Item是否为特殊按钮(拍照)
    private boolean isFirstSpecial = true;
    private static final String address = "file://";
    private HashMap<String,Boolean> mSelectMap = new HashMap<>();

    public void setFirstSpecial(boolean isFirstSpecial) {
        this.isFirstSpecial = isFirstSpecial;
    }

    public PfUpGalleryAdapter(GridView mGridView,Context context,List<String> list,List<String> alreadySubmit) {
        this.context = context;
        this.mGridView = mGridView;
        this.list = list;
        this.alreadySubmit = alreadySubmit;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.pf_up_choose_pic_item,null);
            viewHolder.imageView = (CustomImageView) convertView.findViewById(R.id.pf_up_pic_item_image);
            viewHolder.tv_submit = (TextView) convertView.findViewById(R.id.already_tv);
            viewHolder.pf_child_checkbox = (CheckBox) convertView.findViewById(R.id.pf_child_checkbox);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String path = list.get(position);
        viewHolder.imageView.setTag(path);
        if(!Utils.stringIsNull(path) && !mSelectMap.containsKey(path)){
            viewHolder.pf_child_checkbox.setChecked(false);
        }else{
            viewHolder.pf_child_checkbox.setChecked(true);
        }
        viewHolder.pf_child_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked  && !mSelectMap.containsKey(path)) {

                    return;
                }

                //如果是未选中的CheckBox,则添加动画
                if (!mSelectMap.containsKey(path) || !mSelectMap.get(path)) {
                    addAnimation(viewHolder.pf_child_checkbox);
                }
                if (isChecked) {
                    mSelectMap.put(path, isChecked);
                } else {
                    mSelectMap.remove(path);
                }

                //如果是未选中的CheckBox,则添加动画
                if (!isChecked) {
                    addAnimation(viewHolder.pf_child_checkbox);
                }
//                ((PfUpGalleryActivity) context).changeTitle(mSelectMap.size());
            }
        });

        boolean isAlready = false;
        //比较是否已导入
        viewHolder.imageView.setTag(list.get(position));
        viewHolder.tv_submit.setVisibility(View.GONE);
        if(alreadySubmit != null && alreadySubmit.size() != 0){
            Iterator<String> iterator = alreadySubmit.iterator();
            UUID uuid =  UUID.nameUUIDFromBytes(list.get(position).getBytes());
            while (iterator.hasNext()){
                String isSubmitUuid = iterator.next();
                if(uuid.toString().equals(isSubmitUuid)){
                    viewHolder.imageView.setAlpha(0.3f);
                    viewHolder.tv_submit.setVisibility(View.VISIBLE);
                    isAlready = true;
                    break;
                }
            }
        }

        if(isAlready){
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }

        viewHolder.imageView.setOnMeasureListener(new CustomImageView.OnMeasureListener() {

            @Override
            public void onMeasureSize(int width, int height) {
                point.set(width, height);
            }
        });
        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(list.get(position),point, new NativeImageLoader.NativeImageCallBack() {

            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
                if (bitmap != null && mImageView != null) {
                    mImageView.setImageBitmap(bitmap);
                }
            }
        });

        if (bitmap != null) {
            viewHolder.imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }

    class ViewHolder{
        CustomImageView imageView;
        TextView tv_submit;
        CheckBox pf_child_checkbox;
    }

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
}
