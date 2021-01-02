package com.circle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.circle.socketclient.SendDataManage;
import com.circle.ui.dashboard.DongTaiListManage;
import com.circle.ui.inform.ChatDataManage;

public class LoginActivity extends AppCompatActivity {
    View loginLoad;
    View loginInput;
    LinearLayout lineInput;
    EditText inputName;
    EditText inputPassword;
    int lineHeight;
    int lineWidth;
    LinearLayout activityLogin;
    View main;
    TextView loadText;
    Button loginButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //继承Activity使用，隐藏标题栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //隐藏标题栏
        getSupportActionBar().hide();

        loginInput=findViewById(R.id.include_logininput);
        loginLoad=findViewById(R.id.include_loginload);
        lineInput=findViewById(R.id.line_input);
        inputName=findViewById(R.id.edit_username);
        inputPassword=findViewById(R.id.edit_password);
        activityLogin=findViewById(R.id.activity_login);
        loadText=findViewById(R.id.text_login_load);

        main=findViewById(R.id.container);


        loginButton=findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInform.setUserInfrom(inputName.getText().toString(),inputPassword.getText().toString());
                loginButton.setVisibility(View.GONE);
                loadText.setVisibility(View.VISIBLE);
                loadText.setText("正在连接服务器.");

                lineWidth=lineInput.getWidth();
                lineHeight=lineInput.getHeight();

                loginInput.setVisibility(View.GONE);
                //加一点特效
                setInputAnimator();
            }
        });

        Toast.makeText(this,"当前为未登录状态，请先登录",Toast.LENGTH_LONG).show();
    }

    private void setInputAnimator(){
        final AnimatorSet set = new AnimatorSet();

        //line宽度变小
        ValueAnimator animator1=ValueAnimator.ofInt(lineWidth,lineHeight);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v=(int)animation.getAnimatedValue();
                ViewGroup.LayoutParams params=lineInput.getLayoutParams();
                params.width=v;
                //由于line高度设置wrap,高度会主动适应图案高度，需要手动定高
                if(params.height!=lineHeight) {
                    params.height = lineHeight;
                }
                lineInput.setLayoutParams(params);
            }
        });

        set.setDuration(1000);
        set.setInterpolator(new SpringInterpolator(1));
        set.playSequentially(animator1);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                loginLoad.setVisibility(View.VISIBLE);
                setLoadAnimator();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public class SpringInterpolator implements Interpolator {
        int type;
        public SpringInterpolator(int type){
            this.type=type;
        }

        @Override
        public float getInterpolation(float x) {
            switch (type){
                case 1:return getFloat1(x);
                case 2:return getFloat2(x);
                case 3:return getFloat3(x);
                default: return 0;
            }

        }

        private float getFloat1(float x){
            float tension = 1.5f;
            x -= 1.0f;
            return x * x * ((tension + 1) * x + tension) + 1.0f;
        }

        private float getFloat2(float t){
            float p0=0;
            float p1=1;
            float m0=4;
            float m1=4;
            float t2 = t*t;
            float t3 = t2*t;
            return (2*t3 - 3*t2 + 1)*p0 + (t3-2*t2+t)*m0 + (-2*t3+3*t2)*p1 + (t3-t2)*m1;
        }

        private float getFloat3(float x){
            float tension = 2.0f * 1.0f;
            if (x < 0.5) {
                float t=x * 2.0f;
                return 0.5f * (t * t * ((tension + 1) * t - tension));
            }
            else {
                float t=x * 2.0f - 2.0f;
                return 0.5f * ((t * t * ((tension + 1) * t + tension)) + 2.0f);
            }
        }
    }

    private void setLoadAnimator() {
        loadText.setText("正在登录..");
        //加载图案颜色渐变加深
        ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(loginLoad,"alpha",0.02f,0.6f);
        //加载图案旋转
        ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(loginLoad,"rotation",0,-360);

        final AnimatorSet set1=new AnimatorSet();
        set1.setDuration(4000);
        set1.setInterpolator(new SpringInterpolator(2));
        set1.playSequentially(objectAnimator1);

        final AnimatorSet set2 = new AnimatorSet();
        set2.playSequentially(objectAnimator2);
        set2.setDuration(2200);
        set2.setInterpolator(new SpringInterpolator(3));
        set2.start();
        set1.start();

        //发送登录请求
        SendDataManage.sendLogin();
        int i=0;

        set2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(UserInform.getLoginState()==1) {
                    Log.i("登录页","登录成功");
                    loadText.setText("正在获取数据...");
                    SendDataManage.sendGetIdolNewDongTai(5);
                    SendDataManage.sendGetShowChat(UserInform.getuId());
                    UserInform.loadingData();
                    set2.start();
                }
                else if(UserInform.getLoginState()==2){
                    Log.i("登录页","检测数据加载状态");
                    loadText.setText("正在配置数据....");
                    if(DongTaiListManage.getDongTaiDataState()&& ChatDataManage.getChatsState()){
                        UserInform.loadDataEnd();
                        onBackPressed();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
                        Log.i("登录页","加载完成");
                    }
                    else {
                        Log.i("登录页","加载失败");
                    }
                    set2.start();
                }
                else if(UserInform.getLoginState()==4){
                    Log.i("登录动画类","登录失败,结束动画");
                    loadText.setText("登录验证失败");
                    loginLoad.setVisibility(View.GONE);
                    restoreLine();
                }
                else {
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
    }

    public void restoreLine(){
        final AnimatorSet set = new AnimatorSet();

        ValueAnimator animator1=ValueAnimator.ofInt(lineInput.getWidth(),lineWidth);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v=(int)animation.getAnimatedValue();
                ViewGroup.LayoutParams params=lineInput.getLayoutParams();
                params.width=v;
                lineInput.setLayoutParams(params);
            }
        });

        set.setDuration(700);
        set.setInterpolator(new LinearInterpolator());
        set.playSequentially(animator1);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                loginButton.setVisibility(View.VISIBLE);
                loadText.setVisibility(View.GONE);
                loginInput.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
