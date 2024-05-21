package cz.cuni.mff.tranova;

/**
 * Ukládá výsledky kvízu pro zápis chybového protokolu
 */
public class QuestionResult {
    private String text;
    private String userAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    /**
     * Konstruktory pro novoy QuestionResult instanci
     *
     * @param questionText  znění otázky
     * @param userAnswer    odpověď uživatele
     * @param correctAnswer správná odpověď
     * @param isCorrect     True pokud je otázka správně zodpovězená, False pokud opak
     */
    public QuestionResult(String questionText, String userAnswer, String correctAnswer, boolean isCorrect) {
        this.text = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    /**
     * získá znění otázky
     *
     * @return znění otázky
     */
    public String getQuestionText() {
        return text;
    }

    /**
     * získá odpověď uživatele
     *
     * @return odpověď uživatele
     */
    public String getUserAnswer() {
        return userAnswer;
    }

    /**
     * získá správnou odpověď k otázce
     *
     * @return správná odpověď
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * značí zda je otázka správně zodpovězená
     *
     * @return zda je otázka správně zodpovězená či nikoli
     */
    public boolean isCorrect() {
        return isCorrect;
    }
}
