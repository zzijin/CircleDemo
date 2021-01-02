package com.circle.socketclient;

import android.util.Log;

import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.specification.SocketDataSpecification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileData {
    // 文件名
    public String FileName;
    // 文件扩展名
    public String ExtensionName;
    // 文件总大小
    public int DataSize;
    //文件类型
    public int FileType;
    // 发送/接收文件路径
    public File FilePath;
    // socket传输文件个数
    public int Count;
    // 传输位置
    public int StartIndex=0;
    // 写出数据
    ObjectOutputStream objectOutputStream;
    FileOutputStream fileOutputStream;
    //写入数据
    //ObjectInputStream objectInputStream;
    FileInputStream fileInputStream;

    /// <summary>
    /// 发送文件数据构造函数
    /// </summary>
    public FileData(File file,int type)
    {
        if (fileInputStream==null)
        {
            FileType=type;
            FilePath = file;
            StartIndex=1;
            FileName = file.getName().substring(0,file.getName().lastIndexOf('.')-1);
            ExtensionName = file.getName().substring(file.getName().lastIndexOf('.'));
            Log.i("获取文件:","-文件名:"+FileName+"-文件后缀:"+ExtensionName+"-文件路径:"+file.getPath());

            try {
                fileInputStream=new FileInputStream(FilePath);
                Log.i("获取文件:","-1");
                DataSize =(int)FilePath.length();
                Log.i("获取文件:","-2-大小:"+DataSize);
                Count = DataSize / SocketDataSpecification.FileDataSize + 1;
                Log.i("获取文件:","-3-数量:"+Count);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                Log.i("文件读取错误",e.getLocalizedMessage());
            } catch (IOException e) {
                Log.i("文件读取错误",e.getLocalizedMessage());
            }
        }
    }
    /// <summary>
    /// 读取文件
    /// </summary>
    /// <param name="path">文件路径</param>
    /// <returns>读取的文件</returns>
    public byte[] SendFileData()
    {
        if (StartIndex > Count)
        {
            return null;
        }

        try {
            fileInputStream=new FileInputStream(FilePath);
            byte[] data;
            if (StartIndex == Count)
            {
                data = new byte[DataSize - ((StartIndex - 1) * SocketDataSpecification.FileDataSize)];
            }
            else
            {
                data = new byte[SocketDataSpecification.FileDataSize];
            }

            try {
                fileInputStream.skip((StartIndex - 1) * SocketDataSpecification.FileDataSize);
                fileInputStream.read(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ++StartIndex;

            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /// <summary>
    /// 接受文件数据构造函数
    /// </summary>
    /// <param name="filename">文件名</param>
    /// <param name="extensionname">文件扩展名</param>
    /// <param name="datasize">文件总大小</param>
    /// <param name="count">文件总个数</param>
    ///<param name="fileType">文件类型</param>
    public FileData(String filename, String extensionname, int datasize, int count,int fileType)
    {
        FileName = filename;
        ExtensionName = extensionname;
        Count = count;
        DataSize = datasize;
        FileType=fileType;
        StartIndex = 1;
        FilePath=new File(GetFileType(),FileName+ExtensionName);
        Log.i("文件基本类","已创建新的文件类,文件名:"+filename);
    }

    /// <summary>
    /// 接收文件到本地
    /// </summary>
    /// <param name="data">文件数据</param>
    /// <returns>是否接受完全数据</returns>
    public boolean AcceptFileData(byte[] data)
    {
        if (fileOutputStream == null||objectOutputStream==null)
        {
            try {
                fileOutputStream=new FileOutputStream(FilePath);
                objectOutputStream=new ObjectOutputStream(fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            objectOutputStream.write(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("文件协助类","写入文件数据:" + data.length);

        if (StartIndex == Count)
        {
            try {
                fileOutputStream.close();
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            objectOutputStream=null;
            fileOutputStream=null;

            Log.i("文件协助类","接收完成");

            return true;
        }

        Log.i("文件协助类","接收文件数+1，还需:" + (Count - StartIndex) + "个数据包");

        ++StartIndex;

        return false;
    }

    //
    public FileData(String filename, String extensionname, int fileType,byte[] data)
    {
        FileName = filename;
        ExtensionName = extensionname;
        FileType=fileType;
        Data=data;

        StartIndex=2;
        Count=1;
    }
    //
    public FileData(String path,int type){
        FileType=type;
        FilePath = new File(path);
        StartIndex=1;
        FileName = FilePath.getPath().substring(0,FilePath.getPath().lastIndexOf("."));
        ExtensionName = FilePath.getPath().substring(FilePath.getPath().lastIndexOf("."));

        try {
            fileInputStream=new FileInputStream(FilePath);
            DataSize = (int)FilePath.length();
            Data=new byte[DataSize];
            Count = DataSize / SocketDataSpecification.FileDataSize + 1;
            StartIndex=Count+1;
            fileInputStream.read(Data);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    byte[] Data;
    //获取文件数据到内存
    public void AddBytesToData(byte[] bytes){
        if(StartIndex==1)
        {
            Data=new byte[DataSize];
        }
        if(StartIndex<=Count) {
            for (int i = 0; i < bytes.length; i++) {
                Data[(StartIndex - 1) * SocketDataSpecification.FileDataSize + i] = bytes[i];
            }
            StartIndex++;
        }
    }
    //获取数据
    public byte[] getData() {
        return Data;
    }
    //验证数据是否完整
    public boolean getDataState(){
        return StartIndex>Count;
    }

    public String getFileName(){
        return FileName;
    }

    public File GetFileType()
    {
        switch (FileType){
            //缓存文件
            case 0:return SocketDataSpecification.CachePath;
            //图片
            case 1:return SocketDataSpecification.PicturesPath;
            //视频
            case 2:return SocketDataSpecification.MoviesPath;
            //相机照片和视频
            case 3:return SocketDataSpecification.CameraPath;
            //下载
            case 4:return SocketDataSpecification.DownPath;
            //音乐
            case 5:return SocketDataSpecification.MusicPath;
            //其他(暂放于缓存)
            default:return SocketDataSpecification.CachePath;
        }
    }
}
