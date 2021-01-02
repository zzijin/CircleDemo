package com.circle.ui.inform;

import com.circle.UserInform;
import com.circle.information.WaitQueueChat;
import com.circle.socketclient.FileData;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SendChatWaitQueue {
    private static Lock lock=new ReentrantLock();
    public static ArrayList<WaitQueueChat> waitQueueChats=new ArrayList<>();

    public static int WaitAdd(WaitQueueChat waitQueueChat){
        lock.lock();
        int index;
        if(waitQueueChats.size()>0) {
            index = waitQueueChats.get(waitQueueChats.size() - 1).index + 1;
        }
        index=0;
        waitQueueChat.index = index;
        waitQueueChats.add(waitQueueChat);
        lock.unlock();
        return index;
    }

    public static void WaitSetService(int chatIndex,int chatNumber,String sendTime,ArrayList<String> path){
        for(int o=0;o<waitQueueChats.size();o++){
            if(waitQueueChats.get(o).index==chatIndex) {
                waitQueueChats.get(o).chatNumber=chatNumber;
                waitQueueChats.get(o).sendTime=sendTime;
                for(int i=0;i<path.size();i++){
                    waitQueueChats.get(o).servicePath.set(i,path.get(i));
                }
            }
        }
    }

    public static void WaitRemoveAt(int index){
        lock.lock();
        waitQueueChats.remove(index);
        lock.unlock();
    }
}
