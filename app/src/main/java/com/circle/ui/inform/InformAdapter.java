package com.circle.ui.inform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.circle.R;
import com.circle.UsersListManage;
import com.circle.information.ChatData;

import java.util.concurrent.locks.Lock;

public class InformAdapter extends ArrayAdapter<Integer> {
    private int resourceId;

    public InformAdapter(Context context, int textViewResourceId){
        super(context,textViewResourceId,ChatDataManage.sork);
        Log.i("私信适配器","私信执行构造函数,数量:"+ChatDataManage.sork.size());
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);

        Log.i("私信适配器","私信数据正在加载,数量:"+ChatDataManage.sork.size()+"-真实数据:"+ChatDataManage.Chats.size());
        if(ChatDataManage.sork.size()>0) {

            ChatData chatData=ChatDataManage.Chats.get(getItem(position)).chatData.get(0);

            ImageView image = view.findViewById(R.id.image_chatlist_user);
            TextView name=view.findViewById(R.id.text_chatlist_username);
            TextView time=view.findViewById(R.id.text_chatlist_time);
            TextView boby=view.findViewById(R.id.text_chatlist_inform);

            if(UsersListManage.getUserImageState(chatData.ToUID)){
                image.setImageBitmap(UsersListManage.getUserImage(chatData.ToUID));
            }
            Log.i("私信适配器","获取"+chatData.ToUID+"数据");
            name.setText(UsersListManage.getUserName(chatData.ToUID));
            time.setText(chatData.SendTime);
            if(chatData.Images.length==0) {
                boby.setText(chatData.StrData);
            }

            View line=view.findViewById(R.id.line_chatlist);

            line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(view.getContext(),InformActivity.class);
                    int index=getItem(position);
                    String message=String.valueOf(index);
                    Log.i("私信适配器","传递参数:"+message);
                    intent.putExtra("message",message);
                    view.getContext().startActivity(intent);
                }
            });
        }
        return view;
    }
}
