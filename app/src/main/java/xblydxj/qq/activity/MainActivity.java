package xblydxj.qq.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;

import com.hyphenate.chat.EMClient;
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

public class MainActivity extends BaseActivity {
    private BottomBar mBottomBar;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        initBottom(savedInstanceState);
        EMClient.getInstance().chatManager().loadAllConversations();
        initFragment();
        initListener();
    }

    private void initBottom(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.main_bottom);
        mBottomBar.setActiveTabColor(ContextCompat.getColor(this, R.color.md_teal_500_color_code));
        mBottomBar.setDefaultTabPosition(0);
        BottomBarBadge unRead = mBottomBar.makeBadgeForTabAt(0, ContextCompat.getColor(this, R.color.colorAccent), 15);
        unRead.setCount(15);
        unRead.setAnimationDuration(200);
        unRead.setAutoShowAfterUnSelection(true);

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
    protected void initData() {

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
