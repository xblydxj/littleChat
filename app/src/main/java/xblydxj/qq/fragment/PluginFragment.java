package xblydxj.qq.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import xblydxj.qq.R;
import xblydxj.qq.activity.LoginActivity;
import xblydxj.qq.base.BaseFragment;
import xblydxj.qq.utils.DialogUtil;
import xblydxj.qq.utils.Snackutils;

/**
 * Created by 46321 on 2016/7/8/008.
 */
public class PluginFragment extends BaseFragment {

    private Button mLogout;

    public PluginFragment() {
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plugin, container, false);
    }

    @Override
    protected void initHeader(ImageView iv_left, TextView tv_title, ImageView iv_right) {
        iv_left.setVisibility(View.GONE);
        iv_right.setVisibility(View.GONE);
        tv_title.setText("动态");
    }

    @Override
    protected void initData(View view) {
        mLogout = (Button) view.findViewById(R.id.plugin_logout);
        String user = EMClient.getInstance().getCurrentUser();
        mLogout.setText("退出登录（" + user + ")");
        mLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plugin_logout:
                logout();
                break;

        }
    }

    private void logout() {
        final ProgressDialog progressDialog = DialogUtil.makeDialog(getContext(), "退出登录。。。");
        progressDialog.show();
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                Snackutils.showSnack(mLogout,"错误");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}

