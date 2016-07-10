package xblydxj.qq.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import xblydxj.qq.R;
import xblydxj.qq.base.BaseActivity;
import xblydxj.qq.base.BaseFragment;
import xblydxj.qq.fragment.ContactFragment;
import xblydxj.qq.fragment.ConversationFragment;
import xblydxj.qq.fragment.PluginFragment;
import xblydxj.qq.listener.MessageListener;
import xblydxj.qq.utils.Snackutils;

public class MainActivity extends BaseActivity {
    private BottomBar mBottomBar;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private int currentIndex = 0;
    private EMContactListener mEmContactListener;
    private EMMessageListener emMessageListener;
    private BottomBarBadge mUnRead;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        initBottom(savedInstanceState);

    }

    private void initBottom(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.main_bottom);
        mBottomBar.setActiveTabColor(ContextCompat.getColor(this, R.color.md_teal_500_color_code));
        mBottomBar.setDefaultTabPosition(0);
        mUnRead = mBottomBar.makeBadgeForTabAt(0, ContextCompat.getColor(this, R.color.colorAccent), 15);
        initUnread();
        initMsgListener();
    }

    private void initMsgListener() {
        emMessageListener = new MessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                super.onMessageReceived(list);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        initUnread();
                        ConversationFragment conversationFragment = (ConversationFragment) fragmentList.get(0);
                        if (conversationFragment != null) {
                            conversationFragment.updateConversation();
                        }
                    }
                });
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private void initUnread() {
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (unreadMsgsCount > 99) {
            mUnRead.setCount(99);
        } else if (unreadMsgsCount > 0) {
            mUnRead.setCount(unreadMsgsCount);
        } else {
            mUnRead.setCount(0);
        }
        mUnRead.setAnimationDuration(200);
        mUnRead.setAutoShowAfterUnSelection(true);
    }


    private void initListener() {
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                int index = 0;
                if (menuItemId == R.id.bottomBarItemOne) {
                    index = 0;
                } else if (menuItemId == R.id.bottomBarItemTwo) {
                    index = 1;
                } else if (menuItemId == R.id.bottomBarItemThree) {
                    index = 2;
                }
                if (index == currentIndex) {
                    return;
                }
                BaseFragment fragment = fragmentList.get(index);
                if (fragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .show(fragment)
                            .hide(fragmentList.get(currentIndex))
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fl_content, fragment, index + "")
                            .hide(fragmentList.get(currentIndex))
                            .commit();
                }
                currentIndex = index;
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {

                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEmContactListener != null) {
            EMClient.getInstance().contactManager().removeContactListener(mEmContactListener);
            mEmContactListener = null;
        }
        if (emMessageListener != null) {
            EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
            emMessageListener = null;
        }
    }

    @Override
    protected void initData() {
        EMClient.getInstance().chatManager().loadAllConversations();
        initFragment();
        initContactListener();
        initListener();
    }

    private void initContactListener() {
        mEmContactListener = new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                Snackutils.showSnack(mBottomBar, "添加了新的好友" + s);
                updateContactFragment();
            }

            @Override
            public void onContactDeleted(String s) {
                Snackutils.showSnack(mBottomBar, "解除了好友关系" + s);
                updateContactFragment();
            }

            @Override
            public void onContactInvited(String s, String s1) {
                Snackutils.showSnack(mBottomBar, "收到了来自 " + s + " 发来的好友请求，理由：" + s1);
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(s);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onContactAgreed(String s) {
                Snackutils.showSnack(mBottomBar, s+"同意了您的好友请求");
            }

            @Override
            public void onContactRefused(String s) {
                Snackutils.showSnack(mBottomBar, s+"拒绝了您");
            }
        };

        EMClient.getInstance().contactManager().setContactListener(mEmContactListener);
    }

    private void updateContactFragment() {
        ContactFragment contactFragment = (ContactFragment) fragmentList.get(1);
        if (contactFragment.isAdded()) {
            contactFragment.loadContactFromServer();
        }
    }

    private void initFragment() {
        fragmentList.clear();

        ConversationFragment conversationFragment = new ConversationFragment();
        ContactFragment contactFragment = new ContactFragment();
        PluginFragment pluginFragment = new PluginFragment();

        fragmentList.add(conversationFragment);
        fragmentList.add(contactFragment);
        fragmentList.add(pluginFragment);

        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, conversationFragment, 0 + "").commit();
    }
}
