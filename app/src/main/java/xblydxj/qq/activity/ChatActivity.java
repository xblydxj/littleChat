package xblydxj.qq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import xblydxj.qq.R;
import xblydxj.qq.adapter.ChatAdapter;
import xblydxj.qq.base.BaseActivity;
import xblydxj.qq.listener.MessageListener;
import xblydxj.qq.utils.Snackutils;

/**
 * Created by 46321 on 2016/7/10/010.
 */
public class ChatActivity extends BaseActivity implements TextView.OnEditorActionListener, View.OnClickListener, TextWatcher {
    private ImageView iv_left;
    private TextView tv_title;
    private ImageView iv_right;
    private RecyclerView chat_recycler;
    private EditText chat_et_message;
    private Button chat_send_bt;
    private List<EMMessage> messageList = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private EMMessageListener mEMMessageListener;
    private String mUsername;
    private MessageListener mMessageListener;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setVisibility(View.GONE);

        chat_et_message = (EditText) findViewById(R.id.chat_et_message);
        chat_et_message.setOnEditorActionListener(this);
        chat_et_message.addTextChangedListener(this);

        chat_send_bt = (Button) findViewById(R.id.chat_send_bt);
        chat_send_bt.setOnClickListener(this);
        chat_send_bt.setEnabled(false);

        chat_recycler = (RecyclerView) findViewById(R.id.chat_recycler);
        chat_recycler.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter(messageList);
        chat_recycler.setAdapter(mChatAdapter);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra(SP_KEY_USERNAME);
        if (TextUtils.isEmpty(mUsername)) {
            Snackutils.showSnack(chat_send_bt,"为获取到聊天对象，请重新选择");
            finish();
            return;
        }
        tv_title.setText("与%%聊天中".replace("%%", mUsername));

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mUsername);
        if (conversation != null) {
            List<EMMessage> allMessages = conversation.getAllMessages();
            messageList.clear();
            messageList.addAll(allMessages);
            mChatAdapter.notifyDataSetChanged();
            chat_recycler.scrollToPosition(messageList.size() - 1);
            conversation.markAllMessagesAsRead();
        }
        initMsgListener();
    }

    private void initMsgListener() {
        mEMMessageListener = new MessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                updateConversation();
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }

    private void updateConversation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mUsername);
                conversation.markAllMessagesAsRead();
                List<EMMessage> messages = conversation.getAllMessages();
                messageList.clear();
                messageList.addAll(messages);
                mChatAdapter.notifyDataSetChanged();
                chat_recycler.smoothScrollToPosition(messageList.size() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEMMessageListener != null) {
            EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
            mEMMessageListener = null;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.chat_et_message && actionId == EditorInfo.IME_ACTION_SEND) {
            sendMsg();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_bt:
                sendMsg();
                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    private void sendMsg() {
        String msg = chat_et_message.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            Snackutils.showSnack(chat_et_message, "不允许发送空消息");
            return;
        }
        chat_et_message.getText().clear();
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg, mUsername);
        emMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                updateConversation();
            }

            @Override
            public void onError(int i, String s) {
                Snackutils.showSnack(chat_et_message, "发送失败" + s);
                updateConversation();
            }

            @Override
            public void onProgress(int i, String s) {
                updateConversation();
            }
        });
        EMClient.getInstance().chatManager().sendMessage(emMessage);
        messageList.add(emMessage);
        mChatAdapter.notifyDataSetChanged();
        chat_recycler.smoothScrollToPosition(messageList.size() - 1);
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            chat_send_bt.setEnabled(false);
        } else {
            chat_send_bt.setEnabled(true);
        }
    }
}
