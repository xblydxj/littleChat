package xblydxj.qq.dp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 46321 on 2016/7/9/009.
 */
public class ContactOpenHelper extends SQLiteOpenHelper {
    private String tableName;
    public ContactOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ContactOpenHelper(Context context, String tableName) {
        this(context, "c_contact", null, 1);
        this.tableName = tableName;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+tableName+"(_id integer primary key, " +
                "contact varchar(20), initial varchar(3), avatar integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
