package xblydxj.qq.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class Snackutils {

    private static Snackbar snackbar;

    public static void showSnack(View view, String msg) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        }
        snackbar.setText(msg);
        snackbar.show();
    }
}
