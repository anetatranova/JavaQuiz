package cz.cuni.mff.tranova;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class Game {

    private static ArrayList<Question> questions = new ArrayList<Question>();

    private static void loadQuestions(String fileName){
        try {
            Path path = Paths.get(Game.class.getClassLoader().getResource(fileName).toURI());
            List<String> lines = Files.readAllLines(path);
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
        catch (IOException e){
            System.out.println("unable to read file " + fileName);
        }
        catch (Exception e){
            System.out.println("unexpected error " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void startQuiz() {
        Scanner scanner = new Scanner(System.in);

        List<String> uniqueCategories = new ArrayList<>();
        Map<Integer, String> categories = new LinkedHashMap<>();
        int index = 1;

        for (Question q : questions) {
            if (!uniqueCategories.contains(q.category)) {
                uniqueCategories.add(q.category);
            }
        }

        for (String category : uniqueCategories) {
            categories.put(index++, category);
        }

        System.out.println("vyber kategorii/e: ");
        for (Map.Entry<Integer, String> entry : categories.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        //enter number of the category or 0 for all categories

        String inputLine = scanner.nextLine();
        String[] inputs = inputLine.split("\\s+");
        List<Integer> selectedNumbers = new ArrayList<>();

        try{
            for (String input : inputs){
                int selectedInt = Integer.parseInt(input);
                if (categories.containsKey(selectedInt) || selectedInt == 0){
                    selectedNumbers.add(selectedInt);
                } else {
                    throw new IllegalArgumentException("invalid selection");
                }
            }
        } catch (NumberFormatException e){
            System.out.println("invalid input, numbers expected");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        List<Question> filteredQuestions = new ArrayList<>();
        if (selectedNumbers.contains(0)) {
            filteredQuestions.addAll(questions);
        } else {
            for (Question q : questions) {
                if (isCategorySelected(q, categories, selectedNumbers)) {
                    filteredQuestions.add(q);
                }
            }
        }

        loop(filteredQuestions);
    }

    private static boolean isCategorySelected(Question q, Map<Integer, String> categories, List<Integer> selectedNumbers) {
        for (Integer num : selectedNumbers) {
            if (categories.get(num).equalsIgnoreCase(q.category)) {
                return true;
            }
        }
        return false;
    }


    private static void loop(List<Question> filteredQuestions) {
        Scanner scanner = new Scanner(System.in);

        int correctCount = 0;
        int questionCount = filteredQuestions.size();

        for (int qIndex = 0; qIndex < questionCount; qIndex++){
            Question q = filteredQuestions.get(qIndex);
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
        startQuiz();
    }
}