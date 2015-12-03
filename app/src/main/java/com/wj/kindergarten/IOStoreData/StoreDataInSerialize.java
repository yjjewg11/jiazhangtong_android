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

/**
 * Created by tangt on 2015/12/3.
 */
public abstract class StoreDataInSerialize {
   static File file = new File(CGApplication.context.getFilesDir().toString()+File.separator+GloablUtils.CACHE_DATA);
    public static boolean storeUserInfo(Login login) {

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

        return true;
    }

    public static Login getUserInfo(){
        Login login = null;
        try {
            File wenjian = new File(file, GloablUtils.CACHE_USER_INFO);
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(wenjian));
            login = (Login)input.readObject();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return login;
    }
}
