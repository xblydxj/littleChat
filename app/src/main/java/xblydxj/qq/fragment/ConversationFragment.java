package xblydxj.qq.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xblydxj.qq.R;
import xblydxj.qq.base.BaseFragment;


public class ConversationFragment extends BaseFragment {
    public ConversationFragment() {
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    protected void initHeader(ImageView iv_left, TextView tv_title, ImageView iv_right) {
        iv_left.setVisibility(View.GONE);
        iv_right.setVisibility(View.GONE);
        tv_title.setText("消息");
    }

    @Override
    protected void initData(View view) {

    }

    @Override
    public void onClick(View v) {

    }
}
