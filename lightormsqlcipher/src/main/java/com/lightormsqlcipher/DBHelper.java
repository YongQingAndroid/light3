package com.lightormsqlcipher;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * 加密数据库
 * package kotlinTest:qing.posun.com.calender.orm.DBHelper.class
 * 作者：zyq on 2017/6/9 14:33
 * 邮箱：zyq@posun.com
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_PWD="123456";
    public static   final String db_name="light_help.db",sdCard="/mnt/sdcard/"+db_name;
    public static final int vesion=1;/***/
    public DBHelper(Context context) {
        super(context, db_name, null, vesion);
        SQLiteDatabase.loadLibs(context);
    }
    /**
     * 数据库保存在Sdcard
     * 注意数据安全
     * @param context
     * @param isSdCard
     * */
    public DBHelper(Context context,boolean isSdCard) {
        super(context, isSdCard?sdCard:db_name, null, vesion);
        SQLiteDatabase.loadLibs(context);
    }
    /**
     * 废弃该方法
     * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }
    /**
     * 废弃该方法
     * */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
