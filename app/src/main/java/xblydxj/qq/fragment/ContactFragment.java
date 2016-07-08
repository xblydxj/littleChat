package xblydxj.qq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private RecyclerView contact_recycler;
    private SlideBar contact_slide_bar;
    private CardView contact_slide_bar_card;
    private TextView contact_center_tip;
    private CardView contact_center_tip_card;
    private RecyclerView mContact_recycler;

    private List<Contact> data = new ArrayList<>();
    private List<String> mUserNames;

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
        Log.d("tag", "111");
        initContactList();
        Log.d("tag", "222");
        mContact_recycler = (RecyclerView) view.findViewById(R.id.contact_recycler);
        if (data.size() == 0) {
            Log.d("tag", "0");
            return;
        }
        ContactRecyclerAdapter contactRecyclerAdapter = new ContactRecyclerAdapter(getContext(), data);
        Log.d("tag", "333");
        mContact_recycler.setLayoutManager(new LinearLayoutManager(getContext()));//这里用线性显示 类似于listview
        mContact_recycler.setAdapter(contactRecyclerAdapter);
        Log.d("tag", "444");
    }

    private void initContactList() {
        Log.d("tag", "1112");
//        try {
//            mUserNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            UsernameTest();
            for (int i = 0; i < mUserNames.size(); i++) {
                Log.d("tag", "1113");
                Contact contact = new Contact();
                contact.avatar = i % 8;
                contact.name = mUserNames.get(i);
                contact.initial = getInitial(mUserNames.get(i));
                Log.d("tag", "initial:"+contact.initial);
                data.add(contact);
            }
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }
    }

    private void UsernameTest() {
        Log.d("tag", "1114");
        mUserNames = new ArrayList<>();
        mUserNames.add("asd");
        mUserNames.add("asasd");
        mUserNames.add("aswfgdd");
        mUserNames.add("sfasd");
        mUserNames.add("bbasd");
        mUserNames.add("easd");
        mUserNames.add("hasd");
        mUserNames.add("qasd");
        mUserNames.add("是打开链接发给");
        mUserNames.add("不合法");
        mUserNames.add("人");
        mUserNames.add("asdf");
        mUserNames.add("qasd");
        mUserNames.add("cc");
        mUserNames.add("qasd");
        mUserNames.add("dd");
        mUserNames.add("qahhsd");
        mUserNames.add("wq");
        mUserNames.add("oasd");
        mUserNames.add("lasd");
        mUserNames.add("kasd");
        mUserNames.add("jasd");
        mUserNames.add("hasd");
        mUserNames.add("gasd");
        mUserNames.add("fasd");
        mUserNames.add("dasd");
    }

    private String getInitial(String name) {
        return (PinyinUtils.getPinyin(name).charAt(0) + "").toUpperCase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                startActivity(new Intent(getContext(),AddContactActivity.class));
                break;
        }
    }
}
