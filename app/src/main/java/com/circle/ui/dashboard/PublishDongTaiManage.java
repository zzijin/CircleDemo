package com.circle.ui.dashboard;

import com.circle.information.PublishDongTai;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PublishDongTaiManage {
    static private Lock lock=new ReentrantLock();
    static public ArrayList<PublishDongTai> publishDongTais=new ArrayList<>();

    static public int AddPublishDongTais(PublishDongTai publishDongTai){
        lock.lock();
        int index;
        if(publishDongTais.size()>0) {
            index = publishDongTais.get(publishDongTais.size() - 1).index + 1;
        }
        index=0;
        publishDongTai.index = index;
        publishDongTais.add(publishDongTai);
        lock.unlock();
        return index;
    }

    static public void setPublishDongTai(int index,int dongTaiNumber,String sendTime,ArrayList<String> servicePath){
        for(int i=0;i<publishDongTais.size();i++){
            if(publishDongTais.get(i).index==index){
                publishDongTais.get(i).DongTaiNumber=dongTaiNumber;
                publishDongTais.get(i).SendTime=sendTime;
                publishDongTais.get(i).ServicePath=servicePath;
            }
        }
    }

    public static void RemoveAtPublish(int index){
        lock.lock();
        publishDongTais.remove(index);
        lock.unlock();
    }
}
