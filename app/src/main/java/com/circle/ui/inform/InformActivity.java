package com.circle.ui.inform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.MainActivity;
import com.circle.R;
import com.circle.UsersListManage;
import com.circle.information.ChatData;
import com.circle.information.WaitQueueChat;
import com.circle.socketclient.SendDataManage;

import java.util.ArrayList;

public class InformActivity extends AppCompatActivity {
    private int index;
    private UserInformAdapter adapter;
    private ListView listView;
    private boolean isSendGetNextChat=false;

    GetUserChatInform getUserChatInform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_inform);

        Intent intent=getIntent();
        String message=intent.getStringExtra("message");
        Log.i("私信适配器","获得值："+message);
        index=Integer.valueOf(message).intValue();

        TextView title=findViewById(R.id.text_inform_activity_name);
        title.setText(UsersListManage.getUserName(ChatDataManage.Chats.get(index).ToUID));
        Button button=findViewById(R.id.button_inform_activity_send);
        final TextView textView=findViewById(R.id.text_inform_activity_input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=textView.getText().toString();
                if(input.length()>0){
                    WaitQueueChat waitQueueChat=new WaitQueueChat(ChatDataManage.Chats.get(index).ToUID,input,new ArrayList<String>(),-1);
                    int index=SendChatWaitQueue.WaitAdd(waitQueueChat);
                    Log.i("私信发送追踪","点击发送");
                    SendDataManage.sendChatToUser(index);
                    textView.setText("");
                }
            }
        });

        listView=findViewById(R.id.list_inform_activity);
        if(ChatDataManage.Chats.get(index).chatData.size()<5) {
            ChatDataManage.sendNextChat(index);
            isSendGetNextChat=true;

        }
        initAdapter();
        getUserChatInform=new GetUserChatInform();
        getUserChatInform.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getUserChatInform.isCloseActivity=true;
    }

    public void initAdapter() {
        ((InformActivity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter=new UserInformAdapter(InformActivity.this,R.layout.listview_chat_inform,index);
                listView.setAdapter(adapter);
            }
        });
    }

    public void changeAdapter() {
        ((InformActivity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    class GetUserChatInform extends Thread{
        private boolean isCloseActivity=false;
        @Override
        public void run() {
            while (true){
                if(ChatDataManage.Chats.get(index).waitNumber==0||ChatDataManage.Chats.get(index).IsGetNewChat){
                    if(isSendGetNextChat){
                        isSendGetNextChat=false;
                        changeAdapter();
                    }
                    else if(ChatDataManage.Chats.get(index).IsGetNewChat){
                        ChatDataManage.Chats.get(index).IsGetNewChat=false;
                        changeAdapter();
                    }
                }
                else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(!isSendGetNextChat){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(isCloseActivity){
                    break;
                }
            }
        }
    }
}
