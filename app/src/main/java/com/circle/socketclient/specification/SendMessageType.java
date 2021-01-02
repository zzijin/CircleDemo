package com.circle.socketclient.specification;

public final class SendMessageType {
    //1-99:账户操作
    //100-199:动态操作
    //200-299:私信操作
    ////账户操作
    //1:登录
    final static public int toLogin=1;
    //10:注册
    final static public int toRegister=10;
    //20:注销
    final static public int toLogOut=20;
    //30:断线重连
    final static public int toReconnection=30;
    //40:修改昵称
    final static public int toAlterMyName=40;
    //41:修改头像
    final static public int toAlterMyImage=41;
    //42:修改签名
    final static public int toAlterMyLogin=42;

    //50:获取用户基本信息(发出UID)
    final static public int toGetOtherUser=50;
    //51:返回头像数据验证结果(UID+成功/失败+StartIndex)
    final static public int toVerifyState=51;

    ////动态操作
    //100:发布动态(发出动态基本信息,含:UID/Type/Time/Title)
    final static public int toMyDongTaiInform=100;
    //101:发送动态数据
    final static public int toMyDongTaiData=101;
    //102:发送完成
    final static public int toMyDongTaiSucceed=102;


    //200:请求刷新推荐的用户动态
    final static public int toRecommendNewDongTai=200;
    //201:请求加载后面的用户动态
    final static public int ToRecommendNextDongTai=201;
    //210:请求获取推荐动态的数据基本信息(DongTaiNumber+Index)
    final static public int toRecommendDongTaiInform=210;
    //300:请求获取关注的用户动态(含UID+获取动态起始位置+获取的条数)
    final static public int toIdolNewDongTai=300;
    //301:
    final static public int toIdolNextDongTai=301;
    //310:请求获取推荐动态的数据基本信息
    final static public int toIdolDongTaiInform=310;

    //私信
    //返回用户消息
    final static public int toUserChatInform=400;
    //返回获取的历史信息
    final static public int toBeforeChatInform=402;

    //发送消息
    final static public int SendChatToUser=450;
    //发送数据
    final static public int SendChatData=451;
    //数据发送完毕
    final static public int SendChatSucceed=452;

    //1000:断线重连
    final static public int SendReconnection=1000;
}
