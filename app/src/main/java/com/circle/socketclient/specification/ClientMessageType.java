package com.circle.socketclient.specification;

public final class ClientMessageType {
    ////测试连接
    public final static int getTest=0;

    ////账户操作
    ///对于自己的操作
    //获取登录返回信息(返回登录成功/失败，成功额外返回用户昵称/签名/关注数/粉丝数/收藏动态数)
    public final static int getLoginInform=1;
    //返回用户头像
    public final static int getMyImage=2;
    //注册返回信息(成功/重名)
    public final static int getRegisterInform=10;
    //注销返回信息
    public final static int getLogoutInform=20;
    //断线重连验证返回信息
    public final static int getReconnection=30;
    //返回修改昵称信息
    public final static int getAlterName=40;
    //返回修改头像信息
    public final static int getAlterImage=41;
    //返回修改签名信息
    public final static int getAlterSidn=42;
    ///对于其他用户操作(每当获取动态或私信,客户端根据uid判断本地列表内是否存在该用户的信息,若不存在请求服务器以获取该用户信息)
    //返回新获取到的用户信息(包括UID/头像基本信息/用户名)
    public final static int getNewUser=50;
    //获取到的用户头像信息(UID+StartIndex+data)
    public final static int getNewUserImage=51;

    ////动态操作
    ///发布动态操作
    //返回发布动态编号
    public final static int getMyDongTaiInform=100;

    ///200-299:获取推荐动态
    //返回动态基本信息()
    public final static int getRecommendDongTaiInform=200;
    //返回动态主要数据
    public final static int getRecommendDonTaiData=210;
    ///300-399:获取关注动态
    //返回动态初始信息()
    public final static int getIdolDongTaiInform=300;
    public final static int getIdolDongTaiNextInform=301;
    //返回动态数据基本信息
    public final static int getIdolDongTaiDataInform=310;
    //返回动态数据
    public final static int getIdolDongTaiData=320;
    //动态发送完毕
    public final static int getIdolDongTaiSucceed=330;
    public final static int getIdolDongTaiNextSucceed=331;

    //400-499:私信操作
    //返回用户消息
    final static public int getUserChatInform=400;
    //返回发送完毕信息
    final static public int getUserChatSucceed = 401;
    //返回获取的历史信息
    final static public int getBeforeChatInform=402;
    //返回获取的新信息
    final static public int getNewChatInform=403;
    //返回历史信息完毕
    final static public int getBeforeChatSucceed=404;
    //返回用户图片基本信息
    final static public int getChatFileInform=420;
    //返回用户图片数据
    final static public int getChatFileData=421;

    //返回用户发送私信的编号
    final static public int getNewChatNumber = 450;
}
