package xblydxj.qq.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xblydxj.qq.R;
import xblydxj.qq.bean.Contact;

/**
 * Created by 46321 on 2016/7/8/008.
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> implements SectionIndexer {
    private static final String TAG = ContactRecyclerAdapter.class.getSimpleName();
    private List<Contact> ContactData = new ArrayList<>();
    private Context mContext;
//    private int prePosition = 0;
    public ContactRecyclerAdapter(Context context, List<Contact> data) {
        mContext = context;
        ContactData = data;
    }


    public interface OnItemClickListener{
        void onItemClick(String conversation);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact contact = ContactData.get(position);
        setContactAvatar(holder, contact);
        holder.mContact_item_name.setText(contact.name);
        holder.mContact_item_letter.setText(contact.initial);
        Log.d(TAG, "onBindViewHolder: "+contact.name);
        if (position != 0 && ContactData.get(position-1).initial.equals(contact.initial)) {
            Log.d("tag", "gone");
            holder.mContact_item_card.setVisibility(View.GONE);
        }else {
            holder.mContact_item_card.setVisibility(View.VISIBLE);
        }

        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(contact.name);
                }
            });
        }
    }

    private void setContactAvatar(ViewHolder holder, Contact contact) {
        switch (contact.avatar) {
            case 0:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar1);
                break;
            case 1:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar2);
                break;
            case 2:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar3);
                break;
            case 3:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar4);
                break;
            case 4:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar5);
                break;
            case 5:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar6);
                break;
            case 6:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar7);
                break;
            case 7:
                holder.mContact_item_avatar.setImageResource(R.drawable.avatar8);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return ContactData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mContact_item_letter;
        private ImageView mContact_item_avatar;
        private TextView mContact_item_name;
        private CardView mContact_item_card;

        public ViewHolder(View itemView) {
            super(itemView);
            mContact_item_avatar = (ImageView) itemView.findViewById(R.id.contact_item_avatar);
            mContact_item_name = (TextView) itemView.findViewById(R.id.contact_item_name);
            mContact_item_letter = (TextView) itemView.findViewById(R.id.contact_item_letter);
            mContact_item_card = (CardView) itemView.findViewById(R.id.contact_item_card);

        }
    }

    //类似HashMap
    private SparseIntArray mSparseIntArray = new SparseIntArray();

    //返回统计当前adapter中数据总共可以划分为多少个section
    @Override
    public String[] getSections() {
       //List<Contact> ContactData
        List<String> sections = new ArrayList<>();
        for (int i = 0; i < ContactData.size(); i++) {
            String initial = ContactData.get(i).initial;
            if (!sections.contains(initial)) {
                sections.add(initial);
                //记录section的索引和position，存入对应的键值对
                mSparseIntArray.put(sections.size()-1,i);
            }
        }
        return sections.toArray(new String[sections.size()]);
    }
    //传递一个section的索引号，返回该section第一个条目的position
    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSparseIntArray.get(sectionIndex);
    }
    //传递position返回section索引
    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
