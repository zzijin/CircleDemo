package com.circle.ui.dashboard;

import android.content.Context;
import android.text.PrecomputedText;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.circle.R;
import com.circle.socketclient.FileData;
import com.circle.socketclient.convert.ConvertType;

public class PictureAdapter extends ArrayAdapter<FileData>{
    private int resourceId;

    public PictureAdapter(Context context, int textViewResourceId, FileData[] items){
        super(context,textViewResourceId,items);
        Log.i("图片适配器","构造函数执行-图片数量:"+items.length);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);
        FileData fileData=getItem(position);
        Log.i("图片适配器","适配图片位置"+position+"-名字:"+fileData.getFileName());
        ImageView imageView=view.findViewById(R.id.image_gridview);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        //获取屏幕长宽
        DisplayMetrics dm=getContext().getResources().getDisplayMetrics();
        params.width=(dm.widthPixels-80)/3;
        params.height=params.width;
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(ConvertType.BytesToBitmap(fileData.getData()));
        return view;
    }
}