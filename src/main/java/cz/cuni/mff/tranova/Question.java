package cz.cuni.mff.tranova;

import java.util.*;

public class Question {
    public String category;
    public String text;
    public String rightAnswer;
    public List<String> answers;
    private Map<Character, String> answerMap;

    public Question(String category, String text, String rightAnswer, List<String> wrongAnswers){
        //konstruktry
        this.category = category;
        this.text = text;
        this.rightAnswer = rightAnswer;
        this.answers = new ArrayList<>(wrongAnswers);
        this.answers.add(rightAnswer);
        this.answerMap = new HashMap<>();
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
    // Sets the mapping of answer labels to their corresponding text
    public void setAnswerMap(Map<Character, String> answerMap) {
        this.answerMap = answerMap;
    }
}
