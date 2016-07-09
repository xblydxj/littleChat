package xblydxj.qq.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import xblydxj.qq.dp.ContactOpenHelper;

/**
 * Created by 46321 on 2016/7/9/009.
 */
public class DBUtils {
    public static void updateContacts(Context context, String username, List<String> contacts) {
        if (contacts == null || contacts.size() < 1) {
            return;
        }
        ContactOpenHelper contactOpenHelper = new ContactOpenHelper(context, username);
        SQLiteDatabase readableDatabase = contactOpenHelper.getReadableDatabase();
        readableDatabase.beginTransaction();
        //如果数据已经存在先删除再添加
        readableDatabase.delete(username, null, null);
        ContentValues values = new ContentValues();
        for (int i = 0; i < contacts.size(); i++) {
            values.put("c_contact", contacts.get(i));
            readableDatabase.insert(username, null, values);
        }
        readableDatabase.setTransactionSuccessful();
        readableDatabase.endTransaction();
        readableDatabase.close();
    }

//    public static List<String> getContacts(Context context, String username) {
//        ContactOpenHelper contactOpenHelper = new ContactOpenHelper(context, username);
//        SQLiteDatabase readableDatabase = contactOpenHelper.getReadableDatabase();
//        Cursor cursor = readableDatabase.query(username, new String[]{"c_contact"}, null, null, null, null, null);
//        while (cursor.moveToNext()) {
//            String contact = cursor.getString(0);
//            contacts.add(contact);
//        }
//        cursor.close();
//        readableDatabase.close();
//    }
}
