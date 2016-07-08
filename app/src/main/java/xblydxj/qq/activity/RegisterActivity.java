package xblydxj.qq.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.listener.SaveListener;
import xblydxj.qq.base.BaseActivity;
import xblydxj.qq.R;
import xblydxj.qq.entity.User;
import xblydxj.qq.utils.Snackutils;
import xblydxj.qq.utils.StringUtils;
import xblydxj.qq.utils.ThreadUtils;


public class RegisterActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private TextInputLayout etlay_username;
    private TextInputLayout etlay_password;
    private Button bt_register;
    private EditText mEt_username;
    private EditText mEt_password;


    private void initListener() {
        mEt_password = etlay_password.getEditText();
        mEt_username = etlay_username.getEditText();
        mEt_password.setOnEditorActionListener(this);
        mEt_username.setOnEditorActionListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        etlay_username = (TextInputLayout) findViewById(R.id.register_et_username);
        etlay_password = (TextInputLayout) findViewById(R.id.register_et_password);
        bt_register = (Button) findViewById(R.id.bt_register);
        initListener();
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                register();
                break;
        }
    }

    private void register() {
        /**
         * 1.将用户信息保存到第三方服务器，
         * 2.再上传至环信
         */

        final String username = mEt_username.getText().toString().trim();
        final String password = mEt_password.getText().toString().trim();
        if (!StringUtils.validateUsername(username)) {
            Snackutils.showSnack(mEt_username, "");
            mEt_username.requestFocus(View.FOCUS_RIGHT);
            return;
        }
        if (!StringUtils.validatePassword(password)) {
            Snackutils.showSnack(mEt_password, "");
            mEt_password.requestFocus(View.FOCUS_RIGHT);
            return;
        }
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        Log.d("tag", "" + username + "   dd" + password);
        final ProgressDialog mProgressDialog = makeDialog(getString(R.string.login_wait));
        mProgressDialog.show();
        Log.d("tag", "123");
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d("tag", "1111");
                ThreadUtils.runOnSubThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mSp.edit().putString(SP_KEY_USERNAME, username)
                                    .putString(SP_KEY_PASSWORD, password)
                                    .apply();
                            Log.d("tag", "" + username + "   fff" + password);
                            Log.d("tag", username + "yyyy" + password);

                            EMClient instance = EMClient.getInstance();
                            instance.createAccount(username, password);
                            startActivity(LoginActivity.class, true);
                            showToast("注册成功");

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            showToast("注册失败");
                            user.delete(getApplicationContext());
                        } finally {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("注册失败" + s);
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
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
                    } else {
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
                        register();
                    } else {
                        etlay_password.setError("密码不正确");
                        Snackutils.showSnack(etlay_password, "密码不正确");
                    }
                }
                break;
        }
        return true;
    }
}
