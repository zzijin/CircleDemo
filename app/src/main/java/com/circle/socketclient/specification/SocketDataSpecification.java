package com.circle.socketclient.specification;

import android.os.Environment;

import com.circle.socketclient.convert.ConvertType;

import java.io.File;

/**
 * 规定socket传输中的各项参数
 */
public final class SocketDataSpecification {
    /// <summary>
    /// 规定IP
    /// </summary>
    static public String IP = "";
    //测试IP10.127.105.246
    static public String TestIP="";
    /// <summary>
    /// 规定端口号
    /// </summary>
    static public int Port = 12345;
    /// <summary>
    /// 规定conn类缓存区大小
    /// </summary>
    static public int BuffSize = 200 * 1024;
    /// <summary>
    /// 规定socket发送文件数据单个数据包大小限制
    /// </summary>
    static public int FileDataSize = 20 * 1024;
    /// <summary>
    /// 规定上传文件大小限制
    /// </summary>
    static public int FileMaxSize = 100 * 1024 * 1024;
    /// <summary>
    /// 规定上传图片大小限制
    /// </summary>
    static public int PictureMaxSize = 10 * 1024 * 1024;
    /// <summary>
    /// 规定数据包起始标识符
    /// </summary>
    static public byte DataStartTag = (byte)Integer.parseInt("98",16);
    /// <summary>
    /// 规定数据包结束标识符
    /// </summary>
    static public byte DataEndTag = (byte)Integer.parseInt("99",16);
    /// <summary>
    /// 规定服务器清理数据界限
    /// </summary>
    static public int ClearDataSize = 1 * 1024 * 1024;
    //系统缓存目录
    static public File CachePath= Environment.getDownloadCacheDirectory();
    //系统相机照片和视频目录
    static public File CameraPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    //系统音乐目录
    static public File MusicPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    //系统下载目录
    static public File DownPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    //系统图片目录
    static public File PicturesPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    //系统电影目录
    static public File MoviesPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
}
