package com.android.question;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private RelativeLayout mainLayout;
    private ListView listview;
    SqlLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @SuppressLint("Range")
    private void initView() {
        mainLayout = findViewById(R.id.main_layout);
        listview = findViewById(R.id.listview);
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
                dataMap.put("difficulty", cursor.getString(cursor.getColumnIndex("difficulty")));
                dataMap.put("startdate", cursor.getString(cursor.getColumnIndex("startdate")));
                dataMap.put("enddate", cursor.getString(cursor.getColumnIndex("enddate")));
                dataMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tournament, null, false);
                TextView tvName = view.findViewById(R.id.tv_name);
                TextView tvTime = view.findViewById(R.id.tv_time);
                Map<String, String> map = dataList.get(i);
                tvName.setText(map.get("name"));
                tvTime.setText(map.get("startdate") + "-" + map.get("enddate"));
                return view;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this,QuestionActivity.class));
            }
        });
    }
}