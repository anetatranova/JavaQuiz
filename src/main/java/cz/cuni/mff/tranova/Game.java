package cz.cuni.mff.tranova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Game {

    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static final QuestionManager questionManager = new QuestionManager();
    private static final CategoryManager categoryManager = new CategoryManager();
    private static final UIHelper uiHelper = new UIHelper();
    private static String filename = "questions.txt";

    public static void main(String[] args) {
        initialize();
        boolean continueRunning = true;
        while (continueRunning) {
            runQuizCycle();
            continueRunning = handlePostQuizOptions();
        }
    }

    private static void initialize(){
        System.out.println("zadej přezdívku: ");
        String username = scanner.nextLine().trim();
        User loadedUser = loadUserDataIfExists(username);
        if (loadedUser != null) {
            currentUser = loadedUser;
            System.out.println("Existing data loaded for " + username + ".");
        } else {
            currentUser = new User(username);  // Create a new user if no data found
            System.out.println("New user profile created for " + username + ".");
        }
    }

    private static void runQuizCycle(){
        List<Question> allQuestions = questionManager.loadQuestions(filename);
        QuizManager quizManager = new QuizManager(questionManager,categoryManager, uiHelper, currentUser);
        quizManager.startQuiz(allQuestions, filename);
        //currentUser.updateScores(score);
        DataWriter.writeUserStatistics(currentUser);
    }

    private static boolean handlePostQuizOptions(){
        while (true) {
            System.out.println(" \n Vyber z možností:");
            System.out.println("1. Pokračovat v kvízu (se stejným souborem)");
            System.out.println("2. Načíst nový soubor s otázkama");
            System.out.println("3. Změnit přezdívku");
            System.out.println("4. Podívat se na statistiky");
            System.out.println("5. Ukončit program");

            int choice = getValidChoice(1, 5);

            switch (choice) {
                case 1:
                    return true;
                case 2:
                    loadNewQuizFile();
                    return true;
                case 3:
                    changeUsername();
                    break;
                case 4:
                    uiHelper.displayUserStatistics(currentUser);
                    //DataWriter.writeUserStatistics(currentUser);
                    break;
                case 5:
                    System.out.println("Program ukončen");
                    System.exit(0);
                    return false;
                default:
                    break;
            }
        }
        //continue unless the user choosesto exit
    }

    private  static int getValidChoice(int min, int max){
        while (true){
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= min && input <= max){
                    return input;
                }
                else{
                    System.out.println("Zadej číslo v rozmezí od "+ min + "do " + max);
                }
            } catch (NumberFormatException e){
                System.out.println("Špatný vstup, zadej platný vstup");
            }
        }
    }

    private static void loadNewQuizFile(){
        System.out.println("Zadej jméno souboru s otázkama: ");
        filename = scanner.nextLine().trim();
    }

    private static void changeUsername() {
        System.out.println("Zadej novou přezdívku: ");
        String newUsername = scanner.nextLine().trim();
        User loadedUser = loadUserDataIfExists(newUsername);
        if (loadedUser != null) {
            currentUser = loadedUser;
            System.out.println("Byly nalezena existující data pro " + newUsername);
        } else {
            currentUser = new User(newUsername);
            System.out.println("Přezdívka změněna na " + newUsername);
        }
    }

    private static User loadUserDataIfExists(String username) {
        Path path = Paths.get(DataWriter.USER_STATS_FILE);
        try {
            List<String> lines = Files.readAllLines(path);
            User lastFoundUser = null;

            for (int i = lines.size() - 4; i >= 0; i--) {
                if (lines.get(i).trim().equals(username)) {
                    if (i + 3 < lines.size()) {
                        lastFoundUser = parseUserStatistics(lines.subList(i, i + 4));
                        break;
                    }
                }
            }
            return lastFoundUser;
        } catch (IOException e) {
            System.out.println("Chyba při čtení dat: " + e.getMessage());
            return null;
        }
    }

    private static User parseUserStatistics(List<String> userBlock) {
        String username = userBlock.get(0).trim();
        User user = new User(username);
        user.setTotalQuizzesTaken(Integer.parseInt(userBlock.get(1).split(": ")[1].trim()));
        user.setHighestScore(Integer.parseInt(userBlock.get(2).split(": ")[1].trim()));
        user.setAverageScore(Double.parseDouble(userBlock.get(3).split(": ")[1].trim()));
        return user;
    }
}
