package xblydxj.qq.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import xblydxj.qq.utils.DialogUtil;
import xblydxj.qq.utils.ToastUtil;

public abstract class BaseActivity extends AppCompatActivity {
    protected SharedPreferences mSp;
    Handler handler = new Handler();
    public static final String SP_KEY_USERNAME = "username";
    public static final String SP_KEY_PASSWORD = "password";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSp = getSharedPreferences("config",MODE_PRIVATE);
        initView(savedInstanceState);
        initData();
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
