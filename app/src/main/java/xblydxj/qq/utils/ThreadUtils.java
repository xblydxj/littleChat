package xblydxj.qq.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class ThreadUtils {
    private static Executor sSingleThreadPool = Executors.newSingleThreadExecutor();

    public static void runOnSubThread(Runnable runnable) {
        sSingleThreadPool.execute(runnable);
    }
}
