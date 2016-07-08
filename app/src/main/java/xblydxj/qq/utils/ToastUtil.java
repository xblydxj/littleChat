package xblydxj.qq.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }
}
