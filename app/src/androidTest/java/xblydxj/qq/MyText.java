package xblydxj.qq;

import android.test.AndroidTestCase;

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
}
