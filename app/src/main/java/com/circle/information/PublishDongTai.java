package com.circle.information;

import com.circle.UserInform;
import com.circle.socketclient.FileData;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class PublishDongTai {
    public int UID;
    public int index;
    public int DongTaiNumber;
    public int Type;
    public String SendTime;
    public String Title;
    public byte[] Boby;
    public ArrayList<String> ServicePath;
    public ArrayList<FileData> Data;
    public ArrayList<String> MyPath;

    public PublishDongTai(int type,String title,ArrayList<String> path){
        ServicePath=new ArrayList<>();
        Data=new ArrayList<>();
        MyPath=new ArrayList<>();

        UID= UserInform.getuId();
        Type=type;
        Title=title;
        MyPath.addAll(path);
        for(int o=0;o<MyPath.size();o++){
            Data.add(new FileData(new File(MyPath.get(o)),type));
        }
    }
    public PublishDongTai(int type,String title,String text){
        ServicePath=new ArrayList<>();
        Data=new ArrayList<>();
        MyPath=new ArrayList<>();

        UID= UserInform.getuId();
        Type=type;
        Title=title;
        try {
            Boby=text.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}