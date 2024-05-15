package cz.cuni.mff.tranova;

import java.util.*;

/**
 * Spravuje uživatelské rozhraní jako výčet kategorií a otázek a zachycuje uživatelský vstup, který dál zpracovávát
 * Tato třída přímo iteraguje s uživatelem a organizuje je tak vstupy a výstupy programu
 */

public class UIHelper {
    private static Scanner scanner;

    /**
     * Inicializuje novou instanci UIHelper se Scanner objektem
     */

    public UIHelper() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * ZObrazuje dostupné kvízové kategorie
     *
     * @param categories mapa s číselným identifikétorem a kategorie
     * @param categoryManager pomocná třída pro práci s kategoriemi, použita pro získání počtu otázek
     */

    public void showCategories(Map<Integer, String> categories, CategoryManager categoryManager) {
        System.out.println(" \n Vyber kategorii/e, číslem 0 vybereš všechny.");
        for (Map.Entry<Integer, String> entry : categories.entrySet()) {
            int questionCount = CategoryManager.getQuestionCountByCategory(entry.getValue());
            System.out.println(entry.getKey() + ": " + entry.getValue() + " (" + questionCount + " otázek)");
        }
    }

    /**
     * Zachycuje výběr kategorie
     *
     * @param categories mapa s číselným identifikétorem a kategorie
     * @return seznam jmen vybraných kategorií
     */

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

    /**
     * Zeptá se chtěný počet otázek v kvízu
     *
     * @param maxQuestions maximální počet otázek
     * @return počet otázek na které chce uživatek odpovědět
     */

    public int getUserQuestionCount(int maxQuestions) {
        System.out.println("Kolik otázek v kvízu chceš? (max " + maxQuestions + ")");
        return getValidChoice(1, maxQuestions);
    }

    /**
     * Zobrazí konečné skóre na konci kvízu
     *
     * @param score získané skóre
     * @param totalQuestions celkový počet otázke, na které uživatel odpovíděl
     */

    public void displayFinalScore(int score, int totalQuestions) {
        System.out.println("Kvíz jsi dokončil se skórem " + score + " z " + totalQuestions + ".");
    }

    /**
     * Zobrazí otázku a její odpovědi
     *
     * @param question otázka k zobrazení
     * @param questionNumber kolikátá otázka je zobrazena
     * @param totalQuestions celkový počet otázke, na které uživatel odpovídá
     */

    public void displayQuestionAndOptions(Question question, int questionNumber, int totalQuestions) {
        System.out.println("Otázka " + questionNumber + " z " + totalQuestions + ": ");
        for (String line : wrapText(question.getText(), 80)) {  // Assuming a maximum width of 80 characters
            System.out.println(line);
        }
        List<String> labels = generateAnswerLabels(question.getAnswers().size());
        int labelIndex = 0;
        for (String answer : question.getAnswers()) {
            List<String> wrappedAnswer = wrapText(answer, 80);
            if (!wrappedAnswer.isEmpty()) {
                System.out.println(labels.get(labelIndex++) + ". " + wrappedAnswer.get(0));
                for (int i = 1; i < wrappedAnswer.size(); i++) {
                    System.out.println("   " + wrappedAnswer.get(i)); // Indent continuation lines for clarity
                }
            }
        }
    }

    /**
     * Zajišťuje, že uživatel odpovídá validně
     *
     * @param optionsSize počet možností
     * @return validní odpověď vybraná uživatelem
     */

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

    /**
     * Pomocná metoda pro zajištění správného výběru čísla
     *
     * @param min spodní hranice
     * @param max horní hranice
     * @return validní odpověď vybraná užiavtelem
     */

    static int getValidChoice(int min, int max){
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
                System.out.println("Špatný vstup, zadej platný číselný vstup");
            }
        }
    }

    /**
     * Generuje písmena pro odpovědi, pro odpovědi delší než 26 generuje AA,AB,AC, ...
     *
     * @param numberOfAnswers The number of answers that need labels.
     * @return A list of labels for the answers.
     */

    public static List<String> generateAnswerLabels(int numberOfAnswers) {
        List<String> labels = new ArrayList<>();
        int firstLetter = 'A';

        for (int i = 0; i < numberOfAnswers; i++) {
            int firstCharNum = i / 26;                  //kolikate kolo
            int secondCharNum = i % 26;                 //kolikate pismeno

            StringBuilder label = new StringBuilder();

            if (firstCharNum > 0) {                     //pokud je pocet vyssi nez 26
                label.append((char)(firstLetter + firstCharNum - 1));
            }
            label.append((char)(firstLetter+ secondCharNum));
            labels.add(label.toString());
        }
        return labels;
    }

    /**
     * Zobrazí statistické výsledky
     *
     * @param user přezdívka pro kterou informace ukázat
     */

    public void displayUserStatistics(User user) {
        System.out.println("\nStatistika " + user.getUsername() + ":");
        System.out.println("Nejvyšší dosažené skóre: " + user.getHighestScore());
        System.out.println("Počet dokončených kvízu: " + user.getQuizzesTaken());
        System.out.println("Průměrné skóre: " + user.getAverageScore());
    }

    /**
     * Zarovná text do bloku podel dané délky
     *
     * @param text text, který zarovnat
     * @param maxWidth maximíální počet znakůna řádku
     * @return list stringů, kde každý string reprezentuje jeden řádek
     */
    public List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            if (!currentLine.isEmpty()) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }


        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }
}
