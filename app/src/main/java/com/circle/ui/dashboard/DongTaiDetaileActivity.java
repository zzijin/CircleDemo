package com.circle.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.circle.R;
import com.circle.UsersListManage;
import com.circle.information.UserData;

public class DongTaiDetaileActivity extends AppCompatActivity {
    private UserData userData;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dong_tai_detailed);

        Intent intent=getIntent();
        String message=intent.getStringExtra("DongTaiIndex");
        index=Integer.valueOf(message).intValue();
        userData=DongTaiListManage.getDongTai(index);

        ImageView userimage = findViewById(R.id.image_dashboard_userimage);
        TextView username = findViewById(R.id.text_dashboard_username);
        TextView usertime = findViewById(R.id.text_dashboard_usertime);
        //ImageView usercard=indViewById(R.id.image_dashboard_usercard);
        TextView dongtaititle =findViewById(R.id.text_dashboard_dongtaititle);
        View textcontrol = findViewById(R.id.include_dashboard_text);
        View picturecontrol = findViewById(R.id.include_dashboard_picture);
        View musiccontrol = findViewById(R.id.include_dashboard_music);
        View videocontrol = findViewById(R.id.include_dashboard_video);
        View lineshare=findViewById(R.id.line_listview_dashboard_share);
        View linecomment=findViewById(R.id.line_listview_dashboard_comment);
        View linezan=findViewById(R.id.line_listview_dashboard_zan);
        TextView textshare=findViewById(R.id.text_listview_dashboard_share);
        TextView textcomment=findViewById(R.id.text_listview_dashboard_comment);
        TextView textzan=findViewById(R.id.text_listview_dashboard_zan);

        if (UsersListManage.getUserImageState(userData.Uid))
            userimage.setImageBitmap(UsersListManage.getUserImage(userData.Uid));

        username.setText(UsersListManage.getUserName(userData.Uid));
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
            TextView boby = findViewById(R.id.text_control_boby);
            boby.setText("\t\t"+userData.DongTaiData.getTextData());
        }
        //分享图片
        else if (userData.Type == 1) {
            Log.i("动态适配器","当前为图片动态-编号"+userData.DongTaiNumber);
            picturecontrol.setVisibility(View.VISIBLE);
            PictureAdapter pictureAdapter=new PictureAdapter(DongTaiDetaileActivity.this,R.layout.gridview_picture,userData.DongTaiData.getBobyData());
            GridView gridView=findViewById(R.id.gridview_picture);
            gridView.setAdapter(pictureAdapter);
        }
        //分享音乐
        else if (userData.Type == 2) {
            musiccontrol.setVisibility(View.VISIBLE);
            TextView musicname = findViewById(R.id.text_control_musicname);
            musicname.setText(userData.DongTaiData.getDataName(0));
        }
        //分享视频
        else if (userData.Type == 3) {
            videocontrol.setVisibility(View.VISIBLE);
        }
    }


}
