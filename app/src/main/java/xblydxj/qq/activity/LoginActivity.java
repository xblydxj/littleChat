package xblydxj.qq.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import xblydxj.qq.base.BaseActivity;
import xblydxj.qq.R;
import xblydxj.qq.utils.Snackutils;
import xblydxj.qq.utils.StringUtils;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private ImageView login_avatar;
    private TextInputLayout etlay_username;
    private TextInputLayout etlay_password;
    private Button bt_login;
    private TextView new_user;
    private EditText mEt_username;
    private EditText mEt_password;


    private void initListener() {
        bt_login.setOnClickListener(this);
        new_user.setOnClickListener(this);
        mEt_username = etlay_username.getEditText();
        mEt_username.setOnEditorActionListener(this);
        mEt_password = etlay_password.getEditText();
        mEt_password.setOnEditorActionListener(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        login_avatar = (ImageView) findViewById(R.id.login_avatar);
        etlay_username = (TextInputLayout) findViewById(R.id.et_username);

        etlay_password = (TextInputLayout) findViewById(R.id.et_password);

        bt_login = (Button) findViewById(R.id.bt_login);
        new_user = (TextView) findViewById(R.id.new_user);
        initListener();
    }

    @Override
    protected void initData() {
        String username = mSp.getString(SP_KEY_USERNAME,"");
        String password = mSp.getString(SP_KEY_PASSWORD, "");
        mEt_username.setText(username);
        mEt_password.setText(password);
        mEt_password.requestFocus(View.FOCUS_RIGHT);
        mEt_password.setSelection(password.length());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("tag", "111");
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag", "333");
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                //登录
                login();
                break;
            case R.id.new_user:
                //跳转到注册
                Log.d("tag", "111");
                startActivity(new Intent(this,RegisterActivity.class));
//                startActivity(RegisterActivity.class, false);
                break;
        }
    }

    //键盘监听，事件监听
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.register_et_username:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //获取用户名校验 字母开头，3到12位
                    String username = mEt_username.getText().toString().trim();
                    if (StringUtils.validateUsername(username)) {
                        //如果合法让焦点定位到密码框
//                        mEt_password.requestFocus(View.FOCUS_RIGHT);
                        return false;
                    }else{
                        etlay_username.setError("账户不合法");
                        //如果不合法，提示用户重新输入用户名
                        Snackutils.showSnack(etlay_password, "账户不正确");
                    }
                }
                break;
            case R.id.register_et_password:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //获取密码校验
                    String pwd = mEt_password.getText().toString().trim();
                    if (StringUtils.validatePassword(pwd)) {
                        login();
                    }else{
                        etlay_password.setError("密码不正确");
                        Snackutils.showSnack(etlay_password, "密码不正确");
                    }
                }
                break;
        }
        return true;
    }

    private void login() {
        String password = mEt_password.getText().toString().trim();
        String username = mEt_username.getText().toString().trim();
        Log.d("tag", password+"  dd "+username);
        if (!StringUtils.validateUsername(username)) {
            showToast("账户不正确");
            mEt_username.requestFocus(View.FOCUS_RIGHT);
            return;
        } else if (!StringUtils.validatePassword(password)) {
            showToast("密码不正确");
            mEt_password.requestFocus(View.FOCUS_RIGHT);
            return;
        }
        final ProgressDialog progressDialog =  makeDialog("正在登陆...");
        progressDialog.show();
        //保存用户名和密码
        EMClient.getInstance().login(username, password, getTag(progressDialog));
    }

    @NonNull
    private EMCallBack getTag(final ProgressDialog progressDialog) {
        return new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("tag", "1112222");
                startActivity(MainActivity.class, true);
                progressDialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                showToast("登录失败" + s);
                progressDialog.dismiss();
            }

            @Override
            public void onProgress(int i, String s) {

            }
        };
    }
}
