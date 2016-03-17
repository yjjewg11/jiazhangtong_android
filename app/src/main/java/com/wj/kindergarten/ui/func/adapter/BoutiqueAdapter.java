package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BoutiqueAlbum;
import com.wj.kindergarten.bean.BoutiqueAlbumListSun;
import com.wj.kindergarten.bean.BoutiqueDianzanList;
import com.wj.kindergarten.bean.BoutiqueDianzanListObj;
import com.wj.kindergarten.bean.PfDianZan;
import com.wj.kindergarten.bean.PopAttributes;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.PopWindowUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/31.
 */
public class BoutiqueAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<BoutiqueAlbumListSun> list = new ArrayList<>();
    private String rel_uuid;

    public void setRel_uuid(String rel_uuid) {
        this.rel_uuid = rel_uuid;
    }

    public void setList(List<BoutiqueAlbumListSun> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.boutique_album_itme,null);
            viewHolder.boutique_album_item_title = (TextView) convertView.findViewById(R.id.boutique_album_item_title);
            viewHolder.boutique_album_item_count = (TextView) convertView.findViewById(R.id.boutique_album_item_count);
            viewHolder.boutique_album_item_assess_count = (TextView) convertView.findViewById(R.id.boutique_album_item_assess_count);
            viewHolder.boutique_album_item_dianzan_count = (TextView) convertView.findViewById(R.id.boutique_album_item_dianzan_count);
            viewHolder.boutique_album_item_image = (ImageView) convertView.findViewById(R.id.boutique_album_item_image);
            viewHolder.boutique_bottom_delete = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_delete);
            viewHolder.boutique_bottom_edit = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_edit);
            viewHolder.boutique_bottom_pinglun = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_pinglun);
            viewHolder.boutique_bottom_edit2 = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_edit2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BoutiqueAlbumListSun sun = list.get(position);
        if(sun != null){
            String path = sun.getHerald();
            if(path.contains("@")){
                path = path.substring(0,path.indexOf("@"));
            }
            ImageLoaderUtil.displayAlbumImage(path, viewHolder.boutique_album_item_image);
            viewHolder.boutique_album_item_title.setText("" + sun.getTitle());
            String text = "<font color='#6f2d0c'>"+ Utils.isNull(sun.getCreate_username())+"</font>"+
                    "制作于"+"<font color='#6f2d0c'>"+Utils.isNull(sun.getCreate_time())+"</font>,"+
                    "共"+Utils.isNull(sun.getPhoto_count())+"张照片";
            viewHolder.boutique_album_item_count.setText(Html.fromHtml(text));
        }
        PfDianZan dianZan = sun.getDianZan();
        int dianCount = 0;
        if(dianZan != null){
            dianCount = dianZan.getDianzan_count();
        }
        viewHolder.boutique_album_item_dianzan_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopAttributes attributes = new PopAttributes();
                attributes.setGrarity(Gravity.NO_GRAVITY);
                attributes.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                attributes.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                attributes.setLeftOffset(0);

                UserRequest.getBoutiqueDianzanList(context, 1,rel_uuid,new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        View assessView = View.inflate(context, R.layout.boutique_item_dianzan,null);
                        TextView tv = (TextView) assessView.findViewById(R.id.boutique_item_dianzan_tv);
                        BoutiqueDianzanList dianzanList = (BoutiqueDianzanList) domain;
                        PopupWindow popupWindow = new PopupWindow(assessView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        Utils.setPopWindow(popupWindow);


                        if (dianzanList != null && dianzanList.getData() != null &&
                                dianzanList != null && dianzanList.getData() != null &&
                                dianzanList != null && dianzanList.getData().size() > 0) {
                            List<BoutiqueDianzanListObj> listObjs = dianzanList.getData();
                            initList(dianzanList.getData(), tv);
//                            PopWindowUtil.showPoPWindowLocation(context, viewHolder.boutique_album_item_assess_count
//                                    , new BoutiqueDianzanAdapter(context,listObjs), attributes, null);
                        }else {
//                            PopWindowUtil.showPoPWindowLocation(context, viewHolder.boutique_album_item_assess_count
//                                    , new BoutiqueDianzanAdapter(context,null), attributes, null);
                        }

                        int [] location = Utils.getLocation(viewHolder.boutique_album_item_assess_count);
                        assessView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int measureHeight = assessView.getMeasuredHeight();
                        popupWindow.showAtLocation(viewHolder.boutique_album_item_assess_count, Gravity.NO_GRAVITY,
                                0, location[1] - measureHeight);

                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });


            }
        });
        viewHolder.boutique_album_item_dianzan_count.setText(""+dianCount);
        viewHolder.boutique_album_item_assess_count.setText(""+sun.getReply_count());
        return convertView;
    }

    private void initList(List<BoutiqueDianzanListObj> listObjs,TextView tv) {
        if(listObjs != null && listObjs.size() > 0){
            StringBuilder builder = new StringBuilder();
            int size = listObjs.size();
            for(int count = 0 ; count < size ; count ++){
                if(count != size - 1){
                    builder.append(Utils.isNull(listObjs.get(count).getUsername())+",");
                }else {
                    builder.append(Utils.isNull(listObjs.get(count).getUsername()));
                }
            }
            String text = "<font color='ff5a50'>"+builder.toString()+"</font>"+"\n"+"等人觉得很赞!";
            tv.setText(Html.fromHtml(text));
        }else {
            tv.setText("暂无人点赞!");
        }
    }
    class ViewHolder {
        TextView boutique_album_item_title,
                boutique_album_item_count,boutique_album_item_dianzan_count,boutique_album_item_assess_count;
        ImageView boutique_album_item_image;
        FrameLayout boutique_bottom_delete,boutique_bottom_edit,boutique_bottom_pinglun,boutique_bottom_edit2;
    }
    private void requestList() {

    }
}
