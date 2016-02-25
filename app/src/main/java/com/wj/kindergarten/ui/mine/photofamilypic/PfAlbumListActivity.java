package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

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
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tangt on 2016/1/18.
 */
public class PfAlbumListActivity extends BaseActivity {
    private GridView gridView;
    private PfAlbumListAdapter adapter;
    private List<PfAlbumListSun> listSuns;
    private FrameLayout pf_album_list_bottom_fl;
    private ObjectAnimator anim;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.pf_album_list_activity;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        listSuns = MainActivity.instance.getAlbumList();
        listSuns.add(new PfAlbumListSun());
        initViews();
        initAnimation();
        setTitleText("我的家庭相册");
        setTitleRightImage(R.drawable.new_album_carema,0);
    }

    private void initAnimation() {
        pf_album_list_bottom_fl.measure(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        anim = ObjectAnimator.ofInt(new FrameLayoutWrapper(pf_album_list_bottom_fl),"bottomMargin",-pf_album_list_bottom_fl.getMeasuredHeight());
        anim.setDuration(300);
        anim.start();
    }

    private void initViews() {
        pf_album_list_bottom_fl = (FrameLayout) findViewById(R.id.pf_album_list_bottom_fl);
        gridView = (GridView)findViewById(R.id.pf_list_grid_view);
        adapter = new PfAlbumListAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                anim.reverse();
                AlertDialog.Builder builder = new AlertDialog.Builder(PfAlbumListActivity.this);
                AlertDialog dialog = builder.setTitle("修改相册信息")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //修改孩子信息
                            }
                        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
                deleteAlbum(listSuns.get(position));
            }
        });

    }

    public void editAlbumInfo(PfAlbumListSun sun){
//        UserRequest.editPfAlbumInfo(this,);
    }

    public void deleteAlbum(final PfAlbumListSun sun){
        UserRequest.deleteAlbum(this,sun.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("删除成功");
                Iterator<PfAlbumListSun> iterator = listSuns.iterator();
                while (iterator.hasNext()){
                    PfAlbumListSun pfAlbumListSun = iterator.next();
                    if(pfAlbumListSun.getUuid().equals(sun.getUuid())){
                        listSuns.remove(sun);
                        //更新数据
                        adapter.setList(listSuns);
                        return;
                    }
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
}
