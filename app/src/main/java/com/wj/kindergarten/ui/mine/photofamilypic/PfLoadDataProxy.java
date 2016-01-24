package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfChangeData;
import com.wj.kindergarten.bean.PfFamilyUuid;
import com.wj.kindergarten.bean.UUIDList;
import com.wj.kindergarten.bean.UUIDListSunObj;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tangt on 2016/1/22.
 */
public class PfLoadDataProxy {
    private Handler handler;
    private FinalDb familyUuidSql;
    private FinalDb familyUuidObjectSql;
    private Context context;
    private PfFamilyUuid pfFamilyUuid;
    private ThreadPoolExecutor threadPoolExecutor;
    private LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    public static final int REFRESH_DATA = 3060;
    public static final int NORMAL_DATA = 3061;

    public PfLoadDataProxy(Context context, Handler handler) {
        familyUuidSql = FinalDb.create(context, GloablUtils.FAMILY_UUID, true);
        familyUuidObjectSql = FinalDb.create(context, GloablUtils.FAMILY_UUID_OBJECT, true);
        this.context = context;
        this.handler = handler;
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60 * 60, TimeUnit.SECONDS, linkedBlockingQueue);
    }

    //根据家庭uuid查询所有照片数量,判断是否是上拉刷新，
    public void loadData(String familyUuid, int pageNo, boolean isPullup) {
        //通过familyuuid查找数据库对象
        pfFamilyUuid = familyUuidSql.findById(familyUuid, PfFamilyUuid.class);
        String minTime = null;

        if (!isPullup) {
            //如果为空，则没有获取过数据,从网络获取 ; 有则直接从数据库取
            if (pfFamilyUuid == null) {
                pfFamilyUuid = new PfFamilyUuid();
            } else {
                loadFromSqlite(pfFamilyUuid);
                return;
            }
            pfFamilyUuid.setMaxTime(new Date().toString());
            loadPic(familyUuid, "", pfFamilyUuid.getMaxTime(), pageNo, NORMAL_DATA);
        } else {
            loadPic(familyUuid, "", pfFamilyUuid.getMinTime(), pageNo, NORMAL_DATA);
        }

    }

    private void loadPic(final String familyUuid, final String minTime, final String maxTime, int pageNo, final int type) {
        UserRequest.getPfPicByUuid(context, familyUuid, minTime, maxTime, pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                AllPfAlbum allPfAlbum = (AllPfAlbum) domain;
                if (allPfAlbum == null || allPfAlbum.getList() == null) return;
                //如果是查询指定时间后的数据，maxtime为空，获取到的lasttime赋值给maxtime
                if (TextUtils.isEmpty(maxTime)) {
                    pfFamilyUuid.setMaxTime(Utils.isNull(allPfAlbum.getLastTime()));
                }
                //如果是查询指定时间前的数据,mintime为空，获取的时间赋值给mintime
                if (TextUtils.isEmpty(minTime)) {
                    pfFamilyUuid.setMinTime(Utils.isNull(allPfAlbum.getLastTime()));
                }
                //说明是第一次查询,保存到数据库
                if (familyUuidSql.findById(familyUuid, PfFamilyUuid.class) == null) {
//                    familyUuidSql.f
                    familyUuidSql.save(pfFamilyUuid);
                } else {
                    familyUuidSql.update(pfFamilyUuid);
                }
                if (allPfAlbum.getList().getData() == null
                        || allPfAlbum.getList().getData().size() <= 0) return;
                //如果请求类型是最新的数据，那么则通知主面板进行刷新。

                Message message = new Message();
                message.obj = allPfAlbum.getList().getData();
                if (type == REFRESH_DATA) {
                    message.what = REFRESH_DATA;
                } else if (type == NORMAL_DATA) {
                    message.what = NORMAL_DATA;
                }
                handler.sendMessage(message);
                //在获取到maxtime和mintime的值之后，查询有没有更新
                loadDataIsChange(familyUuid);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void loadFromSqlite(PfFamilyUuid pfFamilyUuid) {
        if (TextUtils.isEmpty(pfFamilyUuid.getMaxTime()) ||
                TextUtils.isEmpty(pfFamilyUuid.getMinTime())) return;
        List<AllPfAlbumSunObject> list = familyUuidObjectSql.findAllByWhere(AllPfAlbumSunObject.class, "family_uuid = " + pfFamilyUuid.getFamily_uuid());
        Message message = new Message();
        message.what = PfFusionFragment.PF_GET_ALBUM_DATA_SUCCESS;
        message.obj = list;
        handler.sendMessage(message);
    }

    //根据最小和最大时间查询是否有新数据或者是数据变更。
    private void loadDataIsChange(final String family_uuid) {
        //如果有一个时间为空，说明是第一次加载，不需要更新
        if (judgeIsLoaded()) return;
        UserRequest.getPfDataIsChange(context, family_uuid, pfFamilyUuid.getMinTime(), pfFamilyUuid.getMaxTime(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                PfChangeData pfChangeData = (PfChangeData) domain;
                if (pfChangeData == null) return;
                if (pfChangeData.getNewDataCount() > 0) {
                    loadPic(family_uuid, pfFamilyUuid.getMaxTime(), "", 1, REFRESH_DATA);
                }
                if (pfChangeData.getUpdateDataCount() > 0) {
                    queryDataByUpdate(family_uuid);
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

    private boolean judgeIsLoaded() {
        if (pfFamilyUuid == null) return true;
        if (TextUtils.isEmpty(pfFamilyUuid.getMaxTime()) || TextUtils.isEmpty(pfFamilyUuid.getMinTime()))
            return true;
        return false;
    }

    //根据上面方法查询到的变化的数量如果大于0，则调用这个接口
    private void queryDataByUpdate(String family_uuid) {
        if (judgeIsLoaded()) return;
        UserRequest.getUUIDListByUpdate(context, family_uuid, pfFamilyUuid.getMinTime(), pfFamilyUuid.getMaxTime(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                final UUIDList uuidList = (UUIDList) domain;
                if (uuidList == null || uuidList.getList() == null || uuidList.getList().getData() == null
                        || uuidList.getList().getData().size() == 0) return;
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<UUIDListSunObj> listSunObjs = uuidList.getList().getData();
                        Iterator<UUIDListSunObj> iterator = listSunObjs.iterator();
                        while (iterator.hasNext()) {
                            UUIDListSunObj obj = iterator.next();
                            AllPfAlbumSunObject objSql = familyUuidObjectSql.findById(obj.getU(), AllPfAlbumSunObject.class);
                            if (objSql == null) continue;
                            objSql.setStatus(obj.getS());
                            familyUuidObjectSql.update(objSql);
                        }
                    }
                });

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    public void onDestory() {
        threadPoolExecutor.shutdownNow();
    }

}
