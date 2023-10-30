package com.android.question;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private List<QuestionBean.Question> questionsArray = new ArrayList<>();
    private int currentQuestionIndex;

    private TextView questionTextView;
    private RadioGroup optionRadioGroup;
    private RadioButton radioButtonTrue;
    private RadioButton radioButtonFalse;
    private Button previousButton;
    private Button nextButton;

    private static final String REQUEST_URL = "https://opentdb.com/api.php?amount=10&type=boolean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        // 初始化控件
        questionTextView = findViewById(R.id.question_textview);
        optionRadioGroup = findViewById(R.id.option_radiogroup);
        radioButtonTrue = findViewById(R.id.option_true);
        radioButtonFalse = findViewById(R.id.option_false);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        requestData();
        // 设置按钮点击事件
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousQuestion();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex == questionsArray.size() - 1) {
                    submitAnswers();
                } else {
                    showNextQuestion();
                }
            }
        });

        optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                QuestionBean.Question question = questionsArray.get(currentQuestionIndex);
                if (radioGroup.getCheckedRadioButtonId() == radioButtonTrue.getId()){
                    // 选择了true 记录用户选对了还是选错了
                    if (TextUtils.equals(question.correct_answer,"True")){
                        question.userSelect = 1;
                    }else {
                        question.userSelect = 2;
                    }
                }else {
                    // 选择了false
                    if (TextUtils.equals(question.correct_answer,"False")){
                        question.userSelect = 1;
                    }else {
                        question.userSelect = 2;
                    }
                }
            }
        });


    }

    void requestData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建请求队列
                RequestQueue requestQueue = Volley.newRequestQueue(QuestionActivity.this);

                // 创建请求对象
                StringRequest stringRequest = new StringRequest(Request.Method.GET, REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // 处理成功响应
                                // response 是服务器返回的响应数据
                                // 显示第一道题目
                                QuestionBean responseBean = new Gson().fromJson(response,QuestionBean.class);
                                questionsArray.clear();
                                questionsArray.addAll(responseBean.results);
                                currentQuestionIndex = 0;
                                showQuestion(currentQuestionIndex);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // 处理错误响应
                                // error 是产生的错误对象
                                Toast.makeText(getApplicationContext(),error.getLocalizedMessage() + "", Toast.LENGTH_LONG).show();
                            }
                        });

                // 将请求对象添加到请求队列中
                requestQueue.add(stringRequest);
            }
        }).start();
    }

    private void showQuestion(int questionIndex) {
        QuestionBean.Question questionObject = questionsArray.get(questionIndex);
        String question = questionObject.question;
        String correctAnswer = questionObject.correct_answer;

        questionTextView.setText((questionIndex + 1) + "." + question + "(" + questionObject.difficulty + ")");
        optionRadioGroup.clearCheck();
        optionRadioGroup.setTag(correctAnswer);
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestion(currentQuestionIndex);
            updateButtonVisibility();
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questionsArray.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
            updateButtonVisibility();
        }
    }

    private void updateButtonVisibility() {
        previousButton.setVisibility(currentQuestionIndex == 0 ? View.INVISIBLE : View.VISIBLE);

        if (currentQuestionIndex == questionsArray.size() - 1) {
            nextButton.setText("submit");
        } else {
            nextButton.setText("next");
        }
    }

    private void submitAnswers() {
        // 获取用户答对了多少道题
        int count = 0;
        for (QuestionBean.Question question : questionsArray){
            if(question.userSelect == 1){
                count ++;
            }
        }
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("count",count);
        intent.putExtra("bean",new Gson().toJson(questionsArray));
        startActivity(intent);
        finish();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        currentQuestionIndex = 0;
        requestData();
        updateButtonVisibility();
    }
}
