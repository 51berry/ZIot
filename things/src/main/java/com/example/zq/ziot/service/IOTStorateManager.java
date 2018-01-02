package com.example.zq.ziot.service;


import android.text.TextUtils;

import java.io.File;

/**
 * Created by zq on 2017/12/28.
 */

public class IOTStorateManager {
    private static IOTStorateManager instance = null;
    public  static final String IMAGE_IDR = "image";
    private IOTStorateManager(){
        init();
    }
    public static synchronized IOTStorateManager getInstance(){
        if(instance == null){
            instance = new IOTStorateManager();
        }
        return instance;
    }

    private static final String rootPath = "/sdcard/IOT/";

    private void init(){
        File rootDir = new File(rootPath);
        if(!rootDir.exists()){
            rootDir.mkdir();
        }
        createNewFolder(IOTStorateManager.IMAGE_IDR);
    }

    public boolean createNewFolder(String path){
        if(TextUtils.isEmpty(path)){return false;}
        if(path.startsWith("/")){
            path = path.substring(1, path.length());
        }
        String abslutePath = rootPath + path;
        File folder = new File(abslutePath);
        if(folder.exists() && folder.isDirectory()){
            return true;
        }
        if(folder.exists() && folder.isFile()){
            folder.delete();
        }
        folder = new File(abslutePath);
        folder.mkdir();
        return true;
    }

    public File getFolder(String path){
        if(TextUtils.isEmpty(path)){return null;}
        if(path.startsWith("/")){
            path = path.substring(1, path.length());
        }
        String abslutePath = rootPath + path;
        File f = new File(abslutePath);
        return f.exists() && f.isDirectory() ? f : null;
    }

    public void clearFolder(String path){
        if(TextUtils.isEmpty(path)){return; }
        if(path.startsWith("/")){
            path = path.substring(1, path.length());
        }
        String abslutePath = rootPath + path;
        File folder = new File(abslutePath);
        if(folder.exists() && folder.isDirectory()){
            return ;
        }
    }


}
