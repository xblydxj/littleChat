package xblydxj.qq.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xblydxj.qq.R;
import xblydxj.qq.entity.User;

/**
 * Created by 46321 on 2016/7/8/008.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {
    private static final String TAG = SearchUserAdapter.class.getSimpleName();
    private List<User> SearchData = new ArrayList<>();
    private List<String> mContacts;

    public SearchUserAdapter(List<String> contacts, List<User> data) {
        mContacts = contacts;
        SearchData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = SearchData.get(position);
        holder.add_contact_name.setText(user.getUsername());
        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        holder.add_contact_time.setText(time);
        if (mContacts.contains(user.getUsername())) {
            holder.mAdd_contact_add.setEnabled(false);
            holder.mAdd_contact_add.setText("已是好友");
        } else {
            holder.mAdd_contact_add.setEnabled(true);
            holder.mAdd_contact_add.setText("添加");
            holder.mAdd_contact_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemButtonClickListener != null) {
                        mOnItemButtonClickListener.OnItemButtonClick(user.getUsername());
                    }
                }
            });
        }
    }


    public interface OnItemButtonClickListener {
        void OnItemButtonClick(String username);
    }

    private OnItemButtonClickListener mOnItemButtonClickListener;

    public void setOnItemButtonClickListener(OnItemButtonClickListener itemButtonClickListener) {
        this.mOnItemButtonClickListener = itemButtonClickListener;
    }

    @Override
    public int getItemCount() {
        return SearchData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView add_contact_time;
        private TextView add_contact_name;
        private final Button mAdd_contact_add;

        public ViewHolder(View itemView) {
            super(itemView);
            add_contact_name = (TextView) itemView.findViewById(R.id.add_contact_name);
            add_contact_time = (TextView) itemView.findViewById(R.id.add_contact_time);
            mAdd_contact_add = (Button) itemView.findViewById(R.id.add_contact_add);

        }
    }
}
