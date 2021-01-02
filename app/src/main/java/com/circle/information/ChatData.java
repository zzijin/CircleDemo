package com.circle.information;

import android.provider.ContactsContract;
import android.util.Log;

import com.circle.UserInform;
import com.circle.socketclient.FileData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatData {
    public int UID;
    public int Number;
    public int ToUID;
    public String SendTime;
    //用户是否为发送方
    public boolean IsSender;
    //消息是否被阅读过
    public boolean SeeState;
    //图片内容
    public FileData[] Images;
    //文字内容
    public String StrData;

    public ChatData(int number,int uid,int toUID,String strData,String sendTime,boolean seeState,boolean isSender,int imageSize){
        UID=uid;
        ToUID=toUID;
        IsSender=isSender;
        Number=number;
        SendTime=sendTime;
        SeeState=seeState;
        StrData=strData;
        Images=new FileData[imageSize];
    }

    public ChatData(int number,int uid,int toUid,String sendTime,ArrayList<String> myPath,String str){
        UID=uid;
        Number=number;
        ToUID=toUid;
        SendTime=sendTime;
        IsSender=true;
        SeeState=true;
        StrData=str;
        Images=new FileData[myPath.size()];

        for(int i=0;i<myPath.size();i++){
            String path=myPath.get(i);
            FileInputStream fileInputStream= null;
            try {
                fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
                int DataSize = objectInputStream.read();
                byte[] data=new byte[DataSize];
                objectInputStream.read(data,0,data.length);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void addImageInform(int index,FileData newData){
        Images[index]=newData;
    }

    /**
     * 查看哪些数据完整
     * @return
     */
    public ArrayList<Integer> getImageState(){
        ArrayList<Integer> integers=new ArrayList<>();
        for(int i=0;i<Images.length;i++){
            if(Images[i]!=null) {
                if (!Images[i].getDataState()) {
                    integers.add(i);
                }
            }
            else {
                integers.add(i);
            }
        }
        return integers;
    }

    public boolean getImagesState(){
        for(int i=0;i<Images.length;i++){
            if(Images[i]!=null) {
                if (!Images[i].getDataState()) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

    public void addChatImageData(int index,byte[] data){
        Images[index].AddBytesToData(data);
    }

    /**
     * 获取数据基本信息
     * @param index
     * @param newData
     */
    public void addNewImage(int index,FileData newData){
        Images[index]=newData;
    }

    public FileData getImageData(int index){
        return Images[index];
    }
    public FileData[] getImages(){
        return Images;
    }
}
