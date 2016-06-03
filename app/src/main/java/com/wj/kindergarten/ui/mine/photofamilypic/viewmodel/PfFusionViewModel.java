package com.wj.kindergarten.ui.mine.photofamilypic.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.mine.photofamilypic.TransportListener;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;

/**
 * Created by tangt on 2016/3/9.
 */
public class PfFusionViewModel {
    private Context context;

    public PfFusionViewModel(Context context) {
        this.context = context;
    }

    public void loadView(List<AllPfAlbumSunObject> objectList, LinearLayout container_linear) {
        int size = objectList.size();
        if (size == 1) {
            loadOne(objectList, container_linear);
        } else if (size == 2) {
            loadTwo(objectList,container_linear);
        } else if (size == 3) {
            loadThree(objectList,container_linear);
        } else if (size == 4) {
            loadFour(objectList,container_linear);
        } else if (size == 5) {
            loadFive(objectList,container_linear);
        } else if (objectList.size() >= 6) {
            loadSix(objectList,container_linear);
        }
    }

    private void loadOne(List<AllPfAlbumSunObject> objectList, LinearLayout container_linear) {
        View view = View.inflate(context, R.layout.pf_classic_by_date_one, null);
        ImageView iv_0 = (ImageView) view.findViewById(R.id.pf_classic_by_date_one_iv_one);
        showBitmap(objectList.get(0).getPath(), iv_0);
        iv_0.setOnClickListener(new TransportListener(context, 0, objectList, null));
        container_linear.addView(view);
    }

    private void loadTwo(List<AllPfAlbumSunObject> objectList, LinearLayout container_linear) {
        View view = View.inflate(context, R.layout.pf_classic_by_date_two, null);
        ImageView iv_0 = (ImageView) view.findViewById(R.id.pf_classic_by_date_two_iv_one);
        ImageView iv_1 = (ImageView) view.findViewById(R.id.pf_classic_by_date_two_iv_two);
        showBitmap(objectList.get(0).getPath(), iv_0);
        showBitmap(objectList.get(1).getPath(), iv_1);
        iv_0.setOnClickListener(new TransportListener(context, 0, objectList, null));
        iv_1.setOnClickListener(new TransportListener(context, 1, objectList, null));
        container_linear.addView(view);
    }

    private void loadThree(List<AllPfAlbumSunObject> objectList, LinearLayout container_linear) {
        View view = View.inflate(context, R.layout.pf_classic_by_date_three, null);
        ImageView iv_0 = (ImageView) view.findViewById(R.id.pf_classic_by_date_three_iv_one);
        ImageView iv_1 = (ImageView) view.findViewById(R.id.pf_classic_by_date_three_iv_two);
        ImageView iv_2 = (ImageView) view.findViewById(R.id.pf_classic_by_date_three_iv_three);
        showBitmap(objectList.get(0).getPath(), iv_0);
        showBitmap(objectList.get(1).getPath(), iv_1);
        showBitmap(objectList.get(2).getPath(), iv_2);
        iv_0.setOnClickListener(new TransportListener(context, 0, objectList, null));
        iv_1.setOnClickListener(new TransportListener(context, 1, objectList, null));
        iv_2.setOnClickListener(new TransportListener(context, 2, objectList, null));
        container_linear.addView(view);
    }

    private void loadFour(List<AllPfAlbumSunObject> objectList, LinearLayout container_linear) {
        View view = View.inflate(context, R.layout.pf_classic_by_date_four, null);
        ImageView iv_0 = (ImageView) view.findViewById(R.id.pf_classic_by_date_four_iv_one);
        ImageView iv_1 = (ImageView) view.findViewById(R.id.pf_classic_by_date_four_iv_two);
        ImageView iv_2 = (ImageView) view.findViewById(R.id.pf_classic_by_date_four_iv_three);
        ImageView iv_3 = (ImageView) view.findViewById(R.id.pf_classic_by_date_four_iv_four);
        showBitmap(objectList.get(0).getPath(), iv_0);
        showBitmap(objectList.get(1).getPath(), iv_1);
        showBitmap(objectList.get(2).getPath(), iv_2);
        showBitmap(objectList.get(3).getPath(), iv_3);
        iv_0.setOnClickListener(new TransportListener(context, 0, objectList, null));
        iv_1.setOnClickListener(new TransportListener(context, 1, objectList, null));
        iv_2.setOnClickListener(new TransportListener(context, 2, objectList, null));
        iv_3.setOnClickListener(new TransportListener(context, 3, objectList, null));
        container_linear.addView(view);
    }

    private void loadFive(List<AllPfAlbumSunObject> objectList, LinearLayout container_linear) {
        View view = View.inflate(context, R.layout.pf_classic_by_date_five, null);
        ImageView iv_0 = (ImageView) view.findViewById(R.id.pf_classic_by_date_five_iv_one);
        ImageView iv_1 = (ImageView) view.findViewById(R.id.pf_classic_by_date_five_iv_two);
        ImageView iv_2 = (ImageView) view.findViewById(R.id.pf_classic_by_date_five_iv_three);
        ImageView iv_3 = (ImageView) view.findViewById(R.id.pf_classic_by_date_five_iv_four);
        ImageView iv_4 = (ImageView) view.findViewById(R.id.pf_classic_by_date_five_iv_five);
        showBitmap(objectList.get(0).getPath(), iv_0);
        showBitmap(objectList.get(1).getPath(), iv_1);
        showBitmap(objectList.get(2).getPath(), iv_2);
        showBitmap(objectList.get(3).getPath(), iv_3);
        showBitmap(objectList.get(4).getPath(), iv_4);
        iv_0.setOnClickListener(new TransportListener(context, 0, objectList, null));
        iv_1.setOnClickListener(new TransportListener(context, 1, objectList, null));
        iv_2.setOnClickListener(new TransportListener(context, 2, objectList, null));
        iv_3.setOnClickListener(new TransportListener(context, 3, objectList, null));
        iv_4.setOnClickListener(new TransportListener(context, 4, objectList, null));
        container_linear.addView(view);
    }

    private void loadSix(List<AllPfAlbumSunObject> objectList, LinearLayout container_linear) {
        View view = View.inflate(context, R.layout.pf_classic_by_date_six, null);
        ImageView iv_0 = (ImageView) view.findViewById(R.id.pf_iv_0);
        ImageView iv_1 = (ImageView) view.findViewById(R.id.pf_iv_1);
        ImageView iv_2 = (ImageView) view.findViewById(R.id.pf_iv_2);
        ImageView iv_3 = (ImageView) view.findViewById(R.id.pf_iv_3);
        ImageView iv_4 = (ImageView) view.findViewById(R.id.pf_iv_4);
        ImageView iv_5 = (ImageView) view.findViewById(R.id.pf_iv_5);
        showBitmap(objectList.get(0).getPath(), iv_0);
        showBitmap(objectList.get(1).getPath(), iv_1);
        showBitmap(objectList.get(2).getPath(), iv_2);
        showBitmap(objectList.get(3).getPath(), iv_3);
        showBitmap(objectList.get(4).getPath(), iv_4);
        showBitmap(objectList.get(5).getPath(), iv_5);
        iv_0.setOnClickListener(new TransportListener(context, 0, objectList, null));
        iv_1.setOnClickListener(new TransportListener(context, 1, objectList, null));
        iv_2.setOnClickListener(new TransportListener(context, 2, objectList, null));
        iv_3.setOnClickListener(new TransportListener(context, 3, objectList, null));
        iv_4.setOnClickListener(new TransportListener(context, 4, objectList, null));
        iv_5.setOnClickListener(new TransportListener(context, 5, objectList, null));
        container_linear.addView(view);
    }

    public void showBitmap(String path,ImageView imageView){
        ImageLoaderUtil.displayAlbumImage(path, imageView);
    }

}
