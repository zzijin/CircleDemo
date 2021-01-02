package com.circle.socketclient;

import android.util.Log;

import com.circle.socketclient.convert.ConvertType;

public class PackagingMessage {
    /// <summary>
    /// 包头(数据大小,占4字节)
    /// </summary>
    private byte[] Size;
    /// <summary>
    /// 内容主体
    /// </summary>
    private FloorMessage floorMsg;
    /// <summary>
    /// 封装成功后的数据
    /// </summary>
    private byte[] Msg;

    /// <summary>
    /// 构造函数
    /// </summary>

    public PackagingMessage()
    {
        floorMsg = new FloorMessage();
    }

    /// <summary>
    /// 封装数据
    /// </summary>
    /// <param name="id">请求id/类别</param>
    /// <param name="data">数据主体</param>
    public byte[] GetMsg(int id,byte[] data)
    {
        floorMsg.GeneratMessage(id, data);
        Log.i("封包","封装数据id:" + id);
        int size = floorMsg.GetFloorMessageSize();
        Size = ConvertType.IntToByte(size);
        Msg = ConvertType.DataCopyByte(Size, floorMsg.Msg);
        Log.i("封包","合成完整数据包头大小:"+size+"--实际大小:"+floorMsg.Msg.length);
        return Msg;

    }
}
