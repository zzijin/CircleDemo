package com.circle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.circle.socketclient.SocketManage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    OpenSocket openSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_inform,R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        getWritePermission();
        getInterPermission();

        openSocket=new OpenSocket();
        openSocket.start();

    }

    private void getWritePermission()
    {
        /**
         * 读写权限
         */
        String[] PERMISSIONS_STORAGE = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};    //请求状态码

        //判断是否已经赋予权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, PERMISSIONS_STORAGE.length);
            }
        }
    }

    private void getInterPermission()
    {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.INTERNET};    //请求状态码

        //判断是否已经赋予权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, PERMISSIONS_STORAGE.length);
            }
        }


    }

    public void hintGetPermission() {
        ((MainActivity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"你必须给予应用联网权限才能正常使用",Toast.LENGTH_LONG).show();

                getInterPermission();
            }
        });
    }

    public void openLogin() {
        ((MainActivity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(UserInform.getLoginState()==0) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
    }

    public void hintInternet(final int num) {
        ((MainActivity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(num<30&&num%5==0) {
                    Toast.makeText(MainActivity.this, "服务器掉线了，正在努力连接中", Toast.LENGTH_SHORT).show();
                }
                else if(num==30){
                    Toast.makeText(MainActivity.this,"多次尝试连接失败,服务器可能因神秘原因离线了,请稍后再试",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class OpenSocket extends Thread{
        @Override
        public void run() {
            while (true){
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED) {
                    hintGetPermission();
                }
                else {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int i=0;i<=30;i++) {
                if(!SocketManage.isOpenState)
                {
                    Log.i("主页连接服务器线程","正在连接服务器");
                    SocketManage.OpenClient();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(SocketManage.isOpenState){
                    Log.i("主页连接服务器线程","服务器连接成功");
                    openLogin();
                    break;
                }
                else {
                    Log.i("主页连接服务器线程","服务器失败");
                    try {
                        Thread.sleep(1000);
                        if(!SocketManage.isOpenState)
                            hintInternet(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
