package com.android.question;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;
    private TextView questionTextview;
    private RadioGroup optionRadiogroup;
    private RadioButton optionTrue;
    private RadioButton optionFalse;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
    }

    private void initView() {
        QuestionBean.Question question = (QuestionBean.Question) getIntent().getSerializableExtra("bean");

        mainLayout = findViewById(R.id.main_layout);
        questionTextview = findViewById(R.id.question_textview);
        optionRadiogroup = findViewById(R.id.option_radiogroup);
        optionTrue = findViewById(R.id.option_true);
        optionFalse = findViewById(R.id.option_false);
        tvResult = findViewById(R.id.tv_result);

        if (TextUtils.equals(question.correct_answer,"True")){
            optionTrue.setChecked(true);
        }else {
            optionFalse.setChecked(true);
        }
        String hint = "";
        if (question.userSelect == 1){
            hint = "✅";
        } else {
            hint = "❌";
        }
        tvResult.setText(hint + "  " + question.question);

    }
}