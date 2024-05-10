package cz.cuni.mff.tranova;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Game {

    private static Scanner scanner = new Scanner(System.in);


    public Game(){
        QuestionManager questionManager = new QuestionManager();
        CategoryManager categoryManager = new CategoryManager();
        Scanner scanner = new Scanner(System.in);
    }

    public static void main(String[] args) throws IOException {
        String filename = "questions.txt"; // Default file name

        QuestionManager questionManager = new QuestionManager();
        CategoryManager categoryManager = new CategoryManager();
        UIHelper uiHelper = new UIHelper();

        while (true) {
            List<Question> allQuestions = questionManager.loadQuestions(filename);
            QuizManager quizManager = new QuizManager(questionManager, categoryManager, uiHelper);
            quizManager.startQuiz(allQuestions);

            System.out.println("Do you want to continue? (Yes to continue, Load to load a new file, No to exit):");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("no")) {
                System.out.println("Exiting the program...");
                break;
            } else if (input.equals("load")) {
                System.out.println("Enter the new file name:");
                filename = scanner.nextLine().trim();
            } // If "Yes", the loop continues with the same file
        }


    }
}
