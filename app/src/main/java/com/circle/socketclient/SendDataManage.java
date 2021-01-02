package com.circle.socketclient;

import android.util.Log;

import com.circle.UserInform;
import com.circle.information.ChatData;
import com.circle.information.PublishDongTai;
import com.circle.information.UserData;
import com.circle.information.WaitQueueChat;
import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.specification.SendMessageType;
import com.circle.ui.dashboard.DongTaiListManage;
import com.circle.ui.dashboard.PublishDongTaiManage;
import com.circle.ui.inform.ChatDataManage;
import com.circle.ui.inform.SendChatWaitQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SendDataManage {
    static public Lock sendFilelock=new ReentrantLock();
    static public ArrayList<FileData> sendFileDataList=new ArrayList<>();

    /**
     * 发送登录请求
     * @return
     */
    static public boolean sendLogin(){
        JSONObject json=new JSONObject();
        try {
            json.put("UserName",UserInform.getUserName());
            json.put("UserPassword",UserInform.getPassWord());
            SocketManage.SendMessage(SendMessageType.toLogin,json.toString().getBytes("utf-8"));
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 请求获取新动态(刷新整个列表)
     * @param number 此次获取动态条数
     * @return
     */
    static public boolean sendGetIdolNewDongTai(int number){
        JSONObject json=new JSONObject();
        try {
            json.put("Number", number);
            json.put("UID",UserInform.getuId());
            try {
                Log.i("请求类","请求刷新关注动态-数量:"+number);
                SocketManage.SendMessage(SendMessageType.toIdolNewDongTai,json.toString().getBytes("utf-8"));
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 请求获取新的动态(已有动态之前的)
     * @param dongTaiNumber 已有动态最后一条编号
     * @param number 此次获取动态条数
     * @return
     */
    static public boolean sendGetIdolNextDongTai(int dongTaiNumber,int number){
        JSONObject json=new JSONObject();
        try {
            json.put("DongTaiNumber", dongTaiNumber);
            json.put("Number",number);
            json.put("UID",UserInform.getuId());
            try {
                SocketManage.SendMessage(SendMessageType.toIdolNextDongTai,json.toString().getBytes("utf-8"));
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public boolean sendGetUserInform(int uid){
        JSONObject json=new JSONObject();
        try {
            json.put("UID", uid);
            try {
                SocketManage.SendMessage(SendMessageType.toGetOtherUser,json.toString().getBytes("utf-8"));
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public boolean sendGetShowChat(int uid){
        JSONObject json=new JSONObject();
        try {
            json.put("UID", uid);
            try {
                SocketManage.SendMessage(SendMessageType.toUserChatInform,json.toString().getBytes("utf-8"));
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public boolean sendGetUserChat(int uid,int toUid,int chatNumber,int number){
        JSONObject json=new JSONObject();
        try {
            json.put("UID", uid);
            json.put("ToUID",toUid);
            json.put("ChatNumber",chatNumber);
            json.put("Number",number);
            try {
                SocketManage.SendMessage(SendMessageType.toBeforeChatInform,json.toString().getBytes("utf-8"));
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public boolean sendChatToUser(int chatIndex){
        int index;
        for(int o=0;o<SendChatWaitQueue.waitQueueChats.size();o++) {
            if (SendChatWaitQueue.waitQueueChats.get(o).index == chatIndex) {
                index=o;
                JSONObject json = new JSONObject();
                try {
                    WaitQueueChat waitQueueChat = SendChatWaitQueue.waitQueueChats.get(index);
                    json.put("UID", waitQueueChat.uid);
                    json.put("ToUID", waitQueueChat.toUid);
                    json.put("StrData", waitQueueChat.strData);
                    json.put("Size", waitQueueChat.data.size());
                    json.put("Index", waitQueueChat.index);
                    for (int i = 0; i < waitQueueChat.data.size(); i++) {
                        StringBuilder type = new StringBuilder("Type");
                        type.append(i);
                        json.put(type + "", waitQueueChat.data.get(i).FileType);
                        StringBuilder name = new StringBuilder("FileName");
                        name.append(i);
                        json.put(name + "", waitQueueChat.data.get(i).FileName);
                        StringBuilder ename = new StringBuilder("ExtensionName");
                        ename.append(i);
                        json.put(ename + "", waitQueueChat.data.get(i).ExtensionName);
                    }
                    try {
                        SocketManage.SendMessage(SendMessageType.SendChatToUser, json.toString().getBytes("utf-8"));
                        Log.i("私信发送追踪","发送消息到服务器");
                        return true;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

    static public boolean SendChatData(int chatIndex){
        try {
            int index;
            for(int o=0;o<SendChatWaitQueue.waitQueueChats.size();o++){
                if(SendChatWaitQueue.waitQueueChats.get(o).index==chatIndex){
                    index=o;
                    WaitQueueChat waitQueueChat=SendChatWaitQueue.waitQueueChats.get(index);
                    for(int i=0;i<waitQueueChat.data.size();i++){
                        while (true) {
                            byte[] data = waitQueueChat.data.get(i).SendFileData();
                            if(data==null){
                                break;
                            }
                            JSONObject json=new JSONObject();
                            json.put("Path",waitQueueChat.servicePath.get(i));
                            json.put("Data",ConvertType.ByteToBase64(data));
                            try {
                                SocketManage.SendMessage(SendMessageType.SendChatData,json.toString().getBytes("utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Log.i("私信发送追踪","发送私信文件数据到服务器");
                    SendChatSucceed(index);
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public boolean SendChatSucceed(int index){
        JSONObject json=new JSONObject();
        try {
            json.put("UID", SendChatWaitQueue.waitQueueChats.get(index).uid);
            json.put("ToUID",SendChatWaitQueue.waitQueueChats.get(index).toUid);
            json.put("ChatNumber",SendChatWaitQueue.waitQueueChats.get(index).chatNumber);
            try {
                SocketManage.SendMessage(SendMessageType.SendChatSucceed,json.toString().getBytes("utf-8"));
                Log.i("私信发送追踪","发送完毕，将数据放入本地-我自己:"+UserInform.getuId()+"-发送方:"+SendChatWaitQueue.waitQueueChats.get(index).uid+"-接收方;"+SendChatWaitQueue.waitQueueChats.get(index).toUid);
                ChatData chatData=new ChatData(SendChatWaitQueue.waitQueueChats.get(index).chatNumber,SendChatWaitQueue.waitQueueChats.get(index).uid,SendChatWaitQueue.waitQueueChats.get(index).toUid,SendChatWaitQueue.waitQueueChats.get(index).sendTime,SendChatWaitQueue.waitQueueChats.get(index).MyPath,SendChatWaitQueue.waitQueueChats.get(index).strData);
                ChatDataManage.addNewChatInform(SendChatWaitQueue.waitQueueChats.get(index).chatNumber,SendChatWaitQueue.waitQueueChats.get(index).uid,SendChatWaitQueue.waitQueueChats.get(index).toUid,chatData);
                SendChatWaitQueue.WaitRemoveAt(index);
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public boolean SendMyDongTaiInform(int dongTaiIndex){
        int index;
        for(int o = 0; o< PublishDongTaiManage.publishDongTais.size(); o++) {
            if (PublishDongTaiManage.publishDongTais.get(o).index == dongTaiIndex) {
                index=o;
                JSONObject json = new JSONObject();
                try {
                    PublishDongTai publishDongTai = PublishDongTaiManage.publishDongTais.get(index);
                    json.put("UID", publishDongTai.UID);
                    json.put("Index", publishDongTai.index);
                    json.put("Type", publishDongTai.Type);
                    json.put("Title", publishDongTai.Title);
                    if(publishDongTai.Type==0){
                        json.put("Size",1);
                    }
                    else {
                        json.put("Size",publishDongTai.Data.size());
                    }

                    for (int i = 0; i < publishDongTai.Data.size(); i++) {
                        StringBuilder name = new StringBuilder("FileName");
                        name.append(i);
                        json.put(name + "", publishDongTai.Data.get(i).FileName);
                        StringBuilder ename = new StringBuilder("ExtensionName");
                        ename.append(i);
                        json.put(ename + "", publishDongTai.Data.get(i).ExtensionName);
                    }
                    try {
                        SocketManage.SendMessage(SendMessageType.toMyDongTaiInform, json.toString().getBytes("utf-8"));
                        Log.i("动态发送追踪","发送基本信息到服务器");
                        return true;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }
    static public boolean SendMyDongTaiData(int dongTaiIndex){
        try {
            int index;
            for(int o=0;o<PublishDongTaiManage.publishDongTais.size();o++){
                if(PublishDongTaiManage.publishDongTais.get(o).index==dongTaiIndex){
                    index=o;
                    PublishDongTai publishDongTai=PublishDongTaiManage.publishDongTais.get(index);
                    if(publishDongTai.Type==0){
                        JSONObject json=new JSONObject();
                        json.put("Path",publishDongTai.ServicePath.get(0));
                        json.put("Data",ConvertType.ByteToBase64(publishDongTai.Boby));
                        try {
                            SocketManage.SendMessage(SendMessageType.toMyDongTaiData,json.toString().getBytes("utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        for(int i=0;i<publishDongTai.Data.size();i++){
                            while (true) {
                                byte[] data = publishDongTai.Data.get(i).SendFileData();
                                if(data==null){
                                    break;
                                }
                                JSONObject json=new JSONObject();
                                json.put("Path",publishDongTai.ServicePath.get(i));
                                json.put("Data",ConvertType.ByteToBase64(data));
                                json.put("StartIndex",publishDongTai.Data.get(i).StartIndex-1);
                                Log.i("动态发送追踪","-位置:"+publishDongTai.ServicePath.get(i)+"-起始:"+publishDongTai.Data.get(i).StartIndex);
                                try {
                                    SocketManage.SendMessage(SendMessageType.toMyDongTaiData,json.toString().getBytes("utf-8"));
                                    Thread.sleep(1);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Log.i("动态发送追踪","发送动态文件数据到服务器");
                    SendMyDongTaiSucceed(index);
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    static public boolean SendMyDongTaiSucceed(int dongTaiIndex){
        JSONObject json=new JSONObject();
        try {
            PublishDongTai publishDongTai=PublishDongTaiManage.publishDongTais.get(dongTaiIndex);
            json.put("UID", publishDongTai.UID);
            json.put("DongTaiNumber",publishDongTai.DongTaiNumber);
            try {
                SocketManage.SendMessage(SendMessageType.toMyDongTaiSucceed,json.toString().getBytes("utf-8"));
                UserData userData=new UserData(publishDongTai.UID,publishDongTai.DongTaiNumber,publishDongTai.SendTime,publishDongTai.Title,publishDongTai.Type,0,0,0,publishDongTai.Data.size());
                Log.i("动态发送追踪","发送完毕，将数据放入本地-用户uid:"+userData.Uid+"动态编号:"+userData.DongTaiNumber);
                if(publishDongTai.Type==0) {
                    FileData[] data={new FileData(publishDongTai.UID+"text",".txt",0,publishDongTai.Boby)};

                    DongTaiListManage.addNewMyInform(userData,data);
                }
                else {
                    FileData[] data=new FileData[publishDongTai.MyPath.size()];
                    for(int i=0;i<publishDongTai.MyPath.size();i++){
                        data[i]=new FileData(publishDongTai.MyPath.get(i),publishDongTai.Type);
                    }

                    DongTaiListManage.addNewMyInform(userData,data);
                }
                PublishDongTaiManage.RemoveAtPublish(dongTaiIndex);
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
