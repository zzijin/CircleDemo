package com.circle.socketclient;

import android.util.Log;

import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.specification.SocketDataSpecification;

import java.io.IOException;
import java.net.Socket;

public class Conn {
    /// <summary>
    /// 缓存区大小
    /// </summary>
    public int BUFFER_SIZE = SocketDataSpecification.BuffSize;
    /// <summary>
    /// Socket连接
    /// </summary>
    public Socket socket;
    /// <summary>
    /// 缓存字节
    /// </summary>
    public byte[] readBuff;
    /// <summary>
    /// 缓存已使用大小
    /// </summary>
    public int buffCount = 0;
    /// <summary>
    /// 拆包类
    /// </summary>
    public UnpackagingMessage unpackag;
    /// <summary>
    /// 数据处理类
    /// </summary>
    public SocketDataManage dataManage;
    /// <summary>
    /// 处理数据线程
    /// </summary>
    private ProcesseDataThread pro;
    /// <summary>
    /// 服务器运行状态(用于结束安全结束所有线程;0:正在运行,1:即将缓慢关闭(将通知所有客户端后并完成数据保存后关闭),2:所有线程均已关闭，通知页面执行关闭操作)
    /// </summary>
    public static long EndServer = 0;

    /// <summary>
    /// 构造函数
    /// </summary>
    public Conn(Socket socket)
    {
        this.socket = socket;
        readBuff = new byte[BUFFER_SIZE];

        buffCount = 0;
        unpackag = new UnpackagingMessage(this);
        dataManage = new SocketDataManage(this);

        //开启线程
        pro=new ProcesseDataThread();
        pro.start();
    }



    /// <summary>
    /// 缓存区剩余的字节
    /// </summary>
    /// <returns></returns>
    public int BuffRemain()
    {
        return BUFFER_SIZE - buffCount;
    }

    /// <summary>
    /// 断开连接
    /// </summary>
    public void Close()
    {
        unpackag = null;
        dataManage = null;

        Log.i("Conn","服务器断开连接");
        try {
            socket.close();
            SocketManage.isOpenState=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /// <summary>
    /// 设置缓存区已使用字符数(结束异步socket挂起时使用)
    /// </summary>
    /// <param name="buffcount">结束异步socket返回值</param>
    public void AddBuffCount(int buffcount)
    {
        buffCount += buffcount;
    }

    /// <summary>
    /// 处理拆解数据
    /// </summary>
    public boolean UnpackagMessage()
    {
        unpackag.LoadMsg(ConvertType.CutByte(readBuff, 0, buffCount));
        readBuff = new byte[BUFFER_SIZE];
        buffCount = 0;
        return unpackag.ProcesseMsg()!=null;
    }

    /// <summary>
    /// 将数据载入内存
    /// </summary>
    public void LoadPackData()
    {
        unpackag.lock.lock();
        unpackag.LoadMsg(ConvertType.CutByte(readBuff, 0, buffCount));
        unpackag.lock.unlock();
        Log.i("Conn","载入数据大小:" + buffCount);
        buffCount = 0;
    }

    class ProcesseDataThread extends Thread {

        @Override
        public void run() {
            Log.i("Conn", "处理数据线程");
            while (true) {
                Log.i("监测处理进程","开始处理");
                unpackag.lock.lock();
                FloorMessage f = unpackag.ProcesseMsg();
                unpackag.lock.unlock();
                Log.i("监测处理进程","拆解数据");
                if (f != null) {
                    //PengFloorMeaage.Add(f);
                    dataManage.DataManager(f);
                    Log.i("监测处理进程","处理数据");
                }

                if(unpackag.Msg.size()>0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
