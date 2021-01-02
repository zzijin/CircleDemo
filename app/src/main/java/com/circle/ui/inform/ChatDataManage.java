package com.circle.ui.inform;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.circle.UserInform;
import com.circle.UsersListManage;
import com.circle.information.ChatData;
import com.circle.information.UserData;
import com.circle.socketclient.FileData;
import com.circle.socketclient.SendDataManage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDataManage {
    //所有私信的数据
    static public  ArrayList<UserChat> Chats=new ArrayList<>();
    //保存私信对象在Chats中的位置,其顺序也是用户信息的新旧顺序
    static public ArrayList<Integer> sork=new ArrayList<>();

    //获取私信数据状态
    public static boolean getChatsState(){
        for(int i=0;i<Chats.size();i++) {
            if(Chats.get(i).chatDataState){
                Log.i("检测加载状态","私信数据:chat["+i+"]-"+Chats.get(i).ToUID+"已加载完成");
                continue;
            }
            else {
                for(int o=0;o<Chats.get(i).chatData.size();i++){
                    if(Chats.get(i).chatData.get(o).Images==null) {
                        Log.i("检测加载状态","私信数据:chat["+i+"].["+o+"]-私信编号:"+Chats.get(i).chatData.get(o).Number+"的图片数据为空");
                        return false;
                    }
                    else {
                        if (Chats.get(i).chatData.get(o).getImageState().size()!=0 || !UsersListManage.getUserImageState(Chats.get(i).ToUID)) {
                            Log.i("检测加载状态","私信数据:chat["+i+"].["+o+"]-私信编号:"+Chats.get(i).chatData.get(o).Number+"的图片数据尚未加载完");
                            return false;
                        }
                    }
                }
            }
        }
        Log.i("检测加载状态","私信数据:已完全加载");
        return true;
    }

    public static void addChatFileInform(int number,int uid, int toUID,  int index, FileData file){
        if(uid==UserInform.getuId()) {
            for (int i = 0; i < Chats.size(); i++) {
                if (Chats.get(i).ToUID == toUID) {
                    Chats.get(i).addChatFileInform(number, index, file);
                    return;
                }
            }
        }
        else {
            for (int i = 0; i < Chats.size(); i++) {
                if (Chats.get(i).ToUID == uid) {
                    Chats.get(i).addChatFileInform(number, index, file);
                    return;
                }
            }
        }
    }

    //附加图片数据到指定私信
    public static void addChatFileData(int number,int uid,int toUID,int index,byte[] data){
        if(uid==UserInform.getuId()) {
            for (int i = 0; i < Chats.size(); i++) {
                if (Chats.get(i).ToUID == toUID) {
                    Chats.get(i).addChatData(number, index, data);
                    return;
                }
            }
        }
        else {
            for (int i = 0; i < Chats.size(); i++) {
                if (Chats.get(i).ToUID == uid) {
                    Chats.get(i).addChatData(number, index, data);
                    return;
                }
            }
        }
    }

    public static int toUidGetIndex(int toUID){
        for(int i=0;i<Chats.size();i++){
            if(Chats.get(i).ToUID==toUID){
                return i;
            }
        }
        return -1;
    }

    //添加初始信息
    public static void addInitChatInform(int number,int uid,int toUID,ChatData chat){
        Log.i("私信数据加入","私信数据正在添加:-编号:"+number);
//        UserChat userChat;
//        userChat=new UserChat(toUID);
//        userChat.addChatDataInform(chat);
//        Chats.add(userChat);
        for(int i=0;i<Chats.size();i++){
            if(Chats.get(i).ToUID==toUID){
                Chats.get(i).addChatDataInform(chat);
                return;
            }
        }

        UserChat userChat;
        userChat=new UserChat(toUID);
        userChat.addChatDataInform(chat);
        Chats.add(userChat);

        Log.i("私信数据加入","私信数据添加完成:-当前数量:"+Chats.size());
    }

    public static void getSucceed(){
        Log.i("私信排序","私信数据:正在排序,数量:"+Chats.size());
        Map<Integer,Integer> number=new HashMap<>();
        for(int i=0;i<Chats.size();i++){
            Log.i("私信排序","私信数据:"+i+"正在排序,数量:"+Chats.get(i).chatData.size());
            Chats.get(i).waitNumber=0;
            number.put(i,Chats.get(i).chatData.get(0).Number);
            for(int o=1;o<Chats.get(i).chatData.size();o++){
                number.put(i,Chats.get(i).chatData.get(o).Number>number.get(i)?Chats.get(i).chatData.get(o).Number:number.get(i));
            }
        }
        Log.i("私信排序","私信数据:正在排序,获取私信编号,数量:"+number.size());
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(number.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>()
        {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2)
            {
                //按照value值，重小到大排序
//                return o1.getValue() - o2.getValue();
                //按照value值，从大到小排序
                return o2.getValue() - o1.getValue();
                //按照value值，用compareTo()方法默认是从小到大排序
               // return o1.getValue().compareTo(o2.getValue());
            }
        });

        for (Map.Entry s : list) {
            sork.add((int)s.getKey());
        }
        Log.i("私信排序","私信数据:已完全排序完成");
    }

    public static void getNextSucceed(){
        for(int i=0;i<Chats.size();i++){
            Chats.get(i).waitNumber=0;
            Chats.get(i).chatDataState=true;
        }
    }

    //添加新信息
    public static void addNewChatInform(int number,int uid,int toUID,ChatData chat){
        for(int i=0;i<Chats.size();i++){
            if(Chats.get(i).ToUID==toUID){
                Chats.get(i).chatData.add(0,chat);
                for(int o=0;o<sork.size();o++){
                    if(sork.get(o)==i){
                        sork.remove(o);
                        sork.add(0,i);
                    }
                }
                Chats.get(i).IsGetNewChat=true;
                Log.i("私信发送追踪","放入新数据，设置刷新状态");
                return;
            }
        }

        UserChat userChat;
        userChat=new UserChat(toUID);
        userChat.addChatDataInform(chat);

        Chats.add(0,userChat);
        sork.add(0,Chats.size()-1);
        return;
    }

    //添加历史信息
    public static void addNextChatInform(int number,int uid,int toUID,ChatData chat){
        for(int i=0;i<Chats.size();i++){
            if(Chats.get(i).ToUID==toUID){
                Chats.get(i).addChatDataInform(chat);
                return;
            }
        }
        UserChat userChat;
        userChat=new UserChat(toUID);
        userChat.addChatDataInform(chat);
        Chats.add(userChat);
    }

    public static ArrayList<ChatData> getUserChat(int index){
        ArrayList<ChatData> chatData=new ArrayList<>();
        chatData.addAll(Chats.get(index).chatData);
        return chatData;
    }

    //请求获取历史信息
    public static void sendNextChat(int index){
        Chats.get(index).chatDataState=false;

        int chatNumber=Chats.get(index).chatData.get(0).Number;
        for(int i=0;i<Chats.get(index).chatData.size();i++){
            chatNumber=Chats.get(index).chatData.get(i).Number<chatNumber?Chats.get(index).chatData.get(i).Number:chatNumber;
        }
        Chats.get(index).waitNumber+=5;
        SendDataManage.sendGetUserChat(UserInform.getuId(),Chats.get(index).ToUID,chatNumber,5);
    }

}
