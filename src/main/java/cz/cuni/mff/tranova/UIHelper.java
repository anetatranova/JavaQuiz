package cz.cuni.mff.tranova;

import java.util.*;

public class UIHelper {
    private Scanner scanner;

    public UIHelper() {
        this.scanner = new Scanner(System.in);
    }

    public void showCategories(Map<Integer, String> categories, CategoryManager categoryManager) {
        System.out.println("Available categories (Enter '0' to select all):");
        for (Map.Entry<Integer, String> entry : categories.entrySet()) {
            int questionCount = categoryManager.getQuestionCountByCategory(entry.getValue());
            System.out.println(entry.getKey() + ": " + entry.getValue() + " (" + questionCount + " questions)");
        }
    }

    public List<String> getUserCategorySelections(Map<Integer, String> categories) {
        System.out.println("Please enter the numbers of the categories you wish to answer questions from, separated by spaces:");
        List<String> selections = new ArrayList<>();
        boolean valid = false;
        while (!valid) {
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            try {
                for (String part : parts) {
                    int selection = Integer.parseInt(part);
                    if (selection == 0) {
                        // If user enters '0', select all categories
                        selections.addAll(categories.values());
                        valid = true;
                        break;
                    } else if (categories.containsKey(selection)) {
                        selections.add(categories.get(selection));
                    } else {
                        System.out.println("Invalid category number: " + selection + ". Please try again.");
                        selections.clear();
                        break;
                    }
                }
                if (!selections.isEmpty()) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numbers only.");
                selections.clear();
            }
        }
        return selections;
    }

    public int getUserQuestionCount(int maxQuestions) {
        System.out.println("How many questions would you like to answer? (up to " + maxQuestions + "):");
        while (true) {
            try {
                int count = Integer.parseInt(scanner.nextLine());
                if (count >= 1 && count <= maxQuestions) {
                    return count;
                } else {
                    System.out.println("Please enter a number between 1 and " + maxQuestions + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input; please enter a number.");
            }
        }
    }

    public void displayFinalScore(int score, int totalQuestions) {
        System.out.println("Quiz completed! You scored " + score + " out of " + totalQuestions + ".");
    }

    public void displayQuestionAndOptions(Question question, int questionNumber, int totalQuestions) {
        System.out.println("Question " + questionNumber + " of " + totalQuestions + ": " + question.getText());
        char option = 'A';
        Map<Character, String> answerMap = new HashMap<>();
        for (String answer : question.getAnswers()) {
            System.out.println(option + ". " + answer);
            answerMap.put(option, answer);
            option++;
        }
        question.setAnswerMap(answerMap);  // Store the map in the Question object if needed, or manage it externally
    }
    public char getValidAnswerFromUser(int optionsSize) {
        while (true) {
            System.out.print("Enter your answer (A, B, C, etc.): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.length() == 1 && input.charAt(0) >= 'A' && input.charAt(0) < 'A' + optionsSize) {
                return input.charAt(0);
            } else {
                System.out.println("Invalid input. Please select from the given options.");
            }
        }
    }
}
