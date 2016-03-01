package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.PfAlbumListAdapter;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.more.FrameLayoutWrapper;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tangt on 2016/1/18.展示我关注的家庭成员，以及对应的删除操作
 */
public class PfAlbumListActivity extends BaseActivity {
    private GridView gridView;
    private PfAlbumListAdapter adapter;
    private LinearLayout pf_album_list_bottom_fl;
    private ObjectAnimator anim;
    @ViewInject(id = R.id.pf_album_list_edit)
    TextView pf_album_list_edit;
    @ViewInject(id = R.id.pf_album_list_delete)
    TextView pf_album_list_delete;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.pf_album_list_activity;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {

        FinalActivity.initInjectedView(this);
        initViews();
        initAnimation();
        setTitleText("我的家庭相册");
        setTitleRightImage(R.drawable.new_album_carema, 0);
        initBottomClick();
    }

    private void initBottomClick() {
        pf_album_list_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showEditDialog();
            }
        });
        pf_album_list_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initAnimation() {
        pf_album_list_bottom_fl.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        anim = ObjectAnimator.ofInt(new FrameLayoutWrapper(pf_album_list_bottom_fl),"bottomMargin",-pf_album_list_bottom_fl.getMeasuredHeight());
        anim.setDuration(300);
        anim.start();
    }

    private void initViews() {
        pf_album_list_bottom_fl = (LinearLayout) findViewById(R.id.pf_album_list_bottom_fl);
        gridView = (GridView)findViewById(R.id.pf_list_grid_view);
        adapter = new PfAlbumListAdapter(this);

        gridView.setAdapter(adapter);
        adapter.setList(null);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == adapter.getSize() - 1){
                    addFamilyMember("");
                }else {
                    if(pf_album_list_bottom_fl.getVisibility() == View.GONE){
                    anim.reverse();
                    }
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                anim.reverse();
//                deleteAlbum(listSuns.get(position));
            }
        });

    }

//    private void showEditDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(PfAlbumListActivity.this);
//        AlertDialog dialog = builder.setView()
//                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        //修改孩子信息
//                    }
//                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).create();
//        dialog.show();
//    }

    public void editAlbumInfo(PfAlbumListSun sun){
//        UserRequest.editPfAlbumInfo(this,);
    }

//    public void deleteAlbum(final PfAlbumListSun sun){
//        UserRequest.deleteAlbum(this,sun.getUuid(), new RequestResultI() {
//            @Override
//            public void result(BaseModel domain) {
//                ToastUtils.showMessage("删除成功");
//
//            }
//
//            @Override
//            public void result(List<BaseModel> domains, int total) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//
//            }
//        });
//    }

    public void addFamilyMember(String uuid){

    }
}
