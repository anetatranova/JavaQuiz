package cz.cuni.mff.tranova;

public class Question {
    public String text;
    public String rightAnswer;
    public String[] answers;

    public Question(String text, String... answers){
        //konstruktry
        this.text = text;
        this.rightAnswer = answers[0];
        this.answers = answers;

        //shuffle
        for (int i = 0; i < answers.length; i++){
            int randomIndex = (int)(Math.random() * answers.length);
            String tmp = answers[i];
            answers[i] = answers[randomIndex];
            answers[randomIndex] = tmp;
        }
    }
}
