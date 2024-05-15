package cz.cuni.mff.tranova;

import java.util.*;
/**
 * Tato třída reprezentuje jednu otázku - obsahuje kategorii, otázku, správnou odpověď a ostatní odpovědi
 */
public class Question {
    public String category;
    public String text;
    public String rightAnswer;
    public List<String> answers;
    private String userAnswer;
    /**
     * Konstruktory pro novou Question instanci
     * Všechny odpovědi jsou zamíchany
     *
     * @param category kategorie ke které otázka patří
     * @param text text otázky
     * @param rightAnswer správná odpověď
     * @param wrongAnswers senznam nesprávných odpovědí
     */
    public Question(String category, String text, String rightAnswer, List<String> wrongAnswers){
        //konstruktry
        this.category = category;
        this.text = text;
        this.rightAnswer = rightAnswer;
        this.answers = new ArrayList<>(wrongAnswers);
        this.answers.add(rightAnswer);
        Collections.shuffle(this.answers);

    }
    /**
     * Získá kategorii otázky
     *
     * @return kategorii otázky
     */
    public String getCategory() {
        return this.category;
    }
    /**
     * Získá správnou odpověď
     *
     * @return správnou odpověď
     */
    public String getRightAnswer(){
        return this.rightAnswer;
    }
    /**
     * Získá text otázky
     *
     * @return text otázky
     */
    public String getText(){
        return  this.text;
    }
    /**
     * Získá všechny možné odpovědi na otázku
     *
     * @return seznam odpovědí
     */
    public List<String> getAnswers(){
        return this.answers;
    }
    /**
     * nastaví odpověď uživatele
     *
     * @param userAnswer odpověď uživatele
     */
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
    /**
     * Zkontruleje jestli je odpověď uživatele správná
     *
     * @return True pokud je odpověď správná, False pokud ne
     */
    public boolean isCorrect() {
        return userAnswer != null && userAnswer.equalsIgnoreCase(rightAnswer);
    }
}
