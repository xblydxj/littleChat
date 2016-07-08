package xblydxj.qq.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class DialogUtil {
    public static ProgressDialog makeDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        return progressDialog;
    }
}
