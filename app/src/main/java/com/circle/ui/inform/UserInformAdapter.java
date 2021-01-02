package com.circle.ui.inform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.circle.R;
import com.circle.UserInform;
import com.circle.UsersListManage;
import com.circle.information.ChatData;

public class UserInformAdapter extends ArrayAdapter<ChatData> {
    int Index;
    private int resourceId;

    public UserInformAdapter(Context context, int textViewResourceId,int index){
        super(context,textViewResourceId,ChatDataManage.Chats.get(index).chatData);
        Index=index;
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view  = LayoutInflater.from(getContext()).inflate(resourceId, null);

        if(ChatDataManage.Chats.get(Index).chatData.size()>0) {
            ChatData chatData = ChatDataManage.Chats.get(Index).chatData.get(ChatDataManage.Chats.get(Index).chatData.size()-position-1);
            TextView time=view.findViewById(R.id.text_listview_chat_inform_time);

            if(chatData.IsSender){
                View lineRight=view.findViewById(R.id.line_listview_chat_inform_right);
                ImageView image=view.findViewById(R.id.image_listview_chat_inform_right);
                TextView text=view.findViewById(R.id.text_listview_chat_inform_right_inform);

                lineRight.setVisibility(View.VISIBLE);
                image.setImageBitmap(UserInform.getUserBitImage());
                text.setText(chatData.StrData);

            }
            else {
                View lineLeft=view.findViewById(R.id.line_listview_chat_inform_left);
                ImageView image=view.findViewById(R.id.image_listview_chat_inform_left);
                TextView text=view.findViewById(R.id.text_listview_chat_inform_left_inform);

                lineLeft.setVisibility(View.VISIBLE);
                image.setImageBitmap(UsersListManage.getUserImage(chatData.ToUID));
                text.setText(chatData.StrData);
            }
            time.setText(chatData.SendTime);
        }

        return view;
    }
}
