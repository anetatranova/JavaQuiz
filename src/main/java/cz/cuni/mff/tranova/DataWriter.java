package cz.cuni.mff.tranova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class DataWriter {
    public static final String QUIZ_RESULTS_FILE = "./data/quiz_results.txt";
    public static final String USER_STATS_FILE = "./data/user_statistics.txt";

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
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Chyba při psaní do souboru quiz_results.txt: " + e.getMessage());
        }
    }

    public static void writeUserStatistics(User user) {
        String data = user.getUsername() + "\n" +
                "počet kvízů: " + user.getQuizzesTaken() + "\n" +
                "nejvyšší dosažené skóre: "+ user.getHighestScore() + "\n" +
                "průměrné skóre: " + String.format("%.2f\n", user.getAverageScore());

        try {
            Files.write(Paths.get(USER_STATS_FILE),
                    (data + "\n").getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Chyba při psaní do souboru user_statistics.txt: " + e.getMessage());
        }
    }

    private static double calculateWinPercentage(Map<String, Integer[]> categoryResults) {
        int totalCorrect = categoryResults.values().stream().mapToInt(c -> c[0]).sum();
        int totalQuestions = categoryResults.values().stream().mapToInt(c -> c[0] + c[1]).sum();
        return totalQuestions > 0 ? (double) totalCorrect / totalQuestions * 100.0 : 0.0;
    }
}
