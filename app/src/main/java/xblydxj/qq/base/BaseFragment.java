package xblydxj.qq.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xblydxj.qq.R;

public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_left);
        ImageView iv_right = (ImageView) view.findViewById(R.id.iv_right);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        initHeader(iv_left, tv_title, iv_right);
        initData(view);
    }

    protected abstract void initHeader(ImageView iv_left, TextView tv_title, ImageView iv_right);

    protected abstract void initData(View view);
}
