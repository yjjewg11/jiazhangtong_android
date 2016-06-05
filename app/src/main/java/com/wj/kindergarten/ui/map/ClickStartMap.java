package com.wj.kindergarten.ui.map;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by tangt on 2016/1/6.
 */
public final class ClickStartMap implements View.OnClickListener {

    private MapTransportObject mapTransportObject;
    private Context context;

    public ClickStartMap(Context context, MapTransportObject mapTransportObject) {
        this.context = context;
        this.mapTransportObject = mapTransportObject;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("map_transport_object", mapTransportObject);
        context.startActivity(intent);
    }
}
