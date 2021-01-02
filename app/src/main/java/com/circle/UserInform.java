package com.circle;

import android.graphics.Bitmap;
import android.util.Log;

import com.circle.socketclient.FileData;
import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.convert.RoundBitmap;

import java.io.File;

public class UserInform {
    static String userName;
    static int uId;
    static String passWord;
    static String userSign;
    static int watchSBNum;
    static int fansNum;
    static int dongTaiNum;
    //0:初始状态;1:登录成功,请求数据;2：加载数据中;3：加载数据完毕或登录成功;4:登录失败
    static int loginState=0;
    static FileData userImage;

    public static int getLoginState(){
        return loginState;
    }
    public static String getUserName() {
        return userName;
    }
    public static String getPassWord() {
        return passWord;
    }
    public static String getUserSign() {
        return userSign;
    }
    public static int getWatchSBNum() {
        return watchSBNum;
    }
    public static int getFansNum() {
        return fansNum;
    }
    public static int getDongTaiNum() {
        return dongTaiNum;
    }
    public static boolean getUserImageState(){
        Log.i("读取头像完整性","应有:"+userImage.Count+",当前:"+userImage.StartIndex);
        return userImage.getDataState();
    }
    public static int getuId(){
        return uId;
    }
    public static Bitmap getUserBitImage(){
        return RoundBitmap.toRoundBitmap(ConvertType.BytesToBitmap(userImage.getData()));
    }
    public static FileData getUserImage() {
        return userImage;
    }
    public static void addUserImageBytes(byte[] data){
        userImage.AddBytesToData(data);
    }

    //点击登录后获取的用户名和密码
    public static void setUserInfrom(String username,String password){
        userName=username;
        passWord=password;
    }

    //登录成功后服务器返回的用户数据
    public static void setUserInfrom(int uid,FileData userimage,String usersign,int watchsbnum,int fansnum,int dongtainum){
        uId=uid;
        userImage=userimage;
        userSign=usersign;
        watchSBNum=watchsbnum;
        fansNum=fansnum;
        dongTaiNum=dongtainum;
        loginState=1;
    }

    public static void loadingData(){
        loginState=2;
    }

    public static void loadDataEnd(){
        loginState=3;
    }

    public static void loseLogin(){
        loginState=4;
        Log.i("用户类","登录失败,更改状态");
    }

    //用户离线(网络掉线或主动注销)
    public static void offLine(){
        loginState=0;
    }
}
