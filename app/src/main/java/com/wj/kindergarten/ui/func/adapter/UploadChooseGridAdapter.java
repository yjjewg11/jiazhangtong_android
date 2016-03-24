package com.wj.kindergarten.ui.func.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.bean.ScanImageAndTime;
import com.wj.kindergarten.ui.imagescan.CustomImageView;
import com.wj.kindergarten.ui.imagescan.GalleryImagesActivity;
import com.wj.kindergarten.ui.imagescan.NativeImageLoader;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfUpGalleryActivity;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tangt on 2016/3/4.
 */
public class UploadChooseGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private final FinalDb db;
    private final List<AlreadySavePath> imageAlreadyList;
    private List<ScanImageAndTime> galleryList ;
    private HashMap<String, Boolean> mSelectMap;
    private Point mPoint = new Point(0, 0);

    private Context context;
    private LayoutInflater inflater;
    private int canSelect = BoutiqueGalleryActivity.IMAGE_MAX;
    private StickyGridHeadersGridView gridView;
    public UploadChooseGridAdapter(Context context, StickyGridHeadersGridView gridView,
                                   List<ScanImageAndTime> galleryList,HashMap<String, Boolean> mSelectMap) {
        this.context = context;
        this.gridView = gridView;
        this.galleryList = galleryList;
        this.mSelectMap = mSelectMap;
        inflater = LayoutInflater.from(context);
        db = FinalUtil.getAlreadyUploadDb(context);
        String sql = "family_uuid = '"+ PhotoFamilyFragment.instance.getCurrentFamily_uuid()+"' and status = '0';";
        imageAlreadyList =   db.findAllByWhere(AlreadySavePath.class, sql);
//        List<AlreadySavePath> allList = db.findAll(AlreadySavePath.class);
    }

    //选择全部照片
    public void chooseAll(){
        Iterator<ScanImageAndTime> iterator = galleryList.iterator();
        while (iterator.hasNext()){
            ScanImageAndTime imageAndTime = iterator.next();
            //如果导入则不选中
                imageAndTime.setChoose("取消全选");
                if(!judgePicInput(imageAndTime.getPath())){
                    mSelectMap.put(imageAndTime.getPath(),true);
                }
        }
        notifyDataSetChanged();
    }
    //取消全部选择
    public void giveUpAll(){
        Iterator<ScanImageAndTime> iterator = galleryList.iterator();
        while (iterator.hasNext()){
            ScanImageAndTime imageAndTime = iterator.next();
            imageAndTime.setChoose("全选");
        }
        mSelectMap.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getHeaderId(int position) {
        long  time =  TimeUtil.getMillionFromYMD(galleryList.get(position).getTime());
        return time;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        final HeadHolder headHolder;
        if(convertView == null){
            headHolder = new HeadHolder();
            convertView = inflater.inflate(R.layout.pf_family_fusion_head,null);
            headHolder.pf_family_first_item_time = (TextView) convertView.findViewById(R.id.pf_family_first_item_time);
            headHolder.pf_family_first_item_count = (TextView) convertView.findViewById(R.id.pf_family_first_item_count);
            headHolder.pf_family_first_item_count.setTextColor(context.getResources().getColor(R.color.title_bg));
            convertView.setTag(headHolder);
        }else {
            headHolder = (HeadHolder) convertView.getTag();
        }
        final ScanImageAndTime scanImageAndTime = galleryList.get(position);
        if(scanImageAndTime != null){
            headHolder.pf_family_first_item_time.setText(""+ scanImageAndTime.getTime());
            headHolder.pf_family_first_item_count.setText(""+scanImageAndTime.getChoose());
            headHolder.pf_family_first_item_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = headHolder.pf_family_first_item_count.getText().toString();
                    //先判断是否还有可选择的

                    if(text.equals("全选")){
                        if(!judgeChoose(scanImageAndTime.getTime())){
                            return;
                        }
                        operation(scanImageAndTime.getTime(), "add");
                        headHolder.pf_family_first_item_count.setText("取消全选");
                    }else {
                        operation(scanImageAndTime.getTime(),"remove");
                        headHolder.pf_family_first_item_count.setText("全选");
                    }
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    //判断是否有可上传的
    private boolean judgeChoose(String time) {
        Iterator<ScanImageAndTime> iterator = galleryList.iterator();
        while (iterator.hasNext()){
            ScanImageAndTime scanImageAndTime = iterator.next();
            if(scanImageAndTime.getTime().equals(time) && !judgePicInput(scanImageAndTime.getPath())){
                return true;
            }
        }
        return false;
    }

    private void operation(String time, String type) {
        Iterator<ScanImageAndTime> iterator = galleryList.iterator();
        if(type.equals("add")){
            while (iterator.hasNext()){
                ScanImageAndTime imageAndTime = iterator.next();
                //如果导入则不选中
                if(imageAndTime.getTime().equals(time)){
                    imageAndTime.setChoose("取消全选");
                    if(!judgePicInput(imageAndTime.getPath())){
                        mSelectMap.put(imageAndTime.getPath(),true);
                    }
                }
            }
        }else {
            while (iterator.hasNext()){
                ScanImageAndTime scanImageAndTime = iterator.next();
                if(scanImageAndTime.getTime().equals(time)){
                    scanImageAndTime.setChoose("全选");
                    if(mSelectMap.containsKey(scanImageAndTime.getPath())){
                        mSelectMap.remove(scanImageAndTime.getPath());
                    }
                }

            }
        }


    }


    class HeadHolder{
        TextView pf_family_first_item_time;
        TextView pf_family_first_item_count;
    }


    @Override
    public int getCount() {
        return galleryList.size();
    }

    @Override
    public Object getItem(int position) {
        return galleryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
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
        ScanImageAndTime o = galleryList.get(position);
        final String path = o.getPath();

        if (!Utils.stringIsNull(path) && !path.contains("CGImage")) {
            viewHolder.mImageView.setTag(path);
            viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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
                    ImageView mImageView = (ImageView) gridView.findViewWithTag(path);
                    if (bitmap != null && mImageView != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });

            if (bitmap != null) {
                viewHolder.mImageView.setImageBitmap(bitmap);
            }
            judgeIsShow(position, viewHolder);
        }

        return convertView;
    }
    public class ViewHolder {
        public CustomImageView mImageView;
        public CheckBox mCheckBox;
        TextView tv_show_already;
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

    private boolean judgePicInput(String path){
        AlreadySavePath alreadySavePath = new AlreadySavePath(path);
        return imageAlreadyList.contains(alreadySavePath);
    }

    private void judgeIsShow(int position, ViewHolder viewHolder) {
        if(judgePicInput(galleryList.get(position).getPath())){
            viewHolder.mCheckBox.setVisibility(View.GONE);
            viewHolder.tv_show_already.setVisibility(View.VISIBLE);
            viewHolder.mImageView.setAlpha(0.2f);
        }else{
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
            viewHolder.tv_show_already.setVisibility(View.GONE);
            viewHolder.mImageView.setAlpha(1.0f);
        }
    }

}
