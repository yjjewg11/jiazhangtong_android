package com.wj.kindergarten.ui.mine.photofamilypic;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
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
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SyncUploadPic;
import com.wj.kindergarten.bean.SyncUploadPicObj;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.services.PicUploadService;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.EditPfActivity;
import com.wj.kindergarten.ui.imagescan.PfGalleryImagesAdapter;
import com.wj.kindergarten.ui.imagescan.PfPhotoPopAdapter;
import com.wj.kindergarten.ui.imagescan.PhotoDirModel;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.ui.imagescan.ScanPhoto_V1;
import com.wj.kindergarten.ui.imagescan.ScanPhoto_V2;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.dbupdate.UploadPathDbTwo;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tangt on 2016/1/19.
 */
public class PfUpGalleryActivity extends BaseActivity implements View.OnClickListener {
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
    private HashMap<String, List<String>> dirMap = new HashMap<>();
    //存储图片的选中情况
    private HashMap<String, Boolean> mSelectMap = new HashMap<>();
    //拍照按钮和扫描到的图片列表
    private List<String> galleryList = new ArrayList<>();
    //扫描到的相册列表
    private ArrayList<String> scanList = new ArrayList<>();
    private PfGalleryImagesAdapter adapter;
    private GridView mGridView;
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

    /**
     * 被选中的图片列表 key
     */


    @Override
    public void setContentLayout() {
        layoutId = R.layout.imagescan_images_activity;
    }

    //如果未从网络同步已上传照片，则需要同步,默认未同步
    @Override
    public void setNeedLoading() {
        uploadDb = FinalDb.create(getApplicationContext(),"afinal.db",true, GloablUtils.ALREADY_DB_VERSION,new UploadPathDbTwo(this));
        isNeedLoading = !CGSharedPreference.getUploadSyncStatus();
    }

    int pageNo = 1;

    @Override
    protected void loadData() {
        UserRequest.initSyncUploadPic(this, pageNo,new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                CGSharedPreference.setUploadSyncStatus(true);
                SyncUploadPic syncUploadPic = (SyncUploadPic) domain;
                if(syncUploadPic != null && syncUploadPic.getList() != null
                        &&syncUploadPic.getList().getData() != null && syncUploadPic.getList().getData().size() > 0){
                    Iterator<SyncUploadPicObj> iterator = syncUploadPic.getList().getData().iterator();
                    while (iterator.hasNext()){
                        SyncUploadPicObj obj = iterator.next();
                        saveUploadObj(obj);
                    }
                }else {
                    ToastUtils.showMessage("暂无同步数据!");
                }
                loadSuc();
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
        AlreadySavePath savePath =  uploadDb.findById(obj.getMd5(), AlreadySavePath.class);
        if(savePath == null){
            savePath = new AlreadySavePath();
            savePath.setStatus(0);
            savePath.setLocalPath(obj.getMd5());
            uploadDb.save(savePath);
        }
    }

    @Override
    public void onCreate() {
        initSelectPic((ArrayList) getIntent().getSerializableExtra(Constants.ALREADY_SELECT_KEY));
        type = getIntent().getIntExtra("type", 0);
        SIGN_BOARD = (getIntent().getIntExtra("signBoard", 0));

        initWidget();

        mHandler.sendEmptyMessage(SCAN_PHOTO_BEGIN);
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
        changeDirButton = (TextView) findViewById(R.id.photo_gallery_dir);
        changeDirButton.setOnClickListener(this);

        changeDirListView = (ListView) findViewById(R.id.change_dir_list);
        changeDirListContent = (FrameLayout) findViewById(R.id.change_dir_list_content);
        changeDirBg = (FrameLayout) findViewById(R.id.change_dir_bg);
        changeDirBg.setOnClickListener(this);

        mGridView = (GridView) findViewById(R.id.child_grid);
//        galleryList.add(0, "");
//        scanList.add(0, "");
        adapter = new PfGalleryImagesAdapter(galleryList, canSelect, mSelectMap, mGridView, type);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && adapter.isFirstSpecial()) {//判断是否是拍照按钮
                    ArrayList<String> selected = getSelectItems();
                    if (null != selected && selected.size() < IMAGE_MAX) {
                        chooseList.clear();
                        chooseList.addAll(selected);
                        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        String filename = timeStampFormat.format(System.currentTimeMillis());
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, filename);
                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        openCameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, REQUEST_CAMERA);
                    } else {
                        Utils.showToast(PfUpGalleryActivity.this, getResources()
                                .getString(R.string.can_select_photo_max, IMAGE_MAX + ""));
                    }
                } else {
                    Intent intent = new Intent(PfUpGalleryActivity.this, PhotoWallActivity.class);
                    ArrayList<String> simplePic = new ArrayList<>();
                    simplePic.add(galleryList.get(position));
                    intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, simplePic);
                    startActivity(intent);
                }
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
        final List<PhotoDirModel> dirLN = new ArrayList<>();
        PhotoDirModel allPDM = new PhotoDirModel();
        allPDM.setDirName(getString(R.string.all_photo));
        allPDM.setPaths(getImages(scanList));
        dirLN.add(allPDM);
        for (Map.Entry<String, List<String>> entry : dirMap.entrySet()) {
            List<String> tempL = entry.getValue();
            if (entry != null && tempL != null && tempL.size() > 0) {
                PhotoDirModel tempPDM = new PhotoDirModel();
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
                    PhotoDirModel photoDirModel = dirLN.get(position);
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
            case R.id.photo_gallery_dir:
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
            case R.id.change_dir_bg:
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
                    galleryList.addAll(getImages(scanList));
                    Collections.reverse(galleryList);
//                    galleryList.add(0, "");
                    adapter.notifyDataSetChanged();
                    nowDir = getString(R.string.all_photo);
                    break;
                case CHANGE_PHOTO_DIR_S://改变目录 第一个为拍照按钮
                    List<String> showListS = (ArrayList) msg.obj;
                    galleryList.clear();
                    galleryList.addAll(getImages(showListS));
                    Collections.reverse(galleryList);
//                    galleryList.add(0, "");
                    adapter.setFirstSpecial(true);
                    adapter.notifyDataSetChanged();
                    break;
                case CHANGE_PHOTO_DIR://改变目录
                    List<String> showList = (ArrayList) msg.obj;
                    galleryList.clear();
                    galleryList.addAll(getImages(showList));
                    Collections.reverse(galleryList);
                    adapter.setFirstSpecial(false);
                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
    private void chooseImageFinish(List<String> images, boolean isCamera) {
        if (null != images && images.size() > 0) {
            if (isCut) {

            } else {
                //判断返回类型
                if (type == PhotoFamilyFragment.ADD_PIC) {
                    //将图片存入数据库
                    for (String path : images) {
                        AlreadySavePath alreadySavePath = new AlreadySavePath();
                        alreadySavePath.setLocalPath(path);
                        alreadySavePath.setFamily_uuid(PhotoFamilyFragment.instance.getCurrentFamily_uuid());
                        alreadySavePath.setStatus(1);
                        uploadDb.save(alreadySavePath);
                    }
                    Intent intent = new Intent(this, PicUploadService.class);
                    startService(intent);
                    ToastUtils.showMessage("已将" + images.size() + "张图片加入下载队列");
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
