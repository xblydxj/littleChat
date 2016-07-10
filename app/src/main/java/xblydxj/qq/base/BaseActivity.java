package xblydxj.qq.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import xblydxj.qq.config.ApplicationConfig;
import xblydxj.qq.utils.DialogUtil;
import xblydxj.qq.utils.ToastUtil;

public abstract class BaseActivity extends AppCompatActivity {
    protected SharedPreferences mSp;
    protected Handler handler = new Handler();
    private ApplicationConfig mApplicationConfig;
    public static final String SP_KEY_USERNAME = "username";
    public static final String SP_KEY_PASSWORD = "password";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSp = getSharedPreferences("config",MODE_PRIVATE);
        initView(savedInstanceState);
        initData();
        mApplicationConfig = (ApplicationConfig) getApplication();
        mApplicationConfig.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApplicationConfig.removeActivity(this);
    }

    protected void startActivity(Class clazz, boolean isFinish) {
        startActivity(new Intent(this, clazz));
        if (isFinish) {
            finish();
        }
    }

    protected ProgressDialog makeDialog(String s) {
        ProgressDialog progressDialog = DialogUtil.makeDialog(this, s);
        return progressDialog;
    }
    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected void showToast(final String msg) {
        if (Looper.myLooper() == handler.getLooper()) {
            ToastUtil.showToast(this, msg);
        }else{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(getApplicationContext(), msg);
                }
            });
        }
    }
}
