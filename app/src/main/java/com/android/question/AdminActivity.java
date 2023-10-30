package com.android.question;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private ListView listview;
    private Button btnAdd;

    // 初始化数据库帮助类对象
    SqlLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initView();
    }

    @SuppressLint("Range")
    private void initView() {
        listview = findViewById(R.id.listview);
        btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,AddTournamentActivity.class));
            }
        });
        dbHelper = new SqlLiteHelper(this);
        // 获取可读取的数据库对象
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// 查询表中的所有数据
        Cursor cursor = db.query(SqlLiteHelper.TABLE_NAME, null, null, null, null, null, null);

        List<Map<String, String>> dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("type", cursor.getString(cursor.getColumnIndex("type")));
                dataMap.put("_id", cursor.getInt(cursor.getColumnIndex("_id")) + "");
                dataMap.put("difficulty", cursor.getString(cursor.getColumnIndex("difficulty")));
                dataMap.put("startdate", cursor.getString(cursor.getColumnIndex("startdate")));
                dataMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
                dataMap.put("enddate", cursor.getString(cursor.getColumnIndex("enddate")));

                dataList.add(dataMap);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tournament,null,false);
                TextView tvName = view.findViewById(R.id.tv_name);
                TextView tvTime = view.findViewById(R.id.tv_time);
                Map<String, String> map = dataList.get(i);
                tvName.setText(map.get("name"));
                tvTime.setText("time：" + map.get("startdate") + "-" +  map.get("enddate"));
                return view;
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                new AlertDialog.Builder(AdminActivity.this).setNegativeButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Map<String, String> map = dataList.get(index);
                        dialogInterface.dismiss();
                        // 获取可写入的数据库对象
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        // 删除指定 _id 的数据
                        String whereClause = "_id = ?";
                        String[] whereArgs = new String[]{String.valueOf(map.get("_id"))}; // 将要删除的数据的 _id 值
                        int result = db.delete(SqlLiteHelper.TABLE_NAME, whereClause, whereArgs);

                        // 检查删除结果
                        if (result > 0) {
                            // 删除成功
                            initView();
                            Toast.makeText(AdminActivity.this,"Delete successful",Toast.LENGTH_LONG).show();
                        } else {
                            // 删除失败或未找到匹配的数据
                            Toast.makeText(AdminActivity.this,"Delete Failed",Toast.LENGTH_LONG).show();
                        }
                        // 关闭数据库连接
                        db.close();
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage("Are you sure you want to delete this tournament?").create().show();
                return false;
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }
}