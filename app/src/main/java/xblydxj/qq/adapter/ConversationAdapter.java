package xblydxj.qq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import xblydxj.qq.R;

/**
 * Created by taojin on 2016/7/10.14:33
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<EMConversation> data;
    public ConversationAdapter(List<EMConversation> data){
        this.data = data;
    }

    public interface OnItemClickListener{
        public void onItemClick(EMConversation conversation);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_conversation, parent, false);
        ConversationViewHolder conversationViewHolder = new ConversationViewHolder(view);
        return conversationViewHolder;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        final EMConversation conversation = data.get(position);
        String userName = conversation.getUserName();

        holder.tvUsername.setText(userName);

        EMMessage lastMessage = conversation.getLastMessage();//会话中的最后一条消息
        long msgTime = lastMessage.getMsgTime();

        holder.tvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));

        if (lastMessage.getType()== EMMessage.Type.TXT){
            EMTextMessageBody body = (EMTextMessageBody) lastMessage.getBody();
            String message = body.getMessage();
            holder.tvMsg.setText(message);
        }
        int unreadMsgCount = conversation.getUnreadMsgCount();
        if (unreadMsgCount>99){
            holder.tvUnread.setVisibility(View.VISIBLE);
            holder.tvUnread.setText("99+");
        }else if (unreadMsgCount>0){
            holder.tvUnread.setVisibility(View.VISIBLE);
            holder.tvUnread.setText(unreadMsgCount+"");
        }else {
            holder.tvUnread.setVisibility(View.GONE);
        }

        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(conversation);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvUsername;
        private final TextView tvMsg;
        private final TextView tvTime;
        private final TextView tvUnread;


        public ConversationViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvUnread = (TextView) itemView.findViewById(R.id.tv_unread);

        }
    }
}
