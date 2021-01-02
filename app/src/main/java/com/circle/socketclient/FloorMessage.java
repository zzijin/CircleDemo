package com.circle.socketclient;

import android.util.Log;

import com.circle.socketclient.convert.ConvertType;

import java.lang.reflect.Array;
import java.util.Arrays;

public class FloorMessage {
    /// <summary>
    /// 请求id/类别,类别将在记事本中记录
    /// </summary>
    public int Type;
    /// <summary>
    /// 数据主体
    /// </summary>
    public byte[] Data;
    /// <summary>
    /// 完整未拆解或已合成的数据
    /// </summary>
    public byte[] Msg;

    /// <summary>
    /// 合成信息
    /// </summary>
    /// <param name="type">请求类别</param>
    /// <param name="data">信息主体</param>
    public void GeneratMessage(int type,byte[] data)
    {
        this.Type = type;
        this.Data = data;

        byte[] id=new byte[4];
        id= ConvertType.IntToByte(Type);

        Msg = ConvertType.CopyByte(id, data);
    }
    /// <summary>
    /// 返回信息大小
    /// </summary>
    /// <returns></returns>
    public int GetFloorMessageSize()
    {
        return Msg.length;
    }

    /// <summary>
    /// 拆分数据，得到请求id及数据主体
    /// </summary>
    /// <param name="msg">未拆分的数据</param>
    public void DisassembleMessage(byte[] msg)
    {
        Msg = msg;
        byte[] type= Arrays.copyOfRange(Msg,0,4);
        Type =ConvertType.ByteToInt(type);
        Data = Arrays.copyOfRange(Msg,4,Msg.length);

        Log.i("FloorMessage", "数据请求id："+Type+"--数据大小"+ Data.length);
    }

    /// <summary>
    /// 获取数据id/type
    /// </summary>
    /// <returns></returns>
    public int getType()
    {
        return Type;
    }

    /// <summary>
    /// 获取主体数据
    /// </summary>
    /// <returns></returns>
    public byte[] getData()
    {

        return Data;
    }
}
