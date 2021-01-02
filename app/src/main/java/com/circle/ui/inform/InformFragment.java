package com.circle.ui.inform;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.circle.MainActivity;
import com.circle.R;
import com.circle.ui.dashboard.DashboardAdapter;

//私信
public class InformFragment extends Fragment {
    private InformAdapter adapter;
    private ListView listView;
    private GetNewChatThread getNewChatThread;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inform, container, false);

        listView=root.findViewById(R.id.list_chatlist);
        adapter=new InformAdapter(root.getContext(),R.layout.listview_chatlist);
        listView.setAdapter(adapter);

        getNewChatThread=new GetNewChatThread();
        getNewChatThread.start();

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        getNewChatThread.isFragmentAlive=false;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter=new InformAdapter(getContext(),R.layout.listview_chatlist);
        listView.setAdapter(adapter);
    }

    public void changeAdapter() {
        (getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                for (int i=0;i<ChatDataManage.Chats.size();i++){
                    if(ChatDataManage.Chats.get(i).IsGetNewChat){
                        ChatDataManage.Chats.get(i).IsGetNewChat=false;
                    }
                }
            }
        });
    }

    class GetNewChatThread extends Thread{
        public boolean isFragmentAlive=true;

        @Override
        public void run() {
            while (isFragmentAlive){
                for(int i=0;i<ChatDataManage.Chats.size();i++){
                    if(ChatDataManage.Chats.get(i).IsGetNewChat){
                        changeAdapter();
                        break;
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
