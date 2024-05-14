package cz.cuni.mff.tranova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionManager {

    private ArrayList<Question> questions;

    public QuestionManager(){
        this.questions = new ArrayList<>();
    }
    public List<Question> loadQuestions(String fileName){
        this.questions.clear();
        
        try {
            Path path = Paths.get(Game.class.getClassLoader().getResource(fileName).toURI());
            List<String> lines = Files.readAllLines(path);
            parseQuestions(lines);
        } catch (Exception e) {
            System.out.println("Error loading questions: " + e.getMessage());
            this.questions = new ArrayList<>();
        }
        return new ArrayList<>(this.questions);
    }

    private void parseQuestions(List<String> lines){
        List<String> wrongAnswers = new ArrayList<>();
        String category = null;
        String questionText = null;
        String rightAnswer = null;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (category != null && questionText != null && rightAnswer != null && !wrongAnswers.isEmpty()) {
                    this.questions.add(new Question(category, questionText, rightAnswer, wrongAnswers));
                    wrongAnswers = new ArrayList<>();
                }
                category = null;
                questionText = null;
                rightAnswer = null;
            } else if (category == null) {
                category = line;
            }else if (questionText == null) {
                questionText = line;
            } else if (rightAnswer == null) {
                rightAnswer = line;
            } else {
                wrongAnswers.add(line);
            }
        }
        if (category != null && questionText != null && rightAnswer != null && !wrongAnswers.isEmpty()) {
            questions.add(new Question(category, questionText, rightAnswer, wrongAnswers));
        }
    }

    public List<Question> filterQuestions(List<Question> questions, List<String> categoryNames){
        return questions.stream()
                .filter(q -> categoryNames.contains(q.getCategory()))
                .collect(Collectors.toList());
    }

}
