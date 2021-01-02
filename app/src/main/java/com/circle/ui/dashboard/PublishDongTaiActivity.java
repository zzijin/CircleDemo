package com.circle.ui.dashboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.R;
import com.circle.SetMyInformActivity;
import com.circle.getPhotoFromPhotoAlbum;
import com.circle.information.PublishDongTai;
import com.circle.socketclient.SendDataManage;
import com.circle.socketclient.convert.RoundBitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.circle.SetMyInformActivity.getLoacalBitmap;

public class PublishDongTaiActivity extends AppCompatActivity {
    private PublishPictureAdapter picAdapter;
    private int type;
    public ArrayList<String> dataPaths;
    private GridView pic;
    private ListView music;
    private ListView video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_publish_dong_tai);

        Intent intent=getIntent();
        String message=intent.getStringExtra("type");
        Log.i("私信适配器","获得值："+message);
        type= Integer.valueOf(message);

        dataPaths=new ArrayList<>();
        dataPaths.add(null);

        final EditText title=findViewById(R.id.edit_publish_dong_tai_activity_title);
        final EditText text=findViewById(R.id.edit_publish_dong_tai_activity_boby);
        pic=findViewById(R.id.gridview_publish_dong_tai_activity_picture);
        music=findViewById(R.id.list_publish_dong_tai_activity_music);
        video=findViewById(R.id.list_publish_dong_tai_activity_video);
        View back=findViewById(R.id.image_publish_dong_tai_activity_back);

        switch (type){
            case 0:text.setVisibility(View.VISIBLE);break;
            case 1:{
                pic.setVisibility(View.VISIBLE);
                PicUpData();
            }break;
            case 2:{
                music.setVisibility(View.VISIBLE);
            }break;
            case 3:{
                video.setVisibility(View.VISIBLE);
            }break;
        }

        TextView publish=findViewById(R.id.text_publish_dong_tai_activity_save);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title=findViewById(R.id.edit_publish_dong_tai_activity_title);

                switch (type){
                    case 0:{
                        if(title.getText().toString().length()>0&&text.getText().toString().length()>0) {
                            PublishDongTai publishDongTai = new PublishDongTai(type, title.getText().toString(), text.getText().toString());
                            int index = PublishDongTaiManage.AddPublishDongTais(publishDongTai);
                            SendDataManage.SendMyDongTaiInform(index);
                            onBackPressed();
                        }
                        else {
                            Toast.makeText(PublishDongTaiActivity.this,"请填写完毕再点击保存",Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                    case 1:{
                        ArrayList<String> paths=new ArrayList<>();
                        paths.addAll(dataPaths);
                        paths.remove(0);
                        if(paths.size()>0&&title.getText().toString().length()>0){
                            PublishDongTai publishDongTai = new PublishDongTai(type, title.getText().toString(), paths);
                            int index = PublishDongTaiManage.AddPublishDongTais(publishDongTai);
                            SendDataManage.SendMyDongTaiInform(index);
                            onBackPressed();
                        }
                        else {
                            Toast.makeText(PublishDongTaiActivity.this,"请选择图片及填写标题完毕后再点击保存",Toast.LENGTH_LONG).show();
                        }
                    };
                    break;
                    case 2:{

                    };
                    break;
                    case 3:{

                    };
                    break;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void PicUpData(){
        picAdapter=new PublishPictureAdapter(PublishDongTaiActivity.this,R.layout.gridview_picture,dataPaths,this);
        pic.setAdapter(picAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photoPath;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //拍照相机返回值
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                photoPath = String.valueOf(cameraSavePath);
//            } else {
//                photoPath = uri.getEncodedPath();
//            }
//            Log.d("拍照返回图片路径:", photoPath);
//            Bitmap bitmap = getLoacalBitmap(photoPath);
//            image.setImageBitmap(RoundBitmap.toRoundBitmap(bitmap));
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Log.i("发布图片", "获取图片返回值:"+photoPath);
            dataPaths.add(photoPath);
            PicUpData();
//            Bitmap bitmap = getLoacalBitmap(photoPath);
//            Log.i("设置头像","-图片path:"+photoPath);
//            image.setImageBitmap(RoundBitmap.toRoundBitmap(bitmap));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            Bitmap bitmap=BitmapFactory.decodeStream(fis);
            fis.close();
            Log.i("发布图片适配器","查找图片-位置:"+url+"-图片高度:"+bitmap.getHeight()+"-图片宽度:"+bitmap.getWidth());
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
    public void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    //激活相机操作(无效)
    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(PublishDongTaiActivity.this, "com.example.hxd.pictest.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        PublishDongTaiActivity.this.startActivityForResult(intent, 1);
    }

    //申请相机权限
    private void getCameraPermission()
    {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.CAMERA};    //请求状态码

        //判断是否已经赋予权限
        if (ActivityCompat.checkSelfPermission(PublishDongTaiActivity.this, Manifest.permission.CAMERA)
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
