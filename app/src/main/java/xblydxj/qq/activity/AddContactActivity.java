package xblydxj.qq.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xblydxj.qq.R;
import xblydxj.qq.adapter.SearchUserAdapter;
import xblydxj.qq.base.BaseActivity;
import xblydxj.qq.bean.Contact;
import xblydxj.qq.entity.User;
import xblydxj.qq.utils.DBUtils;

/**
 * Created by 46321 on 2016/7/8/008.
 */
public class AddContactActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AddContactActivity.class.getSimpleName();
    private ImageView iv_left;
    private TextView tv_title;
    private ImageView iv_right;
    private EditText add_contact_et;
    private ImageView add_contact_search;
    private RecyclerView add_contact_recycler;
    private Animation mShake;
    private List<User> mSearchResult = new ArrayList<>();
    private String mCurrentUser;
    private LinearLayout mNo_data;
    private SearchUserAdapter mAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_contact);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        add_contact_et = (EditText) findViewById(R.id.add_contact_et);
        add_contact_search = (ImageView) findViewById(R.id.add_contact_search);
        add_contact_recycler = (RecyclerView) findViewById(R.id.add_contact_recycler);
        mNo_data = (LinearLayout) findViewById(R.id.add_contact_no_data);
        iv_left.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.GONE);
        tv_title.setText("添加好友");
        add_contact_search.setOnClickListener(this);
        initRecyclerAdapter();
    }

    @Override
    protected void initData() {
        mCurrentUser = EMClient.getInstance().getCurrentUser();
    }

    private void initRecyclerAdapter() {
        List<Contact> contacts = DBUtils.getContacts(this, EMClient.getInstance().getCurrentUser());
        List<String> contactsName = new ArrayList<>();
        for (Contact contact : contacts) {
            contactsName.add(contact.name);
        }
        add_contact_recycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchUserAdapter(contactsName, mSearchResult);
        mAdapter.setOnItemButtonClickListener(new SearchUserAdapter.OnItemButtonClickListener() {
            @Override
            public void OnItemButtonClick(String username) {
                Observable.just(username)
                        .doOnNext(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                try {
                                    EMClient.getInstance().contactManager().addContact(s, "Boom shakalaka");
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(String s) {
                                showToast("添加成功"+s);

                            }
                        });
            }
        });
        add_contact_recycler.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_contact_search:
                onSearch();
                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    private void onSearch() {
        String keyword = add_contact_et.getText().toString().trim();
        if ("".equals(keyword)) {
            mShake = AnimationUtils.loadAnimation(this, R.anim.shake);
            mShake.start();
            return;
        }
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereStartsWith("username",keyword);
        query.addWhereNotEqualTo("username",mCurrentUser);
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                mSearchResult.clear();
                mSearchResult.addAll(list);
                if (list == null || list.size() == 0) {
                    mNo_data.setVisibility(View.VISIBLE);
                    add_contact_recycler.setVisibility(View.GONE);
                } else {
                    mNo_data.setVisibility(View.GONE);
                    add_contact_recycler.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {
                mNo_data.setVisibility(View.VISIBLE);
                add_contact_recycler.setVisibility(View.GONE);
            }
        });
    }
}
