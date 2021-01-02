package com.circle.ui.dashboard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.circle.R;
import com.circle.socketclient.SendDataManage;

//动态页
public class DashboardFragment extends Fragment {
    private DashboardAdapter adapter;
    private ListView listView;
    private boolean loadNewDongTai=false;
    private GetUserDongTaiThread getUserDongTaiThread;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final ImageView publish=root.findViewById(R.id.image_dashboard_publish);
        final ImageView loadMore=root.findViewById(R.id.image_dashboard_load_more);
        listView = (ListView)root.findViewById(R.id.list_dashboard);
        initList();

       publish.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               //旋转图标
               ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(publish,"rotation",0,-90);
               final AnimatorSet set2 = new AnimatorSet();
               set2.playSequentially(objectAnimator2);
               set2.setDuration(500);
               set2.start();
               return false;
           }
       });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishDongTaiDialog publishDongTaiDialog=new PublishDongTaiDialog(root.getContext());
                publishDongTaiDialog.show();
            }
        });

        loadMore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //旋转图标
                ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(loadMore,"rotation",0,-360);
                final AnimatorSet set2 = new AnimatorSet();
                set2.playSequentially(objectAnimator2);
                set2.setDuration(2000);
                set2.start();
                set2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(loadNewDongTai){
                            set2.start();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                return false;
            }
        });

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNewDongTai=true;
                SendDataManage.sendGetIdolNextDongTai(DongTaiListManage.getLostNumber(),5);
                Toast.makeText(root.getContext(),"加载更多数据",Toast.LENGTH_SHORT).show();
            }
        });

        getUserDongTaiThread=new GetUserDongTaiThread();
        getUserDongTaiThread.start();

        return root;
    }

    //初始化适配器
    private void initList(){
        (getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new DashboardAdapter(getContext(), R.layout.listview_dashboard);
                listView.setAdapter(adapter);
                Toast.makeText(getContext(),"已刷新数据",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeAdapter() {
        (getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    class GetUserDongTaiThread extends Thread{
        private boolean isCloseActivity=false;
        @Override
        public void run() {
            while (true) {
                if(DongTaiListManage.isGetNewDongTai){
                    initList();
                    DongTaiListManage.isGetNewDongTai=false;
                    loadNewDongTai=false;
                    Log.i("接收动态","已刷新数据");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}