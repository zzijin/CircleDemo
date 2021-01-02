package com.circle.ui.inform;

import com.circle.information.ChatData;
import com.circle.socketclient.FileData;

import java.util.ArrayList;

public class UserChat {
    public int ToUID;
    public boolean chatDataState;
    public int waitNumber=0;
    public ArrayList<ChatData> chatData;
    public boolean IsGetNewChat=false;

    public UserChat(int toUID){
        ToUID=toUID;
        chatDataState=false;
        waitNumber=1;
        chatData=new ArrayList<>();
    }

    public int addChatFileInform(int num, int index, FileData file){
        for(int i=0;i<chatData.size();i++)
        {
            if(chatData.get(i).Number==num)
            {
                chatData.get(i).addImageInform(index,file);
                return i;
            }
        }
        return -1;
    }

    public int addChatData(int num,int index,byte[] data){
        for(int i=0;i<chatData.size();i++)
        {
            if(chatData.get(i).Number==num)
            {
                chatData.get(i).addChatImageData(index,data);
                if(chatData.get(i).getImagesState()){
                    waitNumber--;
                    if(waitNumber==0){
                        chatDataState=true;
                    }else {
                        chatDataState=false;
                    }
                }
                return i;
            }
        }
        return -1;
    }

    public void addChatDataInform(ChatData chat){
        chatData.add(chat);

        if(chat.Images.length==0) {
            waitNumber--;
        }

        if(waitNumber==0){
            chatDataState=true;
        }else {
            chatDataState=false;
        }
    }
}
