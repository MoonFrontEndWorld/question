package com.android.question;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnNext;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView() {
        tvResult = findViewById(R.id.tv_result);
        btnNext = findViewById(R.id.btn_next);
        gridview = findViewById(R.id.gridview);

        int count = getIntent().getIntExtra("count", 0);

        int rate = (count * 100) / 10;

        tvResult.setText("You answered " + count + "questions correctly in total，The accuracy rate is " + rate + "%");

        String jsonString = getIntent().getStringExtra("bean");
        List<QuestionBean.Question> questionList = new Gson().fromJson(jsonString, new TypeToken<List<QuestionBean.Question>>(){}.getType());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gridview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return questionList.size();
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_question,null,false);
                TextView tvQuestion = view.findViewById(R.id.tv_question);
                TextView tvNo = view.findViewById(R.id.tv_no);
                tvQuestion.setText(questionList.get(i).userSelect == 1 ? "✅" :"❌");
                tvNo.setText((i + 1) + "");
                return view;
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DetailActivity.this,ResultActivity.class);
                intent.putExtra("bean",questionList.get(i));
                startActivity(intent);
            }
        });

    }
}