package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BoutiqueDianzanList;
import com.wj.kindergarten.bean.BoutiqueDianzanListObj;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * Created by tangt on 2016/3/10.
 */
public class BoutiqueDianzanAdapter extends BaseAdapter {
    private Context context;
    private List<BoutiqueDianzanListObj> listObjs;

    public BoutiqueDianzanAdapter(Context context, List<BoutiqueDianzanListObj> listObjs) {
        this.context = context;
        this.listObjs = listObjs;
    }




    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.boutique_item_dianzan,null);
        TextView tv = (TextView) view.findViewById(R.id.boutique_item_dianzan_tv);
//        initList(tv);
        return view;
    }


}
