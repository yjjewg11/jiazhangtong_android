package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;

import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.GloablUtils;

import net.tsz.afinal.FinalDb;

/**
 * Created by tangt on 2016/1/22.
 */
public class PfLoadDataProxy {
    private FinalDb familyUuidSql;
    private  FinalDb familyUuidObjectSql;
    private Context context;
    private String maxTime;
    private String minTime;


    public PfLoadDataProxy(Context context){
        familyUuidSql = FinalDb.create(context, GloablUtils.FAMILY_UUID,true);
        familyUuidObjectSql = FinalDb.create(context,GloablUtils.FAMILY_UUID_OBJECT,true);
        this.context = context;
    }

    public void loadFromHttp(String familyUuid,int pageNo,RequestResultI resultI){

        UserRequest.getPfPicByUuid(context,pageNo,resultI);
    }

    public void loadFromSqlite(String familyUuid){

    }

}
