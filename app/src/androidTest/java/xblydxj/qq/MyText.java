package xblydxj.qq;

import android.test.AndroidTestCase;
import android.util.Log;

import java.util.List;

import xblydxj.qq.bean.Contact;
import xblydxj.qq.utils.DBUtils;
import xblydxj.qq.utils.StringUtils;

/**
 * Created by 46321 on 2016/7/7/007.
 */
public class MyText extends AndroidTestCase {
    public void testUsernameValidate() {
        boolean zhangsan = StringUtils.validateUsername("zhangsan");
        assertEquals(zhangsan, true);
    }
    public void testPasswordValidate(){
        boolean b = StringUtils.validatePassword("12");
        assertEquals(b, false);
    }
    private void testDBUtilsGetContacts(){
        List<Contact> zhangsan = DBUtils.getContacts(getContext(), "zhangsan");
        for (int i = 0; i < zhangsan.size(); i++) {
            Log.d("tag", "testDBUtilsGetContacts: "+zhangsan.get(i).name);
        }
    }
}
