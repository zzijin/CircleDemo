package com.circle.information;

import android.graphics.Bitmap;

import androidx.appcompat.widget.AppCompatImageView;

import com.circle.UserInform;
import com.circle.socketclient.FileData;
import com.circle.socketclient.convert.ConvertType;

import java.util.ArrayList;
import java.util.List;

//用户数据类，包含动态内容和私信内容
public class UserData {
    //发布用户编号，根据本标号获取本地用户信息或请求用户信息
    public int Uid;
    //动态编号，动态唯一标识符
    public int DongTaiNumber;

    //动态类型;0:文字;1:图片;2:音乐；3：视频
    public int Type;
    //动态发布时间
    public String UserTime;
    //动态自定义装饰
    public FileData UserCard;
    //动态标题
    public String UserTitle;
    //分享次数
    public int ShareNumber;
    //评论次数
    public int CommentNumber;
    //点赞次数
    public int Zan;
    //动态内容
    public InformationData DongTaiData;

    //动态类
    public UserData(int uid,int dongTaiNumber,String userTime,String userTitle,int type,int share,int comment,int zan,int datasize){
        Uid=uid;
        DongTaiNumber=dongTaiNumber;
        UserTime=userTime;
        UserTitle=userTitle;
        Type=type;
        ShareNumber=share;
        CommentNumber=comment;
        Zan=zan;
        DongTaiData=new InformationData(datasize);
    }
}
