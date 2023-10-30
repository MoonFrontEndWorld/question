package com.android.question;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity {

    // 名字，密码 注册按钮
    EditText name,password;
    Button register;

    /**
     * 数据库工具类
     */
    SQLiteDatabase db;
    SqlLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化控件
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);

        // 初始化数据库
        dbHelper = new SqlLiteHelper(this);
        db =  dbHelper.getWritableDatabase();

        //点击注册按钮
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户输入的内容
                String namestr = name.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                Cursor cursor =  db.rawQuery("select * from " + SqlLiteHelper.TABLE_USER_NAME
                        + " where name=" + " '"+ namestr + "'", null);
                // 注册先检查数据库中是否已经存在这个用户提示用户已存在
                if (TextUtils.isEmpty(namestr) || TextUtils.isEmpty(pwd)){
                    Toast.makeText(RegisterActivity.this,"Please input value", Toast.LENGTH_LONG).show();
                }else {
                    if (cursor == null || !cursor.moveToNext()){
                        // 存入到数据库中
                        ContentValues values = new ContentValues();
                        values.put("name", name.getText().toString().trim());
                        values.put("password", pwd);
                        db.insert(SqlLiteHelper.TABLE_USER_NAME, null, values);
                        Toast.makeText(RegisterActivity.this,"Success", Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(RegisterActivity.this,"The user has exist", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

}
