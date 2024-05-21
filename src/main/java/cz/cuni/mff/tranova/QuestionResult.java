package cz.cuni.mff.tranova;

public class QuestionResult {
    private String text;
    private String userAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    public QuestionResult(String questionText, String userAnswer, String correctAnswer, boolean isCorrect) {
        this.text = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public String getQuestionText() {
        return text;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }}
