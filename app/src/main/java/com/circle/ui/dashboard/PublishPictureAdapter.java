package com.circle.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.circle.R;
import com.circle.socketclient.FileData;
import com.circle.socketclient.convert.ConvertType;
import com.circle.socketclient.convert.RoundBitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class PublishPictureAdapter extends ArrayAdapter<String> {
    private PublishDongTaiActivity activity;
    private ArrayList<String> images;
    private int resourceId;

    public PublishPictureAdapter(Context context, int textViewResourceId, ArrayList<String> items, PublishDongTaiActivity act){
        super(context,textViewResourceId,items);
        activity=act;
        images=items;
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView imageView=view.findViewById(R.id.image_gridview);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        //获取屏幕长宽2
        DisplayMetrics dm=getContext().getResources().getDisplayMetrics();
        params.width=(dm.widthPixels-400)/3;
        params.height=params.width;
        imageView.setLayoutParams(params);
        Log.i("发布图片适配器","适配图片-位置:"+position+"-选中图片总数量:"+images.size());
        String path=getItem(position);

        if(path==null){
            imageView.setImageResource(R.drawable.ic_publish_add_picture);
            Log.i("发布图片适配器","适配图片-添加默认添加图片"+"-图片高度:"+imageView.getHeight()+"-图片宽度:"+imageView.getWidth());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.goPhotoAlbum();
                    Log.i("发布图片适配器","点击添加-位置:"+position);
                }
            });
        }
        else {
            Bitmap bitmap = activity.getLoacalBitmap(path);
            imageView.setImageBitmap(bitmap);
            Log.i("发布图片适配器","适配图片-位置:"+path+"-图片高度:"+bitmap.getHeight()+"-图片宽度:"+bitmap.getWidth());
        }
        return view;
    }

}
