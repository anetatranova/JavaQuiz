package cz.cuni.mff.tranova;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Question {
    public String category;
    public String text;
    public String rightAnswer;
    public List<String> answers;

    public Question(String text, String rightAnswer, List<String> wrongAnswers){
        //konstruktry
        this.category = category;
        this.text = text;
        this.rightAnswer = rightAnswer;
        this.answers = new ArrayList<>(wrongAnswers);
        this.answers.add(rightAnswer);
        Collections.shuffle(this.answers);

    }
}
