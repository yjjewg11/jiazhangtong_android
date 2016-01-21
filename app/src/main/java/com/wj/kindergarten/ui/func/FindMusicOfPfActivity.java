package com.wj.kindergarten.ui.func;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfMusic;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.ShowMusicAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/21.
 */
public class FindMusicOfPfActivity extends BaseActivity {
    private PullToRefreshListView listView;
    private ShowMusicAdapter adapter;
    List<PfMusic> pfMusics = new ArrayList<PfMusic>();
    private static final int GET_DATA_SUCCESSED = 3006;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESSED:
                    adapter.setList(pfMusics);
                    break;
            }
        }
    };

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.find_music_of_pf;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        initViews();
        setTitleText("选择背景音乐", "保存");
        loadMusic();

    }

    @Override
    protected void titleRightButtonListener() {

    }

    private void initViews() {
        listView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_list);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        adapter = new ShowMusicAdapter(this);
        listView.setAdapter(adapter);

    }


    private void loadMusic() {
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);


                                        //新建一个歌曲对象,将从cursor里读出的信息存放进去,直到取完cursor里面的内容为止.
            cursor.moveToFirst();

            while (cursor.moveToNext()){
                PfMusic pfMusic = new PfMusic();

            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id

            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小

            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));  //文件路径

            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM)); //唱片图片

            long album_id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)); //唱片图片ID

            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

            if (isMusic != 0 && duration/(1000 * 30) >= 1) {     //只把1分钟以上的音乐添加到集合当中
//                mp3Info.setId(id);
                pfMusic.setTitle(title);
//                mp3Info.setArtist(artist);
//                mp3Info.setDuration(duration);
//                mp3Info.setSize(size);
                pfMusic.setPath(url);
//                mp3Info.setAlbum(album);
//                mp3Info.setAlbum_id(album_id);
                pfMusics.add(pfMusic);
            }
            }
        handler.sendEmptyMessage(GET_DATA_SUCCESSED);
    }

}
