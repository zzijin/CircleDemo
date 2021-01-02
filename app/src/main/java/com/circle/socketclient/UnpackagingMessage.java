package com.circle.socketclient;

import android.util.Log;

import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.specification.SocketDataSpecification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class UnpackagingMessage {
    /// <summary>
    /// 包头(代表数据大小,占4字节)
    /// </summary>
    private int Size;


    /// <summary>
    /// 输入的原始数据
    /// </summary>
    public LinkedList<Byte> Msg;

    //给Msg列表加锁
    public Lock lock;

    /// <summary>
    ///
    /// </summary>
    private Conn conn;

    /// <summary>
    /// 集合载入繁忙时使用，用于指示解析读取位置
    /// </summary>
    private int MsgStartIndex;

    /// <summary>
    /// 构造函数(初始化数据)
    /// </summary>
    public UnpackagingMessage(Conn c)
    {
        lock=new ReentrantLock();
        conn = c;
        Msg = new LinkedList<>();
        MsgStartIndex = 0;
    }
    /// <summary>
    /// 载入数据
    /// </summary>
    /// <param name="msg">连接池中已读取的数据</param>
    public void LoadMsg(byte[] msg)
    {
        //int expandNumber=msg.length;
        //一次性拓展列表空间，可以大幅度节约时间
        //Msg.ensureCapacity(expandNumber);
        for(int i=0;i<msg.length;i++)
        {
            Msg.add(msg[i]);
        }
        Log.i("解包类","载入的数据大小：" + msg.length);
    }

    /// <summary>
    /// 处理数据(待优化代码)
    /// </summary>
    /// <returns>返回完成拆包后的数据</returns>
    public FloorMessage ProcesseMsg()
    {
        if(Msg.size()<1)
        {
            return null;
        }

        if (MsgStartIndex < Msg.size())
        {
            if (Msg.get(MsgStartIndex) == SocketDataSpecification.DataStartTag)
            {
                if (MsgStartIndex + 5 <= Msg.size())
                {
                    Log.i("监测处理进程","起始符正确，未丢失数据");

                    FloorMessage floorMsg = new FloorMessage();

                    //跳过开头标识符

                    byte[] size = SubList((MsgStartIndex + 1), 4);

                    Size = ConvertType.ByteToInt(size);

                    Log.i("监测处理进程","包头记录大小：" + Size);

                    if (Msg.size() >= Size + 5 + MsgStartIndex)
                    {
                        //数据为完整数据则进行拆包
                        Log.i("监测处理进程","数据为完整数据进行拆包");
                        //取出数据
                        byte[] floordate=SubList((MsgStartIndex + 5), Size);
                        Log.i("监测处理进程","已取出数据");
                        if(floordate!=null) {
                            floorMsg.DisassembleMessage(floordate);
                            Log.i("监测处理进程","已拆解数据");
                            if (Msg.get(MsgStartIndex + 5 + Size) == SocketDataSpecification.DataEndTag)
                            {
                                MsgStartIndex = MsgStartIndex + 6 + Size;

                                CleanseMsgList();

                                return floorMsg;
                            }
                            else
                            {
                                Log.i("监测处理进程","数据包结尾标识符错误,将开始诊断原因");
                            }
                        }
                        else {
                            Log.i("监测处理进程","MSG列表被占用");
                        }
                    }
                    else
                    {
                        Log.i("监测处理进程","Size正确数据未接收完全，等待后续传输完毕");
                    }
                }
                else
                {
                    Log.i("监测处理进程","起始符正确数据未接收完全，等待后续传输完毕");
                }
            }
            else
            {
                //诊断数据数据:1.数据多余:向后查找起始标识，删除中间的多余数据 2.数据缺失:尝试向客户端发送命令，以重新发送数据
                Log.i("监测处理进程","起始字节错误,将开始诊断原因");
            }
        }
        return null;
    }

    /// <summary>
    /// 清理数据池
    /// </summary>
    public void CleanseMsgList()
    {
        Log.i("监测处理进程", "开始清理");
        for (int i=0;i<MsgStartIndex;i++) {
            Msg.remove(0);
        }
        MsgStartIndex = 0;
        Log.i("监测处理进程", "清理完毕");

        if(Msg.size()<1){
            return;
        }
        if(Msg.get(0)==SocketDataSpecification.DataStartTag)
        {
            return;
        }

        //移除信息标识符前的冗余信息
        for (int i = 0; i < Msg.size(); i++) {
            if (Msg.get(i) == SocketDataSpecification.DataStartTag) {
                if (i == Msg.size() - 1) {
                    Msg.clear();
                } else {
                    for (int o = 0; o < i; o++) {
                        Msg.remove(0);
                    }
                }
                break;
            }
        }
        Log.i("监测处理进程", "清理完毕");
    }

    public byte[] SubList(int Start,int Count)
    {
        List<Byte> subList = Msg.subList(Start,Start+Count);
        Byte[] sub=subList.toArray(new Byte[Count]);

        byte[] su=new byte[sub.length];
        for(int i=0;i<sub.length;i++){
            su[i]=sub[i].byteValue();
        }

        return su;
    }
}
