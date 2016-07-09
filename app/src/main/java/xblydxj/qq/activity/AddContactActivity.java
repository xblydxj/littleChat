package xblydxj.qq.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import xblydxj.qq.R;
import xblydxj.qq.base.BaseActivity;

/**
 * Created by 46321 on 2016/7/8/008.
 */
public class AddContactActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_left;
    private TextView tv_title;
    private ImageView iv_right;
    private EditText add_contact_et;
    private ImageView add_contact_search;
    private RecyclerView add_contact_recycler;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_contact);

    }

    @Override
    protected void initData() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        add_contact_et = (EditText) findViewById(R.id.add_contact_et);
        add_contact_search = (ImageView) findViewById(R.id.add_contact_search);
        add_contact_recycler = (RecyclerView) findViewById(R.id.add_contact_recycler);


        iv_left.setVisibility(View.GONE);
        iv_right.setVisibility(View.GONE);
        tv_title.setText("添加好友");
        add_contact_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_contact_search:

                break;
        }
    }
}
