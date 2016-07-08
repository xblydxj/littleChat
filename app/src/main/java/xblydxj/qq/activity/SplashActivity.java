package xblydxj.qq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import xblydxj.qq.R;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class SplashActivity extends Activity {

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //判断是否已经登录，如果登录了则直接进入主界面，否则进入登录界面
        if (isLoggined()) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //TODO跳转到登录界面
                }
            }, 2000);
        }
    }

    private boolean isLoggined() {
        //TODO
        return false;
    }
}
