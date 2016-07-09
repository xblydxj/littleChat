package xblydxj.qq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

import xblydxj.qq.R;
import xblydxj.qq.activity.AddContactActivity;
import xblydxj.qq.adapter.ContactRecyclerAdapter;
import xblydxj.qq.base.BaseFragment;
import xblydxj.qq.bean.Contact;
import xblydxj.qq.utils.PinyinUtils;
import xblydxj.qq.widget.SlideBar;

/**
 * Created by 46321 on 2016/7/8/008.
 */
public class ContactFragment extends BaseFragment {
    private static final String TAG = ContactFragment.class.getSimpleName();

    private RecyclerView contact_recycler;
    private SlideBar contact_slide_bar;
    private CardView contact_slide_bar_card;
    private TextView contact_center_tip;
    private CardView contact_center_tip_card;
    private RecyclerView mContact_recycler;

    private List<Contact> data = new ArrayList<>();/*new Comparator<Contact>() {
        @Override
        public int compare(Contact preContact, Contact contact) {
            int i = (int) preContact.initial.charAt(0) - (int) contact.initial.charAt(0);
            return i == 0 ? 1 : i;
        }
    });*/
    private List<String> mUserNames;
    private String mCurrentUser;

    public ContactFragment() {
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    protected void initHeader(ImageView iv_left, TextView tv_title, ImageView iv_right) {
        iv_left.setVisibility(View.GONE);
        tv_title.setText("联系人");
        iv_right.setOnClickListener(this);
    }

    @Override
    protected void initData(View view) {
        initContactList();
        mContact_recycler = (RecyclerView) view.findViewById(R.id.contact_recycler);
        if (data != null && data.size() > 0) {
            ContactRecyclerAdapter contactRecyclerAdapter = new ContactRecyclerAdapter(getContext(), data);
            mContact_recycler.setLayoutManager(new LinearLayoutManager(getContext()));//这里用线性显示 类似于listview
            mContact_recycler.setAdapter(contactRecyclerAdapter);
            contactRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void initContactList() {
//        ThreadUtils.runOnSubThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
                    mCurrentUser = EMClient.getInstance().getCurrentUser();
//                    mUserNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    UsernameTest();
                    for (int i = 0; i < mUserNames.size(); i++) {
                        Contact contact = new Contact();
                        contact.avatar = i % 8;
                        contact.name = mUserNames.get(i);
                        contact.initial = getInitial(mUserNames.get(i));
                        data.add(contact);
                    }

                    //将数据保存到本地
//                    DBUtils.getContacts(getContext(), mCurrentUser);
//                    DBUtils.updateContacts(getContext(), mCurrentUser,mUserNames);
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
            }
//        });

//    }

    //    private List<String>
    private void UsernameTest() {
        mUserNames = new ArrayList<>();
        mUserNames.add("asd");
        mUserNames.add("asasd");
        mUserNames.add("aswfgdd");
        mUserNames.add("asdf");
        mUserNames.add("bbasd");
        mUserNames.add("不合法");
        mUserNames.add("cc");
        mUserNames.add("dd");
        mUserNames.add("dasd");
        mUserNames.add("easd");
        mUserNames.add("fasd");
        mUserNames.add("gasd");
        mUserNames.add("hasd");
        mUserNames.add("hasd");
        mUserNames.add("jasd");
        mUserNames.add("kasd");
        mUserNames.add("lasd");
        mUserNames.add("oasd");
        mUserNames.add("人");
        mUserNames.add("sfasd");
        mUserNames.add("是打开链接发给");
        mUserNames.add("qasd");
        mUserNames.add("qasd");
        mUserNames.add("qasd");
        mUserNames.add("qahhsd");
        mUserNames.add("wq");
    }

    private String getInitial(String name) {
        return (PinyinUtils.getPinyin(name).charAt(0) + "").toUpperCase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                startActivity(new Intent(getContext(), AddContactActivity.class));
                break;
        }
    }
}
