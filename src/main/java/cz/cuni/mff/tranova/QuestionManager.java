package cz.cuni.mff.tranova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionManager {
    //load questions from file
    //parse questions into Question objects
    //filtering fuctionality based on categories

    private static ArrayList<Question> questions;

    public QuestionManager(){
        this.questions = new ArrayList<>();
    }
    public List<Question> loadQuestions(String fileName){

        try {
            Path path = Paths.get(Game.class.getClassLoader().getResource(fileName).toURI());
            List<String> lines = Files.readAllLines(path);
            parseQuestions(lines);
        } catch (Exception e) {
            System.out.println("Error loading questions: " + e.getMessage());
            return new ArrayList<>();  // Return an empty list in case of error
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
                    questions.add(new Question(category, questionText, rightAnswer, wrongAnswers));
                }
                category = null;
                questionText = null;
                rightAnswer = null;
                wrongAnswers = new ArrayList<>();
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