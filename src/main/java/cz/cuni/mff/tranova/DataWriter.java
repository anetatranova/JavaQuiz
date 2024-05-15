package cz.cuni.mff.tranova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
/**
 * Spravuje zápis dat do textových souborů
 * Tato třída obsahuje metody pro psaní výsledků kvízu a statistických informací do souborů
 */
public class DataWriter {
    public static final String QUIZ_RESULTS_FILE = "./data/quiz_results.txt";
    public static final String USER_STATS_FILE = "./data/user_statistics.txt";
    /**
     * Zapisuje výsledky kvízu uživatele do textového souboru
     * Každý zápis obsahuje přezdívku, timestamp dokončení kvízu, název kvízového souboru
     * dosažené skóre, úspěšnost a výpis výsledků podle kategorie
     *
     * @param user přezdívka uživatele
     * @param score dosažené skóre
     * @param categoryResults mapa kategorie a pole dvou intů reprezentující správně a špatně zodpovězené otázky
     * @param filename název kvízového souboru
     * @throws IOException pokud se zápis do souboru nezdaří
     */
    public static void writeQuizResults(User user, int score, Map<String, Integer[]> categoryResults, String filename) {
        StringBuilder result = new StringBuilder();
        result.append(user.getUsername()).append("\n");
        result.append(java.time.LocalDateTime.now()).append("\n");
        result.append("název souboru: ").append(filename).append("\n");
        result.append("počet bodů: ").append(score).append("\n");
        double winPercentage = calculateWinPercentage(categoryResults);
        result.append("úspěšnost: ").append(String.format("%.2f", winPercentage)).append(" %").append("\n");

        categoryResults.forEach((category, counts) ->
                result.append(category).append(": ").append(counts[0]).append(" správně, ").append(counts[1]).append(" špatně \n")
        );

        try {
            Files.write(Paths.get(QUIZ_RESULTS_FILE),
                    (result.toString() + "\n").getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Chyba při psaní do souboru quiz_results.txt: " + e.getMessage());
        }
    }
    /**
     * zapisuje aktuální statistické informace pro uživatele do souboru
     * to zahrnuje: počet dokončených kvízů, nejvyšší dosažené skóre, průměrné dosažené skóre
     *
     * @param user přezdívka pro kterou se hodnoty aktualizují
     * @throws IOException pokud se zápis do souboru nezdaří
     */
    public static void writeUserStatistics(User user) {
        String data = user.getUsername() + "\n" +
                "počet kvízů: " + user.getQuizzesTaken() + "\n" +
                "nejvyšší dosažené skóre: "+ user.getHighestScore() + "\n" +
                "průměrné skóre: " + String.format("%.2f\n", user.getAverageScore());

        try {
            Files.write(Paths.get(USER_STATS_FILE),
                    (data + "\n").getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Chyba při psaní do souboru user_statistics.txt: " + e.getMessage());
        }
    }

    /**
     * Vypočítá úspěšnost podle počtu správně zodpovězených otázek a celkového počtu zodpovězených otázek
     *
     * @param categoryResults mapa kategorie a pole dvou intů reprezentující správně a špatně zodpovězené otázky
     * @return double úspěšnost v procentech
     */

    private static double calculateWinPercentage(Map<String, Integer[]> categoryResults) {
        int totalCorrect =0;
        int totalQuestions =0;

        for (Integer[] counts : categoryResults.values()) {
            if (counts.length == 2) {
                totalCorrect += counts[0];     // counts[0] počet sprácných odpovědí
                totalQuestions += counts[0] + counts[1];  // counts[1] počet špatných odpovědí
            }
        }
        if (totalQuestions > 0) {
            return (double) totalCorrect / totalQuestions * 100.0;
        } else {
            return 0.0;
        }
    }

}
