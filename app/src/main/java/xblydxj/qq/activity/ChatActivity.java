package xblydxj.qq.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import xblydxj.qq.R;
import xblydxj.qq.adapter.ChatAdapter;
import xblydxj.qq.base.BaseActivity;

/**
 * Created by 46321 on 2016/7/10/010.
 */
public class ChatActivity extends BaseActivity implements TextView.OnEditorActionListener, View.OnClickListener {
    private ImageView iv_left;
    private TextView tv_title;
    private ImageView iv_right;
    private RecyclerView chat_recycler;
    private EditText chat_et_message;
    private Button chat_send_bt;
    private List<EMMessage> mEMMessages = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private EMMessageListener mEMMessageListener;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setVisibility(View.GONE);

        chat_recycler = (RecyclerView) findViewById(R.id.chat_recycler);
        chat_recycler.setOnClickListener(this);
        chat_et_message = (EditText) findViewById(R.id.chat_et_message);
        chat_et_message.setOnClickListener(this);
        chat_send_bt = (Button) findViewById(R.id.chat_send_bt);
        chat_send_bt.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_bt:

                break;
        }
    }

    private void submit() {
        // validate
        String message = chat_et_message.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "message不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
