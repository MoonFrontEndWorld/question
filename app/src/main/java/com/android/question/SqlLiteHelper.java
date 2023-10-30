package com.android.question;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作工具类 和数据库相关的操作都用到此类
 */
public class SqlLiteHelper extends SQLiteOpenHelper {

    // 数据库名字
    public static final String DB_NAME = "contract.db";

    // 竞赛表
    public static final String TABLE_NAME = "tournament";

    // 登录注册的用户表
    public static final String TABLE_USER_NAME = "user";
  
    public static final int DB_VERSION = 1;

    public SqlLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 创建数据库表的语句
        String sql = "create table " +
                TABLE_NAME +
                "(_id integer primary key autoincrement, " +
                "type" + " varchar, " +
                "difficulty" + " varchar, " +
                "name" + " varchar, " +
                "startdate" + " varchar, " +
                "enddate" + " varchar)";

        String sql3 = "create table " +
                TABLE_USER_NAME +
                "(_id integer primary key autoincrement, " +
                "password" + " varchar, " +
                "name" + " varchar )";

        //执行sql语句 创建数据库表
        db.execSQL(sql);
        db.execSQL(sql3);
    }

  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}