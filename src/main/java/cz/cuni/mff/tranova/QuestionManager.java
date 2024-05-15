package cz.cuni.mff.tranova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Spravuje a organizuje otázky - načítá je ze souboru, parsuje je do Question objektu, filtruje je podle kategorie
 */
public class QuestionManager {

    private ArrayList<Question> questions;
    /**
     * Konstruktor pro novoy QuestionManager instanci, inicializuje seznams  Question objekty
     */
    public QuestionManager(){
        this.questions = new ArrayList<>();
    }
    /**
     * Načítá otázky kvízu ze souboru a zprasuje je na Question objekt
     *
     * @param fileName jméno souboru, ze kterého načíst data
     * @return seznam Question objektů zprasovaných ze souboru
     */
    public List<Question> loadQuestions(String fileName){
        this.questions.clear();
        
        try {
            Path path = Paths.get(Game.class.getClassLoader().getResource(fileName).toURI());
            List<String> lines = Files.readAllLines(path);
            parseQuestions(lines);
        } catch (Exception e) {
            System.out.println("Problém při načítání otázek: " + e.getMessage());
            this.questions = new ArrayList<>();
        }
        return new ArrayList<>(this.questions);
    }
    /**
     * Zparsuje seznam stringů do Question objektů, předpokládá, že jsou otázky napsané ve správném formátu
     * a odděleny prázdným řádkem/řádky
     *
     * @param lines řádky textu reprezentující Question objekt
     */
    private void parseQuestions(List<String> lines){
        List<String> wrongAnswers = new ArrayList<>();
        String category = null;
        String questionText = null;
        String rightAnswer = null;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (category!= null && questionText != null && rightAnswer != null &&!wrongAnswers.isEmpty()) {
                    this.questions.add(new Question(category, questionText, rightAnswer, wrongAnswers));
                    wrongAnswers = new ArrayList<>();
                }
                category =null;
                questionText = null;
                rightAnswer = null;
            } else if (category == null) {
                category = line;
            }else if (questionText== null) {
                questionText = line;
            } else if (rightAnswer == null) {
                rightAnswer= line;
            } else {
                wrongAnswers.add(line);
            }
        }
        if (category != null &&questionText != null && rightAnswer != null &&!wrongAnswers.isEmpty()) {
            questions.add(new Question(category,questionText, rightAnswer, wrongAnswers));
        }
    }
    /**
     * Filtruje senznam otázek podle kategorie
     *
     * @param questions seznam Question objektů
     * @param categoryNames seznam kategorií
     * @return seznam otázek vyfiltrovaných podle kategorie
     */
    public List<Question> filterQuestions(List<Question> questions, List<String> categoryNames){
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (categoryNames.contains(question.getCategory())) {
                filteredQuestions.add(question);
            }
        }
        return filteredQuestions;
    }

}
