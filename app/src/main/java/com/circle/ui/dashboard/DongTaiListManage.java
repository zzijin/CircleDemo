package com.circle.ui.dashboard;

import android.util.Log;

import com.circle.UsersListManage;
import com.circle.information.UserData;
import com.circle.socketclient.FileData;

import java.util.ArrayList;
import java.util.List;

public class DongTaiListManage {
    static private ArrayList<UserData> DongTaiList=new ArrayList<>();
    static public boolean isGetNewDongTai=false;

    public static  UserData getDongTai(int index){
        return DongTaiList.get(index);
    }
    public static  ArrayList<UserData> getDongTaiList() {
        return DongTaiList;
    }
    public static void addNewDongTaiInform(UserData userData){
        DongTaiList.add(userData);
    }
    public static int getDongTaiNum(){
        return DongTaiList.size();
    }

    public static boolean addDongTaiDataInform(int num,int index, FileData fileData){
        for(int i=0;i<DongTaiList.size();i++)
        {
            if(DongTaiList.get(i).DongTaiNumber==num)
            {
                DongTaiList.get(i).DongTaiData.addNewBobyInform(index,fileData);
                return true;
            }
        }
        return false;
    }

    public static int addDongTaiData(int num,int index,byte[] data){
        for(int i=0;i<DongTaiList.size();i++)
        {
            if(DongTaiList.get(i).DongTaiNumber==num)
            {
                DongTaiList.get(i).DongTaiData.addBobyData(index,data);
                return i;
            }
        }
        return -1;
    }

    public static boolean getDongTaiDataState(){
        if(DongTaiList.size()>0) {
            for(int i=0;i<DongTaiList.size();i++) {
                if(DongTaiList.get(i).DongTaiData==null) {
                    Log.i("检测加载状态","失败:动态内容"+i+"为空");
                    return false;
                }
                else {
                    if (!DongTaiList.get(i).DongTaiData.getDataState() || !UsersListManage.getUserImageState(DongTaiList.get(i).Uid)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return true;
    }

    public static int getDataBar(int index,int i){
        return DongTaiList.get(index).DongTaiData.getDataBar(i);
    }

    public static void addNewMyInform(UserData userData,FileData[] data){
        isGetNewDongTai=true;
        DongTaiList.add(0,userData);
        DongTaiList.get(0).DongTaiData.setBobyData(data);
    }

    public static int getLostNumber(){
        return DongTaiList.get(DongTaiList.size()-1).DongTaiNumber;
    }
}
