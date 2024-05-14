package cz.cuni.mff.tranova;

import java.util.*;

public class Question {
    public String category;
    public String text;
    public String rightAnswer;
    public List<String> answers;
    private String userAnswer;

    public Question(String category, String text, String rightAnswer, List<String> wrongAnswers){
        //konstruktry
        this.category = category;
        this.text = text;
        this.rightAnswer = rightAnswer;
        this.answers = new ArrayList<>(wrongAnswers);
        this.answers.add(rightAnswer);
        Collections.shuffle(this.answers);

    }

    public String getCategory() {
        return this.category;
    }

    public String getRightAnswer(){
        return this.rightAnswer;
    }

    public String getText(){
        return  this.text;
    }

    public List<String> getAnswers(){
        return this.answers;
    }
    public boolean checkAnswer(String userAnswer) {
        return rightAnswer.equalsIgnoreCase(userAnswer);
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return userAnswer != null && userAnswer.equalsIgnoreCase(rightAnswer);
    }
}
