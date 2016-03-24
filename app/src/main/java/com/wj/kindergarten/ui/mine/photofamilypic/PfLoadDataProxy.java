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
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.bean.UUIDList;
import com.wj.kindergarten.bean.UUIDListSunObj;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private DataLoadFinish dataLoadFinish;
    private String QUERY_CLOUMN = "photo_time";

    public void setQUERY_CLOUMN(String QUERY_CLOUMN) {
        this.QUERY_CLOUMN = QUERY_CLOUMN;
    }

    public void setDataLoadFinish(DataLoadFinish dataLoadFinish) {
        this.dataLoadFinish = dataLoadFinish;
    }

    private Handler handler;
    private FinalDb familyUuidSql;
    private FinalDb familyUuidObjectSql;
    private Context context;
    private ThreadPoolExecutor threadPoolExecutor;
    private LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    public static final int REFRESH_DATA = 3060;
    public static final int NORMAL_DATA = 3061;
    private PfFamilyUuid pfFamilyUuid;

    public PfLoadDataProxy(Context context, Handler handler) {
        familyUuidSql = FinalDb.create(context, GloablUtils.FAMILY_UUID, true);
        familyUuidObjectSql = FinalDb.create(context, GloablUtils.FAMILY_UUID_OBJECT, true);
        this.context = context;
        this.handler = handler;
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 60 * 60, TimeUnit.SECONDS, linkedBlockingQueue);
    }


    boolean queryChangeandUpdate;

    //根据家庭uuid查询所有照片数量,判断是否是上拉刷新，
    public void loadData(String familyUuid, int pageNo, boolean isPullup) {
        List<PfFamilyUuid> listT = familyUuidSql.findAll(PfFamilyUuid.class);//验证update是否生效
//        CGLog.v("打印存放集合 : " + listT.toString() + "集合大小 : " + listT.size());
        //通过familyuuid查找数据库对象
        pfFamilyUuid = familyUuidSql.findById(familyUuid, PfFamilyUuid.class);
        CGLog.v("打印对象　；" + pfFamilyUuid);
        String minTime = null;

        if (!isPullup) {
            //如果为空，则没有获取过数据,从网络获取 ; 有则直接从数据库取
            if (pfFamilyUuid == null) {
                pfFamilyUuid = new PfFamilyUuid();
                pfFamilyUuid.setFamily_uuid(familyUuid);
                pfFamilyUuid.setMaxTime(new Date());
                savePfFamilyUUid(pfFamilyUuid.getFamily_uuid());
            } else {
                //同时查询有没有新数据
                if(!queryChangeandUpdate) {
                    queryChangeandUpdate = true;
                    loadDataIsChange(pfFamilyUuid.getFamily_uuid());
                }
                loadFromSqliteByCreateTime();
                return;
            }
            loadPic(familyUuid, "", formatTime(pfFamilyUuid.getMaxTime()), "", pageNo, NORMAL_DATA);
        } else {
            loadPic(familyUuid, "", formatTime(pfFamilyUuid.getMinTime()), formatTime(pfFamilyUuid.getUpdateTime()), pageNo, NORMAL_DATA);
        }
    }

    private void loadPic(final String familyUuid, final String minTime, final String maxTime, final String updateNewCountTime, int pageNo, final int type) {
        UserRequest.getPfPicByUuid(context, familyUuid, minTime, maxTime, updateNewCountTime, pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                AllPfAlbum allPfAlbum = (AllPfAlbum) domain;
                if (dataLoadFinish != null) dataLoadFinish.finish();
                if (allPfAlbum == null || allPfAlbum.getList() == null || allPfAlbum.getList().getData() == null ||
                        allPfAlbum.getList().getData().size() == 0) {
                    if(type == NORMAL_DATA){
                        ToastUtils.showMessage("没有更多内容了");
                    }else if(type == REFRESH_DATA){
                        ToastUtils.showMessage("暂无新数据！");
                    }
                    dataLoadFinish.noMoreData();
                    return;
                }
                //如果是查询指定时间后的数据，maxtime为空，获取到的lasttime赋值给maxtime
                if (TextUtils.isEmpty(maxTime)) {
                    pfFamilyUuid.setMaxTime(TimeUtil.formatDate(Utils.isNull(allPfAlbum.getLastTime())));
                }
                //如果是查询指定时间前的数据,mintime为空，获取的时间赋值给mintime
                if (TextUtils.isEmpty(minTime)) {
                    pfFamilyUuid.setMinTime(TimeUtil.formatDate(Utils.isNull(allPfAlbum.getLastTime())));
                }
                pfFamilyUuid.setUpdateTime(pfFamilyUuid.getMaxTime());
                //说明是第一次查询,保存到数据库
                savePfFamilyUUid(familyUuid);
                if (allPfAlbum.getList().getData() == null
                        || allPfAlbum.getList().getData().size() <= 0) return;
                //如果请求类型是最新的数据，那么则通知主面板进行刷新。
                //把获取到的数据保存到数据库中
                for (AllPfAlbumSunObject object : allPfAlbum.getList().getData()) {
                    AllPfAlbumSunObject object1 = familyUuidObjectSql.findById(object.getUuid(), AllPfAlbumSunObject.class);
                    if (object1 == null) {
                        familyUuidObjectSql.save(object);
                    }
                }
                //在获取到maxtime和mintime的值之后，查询有没有更新
                //在获取到新数据之后，保存到数据库中后再从数据库查出来取出来。
                loadFromSqliteByCreateTime();
                if(!queryChangeandUpdate){
                    loadDataIsChange(familyUuid);
                    queryChangeandUpdate = true;
                }
                //判断类型是否是请求的增量更新数据
                if (type == REFRESH_DATA) {
                    handler.sendEmptyMessage(REFRESH_DATA);
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

    private void savePfFamilyUUid(String familyUuid) {
        if (familyUuidSql.findById(familyUuid, PfFamilyUuid.class) == null) {
            familyUuidSql.save(pfFamilyUuid);
        } else {
            familyUuidSql.update(pfFamilyUuid);
        }
    }

    private void loadFromSqliteByPhotoTime(){
        loadFromDataBases("photo_time");
    }



    private void loadFromSqliteByCreateTime() {
        //根据family_uuid把对象根据日期分组，显示多少组及其数量
//        List<AllPfAlbumSunObject> listTest = familyUuidObjectSql.findAll(AllPfAlbumSunObject.class);
        loadFromDataBases(QUERY_CLOUMN);
    }

    private void loadFromDataBases(String timeType) {
        String sql = "SELECT strftime('%Y-%m-%d',"+timeType+"),count(1) from " + "com_wj_kindergarten_bean_AllPfAlbumSunObject" + "  WHERE family_uuid ='" + pfFamilyUuid.getFamily_uuid() +"'"
+ "GROUP BY strftime('%Y-%m-%d',"+timeType+");";
        if (pfFamilyUuid.getMaxTime() == null &&
                pfFamilyUuid.getMinTime() == null) return;
        List<QueryGroupCount> dateArray = new ArrayList<>();
        List<DbModel> dbList = familyUuidObjectSql.findDbModelListBySQL(sql);
        for (DbModel model : dbList) {
            QueryGroupCount count = new QueryGroupCount();
            String date = (String) model.getDataMap().get("strftime('%Y-%m-%d',"+timeType+")");
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
                int cha = 0;
                long o = TimeUtil.getMillionFromYMD(one.getDate());
                long t = TimeUtil.getMillionFromYMD(two.getDate());
                if(t - o >= 0){
                    cha = 1;
                }else {
                    cha = -1;
                }
                return cha;
            }
        });
        Message message = new Message();
        message.what = NORMAL_DATA;
        message.obj = dateArray;
        handler.sendMessage(message);
    }

    //根据最小和最大时间查询是否有新数据或者是数据变更。
    private void loadDataIsChange(final String family_uuid) {
        //如果有一个时间为空，说明是第一次加载，不需要更新
        if (judgeIsLoaded()) return;
        queryDataByChangeCount(family_uuid, new Date());
        queryIncrementNewData(family_uuid);

    }

    public final void queryIncrementNewData(final String family_uuid, final DataLoadFinish dataLoadFinish){
        pfFamilyUuid =  familyUuidSql.findById(family_uuid, PfFamilyUuid.class);
        UserRequest.queryIncrementNewData(context, pfFamilyUuid.getFamily_uuid(), TimeUtil.getStringDate(pfFamilyUuid.getMaxTime()), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if(dataLoadFinish != null) dataLoadFinish.finish();
                PfChangeData pfChangeData = (PfChangeData) domain;
                if (pfChangeData == null || pfChangeData.getNewDataCount() == 0){
                    if(dataLoadFinish != null)
                    dataLoadFinish.noMoreData();
                    return;
                }
                if (pfChangeData.getNewDataCount() > 0) {
                    loadPic(family_uuid, formatTime(pfFamilyUuid.getMaxTime()), "", "", 1, REFRESH_DATA);
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


    public final void queryIncrementNewData(final String family_uuid) {
        queryIncrementNewData(family_uuid,null);
    }

    private String formatTime(Date date) {
        if(date == null)return null;
        return TimeUtil.getStringDate(date);
    }

    private boolean judgeIsLoaded() {
        if (pfFamilyUuid == null) return true;
        if (pfFamilyUuid.getMaxTime() == null || pfFamilyUuid.getMinTime() == null)
            return true;
        return false;
    }

    //根据上面方法查询到的变化的数量如果大于0，则调用这个接口
    private void queryDataByChangeCount(String family_uuid, final Date date) {
        if (judgeIsLoaded()) return;
        UserRequest.getUUIDListByUpdate(context, family_uuid, TimeUtil.getStringDate(pfFamilyUuid.getMinTime()), TimeUtil.getStringDate(pfFamilyUuid.getMaxTime()), TimeUtil.getStringDate(date), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                final UUIDList uuidList = (UUIDList) domain;
                pfFamilyUuid.setUpdateTime(date);
                familyUuidSql.update(pfFamilyUuid);
                if (uuidList == null || uuidList.getList() == null || uuidList.getList().getData() == null
                        || uuidList.getList().getData().size() == 0) return;
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<UUIDListSunObj> listSunObjs = uuidList.getList().getData();
                        Iterator<UUIDListSunObj> iterator = listSunObjs.iterator();
                        while (iterator.hasNext()) {
                            UUIDListSunObj obj = iterator.next();
                            if (obj == null) continue;
                            if (TextUtils.isEmpty(obj.getU())) continue;
                            CGLog.v("打印obj : " + obj);
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

    public List<AllPfAlbumSunObject> queryListByDate(String family_uuid, String date,String limitCount){
//        "limit 6"

        String sql = " strftime('%Y-%m-%d',"+QUERY_CLOUMN+") ='" + date + "' and family_uuid ='" + family_uuid + "' " + limitCount;
        List<AllPfAlbumSunObject> objectList = familyUuidObjectSql.findAllByWhere(AllPfAlbumSunObject.class, sql);
//        String count = "select count(*) from com_wj_kindergarten_bean_AllPfAlbumSunObject where " +
//                " strftime('%Y-%m-%d',"+QUERY_CLOUMN+") ='" + date + "' and family_uuid ='" + family_uuid + "' ";
        return objectList;
    }

    public List<AllPfAlbumSunObject> queryListByDate(String family_uuid, String date) {
       return queryListByDate(family_uuid,date,"");
    }


    public interface DataLoadFinish {
        void finish();

        void noMoreData();
    }
}
