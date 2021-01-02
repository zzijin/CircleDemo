package com.circle.ui.dashboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.circle.R;
import com.circle.ui.inform.InformActivity;

public class PublishDongTaiDialog extends Dialog {

    public PublishDongTaiDialog(@NonNull Context context) {
        super(context);
    }

    public PublishDongTaiDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_publish_dong_tai);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width=WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        View text=findViewById(R.id.line_dialog_publish_dong_tai_text);
        View picture=findViewById(R.id.line_dialog_publish_dong_tai_picture);
        View music=findViewById(R.id.line_dialog_publish_dong_tai_music);
        View video=findViewById(R.id.line_dialog_publish_dong_tai_video);
        final View close=findViewById(R.id.image_dialog_publish_dong_tai_close);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PublishDongTaiActivity.class);
                String message=String.valueOf(0);
                intent.putExtra("type",message);
                getContext().startActivity(intent);
                cancel();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PublishDongTaiActivity.class);
                String message=String.valueOf(1);
                intent.putExtra("type",message);
                getContext().startActivity(intent);
                cancel();
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PublishDongTaiActivity.class);
                String message=String.valueOf(2);
                intent.putExtra("type",message);
                getContext().startActivity(intent);
                cancel();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PublishDongTaiActivity.class);
                String message=String.valueOf(3);
                intent.putExtra("type",message);
                getContext().startActivity(intent);
                cancel();
            }
        });
        //关闭对话框
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //旋转图标
                ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(close,"rotation",0,-90);
                final AnimatorSet set2 = new AnimatorSet();
                set2.playSequentially(objectAnimator2);
                set2.setDuration(500);
                set2.start();
                return false;
            }
        });
    }
}
