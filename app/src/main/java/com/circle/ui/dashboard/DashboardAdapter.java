package com.circle.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.circle.R;
import com.circle.UserInform;
import com.circle.UsersListManage;
import com.circle.information.UserData;
import com.circle.socketclient.FileData;
import com.circle.socketclient.convert.ConvertType;
import com.circle.ui.inform.InformActivity;

import java.util.ArrayList;
import java.util.logging.Handler;

public class DashboardAdapter extends ArrayAdapter<UserData> {
    private int resourceId;

    public DashboardAdapter(Context context, int textViewResourceId){
        super(context,textViewResourceId,DongTaiListManage.getDongTaiList());
        Log.i("动态适配器","构造函数执行");
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i("动态适配器","数据适配执行");
        View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);

        if(DongTaiListManage.getDongTaiNum()>0) {

            Log.i("动态适配器","当前共"+getCount()+"条数据,正在适配第"+position+"条数据");
            UserData userData = getItem(position);

            ImageView userimage = view.findViewById(R.id.image_dashboard_userimage);
            TextView username = view.findViewById(R.id.text_dashboard_username);
            TextView usertime = view.findViewById(R.id.text_dashboard_usertime);
            //ImageView usercard=view.findViewById(R.id.image_dashboard_usercard);
            TextView dongtaititle = view.findViewById(R.id.text_dashboard_dongtaititle);
            View textcontrol = view.findViewById(R.id.include_dashboard_text);
            View picturecontrol = view.findViewById(R.id.include_dashboard_picture);
            View musiccontrol = view.findViewById(R.id.include_dashboard_music);
            View videocontrol = view.findViewById(R.id.include_dashboard_video);
            View lineshare=view.findViewById(R.id.line_listview_dashboard_share);
            View linecomment=view.findViewById(R.id.line_listview_dashboard_comment);
            View linezan=view.findViewById(R.id.line_listview_dashboard_zan);
            TextView textshare=view.findViewById(R.id.text_listview_dashboard_share);
            TextView textcomment=view.findViewById(R.id.text_listview_dashboard_comment);
            TextView textzan=view.findViewById(R.id.text_listview_dashboard_zan);

            Log.i("动态适配器","适配用户"+userData.Uid+"头像和昵称");
            if(userData.Uid== UserInform.getuId()){
                userimage.setImageBitmap(UserInform.getUserBitImage());
                username.setText(UserInform.getUserName());
            }
            else {
                if (UsersListManage.getUserImageState(userData.Uid))
                    userimage.setImageBitmap(UsersListManage.getUserImage(userData.Uid));
                username.setText(UsersListManage.getUserName(userData.Uid));
            }

            usertime.setText("发布时间:"+userData.UserTime);
            dongtaititle.setText("\t\t"+userData.UserTitle);
            if(userData.ShareNumber>0){
                textshare.setText(userData.ShareNumber);
            }
            if (userData.CommentNumber>0){
                textcomment.setText(userData.CommentNumber);
            }
            if(userData.ShareNumber>0){
                textcomment.setText(userData.CommentNumber);
            }

            //文字动态
            if (userData.Type == 0) {
                textcontrol.setVisibility(View.VISIBLE);
                TextView boby = view.findViewById(R.id.text_control_boby);
                boby.setText("\t\t"+userData.DongTaiData.getTextData());
            }
            //分享图片
            else if (userData.Type == 1) {
                Log.i("动态适配器","当前为图片动态-编号"+userData.DongTaiNumber);
                picturecontrol.setVisibility(View.VISIBLE);
                PictureAdapter pictureAdapter=new PictureAdapter(getContext(),R.layout.gridview_picture,userData.DongTaiData.getBobyData());
                GridView gridView=view.findViewById(R.id.gridview_picture);
                gridView.setAdapter(pictureAdapter);
            }
            //分享音乐
            else if (userData.Type == 2) {
                musiccontrol.setVisibility(View.VISIBLE);
                TextView musicname = view.findViewById(R.id.text_control_musicname);
                musicname.setText(userData.DongTaiData.getDataName(0));
            }
            //分享视频
            else if (userData.Type == 3) {
                videocontrol.setVisibility(View.VISIBLE);
            }

            lineshare.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });

            linecomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), DongTaiDetaileActivity.class);
                    String message=String.valueOf(position);
                    intent.putExtra("DongTaiIndex",message);
                    getContext().startActivity(intent);
                }
            });
            linezan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else {
            Log.i("动态适配器","当前无动态数据");
        }
        return view;
    }
}
