package com.circle.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.circle.LoginActivity;
import com.circle.R;
import com.circle.SetMyInformActivity;
import com.circle.UserInform;
import com.circle.socketclient.SocketManage;

//用户页
public class NotificationsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        View inform=root.findViewById(R.id.include_notifications_inform);
        View login=root.findViewById(R.id.include_notifications_login);
        if(UserInform.getLoginState()==3){
            login.setVisibility(View.GONE);
            inform.setVisibility(View.VISIBLE);
            ImageView userImage=root.findViewById(R.id.image_setting_inform_image);
            TextView userName=root.findViewById(R.id.text_setting_inform_name);
            TextView userSign=root.findViewById(R.id.text_setting_inform_sign);
            TextView userIdol=root.findViewById(R.id.text_setting_inform_idol);
            TextView userFans=root.findViewById(R.id.text_setting_inform_fans);
            TextView userDongTai=root.findViewById(R.id.text_setting_inform_dongtai);

            userName.setText(UserInform.getUserName());
            userSign.setText(UserInform.getUserSign());
            userIdol.setText(UserInform.getWatchSBNum()+"");
            userFans.setText(UserInform.getFansNum()+"");
            userDongTai.setText(UserInform.getDongTaiNum()+"");


            if(UserInform.getUserImage()!=null) {
                Log.i("设置页","头像不为空");
                if (UserInform.getUserImageState()) {
                    Log.i("设置页","头像完整");
                    userImage.setImageBitmap(UserInform.getUserBitImage());
                }
                else {
                    Log.i("设置页","头像数据缺失");
                }
            }

            View editData=root.findViewById(R.id.line_setting_inform_my);
            editData.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(root.getContext(), SetMyInformActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {
            Button loginButton=root.findViewById(R.id.button_setting_login_login);
            Button registerButton=root.findViewById(R.id.button_setting_login_register);

            if(SocketManage.isOpenState){
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(root.getContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                });
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            else {
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(root.getContext(),"已与服务器断开连接了，请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                });
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(root.getContext(),"已与服务器断开连接了，请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}