package com.example.quizapp.Model;

public class Question {
    private String Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer,CategoryId,IsImageQuestion,Hint,Help;

    public Question() {}

    public Question(String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer, String categoryId, String isImageQuestion, String hint, String help)
    {
        Question = question;
        OptionA = optionA;
        OptionB = optionB;
        OptionC = optionC;
        OptionD = optionD;
        CorrectAnswer = correctAnswer;
        CategoryId = categoryId;
        IsImageQuestion = isImageQuestion;
        Hint = hint;
        Help=help;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOptionA() {
        return OptionA;
    }

    public void setOptionA(String optionA) {
        OptionA = optionA;
    }

    public String getOptionB() {
        return OptionB;
    }

    public void setOptionB(String optionB) {
        OptionB = optionB;
    }

    public String getOptionC() {
        return OptionC;
    }

    public void setOptionC(String optionC) {
        OptionC = optionC;
    }

    public String getOptionD() {
        return OptionD;
    }

    public void setOptionD(String optionD) {
        OptionD = optionD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getIsImageQuestion() {
        return IsImageQuestion;
    }

    public void setIsImageQuestion(String isImageQuestion) {
        IsImageQuestion = isImageQuestion;
    }

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public String getHelp() {
        return Help;
    }

    public void setHelp(String help) {
        Help = help;
    }
}
