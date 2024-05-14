package cz.cuni.mff.tranova;

import java.util.*;

public class UIHelper {
    private Scanner scanner;

    public UIHelper() {
        this.scanner = new Scanner(System.in);
    }

    public void showCategories(Map<Integer, String> categories, CategoryManager categoryManager) {
        System.out.println(" \n Vyber kategorii/e, číslem 0 vybereš všechny.");
        for (Map.Entry<Integer, String> entry : categories.entrySet()) {
            int questionCount = CategoryManager.getQuestionCountByCategory(entry.getValue());
            System.out.println(entry.getKey() + ": " + entry.getValue() + " (" + questionCount + " otázek)");
        }
    }

    public List<String> getUserCategorySelections(Map<Integer, String> categories) {
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
                        System.out.println("špatně zvolené číslo " + selection + ", zkus to znovu.");
                        selections.clear();
                        break;
                    }
                }
                if (!selections.isEmpty()) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("musíš vybrat číslo.");
                selections.clear();
            }
        }
        return selections;
    }

    public int getUserQuestionCount(int maxQuestions) {
        System.out.println("Kolik otázek v kvízu chceš? (max " + maxQuestions + ")");
        while (true) {
            try {
                int count = Integer.parseInt(scanner.nextLine());
                if (count >= 1 && count <= maxQuestions) {
                    return count;
                } else {
                    System.out.println("Vyber číslo v rozmezí 1 do " + maxQuestions + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Musíš vybrat pouze číslo.");
            }
        }
    }

    public void displayFinalScore(int score, int totalQuestions) {
        System.out.println("Kvíz jsi dokončil se skórem " + score + " z " + totalQuestions + ".");
    }

    public void displayQuestionAndOptions(Question question, int questionNumber, int totalQuestions) {
        System.out.println("Otázka " + questionNumber + " z " + totalQuestions + ": " + question.getText());
        List<String> labels = generateAnswerLabels(question.getAnswers().size());
        for (int i = 0; i < question.getAnswers().size(); i++) {
            System.out.println(labels.get(i) + ". " + question.getAnswers().get(i));
        }
    }

    public String getValidAnswerFromUser(int optionsSize) {
        List<String> validAnswers = generateAnswerLabels(optionsSize);
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            if (validAnswers.contains(input)) {
                return input;
            } else {
                System.out.println("Neplatný vstup, vyber písmeno z nabídky.");
            }
        }
    }

    public static List<String> generateAnswerLabels(int numberOfAnswers) {
        List<String> labels = new ArrayList<>();
        int firstLetter = 'A';
        int range = 26;  // Number of letters in the alphabet

        for (int i = 0; i < numberOfAnswers; i++) {
            int firstCharNum = i / range;
            int secondCharNum = i % range;
            StringBuilder label = new StringBuilder();
            if (firstCharNum > 0) {
                label.append((char) (firstLetter + firstCharNum - 1));
            }
            label.append((char) (firstLetter + secondCharNum));
            labels.add(label.toString());
        }
        return labels;
    }

    public void displayUserStatistics(User user) {
        System.out.println("Statistics for " + user.getUsername() + ":");
        System.out.println("Highest Score: " + user.getHighestScore());
        System.out.println("Total Quizzes Taken: " + user.getQuizzesTaken());
        System.out.println("Average Score: " + user.getAverageScore());
    }
}
