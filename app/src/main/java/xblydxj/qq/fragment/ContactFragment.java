package xblydxj.qq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xblydxj.qq.R;
import xblydxj.qq.activity.AddContactActivity;
import xblydxj.qq.adapter.ContactRecyclerAdapter;
import xblydxj.qq.base.BaseFragment;
import xblydxj.qq.bean.Contact;
import xblydxj.qq.utils.DBUtils;
import xblydxj.qq.utils.PinyinUtils;

/**
 * Created by 46321 on 2016/7/8/008.
 */
public class ContactFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ContactFragment.class.getSimpleName();

    private RecyclerView mContact_recycler;

    private List<Contact> data = new ArrayList<>();

    private String mCurrentUser;
    private SwipeRefreshLayout mContact_refresh;
    private ContactRecyclerAdapter mContactRecyclerAdapter;


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
        initSwipeRefresh(view);
        initRecycler(view);
        mCurrentUser = EMClient.getInstance().getCurrentUser();
        loadContactFromDB();
        loadContactFromServer();
    }

    public void loadContactFromServer() {
        Observable.create(new Observable.OnSubscribe<Contact>() {
            @Override
            public void call(Subscriber<? super Contact> subscriber) {
                try {
                    data.clear();
                    List<String> contactsFromServer = getEMClientData();
                    for (int i = 0; i < contactsFromServer.size(); i++) {
                        Contact contact = new Contact();
                        contact.avatar = i % 8;
                        contact.name = contactsFromServer.get(i);
                        contact.initial = getInitial(contactsFromServer.get(i));
                        subscriber.onNext(contact);
                    }
                    DBUtils.updateContacts(getContext(), mCurrentUser, data);
                    loadContactFromDB();
                    subscriber.onCompleted();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        })
                .onBackpressureBuffer(10000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Contact>() {
                    @Override
                    public void onCompleted() {
                        if (mContact_refresh.isRefreshing()) {
                            mContact_refresh.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Contact contact) {
                        data.add(contact);
                    }
                });
    }

    private List<String> getEMClientData() throws HyphenateException {
        List<String> contactsFromServer = EMClient
                .getInstance()
                .contactManager()
                .getAllContactsFromServer();
        mCurrentUser = EMClient.getInstance().getCurrentUser();
        return contactsFromServer;
    }

    private void loadContactFromDB() {
        List<Contact> contacts = DBUtils.getContacts(getContext(), mCurrentUser);
        data.clear();
        data.addAll(contacts);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mContactRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecycler(View view) {
        mContact_recycler = (RecyclerView) view.findViewById(R.id.contact_recycler);
        mContactRecyclerAdapter = new ContactRecyclerAdapter(getContext(), data);
        mContact_recycler.setLayoutManager(new LinearLayoutManager(getContext()));//这里用线性显示 类似于listview
        mContact_recycler.setAdapter(mContactRecyclerAdapter);
    }

    private void initSwipeRefresh(View view) {
        mContact_refresh = (SwipeRefreshLayout) view.findViewById(R.id.contact_refresh);
        int blue = getResources().getColor(R.color.md_blue_500_color_code);
        int pink = getResources().getColor(R.color.md_pink_500_color_code);
        int teal = getResources().getColor(R.color.md_teal_500_color_code);
        int grey = getResources().getColor(R.color.md_grey_500_color_code);
        mContact_refresh.setColorSchemeColors(blue, pink, grey, teal);
        mContact_refresh.setOnRefreshListener(this);

        mContact_refresh.post(new Runnable() {
            @Override
            public void run() {
                mContact_refresh.setRefreshing(true);
            }
        });
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

    @Override
    public void onRefresh() {
        loadContactFromServer();
    }
}
