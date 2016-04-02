package com.wj.kindergarten.IOStoreData;

import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.utils.GloablUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tangt on 2015/12/3.
 */
public abstract class StoreDataInSerialize {
   static File file = new File(CGApplication.context.getFilesDir().toString()+File.separator+GloablUtils.CACHE_DATA);
    public static boolean storeUserInfo(Login login) {
        synchronized (StoreDataInSerialize.class){
            if(!file.exists()){
                file.mkdirs();
            }
            File wenjian = new File(file,GloablUtils.CACHE_USER_INFO);
            if(!wenjian.exists()){
                try {
                    wenjian.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(wenjian));
                out.writeObject(login);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static Login getUserInfo(){
        Login login = null;
        synchronized (StoreDataInSerialize.class){
            try {
                File wenjian = new File(file, GloablUtils.CACHE_USER_INFO);
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(wenjian));
                login = (Login)input.readObject();
                input.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return login;
    }

    public static void saveAlreadyUploadPic(HashSet<String> set){
        File wenjian = new File(file,GloablUtils.SAVE_ALREADY_UPLOAD_PIC);
        if(!wenjian.exists()){
            try {
                wenjian.createNewFile();
            } catch (IOException e) {
            }
        }

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(wenjian,true));
            outputStream.writeObject(set);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAlreadyUploadPic (){
        File wenjian = new File(file,GloablUtils.SAVE_ALREADY_UPLOAD_PIC);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(wenjian));
            ArrayList<String> list = (ArrayList<String>) inputStream.readObject();
            inputStream.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
