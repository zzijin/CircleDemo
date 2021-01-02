package com.circle.information;

import com.circle.UserInform;
import com.circle.socketclient.FileData;

import java.io.File;
import java.util.ArrayList;

public class WaitQueueChat {
    public int uid;
    public int toUid;
    public String strData;
    public int index;
    public int chatNumber;
    public ArrayList<String> servicePath;
    public ArrayList<FileData> data;
    public String sendTime;
    public ArrayList<String> MyPath;

    public WaitQueueChat(int t,String str,ArrayList<String> myPath,int type){
        servicePath=new ArrayList<>();
        myPath=new ArrayList<>();
        data=new ArrayList<>();
        uid= UserInform.getuId();
        toUid=t;
        strData=str;
        MyPath=myPath;
        for(int o=0;o<myPath.size();o++){
            data.add(new FileData(new File(myPath.get(o)),type));
        }
    }
}