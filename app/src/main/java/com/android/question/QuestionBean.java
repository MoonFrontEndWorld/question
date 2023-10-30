package com.android.question;

import java.io.Serializable;
import java.util.List;

public class QuestionBean implements Serializable{
    public int response_code;
    public List<Question> results;

    public class Question implements Serializable {
        public String category;
        public String type;
        public String difficulty;
        public String question;
        public String correct_answer;
        public List<String> incorrect_answers;

        public int userSelect = 0;
    }
}


