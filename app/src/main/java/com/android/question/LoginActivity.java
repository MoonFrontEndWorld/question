package com.android.question;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// 登录页
public class LoginActivity extends AppCompatActivity {

    // 名字，密码 登录按钮
    EditText name,password;
    Button login;
    TextView register;

    //声名数据库先关操作的对象
    SQLiteDatabase db;
    SqlLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化控件
        setContentView(R.layout.activity_login);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        dbHelper = new SqlLiteHelper(this);
        db =  dbHelper.getWritableDatabase();

        //点击登录按钮事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namestr = name.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                // 获取用户输入的账号密码 查询本地数据库中是否含有这个用户
                // 如果没有提示无此用户 如果有的话检查输入密码和数据库中密码是否一致 如果不一致则做出提示
                Cursor cursor =  db.rawQuery("select * from " + SqlLiteHelper.TABLE_USER_NAME
                        + " where name=" + " '"+ namestr + "'", null);
                if (TextUtils.isEmpty(namestr) || TextUtils.isEmpty(pwd)){
                    Toast.makeText(LoginActivity.this,"Please input value", Toast.LENGTH_LONG).show();
                }else {
                    if (namestr.equals("admin") && pwd.equals("123456")){
                        // 管理员登录
                        startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                        finish();
                    }else if (cursor == null || !cursor.moveToNext()){
                        Toast.makeText(LoginActivity.this,"No such user", Toast.LENGTH_LONG).show();
                    }else {
                        // 查询密码是否和输入相等
                        @SuppressLint("Range") String pwdstr = cursor.getString(cursor.getColumnIndex("password"));
                        if (!TextUtils.equals(pwd,pwdstr)){
                            Toast.makeText(LoginActivity.this,"Incorrect password", Toast.LENGTH_LONG).show();
                        }else {
                            //登陆成功 跳转
                            Toast.makeText(LoginActivity.this,"Login succeed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                    }
                }

            }
        });

        // 注册按钮点击事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转至注册界面
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

}
