package com.circle.information;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

import com.circle.socketclient.FileData;
import com.circle.socketclient.convert.ConvertType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//动态数据基本类
public class InformationData {

    ///以下内容为可拥有内容，但最多一个动态只能拥有其一
    //文字内容
    //图片内容,最大9
    //动态含有的音乐.最大1
    //动态含有的视频,最大1
    private FileData[] BobyData;


    /**
     * 定义Boby大小
     * @param size
     */
    public InformationData(int size){
        BobyData=new FileData[size];
    }

    /**
     * 获取Boby数据
     * @param index
     * @param data
     */
    public void addBobyData(int index,byte[] data){
        BobyData[index].AddBytesToData(data);
    }

    /**
     * 查看某一数据是否完整
     * @param index
     * @return
     */
    public boolean getDataState(int index){
        if(index<BobyData.length){
            return BobyData[index].getDataState();
        }
        return false;
    }

    /**
     * 查看所有数据是否完整
     * @return
     */
    public boolean getDataState(){
        for(int i=0;i<BobyData.length;i++){
            if(BobyData[i]!=null) {
                if (!BobyData[i].getDataState()) {
                    return false;
                }
            }
            else {
                Log.i("检测加载状态","失败:动态数据"+i+"为空");
                return false;
            }
        }
        return true;
    }

    /**
     * 获取数据基本信息
     * @param index
     * @param newData
     */
    public void addNewBobyInform(int index,FileData newData){
        BobyData[index]=newData;
    }

    public String getTextData(){
        try {
            return new String(BobyData[0].getData(),0,BobyData[0].getData().length,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getPictureData(int index){
        return ConvertType.BytesToBitmap(BobyData[index].getData());
    }

    public String getDataName(int index){
        return BobyData[index].getFileName();
    }

    public FileData[] getBobyData() {
        return BobyData;
    }

    public int getDataBar(int index){
        return BobyData[index].Count-BobyData[index].StartIndex;
    }

    public void setBobyData(FileData[] data){
        BobyData=data;
    }
}
