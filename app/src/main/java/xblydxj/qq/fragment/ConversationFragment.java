package xblydxj.qq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import xblydxj.qq.R;
import xblydxj.qq.activity.ChatActivity;
import xblydxj.qq.adapter.ConversationAdapter;
import xblydxj.qq.base.BaseActivity;
import xblydxj.qq.base.BaseFragment;


public class ConversationFragment extends BaseFragment  implements ConversationAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private List<EMConversation> conversationList = new ArrayList<>();
    private ConversationAdapter conversationAdapter;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        conversationAdapter = new ConversationAdapter(conversationList);
        recyclerView.setAdapter(conversationAdapter);
        updateConversation();
        conversationAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    public void updateConversation() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        conversationList.clear();
        //将HashMap的所有的值，放到 集合中
        Collection<EMConversation> values = allConversations.values();
        conversationList.addAll(values);
        //排序：按照会话的最后一条消息的时间的倒序
        Collections.sort(conversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation lhs, EMConversation rhs) {
                return (int) (rhs.getLastMessage().getMsgTime() - lhs.getLastMessage().getMsgTime());
            }
        });
        conversationAdapter.notifyDataSetChanged();//更新ListView
    }

    @Override
    public void onItemClick(EMConversation conversation) {
        String userName = conversation.getUserName();
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(BaseActivity.SP_KEY_USERNAME, userName);
        startActivity(intent);
    }
}
