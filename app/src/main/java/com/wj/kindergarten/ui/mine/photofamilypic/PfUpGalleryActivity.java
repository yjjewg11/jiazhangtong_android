package com.wj.kindergarten.ui.mine.photofamilypic;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ScanImageAndTime;
import com.wj.kindergarten.bean.SyncUploadPic;
import com.wj.kindergarten.bean.SyncUploadPicList;
import com.wj.kindergarten.bean.SyncUploadPicObj;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.services.PicUploadService;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.EditPfActivity;
import com.wj.kindergarten.ui.func.adapter.UploadChooseGridAdapter;
import com.wj.kindergarten.ui.imagescan.PfGalleryImagesAdapter;
import com.wj.kindergarten.ui.imagescan.PfPhotoPopAdapter;
import com.wj.kindergarten.ui.imagescan.PhotoDirModel;
import com.wj.kindergarten.ui.imagescan.PhotoDirTimeModel;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.ui.imagescan.ScanPhoto_V1;
import com.wj.kindergarten.ui.imagescan.ScanPhoto_V2;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.dbupdate.UploadPathDbTwo;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ThreadManager;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

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
 * Created by tangt on 2016/1/19.
 */
public class PfUpGalleryActivity extends BaseActivity implements View.OnClickListener {
    private final int TOAST_DELAY = 1002;
    //开始扫描
    private final int SCAN_PHOTO_BEGIN = 0;
    //扫描完成
    private final int SCAN_PHOTO_COMPLETE = 1;
    //改变目录
    private final int CHANGE_PHOTO_DIR = 2;
    //改变目录 第一个特殊
    private final int CHANGE_PHOTO_DIR_S = 3;
    //拍照ICON
    //private final String CAMERA_ICON = "android.resource://com.wenjie.jiazhangtong/drawable/photo";
    //最多能选择图片数量
    public final static int IMAGE_MAX = 100;
    //取值key
    public static final String RESULT_LIST = "ResultList";
    //拍照后返回REQUEST
    private final int REQUEST_CAMERA = 1;
    //目录
    private HashMap<String, List<ScanImageAndTime>> dirMap = new HashMap<>();
    //存储图片的选中情况
    private HashMap<String, Boolean> mSelectMap = new HashMap<>();
    //拍照按钮和扫描到的图片列表
    private List<ScanImageAndTime> galleryList = new ArrayList<>();
    //扫描到的相册列表
    private ArrayList<ScanImageAndTime> scanList = new ArrayList<>();
    private UploadChooseGridAdapter adapter;
    //存放所有图片地址的集合
    private List<String> pathList = new ArrayList<>();
    private StickyGridHeadersGridView pf_up_gallery_gridView;
    //选择文件夹
    private TextView changeDirButton = null;
    //拍照后图片URI
    private Uri imageUri = null;
    //切换目录列表
    private ListView changeDirListView = null;
    private PfPhotoPopAdapter pfPhotoPopAdapter = null;
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
    private int type;
    private FinalDb uploadDb;
    private TextView pf_gallery_choose_all;

    /**
     * 被选中的图片列表 key
     */


    @Override
    public void setContentLayout() {
        layoutId = R.layout.pf_up_gallery_activity;
    }

    //如果未从网络同步已上传照片，则需要同步,默认未同步
    @Override
    public void setNeedLoading() {
        uploadDb = FinalUtil.getAlreadyUploadDb(this);
        isNeedLoading = !CGSharedPreference.getUploadSyncStatus();
    }

    int pageNo = 1;
    int totalCount = 0;
    int pageSize = 0;

    @Override
    protected void loadData() {
        showProgressDialog("正在同步照片，请稍候...("+(pageSize*(pageNo - 1))+"/"+totalCount+")");
        UserRequest.initSyncUploadPic(this, pageNo,new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                SyncUploadPic syncUploadPic = (SyncUploadPic) domain;
                SyncUploadPicList syncList = syncUploadPic.getList();
                if(syncUploadPic != null && syncList != null
                        &&syncList.getData() != null && syncList.getData().size() > 0){
                    Iterator<SyncUploadPicObj> iterator = syncUploadPic.getList().getData().iterator();
                    while (iterator.hasNext()){
                        SyncUploadPicObj obj = iterator.next();
                        saveUploadObj(obj);
                    }
                }
                if(totalCount == 0){
                    totalCount = syncList.getTotalCount();
                    pageSize = syncList.getPageSize();
                }
                if(syncList.getPageSize() != syncList.getData().size()){
                    hideProgressDialog();
                    CGSharedPreference.setUploadSyncStatus(true);
                    loadSuc();
                    return;
                }
                pageNo++;
                loadData();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                loadSuc();
            }
        });
    }

    private void saveUploadObj(SyncUploadPicObj obj) {
        String sql = "localPath = '"+obj.getMd5()+"';";
        List<AlreadySavePath> list =  uploadDb.findAllByWhere(AlreadySavePath.class,sql);
        if(list == null || list.size() == 0){
            AlreadySavePath savePath = new AlreadySavePath();
            savePath.setStatus(0);
            savePath.setLocalPath(obj.getMd5());
            savePath.setFamily_uuid(obj.getFamily_uuid());
            uploadDb.save(savePath);
        }
    }

    @Override
    public void onBackPressed() {
        if(isProgressDialogIsShowing()){
            loadSuc();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onCreate() {
        initSelectPic((ArrayList) getIntent().getSerializableExtra(Constants.ALREADY_SELECT_KEY));
        type = getIntent().getIntExtra("type", 0);
        SIGN_BOARD = (getIntent().getIntExtra("signBoard", 0));

        initWidget();

        mHandler.sendEmptyMessage(SCAN_PHOTO_BEGIN);
        mHandler.sendEmptyMessageDelayed(TOAST_DELAY,300);
    }

    private void initSelectPic(ArrayList<String> pics) {
        if (null != pics) {
            chooseList = pics;
        } else {
            chooseList = new ArrayList<>();
        }

        if (pics == null || pics.size() <= 0) {
            setTitleText("选择图片", "确定");
            return;
        }
        setTitleText("选择图片共(" + pics.size() + "张)", "确定");
        for (String path : pics) {
            mSelectMap.put(path, true);
        }
    }

    /**
     * init widget
     */
    private void initWidget() {
        changeDirButton = (TextView) findViewById(R.id.pf_gallery_dir);
        changeDirButton.setOnClickListener(this);

        pf_gallery_choose_all = (TextView) findViewById(R.id.pf_gallery_choose_all);
        pf_gallery_choose_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = pf_gallery_choose_all.getText().toString();
                if(text.equals("全部选择")){
                    pf_gallery_choose_all.setText(""+"取消全选");
                    adapter.chooseAll();
                }else {
                    pf_gallery_choose_all.setText(""+"全部选择");
                    adapter.giveUpAll();
                }
            }
        });

        changeDirListView = (ListView) findViewById(R.id.pf_change_dir_list);
        changeDirListContent = (FrameLayout) findViewById(R.id.pf_change_dir_list_content);
        changeDirBg = (FrameLayout) findViewById(R.id.pf_change_dir_bg);
        changeDirBg.setOnClickListener(this);

        pf_up_gallery_gridView = (StickyGridHeadersGridView) findViewById(R.id.pf_up_gallery_gridView);
        adapter = new UploadChooseGridAdapter(this,pf_up_gallery_gridView,galleryList,mSelectMap);
        pf_up_gallery_gridView.setAdapter(adapter);
        pf_up_gallery_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //查出所有图片。
                if (pathList.size() <= 0) {
                    Iterator<ScanImageAndTime> iterator = galleryList.iterator();
                    while (iterator.hasNext()) {
                        ScanImageAndTime scanImageAndTime = iterator.next();
                        pathList.add(scanImageAndTime.getPath());
                    }
                }
                position = pathList.indexOf(galleryList.get(position).getPath());
                Intent intent = new Intent(PfUpGalleryActivity.this, PhotoWallActivity.class);
                ArrayList<String> simplePic = new ArrayList<>();
                simplePic.addAll(pathList);
                intent.putExtra(PhotoWallActivity.KEY_POSITION, position);
                intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, simplePic);
                startActivity(intent);
            }
        });
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
    private void initChangeDirList() {
        final List<PhotoDirTimeModel> dirLN = new ArrayList<>();
        PhotoDirTimeModel allPDM = new PhotoDirTimeModel();
        allPDM.setDirName(getString(R.string.all_photo));
        allPDM.setPaths(scanList);
        dirLN.add(allPDM);
        for (Map.Entry<String, List<ScanImageAndTime>> entry : dirMap.entrySet()) {
            List<ScanImageAndTime> tempL = entry.getValue();
            if (entry != null && tempL != null && tempL.size() > 0) {
                PhotoDirTimeModel tempPDM = new PhotoDirTimeModel();
                tempPDM.setDirName(entry.getKey());
                tempPDM.setPaths((ArrayList) tempL);
                if (!entry.getKey().contains("CGImage") || !entry.getKey().equals("CGImage")) {
                    dirLN.add(tempPDM);
                }
            }
        }
        pfPhotoPopAdapter = new PfPhotoPopAdapter(this, dirLN);
        changeDirListView.setAdapter(pfPhotoPopAdapter);
        changeDirListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message message = new Message();
                message.what = CHANGE_PHOTO_DIR;
                if (position == 0) {
                    message.obj = scanList;
                    message.what = CHANGE_PHOTO_DIR_S;
                    mHandler.sendMessage(message);
                    nowDir = getString(R.string.all_photo);
                } else {
                    PhotoDirTimeModel photoDirModel = dirLN.get(position);
                    message.obj = dirMap.get(photoDirModel.getDirName());
                    mHandler.sendMessage(message);
                    nowDir = photoDirModel.getDirName();
                }
                changeDirButton.setText(nowDir);
                pfPhotoPopAdapter.notifyDataSetChanged();
                hideChangeDirView();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pf_gallery_dir:
                if (scanList == null || scanList.size() <= 0) {
                    return;
                }
                if (mediaY == 570) {
                    int tempH = changeDirListView.getMeasuredHeight();
                    if (tempH > 380) {
                        mediaY = tempH;
                    }
                }

                if (changeDirBg.getVisibility() == View.VISIBLE) {
                    hideChangeDirView();
                } else {
                    if (pfPhotoPopAdapter == null) {
                        initChangeDirList();
                    }
                    showChangeDirView();
                }
                break;
            case R.id.pf_change_dir_bg:
                hideChangeDirView();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_PHOTO_BEGIN://开始扫描
                    scanPhoto_v2();
                    break;
                case SCAN_PHOTO_COMPLETE://扫描结束
                    galleryList.clear();
                    galleryList.addAll(scanList);
                    Collections.sort(galleryList, new Comparator<ScanImageAndTime>() {
                        @Override
                        public int compare(ScanImageAndTime o1, ScanImageAndTime o2) {
                            int sort = 0;
                            long time =Long.valueOf(o2.getTime())-Long.valueOf(o1.getTime());
                            if(time >0){
                                sort = 1;
                            }else if(time < 0){
                                sort = -1;
                            }
                            return sort;
                        }
                    });
                    adapter.notifyDataSetChanged();
                    nowDir = getString(R.string.all_photo);
                    break;
                case CHANGE_PHOTO_DIR_S://改变目录 第一个为拍照按钮
                    List<String> showListS = (ArrayList) msg.obj;
                    galleryList.clear();
                    galleryList.addAll(scanList);
//                    Collections.reverse(galleryList);
                    adapter.notifyDataSetChanged();
                    break;
                case CHANGE_PHOTO_DIR://改变目录
                    List<ScanImageAndTime> showList = (ArrayList) msg.obj;
                    galleryList.clear();
                    galleryList.addAll(showList);
                    Collections.reverse(showList);
//                    adapter.setFirstSpecial(false);
                    adapter.notifyDataSetChanged();
                    break;
                case TOAST_DELAY:
                    if(!Utils.isWifi(getApplicationContext())) {
                        ToastUtils.showMessage("当前处于移动网络下，建议WIFI下进行上传！");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

//    private ArrayList<String> getImages(List<ScanImageAndTime> list) {
//        ArrayList<String> imgs = new ArrayList<>();
//        for (ScanImageAndTime path : list) {
//            if (!Utils.stringIsNull(path) && !path.contains("CGImage")) {
//                imgs.add(path);
//            }
//        }
//        return imgs;
//    }

    /**
     * 获取当前显示目录名称
     *
     * @return 当前显示目录名称
     */
    public String getShowWitchDir() {
        return nowDir;
    }

    @Override
    public void titleRightButtonListener() {
        List<String> selected = getSelectItems();
        chooseImageFinish(selected, false);
    }

    public void selectChange(HashMap<String, Boolean> mSelectMap) {
        setTitleText("选择图片" + "(" + mSelectMap.size() + ")", "确定");
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
    private void chooseImageFinish(final List<String> images, boolean isCamera) {
        if (null != images && images.size() > 0) {
            if (isCut) {

            } else {
                //判断返回类型
                if (type == PhotoFamilyFragment.ADD_PIC) {
                    //同时将拍摄时间存入数据库

                    //将图片存入数据库
                    ThreadManager.instance.excuteRunnable(new Runnable() {
                        @Override
                        public void run() {
                            for (String path : images) {
                                ScanImageAndTime imageAndTime = new ScanImageAndTime(path);
                                imageAndTime = galleryList.get(galleryList.indexOf(imageAndTime)) ;
                                AlreadySavePath alreadySavePath = new AlreadySavePath();
                                alreadySavePath.setLocalPath(path);
                                alreadySavePath.setPhoto_time(TimeUtil.getYMDHMSFromMillion(Long.valueOf(imageAndTime.getTime())));
                                alreadySavePath.setFamily_uuid(PhotoFamilyFragment.instance.getCurrentFamily_uuid());
                                alreadySavePath.setStatus(1);
                                uploadDb.save(alreadySavePath);
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showMessage("已将" + images.size() + "张图片加入上传队列");
                                    Intent intent = new Intent(PfUpGalleryActivity.this, PicUploadService.class);
                                    startService(intent);
                                }
                            });
                        }
                    });
                } else if (type == EditPfActivity.CHOOSE_NEW) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_LIST, (ArrayList<String>) images);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        } else {
            if (isCamera) {

            } else {
                Toast.makeText(PfUpGalleryActivity.this, getString(R.string.please_choose_image), Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 扫描图片
     */
    private void scanPhoto_v2() {
        ScanPhoto_V2.scanMediaTime(this, scanList, dirMap);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        File appDir = new File(Environment.getExternalStorageDirectory() + "/CGImage");
        if (appDir.exists()) {
            FileUtil.deleteFolder(appDir);
        }
    }

}
