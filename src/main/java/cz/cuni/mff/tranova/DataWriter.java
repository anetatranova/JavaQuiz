package cz.cuni.mff.tranova;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
/**
 * Spravuje zápis dat do textových souborů
 * Tato třída obsahuje metody pro psaní výsledků kvízu a statistických informací do souborů
 */
public class DataWriter {
    /**
     * Cesta k souboru s výsledky kvízu, tento soubor obsahuje podrobné záznamy o výsledcích každého kvízu
     */
    public static final String QUIZ_RESULTS_FILE = "./data/quiz_results.txt";
    /**
     * Cesta k souboru se statistikami uživatelů, tento soubor obsahuje souhrnné statistiky o výkonech všech uživatelů
     */
    public static final String USER_STATS_FILE = "./data/user_statistics.txt";

    /**
     * Inicialuzije novou instanci třídy DataWriter
     */
    public DataWriter() {
        super();
    }

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
    public static void writeQuizResults(User user, int score, Map<String, Integer[]> categoryResults, String filename) throws IOException{
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

        Files.write(Paths.get(QUIZ_RESULTS_FILE),
                (result + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);

    }
    /**
     * zapisuje aktuální statistické informace pro uživatele do souboru
     * to zahrnuje: počet dokončených kvízů, nejvyšší dosažené skóre, průměrné dosažené skóre
     *
     * @param user přezdívka pro kterou se hodnoty aktualizují
     * @throws IOException pokud se zápis do souboru nezdaří
     */
    public static void writeUserStatistics(User user) throws IOException {
        String data = user.getUsername() + "\n" +
                "počet kvízů: " + user.getQuizzesTaken() + "\n" +
                "nejvyšší dosažené skóre: "+ user.getHighestScore() + "\n" +
                "průměrné skóre: " + String.format("%.2f\n", user.getAverageScore());

        Files.write(Paths.get(USER_STATS_FILE),
                (data + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
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

    /**
     * Zapíše chybový protokol do vybraného souboru
     *
     * @param user aktuální uživatel
     * @param results výsledky dokončeného kvízu
     * @param protocolFilename soubor do kterého chybový protocol uložit
     * @param filename kvízový soubor
     */
    public static void saveQuizProtocol(User user, List<QuestionResult> results, String protocolFilename, String filename) {
        Path path = Paths.get(protocolFilename);

        List<String> lines = new ArrayList<>();
        lines.add("Přezdívka: "+ user.getUsername());
        lines.add("Čas: " + java.time.LocalDateTime.now());
        lines.add("Název kvízového souboru: " + filename + "\n");
        for (QuestionResult result : results){
            lines.add("Otázka: " + result.getQuestionText());
            lines.add("Vaše odpověď:    " + result.getUserAnswer());
            lines.add("Správná odpověď: " + isCorrectAnswer(result) + "\n");
        }

        try {
            Files.write(path, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
        }catch (IOException e){
            System.out.println("Zápis do protokolu se nezdařil");
        }
    }

    private static String isCorrectAnswer(QuestionResult result){
        String line = "";
        if (result.isCorrect()){
            line = "Vaše odpověď byla správná.";
        }
        else{
            line= result.getCorrectAnswer();
        }
        return line;
    }

}
