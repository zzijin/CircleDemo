package com.circle.socketclient;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.circle.UserInform;
import com.circle.UsersListManage;
import com.circle.information.ChatData;
import com.circle.information.UserData;
import com.circle.information.Users;
import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.specification.ClientMessageType;
import com.circle.ui.dashboard.DongTaiListManage;
import com.circle.ui.dashboard.PublishDongTaiManage;
import com.circle.ui.inform.ChatDataManage;
import com.circle.ui.inform.SendChatWaitQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SocketDataManage {
    Conn conn;
    PackagingMessage packaging;

    public SocketDataManage(Conn c)
    {
        conn = c;
        packaging = new PackagingMessage();
    }

    /// <summary>
    /// 信息处理
    /// </summary>
    public void DataManager(FloorMessage floorMessage)
    {
        if (floorMessage != null)
        {
            //信息归类
            switch (floorMessage.getType())
            {
                //测试连接类
                case ClientMessageType.getTest: {
                    try {
                        Log.i("消息汇总类","收到消息:"+new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                };
                break;
                //登录
                case ClientMessageType.getLoginInform: {
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        if(json.getBoolean("LoginState")){
                            UserInform.setUserInfrom(json.getInt("UID"),getFileData(s),json.getString("UserSign"),json.getInt("WatchSBNum"),json.getInt("FansNum"),json.getInt("DongTaiNum"));
                            Log.i("接收信息处理类","登录成功-用户名:"+UserInform.getUserName()+"-签名:"+UserInform.getUserSign()+"-关注数:"+UserInform.getWatchSBNum()+"-粉丝数:"+UserInform.getFansNum()+"-动态数:"+UserInform.getDongTaiNum());

                        }
                        else {
                            UserInform.loseLogin();
                            Log.i("接收信息处理类","登录失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                };
                break;
                case ClientMessageType.getMyImage: {
                    Log.i("接收信息处理类","正在接收自己的头像");
                    UserInform.addUserImageBytes(floorMessage.getData());
                }
                break;
                case ClientMessageType.getNewUser: {
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        Users user=new Users(json.getInt("UID"),json.getString("UserName"),getFileData(s));
                        UsersListManage.addUserInform(user.getUid(),user);
                        Log.i("接收消息处理类","获取新的用户信息-用户名:"+user.getUserName()+"-UID:"+user.getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case ClientMessageType.getNewUserImage:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        UsersListManage.addUserImageData(json.getInt("UID"),ConvertType.Base64ToByte(json.getString("Data")));
                        Log.i("接收消息处理类","获取用户头像");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case ClientMessageType.getMyDongTaiInform:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        int dongTaiNumber=json.getInt("DongTaiNumber");
                        int index=json.getInt("Index");
                        int size=json.getInt("Size");
                        String time=json.getString("SendTime");

                        ArrayList<String> paths=new ArrayList<>();
                        for(int i=0;i<size;i++){
                            StringBuilder path=new StringBuilder("Path");
                            path.append(i);
                            paths.add(json.getString(path+""));
                        }
                        PublishDongTaiManage.setPublishDongTai(index,dongTaiNumber,time,paths);
                        Log.i("动态发送追踪","接收到服务器返回数据:"+json.toString());
                        SendDataManage.SendMyDongTaiData(index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
                case ClientMessageType.getIdolDongTaiInform: {
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        UserData userData=new UserData(json.getInt("UID"),json.getInt("DongTaiNumber"),json.getString("DongTaiAlterTime"),json.getString("DongTaiTitle"),json.getInt("DongTaiType"),json.getInt("DongTaiShareNumber"),json.getInt("DongTaiCommentNumber"),json.getInt("DongTaiZanNumber"),json.getInt("DongTaiDataNumber"));
                        Log.i("接收动态信息类","获取动态-用户名:"+json.getInt("UID")+"-动态id:"+json.getInt("DongTaiNumber")+"-修改时间:"+json.getString("DongTaiAlterTime")+"-动态标题:"+json.getString("DongTaiTitle")+"-动态类型:"+json.getInt("DongTaiType")+"-动态分享次数:"+json.getInt("DongTaiShareNumber")+"-动态评论次数:"+json.getInt("DongTaiCommentNumber")+"-动态点赞次数:"+json.getInt("DongTaiZanNumber")+"-动态数据个数:"+json.getInt("DongTaiDataNumber"));
                        DongTaiListManage.addNewDongTaiInform(userData);
                        //若新得到的动态非本地用户列表中已有的用户，则需向服务器请求该用户信息
                        sendGetNewUserInform(userData.Uid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case ClientMessageType.getIdolDongTaiDataInform: {
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        DongTaiListManage.addDongTaiDataInform(json.getInt("DongTaiNumber"),json.getInt("Index"),getFileData(s));
                        Log.i("接收消息处理类","获取Boby数据基本信息,动态id:"+json.getInt("DongTaiNumber")+"-数据位置:"+json.getInt("Index"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                case ClientMessageType.getIdolDongTaiData:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        int i=DongTaiListManage.addDongTaiData(json.getInt("DongTaiNumber"),json.getInt("Index"),ConvertType.Base64ToByte(json.getString("Data")));
                        Log.i("接收消息处理类","获取Boby数据,动态id:"+json.getInt("DongTaiNumber")+"-数据位置:"+json.getInt("Index")+"剩余:"+DongTaiListManage.getDataBar(i,json.getInt("Index")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case ClientMessageType.getIdolDongTaiSucceed:{

                };
                break;
                case ClientMessageType.getIdolDongTaiNextInform:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        UserData userData=new UserData(json.getInt("UID"),json.getInt("DongTaiNumber"),json.getString("DongTaiAlterTime"),json.getString("DongTaiTitle"),json.getInt("DongTaiType"),json.getInt("DongTaiShareNumber"),json.getInt("DongTaiCommentNumber"),json.getInt("DongTaiZanNumber"),json.getInt("DongTaiDataNumber"));
                        Log.i("接收动态信息类","获取Next动态-用户名:"+json.getInt("UID")+"-动态id:"+json.getInt("DongTaiNumber")+"-修改时间:"+json.getString("DongTaiAlterTime")+"-动态标题:"+json.getString("DongTaiTitle")+"-动态类型:"+json.getInt("DongTaiType")+"-动态分享次数:"+json.getInt("DongTaiShareNumber")+"-动态评论次数:"+json.getInt("DongTaiCommentNumber")+"-动态点赞次数:"+json.getInt("DongTaiZanNumber")+"-动态数据个数:"+json.getInt("DongTaiDataNumber"));
                        DongTaiListManage.addNewDongTaiInform(userData);
                        //若新得到的动态非本地用户列表中已有的用户，则需向服务器请求该用户信息
                        sendGetNewUserInform(userData.Uid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case  ClientMessageType.getIdolDongTaiNextSucceed:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                        DongTaiListManage.isGetNewDongTai=true;
                        Log.i("接收动态信息类","获取Next动态成功");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        DongTaiListManage.isGetNewDongTai=true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case ClientMessageType.getUserChatInform:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        ChatData chat=null;
                        if(json.getInt("UID")==UserInform.getuId()){
                            chat=new ChatData(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),json.getString("StrData"),json.getString("SendTime"),json.getBoolean("SeeState"),true,json.getInt("ImageSize"));
                            ChatDataManage.addInitChatInform(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),chat);
                        }
                        else {
                            chat=new ChatData(json.getInt("Number"),json.getInt("ToUID"),json.getInt("UID"),json.getString("StrData"),json.getString("SendTime"),json.getBoolean("SeeState"),false,json.getInt("ImageSize"));
                            ChatDataManage.addInitChatInform(json.getInt("Number"),json.getInt("ToUID"),json.getInt("UID"),chat);
                        }

                        sendGetNewUserInform(chat.ToUID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                //初始信息发送完毕
                case ClientMessageType.getUserChatSucceed:{
                    ChatDataManage.getSucceed();
                };
                break;
                case ClientMessageType.getBeforeChatInform:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        ChatData chat=null;
                        if(json.getInt("UID")==UserInform.getuId()){
                            chat=new ChatData(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),json.getString("StrData"),json.getString("SendTime"),json.getBoolean("SeeState"),true,json.getInt("ImageSize"));
                            ChatDataManage.addNextChatInform(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),chat);
                        }
                        else {
                            chat=new ChatData(json.getInt("Number"),json.getInt("ToUID"),json.getInt("UID"),json.getString("StrData"),json.getString("SendTime"),json.getBoolean("SeeState"),false,json.getInt("ImageSize"));
                            ChatDataManage.addNextChatInform(json.getInt("Number"),json.getInt("ToUID"),json.getInt("UID"),chat);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case ClientMessageType.getNewChatInform:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        ChatData chat=null;
                        Log.i("私信发送追踪","收到新的私信");
                        if(json.getInt("UID")==UserInform.getuId()){
                            chat=new ChatData(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),json.getString("StrData"),json.getString("SendTime"),json.getBoolean("SeeState"),true,json.getInt("ImageSize"));
                            ChatDataManage.addNewChatInform(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),chat);
                        }
                        else {
                            chat=new ChatData(json.getInt("Number"),json.getInt("ToUID"),json.getInt("UID"),json.getString("StrData"),json.getString("SendTime"),json.getBoolean("SeeState"),false,json.getInt("ImageSize"));
                            ChatDataManage.addNewChatInform(json.getInt("Number"),json.getInt("ToUID"),json.getInt("UID"),chat);
                        }
                        sendGetNewUserInform(chat.ToUID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                //获取文件基本信息
                case ClientMessageType.getChatFileInform:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        ChatDataManage.addChatFileInform(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),json.getInt("Index"),getFileData(s));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                //获取信息文件数据
                case ClientMessageType.getChatFileData:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        ChatDataManage.addChatFileData(json.getInt("Number"),json.getInt("UID"),json.getInt("ToUID"),json.getInt("Index"),ConvertType.Base64ToByte(json.getString("Data")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                case ClientMessageType.getBeforeChatSucceed:{
                    ChatDataManage.getNextSucceed();
                };
                break;
                case ClientMessageType.getNewChatNumber:{
                    String s = null;
                    try {
                        s = new String(floorMessage.getData(),0,floorMessage.getData().length,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json=new JSONObject(s);
                        int chatNumber=json.getInt("ChatNumber");
                        int index=json.getInt("Index");
                        int size=json.getInt("Size");
                        String time=json.getString("SendTime");
                        ArrayList<String> paths=new ArrayList<>();
                        for(int i=0;i<size;i++){
                            StringBuilder path=new StringBuilder("Path");
                            path.append(i);
                            paths.add(json.getString(path+""));
                        }
                        SendChatWaitQueue.WaitSetService(index,chatNumber,time,paths);
                        Log.i("私信发送追踪","接收到服务器返回数据-私信编号"+chatNumber+"-私信时间"+time);
                        SendDataManage.SendChatData(index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                break;
                default:
                {

                };
                break;
            }
        }
    }

    private FileData getFileData(String str){
        JSONObject json= null;
        try {
            json = new JSONObject(str);
            return new FileData(json.getString("FileName"), json.getString("ExtensionName"), json.getInt("DataSize"), json.getInt("Count"),json.getInt("Type"));

        } catch (JSONException e) {
            Log.i("接收信息处理类","文件信息接收失败"+e.toString());
            return null;
        }
    }

    private void sendGetNewUserInform(int uid){
        if(!UsersListManage.getUserInformIsInList(uid)){
            UsersListManage.addUserUID(uid);
            SendDataManage.sendGetUserInform(uid);
            Log.i("接收信息处理类","新获取的私信非已有用户发送，向服务器请求该用户信息");
        }
    }
}
