package com.wj.kindergarten.ui.emot;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Emot;
import com.wj.kindergarten.ui.addressbook.EmotManager;
import com.wj.kindergarten.ui.viewpager.CirclePageIndicator;
import com.wj.kindergarten.ui.viewpager.ViewPagerAdapter;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;

/**
 * ViewEmot
 *
 * @Description:表情view
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-14 9:29
 */
public class ViewEmot2 extends LinearLayout implements View.OnClickListener {
    private ViewPager pager = null;
    private CirclePageIndicator pageIndicator = null;
    private RelativeLayout layoutBQ = null;//点击展开表情
    private EditText etMessage = null;
    private RelativeLayout layoutSend = null;
    private ViewPagerAdapter adapter = null;

    private ArrayList<View> pageViews;
    private static final int PAGE_SIZE = 21;//每一页显示的表情个数 显示3行
    private ChooseFaceImpl chooseFaceImpl = null;
    private ArrayList<Emot> emojis = new ArrayList<Emot>();
    public ArrayList<ArrayList<Emot>> emojiLists = new ArrayList<ArrayList<Emot>>();
    private Context context;
    private LinearLayout layoutImgs = null;//表情符号列表
    private int height = 0;
    private boolean isShow = false;
    private boolean isJianPan = true;//是否是
    private ImageView imageView = null;

    private SendMessage sendMessage = null;

    public ViewEmot2(Context context, SendMessage sendMessage) {
        super(context);
        this.context = context;
        this.sendMessage = sendMessage;
        LayoutInflater.from(context).inflate(R.layout.view_emot_2, this);
        init();
        page();
        initViews();
        initData();
    }

    private void init() {
        pager = (ViewPager) findViewById(R.id.vp_images);
        pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        layoutBQ = (RelativeLayout) findViewById(R.id.layout_biao_qing);
        etMessage = (EditText) findViewById(R.id.et_message);
        layoutSend = (RelativeLayout) findViewById(R.id.layout_send);
        layoutImgs = (LinearLayout) findViewById(R.id.layout_images_1);
        imageView = (ImageView) findViewById(R.id.iv_biao_qing);
        layoutBQ.setOnClickListener(this);
        layoutSend.setOnClickListener(this);
        etMessage.setOnClickListener(this);
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
            GridView view = new GridView(context);
            FaceAdapter adapter = new FaceAdapter(context, emojiLists.get(i), chooseFaceImpl);
            view.setAdapter(adapter);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            pageViews.add(view);
        }
    }

    private void initData() {
        adapter = new ViewPagerAdapter(pageViews);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pageIndicator.setViewPager(pager);//加上小圆圈

        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        layoutImgs.measure(w, h);
        height = layoutImgs.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParamsChild = layoutImgs.getLayoutParams();
        layoutParamsChild.height = 0;
        layoutImgs.setLayoutParams(layoutParamsChild);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_biao_qing:
                CGLog.d("H: " + height);
                if (isJianPan) {
                    Utils.inputMethod(context, false, layoutImgs);//先隐藏输入法
                    handler.sendEmptyMessageDelayed(1, 300);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.jianpan));
                    isJianPan = false;
                } else {
                    Utils.showLayout(layoutImgs, height, 0, Utils.ANIMATION_DURATION);
                    handler.sendEmptyMessageDelayed(2, 300);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.bq));
                    isJianPan = true;
                }
                break;
            case R.id.et_message:
                if (!isJianPan) {
                    isJianPan = true;
                    Utils.showLayout(layoutImgs, height, 0, 100);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.bq));
                }
                break;
            case R.id.layout_send:
                if (Utils.stringIsNull(etMessage.getText().toString())) {
                    Utils.showToast(context, "消息不能为空");
                    return;
                } else {
                    sendMessage.send(etMessage.getText().toString().trim());
                }
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Utils.showLayout(layoutImgs, 0, height, Utils.ANIMATION_DURATION);
            } else if (msg.what == 2) {
                Utils.inputMethod(context, true, layoutImgs);//先隐藏输入法
            }
        }
    };

    private class ChooseFaceImpl implements ChooseFace {

        @Override
        public void choose(Emot emot) {
            String text = etMessage.getText().toString().trim();
            text = text + "[" + emot.getDatavalue() + "]";
            etMessage.setText(EmotUtil.getEmotionContent(context, text));
            CharSequence text2 = etMessage.getText();//设置光标在文本末尾
            if (text2 instanceof Spannable) {
                Spannable spanText = (Spannable) text2;
                Selection.setSelection(spanText, text.length());
            }
        }
    }

    public void showSoftKeyboard() {
        etMessage.requestFocus();
        Utils.inputMethod(context, true, etMessage);
    }

    public void hideSoftKeyboard() {
        Utils.inputMethod(context, false, etMessage);
    }

    /**
     * 消息发送成功后 清空editText
     */
    public void cleanEditText() {
        etMessage.setText("");
    }

    /**
     * 点击表情图片的其它地方时隐藏表情
     */
    public void hideFaceLayout() {
        if (!isJianPan) {
            isJianPan = true;
            Utils.showLayout(layoutImgs, height, 0, Utils.ANIMATION_DURATION);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.jianpan));
        }

        Utils.inputMethod(context, false, layoutImgs);//先隐藏输入法
    }

    public void showInput() {
        Utils.inputMethod(context, true, layoutImgs);//先隐藏输入法
        etMessage.setFocusable(true);
        etMessage.requestFocus();
    }
}
