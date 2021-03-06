package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.PfPopPicAdapter;
import com.wj.kindergarten.ui.func.adapter.RecycleAdapter;
import com.wj.kindergarten.ui.imagescan.AutoDownLoadListener;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PFSingleObjectInfoFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PfInfoAllPIcFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PfSingleInfoFragment;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import net.tsz.afinal.FinalDb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by tangt on 2016/1/14.
 */
public class PfGalleryActivity extends BaseActivity {

    public static final int EDIT_SINGLE_PF_REQUEST_CODE = 4060;
    private FrameLayout pf_gallery_new_layout_fl;
    private boolean isSpecial;
    private AllPfAlbumSunObject allObj;
    private FinalDb dbObj;
    private PfSingleInfoFragment singleFragment;

    private Queue<PFSingleObjectInfoFragment> infoFragmentQueue = new LinkedBlockingDeque<>(3);

    public void addFragment(PFSingleObjectInfoFragment pfSingleObjectInfoFragment){
        if(infoFragmentQueue.size() == 3) infoFragmentQueue.remove();
        infoFragmentQueue.add(pfSingleObjectInfoFragment);
    }


    public ArrayList<AllPfAlbumSunObject> getObjectList() {
        return objectList;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    private String[] tags = new String[]{

            "album_list",//fragment放置照片集合
            "single_pf"//放置单张照片详情
    };

    public String[] getTags() {
        return tags;
    }

    private ArrayList<AllPfAlbumSunObject> objectList = new ArrayList<>();
    private int position;
    private ArrayList<QueryGroupCount> queryGroupCounts;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.pf_gallery_activity_new_layout;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void titleLeftButtonListener() {
        commonDelete();
        super.titleLeftButtonListener();
    }

    public void commonDelete() {
        sendBroadcast(new Intent(GloablUtils.DELETE_PF_SINGLE_INFO_SUCCESSED));
    }

    @Override
    public void onBackPressed() {

        Iterator<PFSingleObjectInfoFragment> iterator = infoFragmentQueue.iterator();
        while (iterator.hasNext()){
            PFSingleObjectInfoFragment pfSingleObjectInfoFragment = iterator.next();
            if (pfSingleObjectInfoFragment != null){
                if(pfSingleObjectInfoFragment.bottomIsShow()){
                    pfSingleObjectInfoFragment.onlyCancleBottom();
                    return;
                }
            }
        }
        infoFragmentQueue.clear();
        commonDelete();
        super.onBackPressed();
    }

    @Override
    protected void onCreate() {
        commonDialog = new HintInfoDialog(this, "数据加载中，请稍后...");
        setTitleText("选择照片");
        initDb();
        getData();
        initViews();
        initFragment();
        getDataFromDataBases();
    }

    private void sortList(List<AllPfAlbumSunObject> list) {
        Collections.sort(list, new Comparator<AllPfAlbumSunObject>() {
            @Override
            public int compare(AllPfAlbumSunObject o, AllPfAlbumSunObject t) {
                int cha = 0;
                long t1 = TimeUtil.getYMDHMSTime(o.getCreate_time());
                long t2 = TimeUtil.getYMDHMSTime(t.getCreate_time());
                if (t2 - t1 > 0) {
                    cha = 1;
                } else if (t2 - t1 < 0) {
                    cha = -1;
                }

                return cha;
            }
        });
    }
    private void initDb() {
        dbObj = FinalUtil.getFamilyUuidObjectDb(this);
    }

    private void getDataFromDataBases() {
        if (allObj != null) {
            String sql = " strftime('%Y-%m-%d',create_time) DESC;";
            //按倒序排列
            List<AllPfAlbumSunObject> shortList = dbObj.findAll(AllPfAlbumSunObject.class);
            sortList(shortList);
            if(objectList == null) objectList = new ArrayList<>();
            objectList.clear();
            objectList.addAll(shortList);
            position = shortList.indexOf(allObj);
            singleFragment.setList(objectList, position);
        }
    }

    private void initFragment() {
        if(allObj == null){
            singleFragment = new PfSingleInfoFragment(position, objectList);
        }else {
            singleFragment = new PfSingleInfoFragment();
        }
        //先添加详情的fragment。
        getSupportFragmentManager().beginTransaction().replace(R.id.pf_gallery_new_layout_fl, singleFragment, tags[1]).commit();
    }

    private void initViews() {
        pf_gallery_new_layout_fl = (FrameLayout) findViewById(R.id.pf_gallery_new_layout_fl);
    }


    public void getData() {
        position = getIntent().getIntExtra("position", 0);
        objectList = (ArrayList<AllPfAlbumSunObject>) getIntent().getSerializableExtra("list");
        //判断querygroupcounts是否为空，如果是说明显示数据库的全部照片；不是，则返回显示传送过来的指定集合的照片
        queryGroupCounts = (ArrayList<QueryGroupCount>) getIntent().getSerializableExtra("countList");
        if (queryGroupCounts == null) {
            isSpecial = true;
        } else {
            isSpecial = false;
        }
        allObj = (AllPfAlbumSunObject) getIntent().getSerializableExtra("all");
    }

    public void changeToSingleFragment(int position, List<AllPfAlbumSunObject> objectList) {
        PfSingleInfoFragment fragment = (PfSingleInfoFragment) getSupportFragmentManager().findFragmentByTag(tags[1]);
        if (fragment == null) {
            fragment = new PfSingleInfoFragment(position, objectList);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.pf_gallery_new_layout_fl, fragment, tags[1]).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    public void checkAlbumListFragment() {
        Fragment album_fagment = getSupportFragmentManager().findFragmentByTag(tags[0]);
        if (album_fagment == null) {
            album_fagment = new PfInfoAllPIcFragment(queryGroupCounts);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.pf_gallery_new_layout_fl, album_fagment, tags[0])
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    public void changeTitle(String title) {
        setTitleText("" + title);
    }

    public void showDialog(String text) {
        commonDialog.show();
        if (!TextUtils.isEmpty(text) && commonDialog.isShowing()) {
            commonDialog.setText(text);
        }

    }

    public void startEditActivity(AllPfAlbumSunObject object) {
        Intent intent = new Intent(this, SinglePfEditActivity.class);
        intent.putExtra("object", object);
        startActivityForResult(intent, EDIT_SINGLE_PF_REQUEST_CODE, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == EDIT_SINGLE_PF_REQUEST_CODE) {
            AllPfAlbumSunObject object = (AllPfAlbumSunObject) data.getSerializableExtra("object");
            if (object != null)
                notifiFragmentUpdate(object);
        }
    }

    private void notifiFragmentUpdate(AllPfAlbumSunObject object) {
        //返回的编辑相片的数据通知fragment进行刷新
        PfSingleInfoFragment fragment = (PfSingleInfoFragment) getSupportFragmentManager().findFragmentByTag(tags[1]);
        if (fragment != null) {
            fragment.updateSingleData(object);
        }
    }
}
