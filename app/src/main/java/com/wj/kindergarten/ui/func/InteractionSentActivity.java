package com.wj.kindergarten.ui.func;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.*;
import com.wj.kindergarten.bean.Class;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.addressbook.EmotManager;
import com.wj.kindergarten.ui.emot.ChooseFace;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.emot.FaceAdapter;
import com.wj.kindergarten.ui.imagescan.GalleryImagesActivity;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.ui.viewpager.CirclePageIndicator;
import com.wj.kindergarten.ui.viewpager.ViewPagerAdapter;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * InteractionSentActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 21:28
 */
public class InteractionSentActivity extends BaseActivity {
    private EditText editText;
    private TextView classTv;
    private ImageView bqIv;
    private LinearLayout layoutImgs = null;//表情符号列表
    private ViewPager pager = null;
    private CirclePageIndicator pageIndicator = null;
    private ViewPagerAdapter adapter = null;
    private ArrayList<View> pageViews;
    private static final int PAGE_SIZE = 21;//每一页显示的表情个数 显示3行
    private ChooseFaceImpl chooseFaceImpl = null;
    private ArrayList<Emot> emojis = new ArrayList<Emot>();
    public ArrayList<ArrayList<Emot>> emojiLists = new ArrayList<ArrayList<Emot>>();
    private boolean isJianPan = true;//是否是
    private LinearLayout bottomLayout;
    private PopupWindow mPopupWindow = null;
    private PopAdapter popAdapter = null;

    private LinearLayout photoContent = null;
    private final int REQUEST_TAKE_PHOTO = 0;
    private final int REQUEST_SHOW_GALLERY = 1;

    private UploadFile uploadFile;
    private ArrayList<String> images = new ArrayList<>();
    private HintInfoDialog dialog = null;
    //发表互动的班级集合
    private List<Class> class_list = CGApplication.getInstance().getLogin().getClass_list();

    private int count = 0;
    private String path = "";
    private int time = 0;
    private IntentFilter filter = null;
    private NetworkConnectChangedReceiver networkConnectChangedReceiver = null;
    private boolean isCon = false;
    private String newClassUUid;
    private TextView send_interaction_link;
    private String link_title;
    private String link_url;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_interaction_send;
    }

    @Override
    protected void setNeedLoading() {

    }

    boolean isResult = true;
    AlertDialog alertDialog;
    @Override
    protected void onCreate() {
        setTitleText("发表互动", "发表");
        send_interaction_link = (TextView)findViewById(R.id.send_interaction_link);
        send_interaction_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send_interaction_link.setText("");
                //弹出对话框
                if(alertDialog != null) {
                    alertDialog.show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(InteractionSentActivity.this);
                View view = View.inflate(InteractionSentActivity.this,R.layout.custom_title,null);
                final EditText et_dialog = (EditText) view.findViewById(R.id.et_dialog);
                alertDialog = builder.setView(view)
                        .setTitle("编辑分享地址")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getLinkTitle(et_dialog.getText().toString());
                                link_url = et_dialog.getText().toString();
                                send_interaction_link.setText(""+link_url);

                            }
                        }).create();
                alertDialog.show();

            }
        });
        send_interaction_link.setFocusableInTouchMode(false);
        editText = (EditText) findViewById(R.id.interaction_send_edit);
        photoContent = (LinearLayout) findViewById(R.id.repairs_photo_content);
        bqIv = (ImageView) findViewById(R.id.send_interaction_bq);
        pager = (ViewPager) findViewById(R.id.vp_images);
        pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        layoutImgs = (LinearLayout) findViewById(R.id.layout_images_1);
        bottomLayout = (LinearLayout) findViewById(R.id.send_interaction_bq_layout);
        bottomLayout.setVisibility(View.GONE);

        page();
        initViews();
        initData();

        classTv = (TextView) findViewById(R.id.interaction_send_class);

        if (class_list != null && class_list.size() > 0) {
            newClassUUid = class_list.get(0).getUuid();
            classTv.setText(class_list.get(0).getName());
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    bottomLayout.setVisibility(View.VISIBLE);
                    layoutImgs.setVisibility(View.GONE);
                    handler.sendEmptyMessageDelayed(3, 300);
                } else {
                    bottomLayout.setVisibility(View.GONE);
                }
            }
        });

        bqIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBq();
            }
        });


//        CGApplication cgApplication = CGApplication.getInstance();
//        if (cgApplication.getLogin() != null && cgApplication.getLogin().getList() != null &&
//                cgApplication.getLogin().getList().size() > 0) {
//            Map<String, ChildInfo> tempMap = new HashMap<>();
//            for (int i = 0; i < cgApplication.getLogin().getList().size(); i++) {
//                ChildInfo childInfoTemp = cgApplication.getLogin().getList().get(i);
//                tempMap.put(childInfoTemp.getClassuuid(), childInfoTemp);
//            }
//            for (Map.Entry entry : tempMap.entrySet()) {
//                childInfos.add((ChildInfo) entry.getValue());
//            }
//        }


        classTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
            }
        });

        refreshPhoto();

        uploadFile = new UploadFile(mContext, new UploadImageImpl(), 0, 720, 1280);

        filter = new IntentFilter();
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkConnectChangedReceiver, filter);


        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                CGLog.d("AN: " + getRunningActivityName());
                if (msg.what == 1024) {
                    //这个消息是网络状态改变，不是用来上图片的
//                    if (!isCon && null != images && images.size() > 0) {
//                        isCon = true;
//                        uploadImage(count);
//                    }
                }
            }
        });
        getIntentData();
    }

    boolean isListFrom = true;
    private void getIntentData() {
        link_title =  getIntent().getStringExtra("title");
        link_url = getIntent().getStringExtra("url");
        isListFrom = getIntent().getBooleanExtra("isListFrom",true);
        if(Utils.isNull(link_title) != null && !TextUtils.isEmpty(link_title)){
            send_interaction_link.append(""+Utils.isNull(link_title));
        }
        if(Utils.isNull(link_url) != null && !TextUtils.isEmpty(link_url)){
            getLinkTitle(link_url);
        }



    }

    private void getLinkTitle(String url) {
        //调用获取title方法
        UserRequest.getInteractionLinkTitle(InteractionSentActivity.this, url, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                HtmlTitle ht = (HtmlTitle) domain;
                if (ht != null) {
                    link_title = "" + Utils.isNull(ht.getData());
                    editText.append("" + ht.getData());
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

    private String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }


    //表情分页
    private void page() {
        emojis.clear();
        emojis.addAll(EmotManager.getEmots());

        if (null != emojis && emojis.size() > 0) {
            int page = 0;
            if (emojis.size() % PAGE_SIZE == 0) {
                page = emojis.size() / PAGE_SIZE;
            } else {
                page = emojis.size() / PAGE_SIZE + 1;
            }
            if (page == 1) {
                emojiLists.add(emojis);
            } else {
                for (int i = 0; i < page; i++) {
                    emojiLists.add(getData(i));
                }
            }
        }
    }

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */
    private ArrayList<Emot> getData(int page) {
        int startIndex = page * PAGE_SIZE;
        int endIndex = startIndex + PAGE_SIZE;

        if (endIndex > emojis.size()) {
            endIndex = emojis.size();
        }
        // 不这么写，会在viewpager加载中报集合操作异常
        ArrayList<Emot> list = new ArrayList<Emot>();
        list.addAll(emojis.subList(startIndex, endIndex));
        return list;
    }

    private void initViews() {
        pageViews = new ArrayList<View>();
        chooseFaceImpl = new ChooseFaceImpl();
        for (int i = 0; i < emojiLists.size(); i++) {
            GridView view = new GridView(mContext);
            FaceAdapter adapter = new FaceAdapter(mContext, emojiLists.get(i), chooseFaceImpl);
            view.setAdapter(adapter);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            pageViews.add(view);
        }
    }

    private void initData() {
        adapter = new ViewPagerAdapter(pageViews);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pageIndicator.setViewPager(pager);//加上小圆圈

        layoutImgs.setVisibility(View.GONE);
    }

    /**
     * 刷新图片
     */
    private void refreshPhoto() {
        int photoW = (int) getResources().getDimension(R.dimen.fix_detail_photo_height);
        int photoMargin = (int) getResources().getDimension(R.dimen.small_padding);

        photoContent.removeAllViews();
        // photoContent.setBackgroundColor(getResources().getColor(android.R.color.black));
        for (int i = 0; i < images.size(); i++) {
            final int position = i;
            ImageView photoView = new ImageView(mContext);
            LinearLayout.LayoutParams photoLayoutParams = new LinearLayout.LayoutParams(photoW, photoW);
            if (i > 0) {
                photoLayoutParams.leftMargin = photoMargin;
            }
            photoView.setLayoutParams(photoLayoutParams);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoaderUtil.displayImage("file://" + images.get(i), photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPhoto(position);
                }
            });
            photoContent.addView(photoView);
        }

        if (images.size() < GalleryImagesActivity.IMAGE_MAX) {
            ImageView photoView = new ImageView(mContext);
            LinearLayout.LayoutParams photoLayoutParams = new LinearLayout.LayoutParams(photoW, photoW);
            if (images.size() > 0) {
                photoLayoutParams.leftMargin = photoMargin;
            }
            photoLayoutParams.gravity = Gravity.CENTER;
            photoView.setLayoutParams(photoLayoutParams);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            photoView.setImageResource(R.drawable.interaction_add_image);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    File appDir = new File(Environment.getExternalStorageDirectory()+"/CGImage");
//                    FileUtil.deleteFolder(appDir);
                    takePhoto();
                }
            });
            photoContent.addView(photoView);
        }
    }

    private void clickBq() {
        if (isJianPan) {
            Utils.inputMethod(mContext, false, layoutImgs);//先隐藏输入法
            handler.sendEmptyMessageDelayed(1, 300);
            bqIv.setImageDrawable(getResources().getDrawable(R.drawable.jianpan));
            isJianPan = false;
        } else {
            layoutImgs.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(2, 300);
            bqIv.setImageDrawable(getResources().getDrawable(R.drawable.bq));
            isJianPan = true;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                layoutImgs.setVisibility(View.VISIBLE);
            } else if (msg.what == 2) {
                Utils.inputMethod(mContext, true, editText);
            } else if (msg.what == 3) {
                bottomLayout.setVisibility(View.VISIBLE);
                layoutImgs.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 添加已有图片
     */
    private void takePhoto() {
        Intent intent = new Intent(this, GalleryImagesActivity.class);
        intent.putExtra(Constants.ALREADY_SELECT_KEY, images);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 点击图片
     *
     * @param position 点击的图片位置
     */
    private void clickPhoto(int position) {
        Intent intent = new Intent(this, PhotoWallActivity.class);
        intent.putExtra(PhotoWallActivity.KEY_POSITION, position);
        intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, images);
        intent.putExtra(PhotoWallActivity.KEY_TYPE, "删除");
        startActivityForResult(intent, REQUEST_SHOW_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                images.clear();
                ArrayList<String> result = data.getStringArrayListExtra(GalleryImagesActivity.RESULT_LIST);
                images.addAll(result);
                refreshPhoto();
            } else if (requestCode == REQUEST_SHOW_GALLERY) {
                images.clear();
                ArrayList<String> result = data.getStringArrayListExtra(PhotoWallActivity.RESULT_LIST);
                images.addAll(result);
                refreshPhoto();
            }
        }
    }

    private void showPop() {
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_up);
        classTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(classTv, 0, 1);
            popAdapter.notifyDataSetChanged();
            return;
        }
        LinearLayout popupView = null;
        popupView = (LinearLayout) View.inflate(mContext, R.layout.layout_send_interaction, null);
        ListView listView = (ListView) popupView.findViewById(R.id.send_interaction_class_list);
        popAdapter = new PopAdapter();
        listView.setAdapter(popAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                newClassUUid = class_list.get(i).getUuid();
                classTv.setText(class_list.get(i).getName());
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setAnimationStyle(R.style.PopWindowAnim);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);//outside hide
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(classTv, 0, 1);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable drawable = getResources().getDrawable(R.drawable.arrow_down);
                classTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            }
        });
    }

    class PopAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return class_list.size();
        }

        @Override
        public Object getItem(int i) {
            return class_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(mContext, R.layout.item_send_interaction_class, null);
            TextView textView = (TextView) view.findViewById(R.id.item_send_interaction_class);
            Class aClass = class_list.get(i);
            textView.setText(aClass.getName());
            if (newClassUUid != null && newClassUUid.equals(aClass.getUuid())) {
                Drawable drawable = getResources().getDrawable(R.drawable.select_right);
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            return view;
        }
    }

    private void uploadImage(int index) {
        try {
            if (null == dialog) {
                dialog = new HintInfoDialog(InteractionSentActivity.this, "第(" + (index + 1) + "/" + images.size() + ")张图片上传中，请稍后...");
                dialog.setCancelable(false);
                dialog.setOnKeyListener(onKeyListener);
                dialog.show();
            } else {
                dialog.setText("第(" + (index + 1) + "/" + images.size() + ")张图片上传中，请稍后...");
            }
            uploadFile.upload(images.get(index));
            CGLog.d("path: " + images.get(index));
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToast(InteractionSentActivity.this, "上传图片失败");
            if (null != dialog) {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void titleRightButtonListener() {
        String content = "";
        content = editText.getText().toString();

        if (Utils.stringIsNull(content) && images.size() == 0) {
            Utils.showToast(mContext, "请输入内容或添加图片");
            return;
        }

        if (Utils.stringIsNull(newClassUUid)) {
            Utils.showToast(mContext, "请选择关联班级");
            return;
        }

        if (images.size() > 0) {
            uploadImage(0);
        } else {
            sendInteraction(newClassUUid, editText.getText().toString(), path);
        }
    }


    private class UploadImageImpl implements UploadImage {

        @Override
        public void success(Result result) {
            time = 0;
            if (Utils.stringIsNull(path)) {
                path = result.getImgUrl();
            } else {
                path = path + "," + result.getImgUrl();
            }
            count++;
            if (count < images.size()) {
                uploadImage(count);
            } else {
                sendInteraction(newClassUUid, editText.getText().toString(), path);
            }
        }

        @Override
        public void failure(String message) {
            if (time < 3) {
                time++;
                uploadImage(count);
            } else {
                if (null != dialog) {
                    dialog.dismiss();
                }
                Utils.showToast(InteractionSentActivity.this, /*"抱歉，图片上传失败。"*/message);
            }
        }
    }

    private void sendInteraction(String uuid, String content, String urls) {
        if (null != dialog) {
            dialog.setText("发表互动中，请稍后...");
        } else {
            dialog = new HintInfoDialog(InteractionSentActivity.this, "发表互动中，请稍后...");
            dialog.show();
        }
        UserRequest.sendInteraction(mContext, link_title, uuid, "", content, urls,link_url,new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(mContext, domain.getResMsg().getMessage());
                images.clear();
                count = 0;
                if(isListFrom){
                    setResult(RESULT_OK);
                    finish();
                }else {
                    Intent intent = new Intent(InteractionSentActivity.this,InteractionListActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                dialog.dismiss();
                if (Utils.stringIsNull(message)) {
                    Utils.showToast(mContext, message);
                } else {
                    Utils.showToast(mContext, "发表互动失败");
                }
            }
        });
    }

    private class ChooseFaceImpl implements ChooseFace {

        @Override
        public void choose(Emot emot) {
            String text = editText.getText().toString().trim();
            text = text + "[" + emot.getDatavalue() + "]";
            editText.setText(EmotUtil.getEmotionContent(mContext, text));
            CharSequence text2 = editText.getText();//设置光标在文本末尾
            if (text2 instanceof Spannable) {
                Spannable spanText = (Spannable) text2;
                Selection.setSelection(spanText, text.length());
            }
        }
    }

    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {

        @Override

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                images.clear();
                count = 0;

                finish();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkConnectChangedReceiver);//关闭广播
    }
}
//}
//=======
//package com.wj.kindergarten.ui.func;
//
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.ConnectivityManager;
//import android.net.wifi.WifiManager;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.ViewPager;
//import android.text.Selection;
//import android.text.Spannable;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.wenjie.jiazhangtong.R;
//import com.wj.kindergarten.CGApplication;
//import com.wj.kindergarten.bean.BaseModel;
//import com.wj.kindergarten.bean.ChildInfo;
//import com.wj.kindergarten.bean.Emot;
//import com.wj.kindergarten.common.Constants;
//import com.wj.kindergarten.handler.GlobalHandler;
//import com.wj.kindergarten.handler.MessageHandlerListener;
//import com.wj.kindergarten.net.RequestResultI;
//import com.wj.kindergarten.net.request.UserRequest;
//import com.wj.kindergarten.net.upload.Result;
//import com.wj.kindergarten.net.upload.UploadFile;
//import com.wj.kindergarten.net.upload.UploadImage;
//import com.wj.kindergarten.ui.BaseActivity;
//import com.wj.kindergarten.ui.addressbook.EmotManager;
//import com.wj.kindergarten.ui.emot.ChooseFace;
//import com.wj.kindergarten.ui.emot.EmotUtil;
//import com.wj.kindergarten.ui.emot.FaceAdapter;
//import com.wj.kindergarten.ui.imagescan.GalleryImagesActivity;
//import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
//import com.wj.kindergarten.ui.viewpager.CirclePageIndicator;
//import com.wj.kindergarten.ui.viewpager.ViewPagerAdapter;
//import com.wj.kindergarten.utils.CGLog;
//import com.wj.kindergarten.utils.FileUtil;
//import com.wj.kindergarten.utils.HintInfoDialog;
//import com.wj.kindergarten.utils.ImageLoaderUtil;
//import com.wj.kindergarten.utils.Utils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * InteractionSentActivity
// *
// * @Description:
// * @Author: Wave
// * @CreateDate: 2015/7/26 21:28
// */
//public class InteractionSentActivity extends BaseActivity {
//    private EditText editText;
//    private TextView classTv;
//    private ImageView bqIv;
//    private LinearLayout layoutImgs = null;//表情符号列表
//    private ViewPager pager = null;
//    private CirclePageIndicator pageIndicator = null;
//    private ViewPagerAdapter adapter = null;
//    private ArrayList<View> pageViews;
//    private static final int PAGE_SIZE = 21;//每一页显示的表情个数 显示3行
//    private ChooseFaceImpl chooseFaceImpl = null;
//    private ArrayList<Emot> emojis = new ArrayList<Emot>();
//    public ArrayList<ArrayList<Emot>> emojiLists = new ArrayList<ArrayList<Emot>>();
//    private boolean isJianPan = true;//是否是
//    private LinearLayout bottomLayout;
//    private String nowClassUUID = "";
//    private String nowClassName = "";
//    private PopupWindow mPopupWindow = null;
//    private PopAdapter popAdapter = null;
//    private List<ChildInfo> childInfos = new ArrayList<>();
//
//    private LinearLayout photoContent = null;
//    private final int REQUEST_TAKE_PHOTO = 0;
//    private final int REQUEST_SHOW_GALLERY = 1;
//
//    private UploadFile uploadFile;
//    private ArrayList<String> images = new ArrayList<>();
//    private HintInfoDialog dialog = null;
//
//    private int count = 0;
//    private String path = "";
//    private int time = 0;
//    private IntentFilter filter = null;
//    private NetworkConnectChangedReceiver networkConnectChangedReceiver = null;
//    private boolean isCon = false;
//
//    @Override
//    protected void setContentLayout() {
//        layoutId = R.layout.activity_interaction_send;
//    }
//
//    @Override
//    protected void setNeedLoading() {
//
//    }
//
//    @Override
//    protected void onCreate() {
//        setTitleText("发表互动", "发表");
//
//        editText = (EditText) findViewById(R.id.interaction_send_edit);
//        photoContent = (LinearLayout) findViewById(R.id.repairs_photo_content);
//        bqIv = (ImageView) findViewById(R.id.send_interaction_bq);
//        pager = (ViewPager) findViewById(R.id.vp_images);
//        pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
//        layoutImgs = (LinearLayout) findViewById(R.id.layout_images_1);
//        bottomLayout = (LinearLayout) findViewById(R.id.send_interaction_bq_layout);
//        bottomLayout.setVisibility(View.GONE);
//
//        page();
//        initViews();
//        initData();
//
//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    bottomLayout.setVisibility(View.VISIBLE);
//                    layoutImgs.setVisibility(View.GONE);
//                    handler.sendEmptyMessageDelayed(3, 300);
//                } else {
//                    bottomLayout.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        bqIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickBq();
//            }
//        });
//
//        classTv = (TextView) findViewById(R.id.interaction_send_class);
//        CGApplication cgApplication = CGApplication.getInstance();
//        if (cgApplication.getLogin() != null && cgApplication.getLogin().getList() != null &&
//                cgApplication.getLogin().getList().size() > 0) {
//            Map<String, ChildInfo> tempMap = new HashMap<>();
//            for (int i = 0; i < cgApplication.getLogin().getList().size(); i++) {
//                ChildInfo childInfoTemp = cgApplication.getLogin().getList().get(i);
//                tempMap.put(childInfoTemp.getClassuuid(), childInfoTemp);
//            }
//            for (Map.Entry entry : tempMap.entrySet()) {
//                childInfos.add((ChildInfo) entry.getValue());
//            }
//        }
//
//        if (childInfos != null && childInfos.size() > 0) {
//            nowClassUUID = childInfos.get(0).getClassuuid();
//            nowClassName = Utils.getClassNameFromId(nowClassUUID);
//            classTv.setText(nowClassName);
//        }
//        classTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPop();
//            }
//        });
//
//        refreshPhoto();
//
//        uploadFile = new UploadFile(mContext, new UploadImageImpl(), 0, 720, 1280);
//
//        filter = new IntentFilter();
//        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(networkConnectChangedReceiver, filter);
//
//
//        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
//            @Override
//            public void handleMessage(Message msg) {
//                CGLog.d("AN: " + getRunningActivityName());
//                if (msg.what == 1024) {
//                    if (!isCon && null != images && images.size() > 0) {
//                        isCon = true;
//                        uploadImage(count);
//                    }
//                }
//            }
//        });
//    }
//
//    private String getRunningActivityName() {
//        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
//        return runningActivity;
//    }
//
//
//    //表情分页
//    private void page() {
//        emojis.clear();
//        emojis.addAll(EmotManager.getEmots());
//
//        if (null != emojis && emojis.size() > 0) {
//            int page = 0;
//            if (emojis.size() % PAGE_SIZE == 0) {
//                page = emojis.size() / PAGE_SIZE;
//            } else {
//                page = emojis.size() / PAGE_SIZE + 1;
//            }
//            if (page == 1) {
//                emojiLists.add(emojis);
//            } else {
//                for (int i = 0; i < page; i++) {
//                    emojiLists.add(getData(i));
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取分页数据
//     *
//     * @param page
//     * @return
//     */
//    private ArrayList<Emot> getData(int page) {
//        int startIndex = page * PAGE_SIZE;
//        int endIndex = startIndex + PAGE_SIZE;
//
//        if (endIndex > emojis.size()) {
//            endIndex = emojis.size();
//        }
//        // 不这么写，会在viewpager加载中报集合操作异常
//        ArrayList<Emot> list = new ArrayList<Emot>();
//        list.addAll(emojis.subList(startIndex, endIndex));
//        return list;
//    }
//
//    private void initViews() {
//        pageViews = new ArrayList<View>();
//        chooseFaceImpl = new ChooseFaceImpl();
//        for (int i = 0; i < emojiLists.size(); i++) {
//            GridView view = new GridView(mContext);
//            FaceAdapter adapter = new FaceAdapter(mContext, emojiLists.get(i), chooseFaceImpl);
//            view.setAdapter(adapter);
//            view.setNumColumns(7);
//            view.setBackgroundColor(Color.TRANSPARENT);
//            view.setHorizontalSpacing(1);
//            view.setVerticalSpacing(1);
//            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//            view.setCacheColorHint(0);
//            view.setPadding(5, 0, 5, 0);
//            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
//            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
//            view.setGravity(Gravity.CENTER);
//            pageViews.add(view);
//        }
//    }
//
//    private void initData() {
//        adapter = new ViewPagerAdapter(pageViews);
//        pager.setAdapter(adapter);
//        pager.setCurrentItem(0);
//        pageIndicator.setViewPager(pager);//加上小圆圈
//
//        layoutImgs.setVisibility(View.GONE);
//    }
//
//    /**
//     * 刷新图片
//     */
//    private void refreshPhoto() {
//        int photoW = (int) getResources().getDimension(R.dimen.fix_detail_photo_height);
//        int photoMargin = (int) getResources().getDimension(R.dimen.small_padding);
//
//        photoContent.removeAllViews();
//        // photoContent.setBackgroundColor(getResources().getColor(android.R.color.black));
//        for (int i = 0; i < images.size(); i++) {
//            final int position = i;
//            ImageView photoView = new ImageView(mContext);
//            LinearLayout.LayoutParams photoLayoutParams = new LinearLayout.LayoutParams(photoW, photoW);
//            if (i > 0) {
//                photoLayoutParams.leftMargin = photoMargin;
//            }
//            photoView.setLayoutParams(photoLayoutParams);
//            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
//            ImageLoaderUtil.displayImage("file://" + images.get(i), photoView);
//            photoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clickPhoto(position);
//                }
//            });
//            photoContent.addView(photoView);
//        }
//
//        if (images.size() < GalleryImagesActivity.IMAGE_MAX) {
//            ImageView photoView = new ImageView(mContext);
//            LinearLayout.LayoutParams photoLayoutParams = new LinearLayout.LayoutParams(photoW, photoW);
//            if (images.size() > 0) {
//                photoLayoutParams.leftMargin = photoMargin;
//            }
//            photoLayoutParams.gravity = Gravity.CENTER;
//            photoView.setLayoutParams(photoLayoutParams);
//            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
//            photoView.setImageResource(R.drawable.interaction_add_image);
//            photoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    File appDir = new File(Environment.getExternalStorageDirectory()+"/CGImage");
////                    FileUtil.deleteFolder(appDir);
//                    takePhoto();
//                }
//            });
//            photoContent.addView(photoView);
//        }
//    }
//
//    private void clickBq() {
//        if (isJianPan) {
//            Utils.inputMethod(mContext, false, layoutImgs);//先隐藏输入法
//            handler.sendEmptyMessageDelayed(1, 300);
//            bqIv.setImageDrawable(getResources().getDrawable(R.drawable.jianpan));
//            isJianPan = false;
//        } else {
//            layoutImgs.setVisibility(View.GONE);
//            handler.sendEmptyMessageDelayed(2, 300);
//            bqIv.setImageDrawable(getResources().getDrawable(R.drawable.bq));
//            isJianPan = true;
//        }
//    }
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                layoutImgs.setVisibility(View.VISIBLE);
//            } else if (msg.what == 2) {
//                Utils.inputMethod(mContext, true, editText);
//            } else if (msg.what == 3) {
//                bottomLayout.setVisibility(View.VISIBLE);
//                layoutImgs.setVisibility(View.GONE);
//            }
//        }
//    };
//
//    /**
//     * 添加已有图片
//     */
//    private void takePhoto() {
//        Intent intent = new Intent(this, GalleryImagesActivity.class);
//        intent.putExtra(Constants.ALREADY_SELECT_KEY, images);
//        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
//    }
//
//    /**
//     * 点击图片
//     *
//     * @param position 点击的图片位置
//     */
//    private void clickPhoto(int position) {
//        Intent intent = new Intent(this, PhotoWallActivity.class);
//        intent.putExtra(PhotoWallActivity.KEY_POSITION, position);
//        intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, images);
//        intent.putExtra(PhotoWallActivity.KEY_TYPE, "删除");
//        startActivityForResult(intent, REQUEST_SHOW_GALLERY);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_TAKE_PHOTO) {
//                images.clear();
//                ArrayList<String> result = data.getStringArrayListExtra(GalleryImagesActivity.RESULT_LIST);
//                images.addAll(result);
//                refreshPhoto();
//            } else if (requestCode == REQUEST_SHOW_GALLERY) {
//                images.clear();
//                ArrayList<String> result = data.getStringArrayListExtra(PhotoWallActivity.RESULT_LIST);
//                images.addAll(result);
//                refreshPhoto();
//            }
//        }
//    }
//
//    private void showPop() {
//        Drawable drawable = getResources().getDrawable(R.drawable.arrow_up);
//        classTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//        if (mPopupWindow != null) {
//            mPopupWindow.showAsDropDown(classTv, 0, 1);
//            popAdapter.notifyDataSetChanged();
//            return;
//        }
//        LinearLayout popupView = null;
//        popupView = (LinearLayout) View.inflate(mContext, R.layout.layout_send_interaction, null);
//        ListView listView = (ListView) popupView.findViewById(R.id.send_interaction_class_list);
//        popAdapter = new PopAdapter();
//        listView.setAdapter(popAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                nowClassUUID = childInfos.get(i).getClassuuid();
//                nowClassName = Utils.getClassNameFromId(nowClassUUID);
//                classTv.setText(nowClassName);
//                mPopupWindow.dismiss();
//            }
//        });
//        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPopupWindow.dismiss();
//            }
//        });
//
//        mPopupWindow.setAnimationStyle(R.style.PopWindowAnim);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setTouchable(true);
//        mPopupWindow.setOutsideTouchable(true);//outside hide
//        mPopupWindow.getContentView().setFocusableInTouchMode(true);
//        mPopupWindow.getContentView().setFocusable(true);
//        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mPopupWindow.dismiss();
//            }
//        });
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        mPopupWindow.update();
//        mPopupWindow.showAsDropDown(classTv, 0, 1);
//        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Drawable drawable = getResources().getDrawable(R.drawable.arrow_down);
//                classTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//            }
//        });
//    }
//
//    class PopAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return childInfos.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return childInfos.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            view = View.inflate(mContext, R.layout.item_send_interaction_class, null);
//            TextView textView = (TextView) view.findViewById(R.id.item_send_interaction_class);
//            ChildInfo childInfo = childInfos.get(i);
//            textView.setText(Utils.getClassNameFromId(childInfo.getClassuuid()));
//            if (nowClassUUID != null && nowClassUUID.equals(childInfo.getClassuuid())) {
//                Drawable drawable = getResources().getDrawable(R.drawable.select_right);
//                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//            } else {
//                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//            }
//            return view;
//        }
//    }
//
//    private void uploadImage(int index) {
//        try {
//            if (null == dialog) {
//                dialog = new HintInfoDialog(InteractionSentActivity.this, "第(" + (index + 1) + "/" + images.size() + ")张图片上传中，请稍后...");
//                dialog.setCancelable(false);
//                dialog.setOnKeyListener(onKeyListener);
//                dialog.show();
//            } else {
//                dialog.setText("第(" + (index + 1) + "/" + images.size() + ")张图片上传中，请稍后...");
//            }
//            uploadFile.upload(images.get(index));
//            CGLog.d("path: " + images.get(index));
//        } catch (Exception e) {
//            e.printStackTrace();
//            Utils.showToast(InteractionSentActivity.this, "上传图片失败");
//            if (null != dialog) {
//                dialog.dismiss();
//            }
//        }
//    }
//
//    @Override
//    protected void titleRightButtonListener() {
//        String content = "";
//        content = editText.getText().toString();
//
//        if (Utils.stringIsNull(content) && images.size() == 0) {
//            Utils.showToast(mContext, "请输入内容或添加图片");
//            return;
//        }
//
//        if (Utils.stringIsNull(nowClassUUID)) {
//            Utils.showToast(mContext, "请选择关联班级");
//            return;
//        }
//
//        if (images.size() > 0) {
//            uploadImage(0);
//        } else {
//            sendInteraction(nowClassUUID, editText.getText().toString(), path);
//        }
//    }
//
//
//    private class UploadImageImpl implements UploadImage {
//
//        @Override
//        public void success(Result result) {
//            time = 0;
//            if (Utils.stringIsNull(path)) {
//                path = result.getImgUrl();
//            } else {
//                path = path + "," + result.getImgUrl();
//            }
//            count++;
//            if (count < images.size()) {
//                uploadImage(count);
//            } else {
//                sendInteraction(nowClassUUID, editText.getText().toString(), path);
//            }
//        }
//
//        @Override
//        public void failure(String message) {
//            if (time < 3) {
//                time++;
//                uploadImage(count);
//            } else {
//                if (null != dialog) {
//                    dialog.dismiss();
//                }
//                Utils.showToast(InteractionSentActivity.this, /*"抱歉，图片上传失败。"*/message);
//            }
//        }
//    }
//
//    private void sendInteraction(String uuid, String content, String urls) {
//        if (null != dialog) {
//            dialog.setText("发表互动中，请稍后...");
//        } else {
//            dialog = new HintInfoDialog(InteractionSentActivity.this, "发表互动中，请稍后...");
//            dialog.show();
//        }
//        UserRequest.sendInteraction(mContext, "标题", uuid, "", content, urls, new RequestResultI() {
//            @Override
//            public void result(BaseModel domain) {
//                dialog.dismiss();
//                Utils.showToast(mContext, domain.getResMsg().getMessage());
//                images.clear();
//                count = 0;
//                setResult(RESULT_OK);
//                finish();
//            }
//
//            @Override
//            public void result(List<BaseModel> domains, int total) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//                dialog.dismiss();
//                if (Utils.stringIsNull(message)) {
//                    Utils.showToast(mContext, message);
//                } else {
//                    Utils.showToast(mContext, "发表互动失败");
//                }
//            }
//        });
//    }
//
//    private class ChooseFaceImpl implements ChooseFace {
//
//        @Override
//        public void choose(Emot emot) {
//            String text = editText.getText().toString().trim();
//            text = text + "[" + emot.getDatavalue() + "]";
//            editText.setText(EmotUtil.getEmotionContent(mContext, text));
//            CharSequence text2 = editText.getText();//设置光标在文本末尾
//            if (text2 instanceof Spannable) {
//                Spannable spanText = (Spannable) text2;
//                Selection.setSelection(spanText, text.length());
//            }
//        }
//    }
//
//    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
//
//        @Override
//
//        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (null != dialog) {
//                    dialog.dismiss();
//                }
//                images.clear();
//                count = 0;
//
//                finish();
//                return true;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onResume();
//        unregisterReceiver(networkConnectChangedReceiver);//关闭广播
//    }
//}
//>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
