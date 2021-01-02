package com.circle.socketclient;

import android.util.Log;

import com.circle.socketclient.specification.SocketDataSpecification;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

public class SocketManage {
    public static Conn conn;
    private static OpenClientThread open;
    private static ReceiveThread receive;
    public static boolean isOpenState=false;
    private static SendThread sendThread=null;

    public static void OpenClient(){
        open=new OpenClientThread();
        open.start();
    }

    static class OpenClientThread extends Thread{
        @Override
        public void run() {
            try {
                Log.i("打开连接类","尝试连接正式服务器");
                Socket socket=new Socket();
                socket.connect(new InetSocketAddress(SocketDataSpecification.IP,SocketDataSpecification.Port),500);
                conn = new Conn(socket);
                isOpenState=true;
                Log.i("打开连接类","连接正式服务器成功");
                receive=new ReceiveThread();
                receive.start();
            }
            catch (IOException e) {
                Log.i("打开连接类","正式服务器连接失败");

                try {
                    Log.i("打开连接类","尝试连接测试服务器");
                    Socket socket=new Socket();
                    socket.connect(new InetSocketAddress(SocketDataSpecification.TestIP,SocketDataSpecification.Port),500);
                    conn = new Conn(socket);
                    isOpenState=true;
                    Log.i("打开连接类","连接测试服务器成功");
                    receive=new ReceiveThread();
                    receive.start();
                }
                catch (IOException r){
                    Log.i("打开连接类","测试服务器连接失败"+r.toString());
                }
            }
        }
    }

    static class ReceiveThread extends Thread{
        @Override
        public void run() {
            Log.i("打开接收类","建立接收线程");
            while (true) {
                Log.i("监测接收进程","开始检测");
                int count = 0;
                try {
                    count = conn.socket.getInputStream().read(conn.readBuff);
                    Log.i("监测接收进程","读取数据");
                    if(count==0)
                    {
                        conn.Close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.AddBuffCount(count);
                Log.i("监测接收进程","添加count");
                conn.LoadPackData();
                Log.i("监测接收进程","添入数据");
            }
        }
    }

    //发送消息
    public static void SendMessage(int type,byte[] data){
        if (sendThread==null) {
            sendThread=new SendThread();
            sendThread.start();
        }
        sendThread.addData(PackagData(type, data));
    }

    static class SendThread extends Thread{
        public LinkedList<byte[]> sendData;

        public SendThread(){
            sendData=new LinkedList<>();
        }
        @Override
        public void run() {
            while (isOpenState) {
                if(sendData.size()>0){
                    try {
                        conn.socket.getOutputStream().write(sendData.get(0));
                        sendData.remove(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void addData(byte[] data){
            sendData.add(data);
        }
    }

    public static byte[] PackagData(int type,byte[] data){
        PackagingMessage packag=new PackagingMessage();
        return packag.GetMsg(type,data);
    }
}
