package com.wj.kindergarten.ui.mine.photofamilypic.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/3/7.
 */
public class ObserverFamilyUuid {
    private List<Watcher> list = new ArrayList<>();
    public final void registerObserver(Watcher watcher){
        list.add(watcher);
    }
    public final void unRegisterObserver(Watcher watcher){
        list.remove(watcher);
    }
    private String family_uuid;

    public final void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
        notifyDataChanged();
    }

    private final void notifyDataChanged(){
        for(Watcher watcher : list){
            watcher.refreshUUid(family_uuid);
        }
    }

}
