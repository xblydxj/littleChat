package xblydxj.qq.config;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import xblydxj.qq.R;
import xblydxj.qq.activity.ChatActivity;
import xblydxj.qq.activity.LoginActivity;
import xblydxj.qq.activity.MainActivity;
import xblydxj.qq.base.BaseActivity;
import xblydxj.qq.listener.MessageListener;
import xblydxj.qq.utils.ToastUtil;


public class ApplicationConfig extends Application {
    private static final String TAG = "ApplicationConfig";
    private List<BaseActivity> activityList = new ArrayList<>();
    private SoundPool mSoundPool;
    private int shortRing;
    private int longRing;
    private NotificationManager mNotificationManager;
    private Bitmap mBitmap;
    private Handler mHandler = new Handler();
    private SoundPool mSoundPool1;

    @Override
    public void onCreate() {
        super.onCreate();
        initBmob();
        initHuaxin();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar1);
        initSoundPool();
        initMsgListener();
        initConnectListener();
    }

    public void removeActivity(BaseActivity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

    private void initConnectListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int i) {
                final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (i == EMError.USER_REMOVED) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getApplicationContext(), "您已被后台删除，请重新登录或注册");
                            clearAllActivity();
                            startActivity(intent);
                        }
                    });
                } else if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getApplicationContext(), "您的账号已经在另一台设备登录，请重新登录");
                            clearAllActivity();
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void clearAllActivity() {
        for (int i = 0; i < activityList.size(); i++) {
            BaseActivity baseActivity = activityList.get(i);
            baseActivity.finish();
        }
        activityList.clear();
    }

    private void initMsgListener() {
        EMClient.getInstance().chatManager().addMessageListener(new MessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                if (isRunningInBackground()) {
                    mSoundPool.play(longRing, 1, 1, 1, 0, 1);
                    showNotification(list);
                } else {
                    mSoundPool.play(shortRing, 1, 1, 1, 0, 1);
                }
            }
        });
    }

    private void showNotification(List<EMMessage> list) {
        String msg = "";
        EMMessage message = list.get(0);
        if (message.getType() == EMMessage.Type.TXT) {
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            msg = body.getMessage();
        }
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra(BaseActivity.SP_KEY_USERNAME, message.getUserName());

        Intent[] intents = {mainIntent, chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(mBitmap)
                .setSmallIcon(R.drawable.avatar5)
                .setContentText(msg)
                .setContentTitle("您有一条新消息")
                .setContentInfo(message.getFrom())
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        mNotificationManager.notify(1, notification);
    }

    private boolean isRunningInBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        ComponentName topActivity = runningTaskInfo.topActivity;
        if (getPackageName().equals(topActivity.getPackageName())) {
            return false;
        } else {
            return true;
        }
    }

    private void initSoundPool() {
        mSoundPool1 = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        shortRing = mSoundPool.load(this, R.raw.duan, 1);
        longRing = mSoundPool.load(this, R.raw.yulu, 1);
    }

    private void initHuaxin() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    private void initBmob() {
        Bmob.initialize(this, "25bf1d6239ae76152474b3c9e0786d8a");
    }

    public void addActivity(BaseActivity baseActivity) {
        if (!activityList.contains(baseActivity)) {
            activityList.add(baseActivity);
        }
    }
}
