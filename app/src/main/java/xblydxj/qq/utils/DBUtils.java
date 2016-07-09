package xblydxj.qq.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import xblydxj.qq.bean.Contact;
import xblydxj.qq.dp.ContactOpenHelper;

/**
 * Created by 46321 on 2016/7/9/009.
 */
public class DBUtils {
    public static void updateContacts(Context context, String username, List<Contact> contacts) {
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
            Contact contact = contacts.get(i);
            values.put("contact", contact.name);
            values.put("initial", contact.initial);
            values.put("avatar", contact.avatar);
            readableDatabase.insert(username, null, values);
        }
        readableDatabase.setTransactionSuccessful();
        readableDatabase.endTransaction();
        readableDatabase.close();
    }

    public static List<Contact> getContacts(Context context, String username) {
        List<Contact> contacts = new ArrayList<>();
        ContactOpenHelper contactOpenHelper = new ContactOpenHelper(context, username);
        SQLiteDatabase readableDatabase = contactOpenHelper.getReadableDatabase();
        readableDatabase.execSQL(
                "create table IF NOT EXISTS " + username + "(_id integer primary key," +
                        " contact varchar(20)," +
                        " initial varchar(3)," +
                        " avatar integer)"
        );
        Cursor cursor = readableDatabase.query(username, new String[]{" contact,initial,avatar"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.name = cursor.getString(0);
            contact.initial = cursor.getString(1);
            contact.avatar = cursor.getInt(2);
            contacts.add(contact);
        }
        cursor.close();
        readableDatabase.close();
        return contacts;
    }
}
