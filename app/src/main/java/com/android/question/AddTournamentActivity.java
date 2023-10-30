package com.android.question;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//添加竞赛
public class AddTournamentActivity extends AppCompatActivity {

    private EditText etType;
    private EditText etName;
    private EditText etDifficulty;
    private EditText etStart;
    private EditText etEnd;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournament);
        initView();
    }

    private void initView() {
        etType = findViewById(R.id.et_type);
        etDifficulty = findViewById(R.id.et_difficulty);
        etStart = findViewById(R.id.et_start);
        etEnd = findViewById(R.id.et_end);
        etName = findViewById(R.id.et_name);
        btnSubmit = findViewById(R.id.btn_submit);

        // 初始化数据库帮助类对象
        SqlLiteHelper dbHelper = new SqlLiteHelper(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = etType.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String difficulty = etDifficulty.getText().toString().trim();
                String startdate = etStart.getText().toString().trim();
                String enddate = etEnd.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (type.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (difficulty.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter difficulty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (startdate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter start date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (enddate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 获取可写入的数据库对象
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // 创建要插入的数据对象
                ContentValues values = new ContentValues();
                values.put("type", type);
                values.put("difficulty", difficulty);
                values.put("startdate", startdate);
                values.put("enddate", enddate);
                values.put("name", name);
                // 插入数据
                long result = db.insert(SqlLiteHelper.TABLE_NAME, null, values);
                if (result != -1) {
                    // 插入成功
                    Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 插入失败
                    Toast.makeText(getApplicationContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
                }
                // 关闭数据库连接
                db.close();
            }
        });



    }
}