package com.circle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.convert.RoundBitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SetMyInformActivity extends AppCompatActivity {
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_my_inform);

        View save=findViewById(R.id.text_set_my_inform_save);
        image=findViewById(R.id.image_set_my_inform_image);
        EditText name=findViewById(R.id.edit_set_my_inform_username);
        EditText sign=findViewById(R.id.edit_set_my_inform_sign);
        EditText password=findViewById(R.id.edit_set_my_inform_password);
        EditText uid=findViewById(R.id.edit_set_my_inform_uid);

        image.setImageBitmap(UserInform.getUserBitImage());
        name.setText(UserInform.getUserName());
        sign.setText(UserInform.getUserSign());
        password.setText(UserInform.getPassWord());
        uid.setText(UserInform.getuId()+"");

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"相册", "相机"};//创建item
                AlertDialog alertDialog = new AlertDialog.Builder(SetMyInformActivity.this)
                        .setTitle("请选择你想更换头像的方式")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:{
                                        goPhotoAlbum();
                                    };
                                    break;
                                    case 1:{
                                        getCameraPermission();
                                    };
                                    break;
                                }
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photoPath;
        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }
            Log.d("拍照返回图片路径:", photoPath);
            Bitmap bitmap = getLoacalBitmap(photoPath);
            image.setImageBitmap(RoundBitmap.toRoundBitmap(bitmap));
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Bitmap bitmap = getLoacalBitmap(photoPath);
            Log.i("设置头像","-图片path:"+photoPath+"-图片高度:"+bitmap.getHeight()+"-图片宽度:"+bitmap.getWidth());
            image.setImageBitmap(RoundBitmap.toRoundBitmap(bitmap));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            Bitmap bitmap=BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File cameraSavePath;//拍照照片路径
    private Uri uri;

    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    //激活相机操作
    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(SetMyInformActivity.this, "com.example.hxd.pictest.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        SetMyInformActivity.this.startActivityForResult(intent, 1);
    }

    //申请相机权限
    private void getCameraPermission()
    {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.CAMERA};    //请求状态码

        //判断是否已经赋予权限
        if (ActivityCompat.checkSelfPermission(SetMyInformActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, PERMISSIONS_STORAGE.length);
            }
        }
        else {
            goCamera();
        }
    }
}
