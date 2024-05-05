package cz.cuni.mff.tranova;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Game {

    private static ArrayList<Question> questions = new ArrayList<Question>();

    private static void loadQuestions(String fileName){
        try {
            Path path = Paths.get(Game.class.getClassLoader().getResource(fileName).toURI());
            List<String> lines = Files.readAllLines(path);
            List<String> wrongAnswers = new ArrayList<>();
            String questionText = null;
            String rightAnswer = null;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    if (questionText != null && rightAnswer != null && !wrongAnswers.isEmpty()) {
                        questions.add(new Question(questionText, rightAnswer, wrongAnswers));
                    }
                    questionText = null;
                    rightAnswer = null;
                    wrongAnswers = new ArrayList<>();
                } else if (questionText == null) {
                    questionText = line;
                } else if (rightAnswer == null) {
                    rightAnswer = line;
                } else {
                    wrongAnswers.add(line);
                }
            }
            if (questionText != null && rightAnswer != null && !wrongAnswers.isEmpty()) {
                questions.add(new Question(questionText, rightAnswer, wrongAnswers));
            }
        }
        catch (IOException e){
            System.out.println("unable to read file " + fileName);
        }
        catch (Exception e){
            System.out.println("unexpected error " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void loop() {
        Scanner scanner = new Scanner(System.in);

        int correctCount = 0;
        int questionCount = questions.size();

        for (int qIndex = 0; qIndex < questionCount; qIndex++){
            Question q = questions.get(qIndex);
            System.out.println("Otázka "+ (qIndex + 1) +"/"+questionCount + ": " + q.text);
            char option = 'A';

            for (String answer : q.answers){
                System.out.println(option + ") " + answer);
                option++;
            }

            boolean validInput = false;
            int selectedOpt = -1;

            while (!validInput) {
                String input = scanner.next().toUpperCase();

                if (input.length() == 1 && input.charAt(0) >= 'A' && input.charAt(0) < 'A' + q.answers.size()){
                    selectedOpt = input.charAt(0) - 'A';
                    validInput = true;
                } else {
                    System.out.println("invalid input, try again");
                }
            }

            //input valid
            if (q.rightAnswer.equals(q.answers.get(selectedOpt))){
                System.out.println("right");
                correctCount++;
            }
            else {
                System.out.println("wrong");
            }
        }
        System.out.println("hotovo, správně zodpovězených otázek bylo " + correctCount + " z " + questionCount + " otázek");
        scanner.close();
    }


    public static void main(String[] args){
        String fileName = "questions.txt"; // Default file name
        if (args.length > 0) {
            fileName = args[0]; // Takes the first command line argument as file name if provided
        }

        loadQuestions(fileName);
        loop();
    }
}