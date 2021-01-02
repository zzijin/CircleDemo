package com.circle.information;

import android.graphics.Bitmap;

import com.circle.socketclient.FileData;
import com.circle.socketclient.convert.ConvertType;

public class Users {
    //昵称
    private String UserName;
    //UID
    private int Uid;
    //头像
    private FileData UserImage;
    //在线状态
    private boolean IsLineState;
    //详细信息
    private String Sign;
    private int watchSBNum;
    private int faceNum;
    private int dongTaiNum;

    public Users(int uid,String name,FileData image){
        Uid=uid;
        UserName=name;
        UserImage=image;
    }

    public Bitmap getUserImage(){
        return ConvertType.BytesToBitmap(UserImage.getData());
    }
    public boolean getUserImageState(){
        if(UserImage!=null) {
            return UserImage.getDataState();
        }
        else {
            return false;
        }
    }
    public void addUserImageData(byte[] data){
        UserImage.AddBytesToData(data);
    }

    public boolean getIsUid(int uid){
        return Uid==uid;
    }

    public int getUid() {
        return Uid;
    }
    public int getFaceNum() {
        return faceNum;
    }
    public int getWatchSBNum() {
        return watchSBNum;
    }
    public String getUserName() {
        return UserName;
    }
    public int getDongTaiNum() {
        return dongTaiNum;
    }
    public String getSign() {
        return Sign;
    }
}
