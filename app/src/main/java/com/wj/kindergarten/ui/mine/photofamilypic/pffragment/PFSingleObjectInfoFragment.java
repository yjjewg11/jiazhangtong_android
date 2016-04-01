package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfDianZan;
import com.wj.kindergarten.bean.PfInfoDianZan;
import com.wj.kindergarten.bean.PfInfoDianZanObj;
import com.wj.kindergarten.bean.PfSingleAssess;
import com.wj.kindergarten.bean.PfSingleAssessObject;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.bean.SinlePfExtraInfo;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.PfCommonAssessAdapter;
import com.wj.kindergarten.ui.func.adapter.PfInfoFragmentAdapter;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.viewmodel.SingleLoadPicModel;
import com.wj.kindergarten.ui.more.ListenScrollView;
import com.wj.kindergarten.utils.CGLog;
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
import net.tsz.afinal.db.sqlite.DbModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tangt on 2016/2/23.
 */
public class PFSingleObjectInfoFragment extends Fragment {
    private final int UPDATE_ASSESS_COUNT = 301;
    private final int POP_DELAY_300 = 300;
    private AllPfAlbumSunObject sunObject;
    private ListenScrollView pf_single_scroll;
    private View innerView;
    private String maxTime;
    int pageNo = 1;
    private FinalDb family_uuid_object;
    private SinlePfExtraInfo singleInfo;
    private ImageView pf_gallery_image;
    private LinearLayout top_left;
    private TextView title_webview_normal_text;
    private TextView title_webview_normal_spinner;
    private ImageView normal_title_right_icon;
    private LinearLayout bottom_assess;
    private FrameLayout frame_bottom_tab;
    private TextView[] textViews;
    private ViewEmot2 emot2;
    private HintInfoDialog dialog;
    private View pf_more_view;
    private PfSingleInfoFragment pfSingleInfoFragment;
    private ImageView pf_delete;
    private ImageView pf_edit;
    private PfInfoFragmentAdapter pfInfoFragmentAdapter;
    private TextView pf_gallery_fragment_extra_info_description;
    private TextView pf_gallery_fragment_extra_info_time;
    private TextView pf_gallery_fragment_extra_info_human;
    private TextView pf_gallery_fragment_extra_info_address;
    private View assessView;
    private TextView pf_common_show_assess_title;
    private PullToRefreshListView assessListView;
    private PopupWindow popAssessWindow;
    private PfCommonAssessAdapter pfCommonAssessAdapter;
    private List<PfSingleAssessObject> assessObjectList = new ArrayList<>();
    private LinearLayout pf_pic_bottom_viewGroup;
    private TextView pf_pic_bottom_assess_count;
    private ProgressBar progressBar;
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POP_DELAY_300:
                    emot2.showSoftKeyboard();
                    bottom_assess.setVisibility(View.VISIBLE);
                    pf_pic_bottom_viewGroup.setVisibility(View.GONE);
                    break;
                case UPDATE_ASSESS_COUNT:
                    int count = singleInfo.getReply_count();
                    if (count == 0) {
                        pf_pic_bottom_assess_count.setVisibility(View.GONE);
                    } else {
                        pf_pic_bottom_assess_count.setVisibility(View.VISIBLE);
                    }
                    pf_pic_bottom_assess_count.setText("" + count);
                    break;
            }
        }
    };
    private TextView pf_fragment_extra_dianzan_count;
    private List<PfInfoDianZanObj> dianZanList;
    private FinalDb dbObj;
    private int position;
    private LinearLayout pf_common_show_asseess_send;
    private SingleLoadPicModel singlePicModel;

    public PFSingleObjectInfoFragment(PfInfoFragmentAdapter pfInfoFragmentAdapter, PfSingleInfoFragment pfSingleInfoFragment, int position) {
        this.pfSingleInfoFragment = pfSingleInfoFragment;
        this.pfInfoFragmentAdapter = pfInfoFragmentAdapter;
        this.position = position;
    }

    public PFSingleObjectInfoFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            dialog = new HintInfoDialog(getActivity(), "加载数据中...请稍后");
            dbObj = FinalUtil.getFamilyUuidObjectDb(getActivity());
            sunObject = pfInfoFragmentAdapter.getPositionObject(position);
            ((PfGalleryActivity) getActivity()).addFragment(this);
            family_uuid_object = FinalDb.create(getActivity(), GloablUtils.FAMILY_UUID_OBJECT);
            innerView = View.inflate(getActivity(), R.layout.pf_gallery_fragment, null);
            progressBar = (ProgressBar) innerView.findViewById(R.id.pf_gallery_fragment_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            initExtraView();
            pf_pic_bottom_assess_count = (TextView) innerView.findViewById(R.id.pf_pic_bottom_assess_count);
            pf_pic_bottom_viewGroup = (LinearLayout) innerView.findViewById(R.id.pf_pic_bottom_viewGroup);
            pf_single_scroll = (ListenScrollView) innerView.findViewById(R.id.pf_single_scroll);
            pf_gallery_image = (ImageView) innerView.findViewById(R.id.pf_gallery_image);
            pf_single_scroll.setMode(PullToRefreshBase.Mode.DISABLED);
            maxTime = TimeUtil.getStringDate(new Date());
            pf_single_scroll.setOnScrollChanged(new ListenScrollView.OnScrollChanged() {
                @Override
                public void onScrollChanged(int l, int t, int oldl, int oldt) {
                    //隐藏输入法
                    bottomCancle();
                }
            });
            pf_gallery_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //查询所有照片地址，进行显示.
                    ArrayList<String> list = new ArrayList();
                    String sql = "select path from " + GloablUtils.PF_FAMILY_TABLE_OBJ_NAME + ";";
                    List<DbModel> listDb = dbObj.findDbModelListBySQL(sql);
                    Iterator<DbModel> iterator = listDb.iterator();
                    while (iterator.hasNext()) {
                        DbModel dbModel = iterator.next();
                        list.add((String) dbModel.get("path"));
                    }
                    int position = list.indexOf(sunObject.getPath());
                    if (position < 0) position = 0;
                    Utils.carouselPic(getActivity(), position, list, false);
                }
            });
            pf_single_scroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    pageNo++;
                    queSingleAssess();
                }
            });
            initData();
            singlePicModel = new SingleLoadPicModel(getActivity());
            showPic();
            chcekUpadate();
            queryExtraData(sunObject.getUuid());
            queSingleAssess();
            initBottomBt();
            initTopView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return innerView;
    }

    public void showDia() {
//        ((BaseActivity)getActivity()).showCommonDialog();
    }

    public void cancleDia() {
//        ((BaseActivity)getActivity()).cancleCommonDialog();
    }

    private void initExtraView() {
        pf_gallery_fragment_extra_info_description = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_description);
        pf_gallery_fragment_extra_info_time = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_time);
        pf_gallery_fragment_extra_info_address = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_address);
        pf_gallery_fragment_extra_info_human = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_human);
        pf_fragment_extra_dianzan_count = (TextView) innerView.findViewById(R.id.pf_fragment_extra_dianzan_count);
    }

    private void initData() {
//        if (pfInfoFragmentAdapter != null) {
//            sunObject = pfInfoFragmentAdapter.getCurrentObject();
        updatePfInfo();
//        }
    }

    private void initTopView() {
        top_left = (LinearLayout) innerView.findViewById(R.id.normal_title_left_layout);
        title_webview_normal_text = (TextView) innerView.findViewById(R.id.title_webview_normal_text);
        title_webview_normal_spinner = (TextView) innerView.findViewById(R.id.title_webview_normal_spinner);
        title_webview_normal_spinner.setText("");
        title_webview_normal_spinner.setCompoundDrawables(null, null, null, null);
        normal_title_right_icon = (ImageView) innerView.findViewById(R.id.normal_title_right_icon);
        top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        normal_title_right_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showRightInfo();
//            }
//        });
        Drawable drawable = getResources().getDrawable(R.drawable.pf_card_view);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getIntrinsicHeight());
//        title_webview_normal_text.setCompoundDrawables(null, null, drawable, null);
        title_webview_normal_text.setCompoundDrawablePadding(4);
        title_webview_normal_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PfGalleryActivity) getActivity()).checkAlbumListFragment();
            }
        });
        title_webview_normal_text.setText("" + TimeUtil.getYMDTimeFromYMDHMS(Utils.isNull(sunObject.getCreate_time())));
    }

    private void chcekUpadate() {
        if (sunObject.getStatus() != 0) {
            queryItemNewInfo(sunObject.getUuid());
        } else {
            updatePfInfo();
        }
    }

    private void updatePfInfo() {
        pf_gallery_fragment_extra_info_description.setText("" + Utils.isNull(sunObject.getNote()));
        pf_gallery_fragment_extra_info_time.setText("拍摄时间: " + Utils.isNull(sunObject.getPhoto_time()));
        pf_gallery_fragment_extra_info_address.setText("拍摄地点: " + Utils.isNull(sunObject.getAddress()).replace("null", ""));
        pf_gallery_fragment_extra_info_human.setText("上传人: " + Utils.isNull(sunObject.getCreate_user()));
    }

    private void showAssessList() {
        if (assessView == null) {
            assessView = View.inflate(getActivity(), R.layout.pf_common_show_assess_layout, null);
            pf_common_show_asseess_send = (LinearLayout) assessView.findViewById(R.id.pf_common_show_asseess_send);
            pf_common_show_asseess_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomShow();
                }
            });
            pf_common_show_assess_title = (TextView) assessView.findViewById(R.id.pf_common_show_assess_title);
            assessListView = (PullToRefreshListView) assessView.findViewById(R.id.pulltorefresh_list);
            assessListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            assessListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNo++;
                    queSingleAssess();
                }
            });
            pf_common_show_assess_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popAssessWindow.dismiss();
                }
            });
            pfCommonAssessAdapter = new PfCommonAssessAdapter(getActivity());
            pfCommonAssessAdapter.setDeleteDataListener(new PfCommonAssessAdapter.DeleteAssessItemListener() {
                @Override
                public void deleteData(PfSingleAssessObject object) {
                    deleteReply(object);
                }
            });
            pfCommonAssessAdapter.setBottomListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomShow();
                }
            });
            assessListView.setAdapter(pfCommonAssessAdapter);
            //初始化底部评论模块
            emot2 = new ViewEmot2(getActivity(), new SendMessage() {
                @Override
                public void send(String message) {
                    sendReply(message);
                }
            });

            bottom_assess.addView(emot2);
        }
        pfCommonAssessAdapter.setObjectList(assessObjectList);

        //指定显示高度
        int height = WindowUtils.dm.heightPixels / 2;
        popAssessWindow = new PopupWindow(assessView, ViewGroup.LayoutParams.MATCH_PARENT, height);
        Utils.setPopWindow(popAssessWindow);
        popAssessWindow.showAsDropDown(pf_common_show_asseess_send);
    }

    private void deleteReply(final PfSingleAssessObject object) {
        UserRequest.deleteCommonReply(getActivity(), "21", object.getUuid(), new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("删除成功!");
                assessObjectList.remove(object);
                pfCommonAssessAdapter.setObjectList(assessObjectList);
                initBottomAssessCount();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }
        });

    }


    private void showPic() {
        if (sunObject != null) {
            String lishipath = sunObject.getPath();
            if (!TextUtils.isEmpty(lishipath)) {
                if (lishipath.contains("@")) {
                    lishipath = lishipath.substring(0, lishipath.indexOf("@"));
                }
            }
            pf_gallery_image.setTag(lishipath);
            singlePicModel.getBitmap(lishipath, sunObject, pf_gallery_image, mHanlder, new SingleLoadPicModel.LoadSuccessed() {
                @Override
                public void loadSuccess() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    }


    private void queSingleAssess() {
        showDia();
        UserRequest.getPfSingleAssess(getActivity(), pageNo, GloablUtils.MODE_OF_PF, sunObject.getUuid(), maxTime,
                new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        cancleDia();
                        if (pf_single_scroll.isRefreshing()) {
                            pf_single_scroll.onRefreshComplete();
                        }
                        if (assessListView != null && assessListView.isRefreshing())
                            assessListView.onRefreshComplete();
                        PfSingleAssess pfSingleAssess = (PfSingleAssess) domain;
                        if (pfSingleAssess != null && pfSingleAssess.getList() != null
                                && pfSingleAssess.getList().getData() != null
                                && pfSingleAssess.getList().getData().size() > 0) {
                            if (pageNo == 1) assessObjectList.clear();
                            assessObjectList.addAll(pfSingleAssess.getList().getData());
                            initBottomAssessCount();
                            if (pfCommonAssessAdapter != null) {
                                pfCommonAssessAdapter.setObjectList(assessObjectList);
                            }
                        } else {
                            if (pageNo == 1) {
                                addNothing();
                            } else {
                                ToastUtils.showMessage("没有更多内容了!");
                            }
                            if (assessListView != null)
                                assessListView.setMode(PullToRefreshBase.Mode.DISABLED);
                            pf_single_scroll.setMode(PullToRefreshBase.Mode.DISABLED);
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

    private void addNothing() {

    }

    private void addAssess(List<PfSingleAssessObject> data) {
        for (PfSingleAssessObject object : data) {

            View assessView = View.inflate(getActivity(), R.layout.pf_info_aeesee_item, null);
            TextView tv_name = (TextView) assessView.findViewById(R.id.pf_info_name);
            TextView tv_content = (TextView) assessView.findViewById(R.id.pf_info_content);
            TextView tv_time = (TextView) assessView.findViewById(R.id.pf_info_time);
            CircleImage circleImage = (CircleImage) assessView.findViewById(R.id.pf_info_image);

            if (object != null) {
                tv_name.setText("" + Utils.isNull(object.getCreate_user()));
                tv_content.setText(EmotUtil.getEmotionContent(getActivity(), Utils.isNull(object.getContent())));
                tv_time.setText("" + Utils.isNull(object.getCreate_time()));
                ImageLoaderUtil.displayImage(object.getCreate_img(), circleImage);
            } else {

            }
            pf_single_scroll.addView(assessView, 0, null);
        }
    }

    private void queryItemNewInfo(String uuid) {
        showDia();
        UserRequest.getSinglePfInfo(getActivity(), uuid, new RequestFailedResult(((BaseActivity) getActivity()).getCommonDialog()) {
            @Override
            public void result(BaseModel domain) {
                cancleDia();
                SingleNewInfo singleNewInfo = (SingleNewInfo) domain;
                if (singleNewInfo != null) {
                    sunObject = singleNewInfo.getData();
                    sunObject.setStatus(0);
                    family_uuid_object.update(sunObject);
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }
        });
    }

    private void queryExtraData(String uuid) {
        showDia();
        UserRequest.getSinglePfExtraInfo(getActivity(), uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDia();
                singleInfo = (SinlePfExtraInfo) domain;
                if (singleInfo != null) {
                    //初始化底部按钮显示状态
                    initCollect();
                    initDianZan();
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

    private void initDianZan() {
        PfInfoDianZan pfDianzan = singleInfo.getDianZanNameList();
        if (dianZanList == null && pfDianzan != null &&
                pfDianzan.getData() != null && pfDianzan.getData().size() > 0)
            dianZanList = pfDianzan.getData();
        if (dianZanList != null && dianZanList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            int size = dianZanList.size();
            for (int cou = 0; cou < size; cou++) {
                PfInfoDianZanObj dian = dianZanList.get(cou);
                if (cou != size - 1) {
                    builder.append(Utils.isNull(dian.getUsername()) + ",");
                } else {
                    builder.append(Utils.isNull(dian.getUsername()));
                }
            }
            String text = "<font color='#ff4966'>" + builder.toString() + "</font>" + size + "人觉得很赞";
            pf_fragment_extra_dianzan_count.setText(Html.fromHtml(text));
        } else {
            pf_fragment_extra_dianzan_count.setText("0人点过赞");
        }
    }

    private void initCollect() {
        //可以收藏
        if (singleInfo.getStatus() == 2) {
            deleteData(sunObject);
            return;
        }
        if (singleInfo.isFavor()) {
            Utils.cancleStoreStatus(getActivity(), textViews[0]);
        } else {
            Utils.showStoreStatus(getActivity(), textViews[0]);
        }

        PfDianZan dianZan = singleInfo.getDianZan();
        if (dianZan != null) {
            //可以点赞
            if (dianZan.getYidianzan() == 0) {
                Utils.cancleDizanStatus(getActivity(), textViews[2]);
            } else {
                Utils.showDianzanStatus(getActivity(), textViews[2]);
            }
        }


        mHanlder.sendEmptyMessage(UPDATE_ASSESS_COUNT);
    }

    private void initBottomBt() {
        bottom_assess = (LinearLayout) innerView.findViewById(R.id.pf_bottom_assess_linear);
        frame_bottom_tab = (FrameLayout) innerView.findViewById(R.id.bottom_tab);
        textViews = new TextView[]{
                (TextView) innerView.findViewById(R.id.pf_bottom_collect),
                (TextView) innerView.findViewById(R.id.pf_bottom_assess),
                (TextView) innerView.findViewById(R.id.pf_bottom_dianzan),
                (TextView) innerView.findViewById(R.id.pf_bottom_share),
                (TextView) innerView.findViewById(R.id.pf_bottom_more),
        };

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunObject == null) return;
                switch (v.getId()) {
                    case R.id.pf_bottom_collect:
                        String text = textViews[0].getText().toString();
                        if (text.equals("收藏")) {
                            //进行收藏
                            store();
                        } else {
                            //取消收藏
                            cacleStore();
                        }
                        break;
                    case R.id.pf_bottom_assess:
                        showAssessList();
//                        beginAssess();
                        break;
                    case R.id.pf_bottom_share:
                        String note = sunObject.getNote();
                        ShareUtils.showShareDialog(getActivity(), textViews[3], note, note, sunObject.getPath(), sunObject.getPath(), false);
                        break;
                    case R.id.pf_bottom_dianzan:
                        String dianzanText = textViews[2].getText().toString();
                        if (dianzanText.equals("点赞")) {
                            dianzan();
                        } else {
                            cancleDianzan();
                        }
                        break;
                    case R.id.pf_bottom_more:
                        showMore();
                        break;
                }
            }
        };

        for (TextView textView : textViews) {
            textView.setOnClickListener(listener);
        }
    }

    public void cancleDianzan() {
        showDialog("取消点赞中...");
        UserRequest.commonCancleZan(getActivity(), sunObject.getUuid(), GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
                Utils.cancleDizanStatus(getActivity(), textViews[2]);
                ToastUtils.showMessage("取消点赞成功");
                PfInfoDianZanObj obj = new PfInfoDianZanObj(CGApplication.getInstance().getLogin().getUserinfo().getUuid());
                if (dianZanList == null) dianZanList = new ArrayList<>();
                if (dianZanList.contains(obj)) {
                    dianZanList.remove(obj);
                    initDianZan();
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

    private void dianzan() {
        showDialog("点赞中...");
        UserRequest.commonZan(getActivity(), sunObject.getUuid(), GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("点赞成功");
                Utils.showDianzanStatus(getActivity(), textViews[2]);
                cancleDialog();
                PfInfoDianZanObj obj = new PfInfoDianZanObj();
                obj.setUsername(CGApplication.getInstance().getLogin().getUserinfo().getName());
                obj.setUseruuid(CGApplication.getInstance().getLogin().getUserinfo().getUuid());
                if (dianZanList == null) dianZanList = new ArrayList<>();
                dianZanList.add(obj);
                initDianZan();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void beginAssess() {
        bottomShow();
    }

    public void bottomShow() {
        if (popAssessWindow.isShowing()) {
            popAssessWindow.dismiss();
        }
        mHanlder.sendEmptyMessageDelayed(POP_DELAY_300, 300);
    }

    public void bottomCancle() {
        if (emot2 == null) return;
        onlyCancleBottom();
        showAssessList();
    }

    public boolean bottomIsShow() {
        CGLog.v("打印bottomIsShow : " + bottom_assess.getVisibility());
        return bottom_assess.getVisibility() == View.VISIBLE;
    }

    public void onlyCancleBottom() {
        if (emot2 == null) return;
        emot2.hideSoftKeyboard();
        bottom_assess.setVisibility(View.GONE);
        pf_pic_bottom_viewGroup.setVisibility(View.VISIBLE);
    }

    private void sendReply(final String message) {
        showDialog("评论回复中...");
        Utils.commonSendReply(getActivity(), sunObject.getUuid(), sunObject.getCreate_useruuid(), "", message, GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
                ToastUtils.showMessage("评论回复成功!");
                queSingleAssess();
                bottomCancle();
                emot2.cleanEditText();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void initBottomAssessCount() {
        if (assessObjectList.size() <= 0) {
            pf_pic_bottom_assess_count.setVisibility(View.GONE);
        } else {
            pf_pic_bottom_assess_count.setText("" + assessObjectList.size());
            pf_pic_bottom_assess_count.setVisibility(View.VISIBLE);
        }
    }

    private void cacleStore() {
        showDialog("取消收藏中...");
        Utils.picCommonCancleStore(getActivity(), sunObject.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
                ToastUtils.showMessage("取消收藏成功");
                Utils.cancleStoreStatus(getActivity(), textViews[0]);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void store() {
        showDialog("收藏中...");
        String title = sunObject.getNote();
        if (TextUtils.isEmpty(title)) {
            title = "这是一张照片!";
        }
        Utils.picCommonStore(getActivity(), sunObject.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
                ToastUtils.showMessage("收藏成功");
                Utils.showStoreStatus(getActivity(), textViews[0]);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    public void showDialog(String text) {
        if (TextUtils.isEmpty(text)) {
            text = "加载数据中...请稍候";
        }
        dialog.show();
        dialog.setText(text);
    }

    public void cancleDialog() {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
    }

    private void showMore() {
        if (pf_more_view == null) {
            pf_more_view = View.inflate(getActivity(), R.layout.pf_single_more, null);
            pf_delete = (ImageView) pf_more_view.findViewById(R.id.pf_single_delete);
            pf_edit = (ImageView) pf_more_view.findViewById(R.id.pf_single_edit);
        }

        final PopupWindow popupWindow = new PopupWindow(pf_more_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Utils.setPopWindow(popupWindow, R.style.ShareAnimTopAndBottom);
        pf_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ToastUtils.showDialog(getActivity(), "提示!", "您确认删除此图片吗?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            popupWindow.dismiss();
                            deleteData(sunObject);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pf_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                ((PfGalleryActivity) getActivity()).startEditActivity(sunObject);
            }
        });
        int[] location = Utils.getLocation(textViews[4]);
        pf_more_view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        CGLog.v("打印测量的高度 　：" + pf_more_view.getMeasuredHeight() + " 宽度 : " + pf_more_view.getMeasuredWidth());
        pf_more_view.getMeasuredHeight();
        popupWindow.showAtLocation(textViews[4], Gravity.NO_GRAVITY, location[0], location[1] - pf_more_view.getMeasuredHeight());
    }

    private void deleteData(AllPfAlbumSunObject sunObject) {
        //从数据库，网络，轮播图中删除
        pfSingleInfoFragment.deleteCurrentItem(sunObject);
    }

    public void showRightInfo() {
        View view = View.inflate(getActivity(), R.layout.pf_info_four_message, null);
        TextView pf_info_careme_time = (TextView) view.findViewById(R.id.pf_info_careme_time);
        TextView pf_info_location = (TextView) view.findViewById(R.id.pf_info_location);
        TextView pf_info_device = (TextView) view.findViewById(R.id.pf_info_device);
        TextView pf_info_upload_people = (TextView) view.findViewById(R.id.pf_info_upload_people);
        pf_info_careme_time.append("" + Utils.isNull(sunObject.getCreate_time()));
        pf_info_location.append("" + Utils.isNull(sunObject.getAddress()));
        pf_info_device.append("" + Utils.isNull(sunObject.getCreate_useruuid()));
        pf_info_upload_people.append("" + Utils.isNull(sunObject.getCreate_useruuid()));
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Utils.setPopWindow(popupWindow);
        popupWindow.showAsDropDown(normal_title_right_icon);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    //构建接口和activity进行通信
}

