package com.circle;

import android.graphics.Bitmap;
import android.util.Log;

import com.circle.information.Users;
import com.circle.socketclient.convert.RoundBitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class UsersListManage {
    public static Lock lock;

     private static Map<Integer,Users> userList=new HashMap<>();

     public static void addUser(int uid,Users users){
         userList.put(uid,users);
     }
     public static void addUserUID(int uid){
         userList.put(uid,null);
     }
     public static void addUserInform(int uid,Users user){
         userList.put(uid,user);
     }
     public static Bitmap getUserImage(int uid){
         return RoundBitmap.toRoundBitmap(userList.get(uid).getUserImage());
     }
     public static boolean getUserImageState(int uid){
         if(userList.get(uid)==null)
         {
             Log.i("检测加载状态","失败:用户"+uid+"头像为空");
             return false;
         }
         else
             return userList.get(uid).getUserImageState();
     }
     public static boolean addUserImageData(int uid,byte[] data){
         userList.get(uid).addUserImageData(data);
         if(getUserImageState(uid))
             return true;
         else
             return false;
     }
     public static String getUserName(int uid){
         return userList.get(uid).getUserName();
     }
     public static String getUserSign(int uid){
         return userList.get(uid).getSign();
     }
    public static int getUserWatchSBNum(int uid){
        return userList.get(uid).getWatchSBNum();
    }
    public static int getUserFaceNum(int uid){
        return userList.get(uid).getFaceNum();
    }
    public static int getUserDongTaiNum(int uid){
        return userList.get(uid).getDongTaiNum();
    }
    public static boolean getUserInformIsInList(int uid){
         return userList.containsKey(uid);
    }
}
