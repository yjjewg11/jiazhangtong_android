package com.wj.kindergarten.ui.mine.photofamilypic;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.BoutiquePopAdapter;
import com.wj.kindergarten.ui.imagescan.GalleryImagesAdapter;
import com.wj.kindergarten.ui.imagescan.PhotoDirModel;
import com.wj.kindergarten.ui.imagescan.PhotoPopAdapter;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.ui.imagescan.ScanPhoto_V1;
import com.wj.kindergarten.ui.imagescan.ScanPhoto_V2;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * GalleryImagesActivity
 *
 * @author weiwu.song
 * @data: 2015/1/4 16:09
 * @version: v1.0
 */
public class BoutiqueGalleryActivity extends BaseActivity implements View.OnClickListener {
    private final int CHANGE_DIR = 3;
    //开始扫描
    private final int SCAN_PHOTO_BEGIN = 0;
    //扫描完成
    private final int SCAN_PHOTO_COMPLETE = 1;
    //改变目录

    private final int SCAN_GROUP_BY_DATE = 2;
    //private final String CAMERA_ICON = "android.resource://com.wenjie.jiazhangtong/drawable/photo";
    //最多能选择图片数量
    public final static int IMAGE_MAX = 100;
    //取值key
    public static final String RESULT_LIST = "ResultList";
    //拍照后返回REQUEST
    private final int REQUEST_CAMERA = 1;
    //目录
    private HashMap<String, List<String>> dirMap = new HashMap<>();
    //存储图片的选中情况
    private HashMap<String, Boolean> mSelectMap = new HashMap<>();
    //拍照按钮和扫描到的图片列表
    private List<AllPfAlbumSunObject> galleryList = new ArrayList<>();
    //扫描到的相册列表
    //扫描到的通过分组来计算数量的照片
    private List<QueryGroupCount> queryGroupCounts = new ArrayList<>();
    private ArrayList<String> scanList = new ArrayList<>();
    private BoutiqueGalleryImagesAdapter adapter;
    private GridView mGridView;
    //选择文件夹
    private TextView changeDirButton = null;
    //拍照后图片URI
    private Uri imageUri = null;
    //切换目录列表
    private PullToRefreshListView changeDirListView = null;
    private BoutiquePopAdapter boutiquePopAdapter = null;
    private FrameLayout changeDirListContent = null;
    //切换目录背景
    private FrameLayout changeDirBg = null;
    //当前还能选择的图片数
    private int canSelect = IMAGE_MAX;
    //底部动画Y轴偏移量
    private int mediaY = 570;
    //当前显示的目录
    private String nowDir = "";
    private ArrayList<String> chooseList = null;
    private boolean isCut = false;

    private static int SIGN_BOARD;
    //右下角确定按钮
    private TextView boutique_gallery_confirm;
    private FinalDb db;
    private View view;

    /**
     * 被选中的图片列表 key
     */


    @Override
    public void setContentLayout() {
        layoutId = R.layout.boutique_gallery_activity;
    }

    @Override
    public void setNeedLoading() {
        isNeedLoading = false;
    }

    @Override
    public void onCreate() {
        ActivityManger.getInstance().addPfActivities(this);
        db = FinalDb.create(this, GloablUtils.FAMILY_UUID_OBJECT);
        initSelectPic((ArrayList) getIntent().getSerializableExtra(Constants.ALREADY_SELECT_KEY));
        SIGN_BOARD = (getIntent().getIntExtra("signBoard", 0));

        initWidget();
        initClickListener();

        mHandler.sendEmptyMessage(SCAN_PHOTO_BEGIN);
        mHandler.sendEmptyMessage(SCAN_GROUP_BY_DATE);
    }

    private void initClickListener() {
        boutique_gallery_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selected = getSelectItems();
                chooseImageFinish(selected, false);
            }
        });
    }

    private void initSelectPic(ArrayList<String> pics) {
        if (null != pics) {
            chooseList = pics;
        } else {
            chooseList = new ArrayList<>();
        }

        if (pics == null || pics.size() <= 0) {
            setTitleText("选择图片", "");
            return;
        }
        boutique_gallery_confirm.setText("确定" +
                "(" + pics.size() + ")");
        for (String path : pics) {
            mSelectMap.put(path, true);
        }
    }

    /**
     * init widget
     */
    private void initWidget() {
        boutique_gallery_confirm = (TextView) findViewById(R.id.boutique_gallery_confirms);
        changeDirButton = (TextView) findViewById(R.id.boutique_photo_gallery_dir);
        changeDirButton.setOnClickListener(this);

//        changeDirListView = (ListView) findViewById(R.id.boutique_change_dir_list);
        changeDirListContent = (FrameLayout) findViewById(R.id.boutique_change_dir_list_content);
        changeDirBg = (FrameLayout) findViewById(R.id.boutique_change_dir_bg);
        changeDirBg.setOnClickListener(this);

        mGridView = (GridView) findViewById(R.id.boutique_child_grid);
//        galleryList.add(0, "");
//        scanList.add(0, "");
        adapter = new BoutiqueGalleryImagesAdapter(galleryList, canSelect, mSelectMap, mGridView);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BoutiqueGalleryActivity.this, PhotoWallActivity.class);
                ArrayList<String> simplePic = new ArrayList<>();
                simplePic.add(galleryList.get(position).getPath());
                intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, simplePic);
                startActivity(intent);
//                boutique_gallery_confirm.setText("" + boutiquePopAdapter.getItem(position));
            }
        });
//        initChangeDirList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {//拍照完成
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    if (imageUri != null) {
                        uri = imageUri;
                    } else {
                        return;
                    }
                }
                String path = ScanPhoto_V1.getPath(this, uri);
                chooseList.add(path);
                chooseImageFinish(chooseList, true);
            }else if(requestCode == PF_CHOOSE_PIC){
                ArrayList<AllPfAlbumSunObject> list = (ArrayList) data.getSerializableExtra("selectList");
                HashMap<String,Boolean> map = new HashMap<>();
                Iterator<AllPfAlbumSunObject> iterator = list.iterator();
                while (iterator.hasNext()){
                    AllPfAlbumSunObject object = iterator.next();
                    map.put(object.getPath(),true);
                }
                adapter.setmSelectMap(map);
                adapter.notifyDataSetChanged();
            }
        } else if (resultCode == RESULT_CANCELED) {//取消拍照 删除之前存入相册的占位图片
            try {
                getContentResolver().delete(imageUri, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                if (imageUri != null) {
                    String path = ScanPhoto_V1.getPath(this, imageUri);
                    if (!Utils.stringIsNull(path)) {
                        File file = new File(path);
                        if (file != null && file.exists()) {
                            file.delete();
                        }
                    }
                }
            }
        }


    }

    /**
     * 显示改变目录view
     */
    private void showChangeDirView() {
        doBottomSlidAnimations(changeDirListContent, -mediaY);
        changeDirBg.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏改变目录view
     */
    private void hideChangeDirView() {
        doBottomSlidAnimations(changeDirListContent, mediaY);
        changeDirBg.setVisibility(View.GONE);
    }

    /**
     * 执行Y轴平移动画，并保持动画结束时状态
     *
     * @param moveLayout 要执行动画的view
     * @param deltaY     Y轴偏移量
     */
    private void doBottomSlidAnimations(View moveLayout, float deltaY) {
        if (Build.VERSION.SDK_INT < 11) {
            com.nineoldandroids.animation.ObjectAnimator animY
                    = com.nineoldandroids.animation.ObjectAnimator.ofFloat(moveLayout, "translationY", deltaY);
            animY.setDuration(300);
            animY.start();
        } else {
            ObjectAnimator animY = ObjectAnimator.ofFloat(moveLayout, "translationY", deltaY);
            animY.setDuration(300);
            animY.start();
        }
    }

    /**
     * 初始化change ListView
     */
//    private void initChangeDirList() {
//        QueryGroupCount count = new QueryGroupCount();
//        count.setDate("所有照片");
//        queryGroupCounts.add(0, count);
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boutique_photo_gallery_dir:
                //切换目录列表
                showSelect();
                break;
            case R.id.boutique_change_dir_bg:
                hideChangeDirView();
                break;
        }
    }

    private void showSelect() {
        //TODO
        if(view == null){
            QueryGroupCount queryGroupCount = new QueryGroupCount();
            queryGroupCount.setDate("所有照片");
            queryGroupCounts.add(0, queryGroupCount);
        view = View.inflate(this,R.layout.boutique_gallery_pop_layout,null);
        changeDirListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
        changeDirListView.setMode(PullToRefreshBase.Mode.DISABLED);
        boutiquePopAdapter = new BoutiquePopAdapter(this, queryGroupCounts);
        changeDirListView.setAdapter(boutiquePopAdapter);
        }
        final PopupWindow popupPic = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Utils.setPopWindow(popupPic);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupPic.dismiss();
            }
        });
        changeDirListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryGroupCount queryGroupCount = queryGroupCounts.get(position-1);
                Message message = new Message();
                message.what = CHANGE_DIR;
                message.obj = queryGroupCount;
                message.arg1 = position-1;
                mHandler.sendMessage(message);
//                boutiquePopAdapter.notifyDataSetChanged();
//                hideChangeDirView();
                popupPic.dismiss();
            }
        });
        int [] location = new int[2];
        boutique_gallery_confirm.getLocationInWindow(location);
        popupPic.showAtLocation(boutique_gallery_confirm, Gravity.NO_GRAVITY,0,location[1]);
    }

    private List<AllPfAlbumSunObject> objectList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_PHOTO_BEGIN://开始扫描
//                    scanPhoto_v2();
                    objectList = db.findAll(AllPfAlbumSunObject.class);
                    if (objectList != null && objectList.size() > 0) {
                        galleryList.addAll(objectList);
                        mHandler.sendEmptyMessage(SCAN_PHOTO_COMPLETE);
                    }
                    break;
                case SCAN_PHOTO_COMPLETE://扫描结束
                    adapter.notifyDataSetChanged();
                    nowDir = getString(R.string.all_photo);
                    break;
                case SCAN_GROUP_BY_DATE:
                    String sql = "SELECT strftime('%Y-%m-%d',create_time),count(1) from " + "com_wj_kindergarten_bean_AllPfAlbumSunObject" +
                            " GROUP BY strftime('%Y-%m-%d',create_time)";
                    List<QueryGroupCount> dateArray = new ArrayList<>();
                    List<DbModel> dbList = db.findDbModelListBySQL(sql);
                    for (DbModel model : dbList) {
                        QueryGroupCount count = new QueryGroupCount();
                        String date = (String) model.getDataMap().get("strftime('%Y-%m-%d',create_time)");
                        int sumCount = Integer.valueOf((String) model.getDataMap().get("count(1)"));
                        if (!TextUtils.isEmpty(date)) {
                            count.setDate(date);
                        }
                        if (sumCount != 0) {
                            count.setCount(sumCount);
                        }
                        dateArray.add(count);
                    }
                    Collections.sort(dateArray, new Comparator<QueryGroupCount>() {
                        @Override
                        public int compare(QueryGroupCount one, QueryGroupCount two) {

                            long o = TimeUtil.getMillionFromYMD(one.getDate());
                            long t = TimeUtil.getMillionFromYMD(two.getDate());
                            return o - t > 0 ? 1 : -1;
                        }
                    });
                    if (dateArray.size() > 0) {
                        queryGroupCounts.addAll(dateArray);
                    }
                    break;
                case CHANGE_DIR:
                    if (msg.arg1 == 0) {
                        galleryList.clear();
                        galleryList.addAll(objectList);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    QueryGroupCount queryGroupCount = (QueryGroupCount) msg.obj;
                    List<AllPfAlbumSunObject> objectLists = queryByDate(queryGroupCount.getDate());
                    galleryList.clear();
                    galleryList.addAll(objectLists);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public List<AllPfAlbumSunObject> queryByDate(String date) {
        String sql = "strftime('%Y-%m-%d',create_time) = '" + date+"';";
        List<AllPfAlbumSunObject> objectList = db.findAllByWhere(AllPfAlbumSunObject.class, sql);
        if (objectList != null && objectList.size() > 0) {
            return objectList;
        }
        return null;
    }

    private ArrayList<String> getImages(List<String> list) {
        ArrayList<String> imgs = new ArrayList<>();
        for (String path : list) {
            if (!Utils.stringIsNull(path) && !path.contains("CGImage")) {
                imgs.add(path);
            }
        }
        return imgs;
    }

    /**
     * 获取当前显示目录名称
     *
     * @return 当前显示目录名称
     */
    public String getShowWitchDir() {
        return nowDir;
    }


    public void selectChange(HashMap<String, Boolean> mSelectMap) {
        boutique_gallery_confirm.setText("" + "确定" +
                "(" + mSelectMap.size()+ ")");
    }

    /**
     * 获取选中的Item
     *
     * @return
     */
    public ArrayList<String> getSelectItems() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Iterator<Map.Entry<String, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Boolean> entry = it.next();
            if (entry.getValue()) {
                arrayList.add(entry.getKey());
            }
        }
        return arrayList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            needBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void titleLeftButtonListener() {
        needBack();
    }

    /**
     * 处理返回按钮 判断change dir view是否显示
     */
    private void needBack() {
        if (changeDirBg != null && changeDirBg.getVisibility() == View.VISIBLE) {
            hideChangeDirView();
        } else {
            finish();
        }
    }

    /**
     * 返回图片到上一页
     *
     * @param images   需要返回上一页的图片数组
     * @param isCamera 是否是拍照
     */
    private int PF_CHOOSE_PIC = 5001;
    private void chooseImageFinish(List<String> images, boolean isCamera) {
        if (null != images && images.size() > 0) {
            if (isCut) {

            } else {
                Intent intent = new Intent(this,PfChoosedPicActivity.class);
                intent.putExtra("objectList", (ArrayList) adapter.getSelectList());
                startActivityForResult(intent,PF_CHOOSE_PIC,null);


            }
        } else {
            if (isCamera) {

            } else {
                Toast.makeText(BoutiqueGalleryActivity.this, getString(R.string.please_choose_image), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 扫描图片
     */
    private void scanPhoto_v2() {
        ScanPhoto_V2.scanMediaDir(this, scanList, dirMap);

        //通知Handler扫描图片完成
        mHandler.sendEmptyMessage(SCAN_PHOTO_COMPLETE);
    }

    /**
     * 获取view高度
     *
     * @param view 要获取高度的view
     * @return view的高度
     */
    private void getViewHeight(final View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                mediaY = view.getHeight() + 2;
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    /**
     * 扫描图片 不再使用
     */
    @Deprecated
    private void scanPhoto_v1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanList = new ArrayList<>();
                String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
                ScanPhoto_V1.getImageFileFromDir(dir, scanList);

                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_PHOTO_COMPLETE);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File appDir = new File(Environment.getExternalStorageDirectory() + "/CGImage");
        if (appDir.exists()) {
            FileUtil.deleteFolder(appDir);
        }
    }
}