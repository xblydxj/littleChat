package xblydxj.qq.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import xblydxj.qq.R;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<EMMessage> data;

    public ChatAdapter(List<EMMessage> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = data.get(position);
        return emMessage.direct() == EMMessage.Direct.SEND ? 0 : 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_send, parent, false);
            viewHolder = new ViewHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_received, parent, false);
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EMMessage message = data.get(position);
        long msgTime = message.getMsgTime();
        holder.tvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (position == 0) {
            holder.tvTime.setVisibility(View.VISIBLE);
        } else {
            EMMessage preMessage = data.get(position - 1);
            long preMessageMsgTime = preMessage.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime, preMessageMsgTime)) {
                holder.tvTime.setVisibility(View.GONE);
            } else {
                holder.tvTime.setVisibility(View.VISIBLE);
            }
        }
        if (message.getType() == EMMessage.Type.TXT) {
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            String msg = body.getMessage();
            holder.tvMsg.setText(msg);
        }

        if (holder.mChat_item_state != null) {
            switch (message.status()) {
                case SUCCESS:
                    holder.mChat_item_state.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.mChat_item_state.setVisibility(View.VISIBLE);
                    holder.mChat_item_state.setImageResource(R.drawable.msg_error);
                    break;
                case CREATE:
                case INPROGRESS:
                    holder.mChat_item_state.setVisibility(View.VISIBLE);
                    holder.mChat_item_state.setImageResource(R.drawable.msg_state_anim);
                    AnimationDrawable drawable = (AnimationDrawable) holder.mChat_item_state.getDrawable();
                    if (drawable.isRunning()) {
                        drawable.stop();
                    }
                    drawable.start();
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mChat_item_state;
        private final TextView tvTime;
        private final TextView tvMsg;

        public ViewHolder(View itemView) {
            super(itemView);
            mChat_item_state = (ImageView) itemView.findViewById(R.id.chat_item_state);
            tvTime = (TextView) itemView.findViewById(R.id.chat_item_time);
            tvMsg = (TextView) itemView.findViewById(R.id.chat_item_message);
        }
    }
}
