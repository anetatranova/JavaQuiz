package cz.cuni.mff.tranova;

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
        catch (Exception e){
            System.out.println("couldnt read " + fileName);
            System.exit(-1);
        }
    }

    private static void loop() {
        Scanner scanner = new Scanner(System.in);

        while (questions.size() > 0){
            Question q = questions.remove(0);
            System.out.println(q.text);

            for (int i = 0; i < q.answers.size(); i++){ //number of question + actual question
                System.out.println(i + " " + q.answers.get(i));
            }

            int input = scanner.nextInt();

            if (input< 0 || input > q.answers.size() - 1){ //check if input is in valid range
                System.out.println("invalid input");
                System.exit(-2); //either exit or continue and show next question
            }

            //input valid
            if (q.rightAnswer.equals(q.answers.get(input))){
                System.out.println("right");
            }
            else {
                System.out.println("wrong");
            }
        }
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