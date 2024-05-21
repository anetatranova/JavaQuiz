package cz.cuni.mff.tranova;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Třída spravuje hlavní game loop a část interakce s uživatelem.
 * Spravuje vstup, quiz flow a podle výběru uživatele zřizuje akce, které provést dál.
 * Dovoluje uživateli kvíz začít, změnit kvízový soubor nebo přezdívku, či kouknout na statistiku pro danou přezdívku.
 */
public class Game {

    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static final QuestionManager questionManager = new QuestionManager();
    private static final CategoryManager categoryManager = new CategoryManager();
    private static final UIHelper uiHelper = new UIHelper();
    /**
     * Uložené výsledky kvízu
     */
    //private  static final  QuizManager quizManager = new QuizManager(questionManager, categoryManager, uiHelper, currentUser);
    public static List<QuestionResult> results = new ArrayList<>();
    private static String filename = "questions.txt";

    /**
     * Inicializuje novou instanci třídy Game
     */
    public Game(){
        super();
    }

    /**
     * Inicializuje kvíz
     *
     * @param args zde může být cesta ke kvízovému souboru
     * @throws IOException pokud nastane chyba při zápisu do souboru
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 0){
            filename = args[0];
        }
        initialize();
        boolean continueRunning = true;
        while (continueRunning) {
            runQuizCycle();
            continueRunning = handlePostQuizOptions();
        }
    }
    /**
     * inicializuje kvíz otázkou uživatele na přezdívku
     */
    private static void initialize(){
        System.out.println("\nJavaQuiz program spuštěn.");
        System.out.println("\nZadej přezdívku: ");
        String username = scanner.nextLine().trim();
        User loadedUser = loadUserDataIfExists(username);
        if (loadedUser != null) {
            currentUser = loadedUser;
            System.out.println("Byly nalezena existující data pro " + username + ".");
        } else {
            currentUser = new User(username);
            System.out.println("Byla načtena nová přezdívka " + username + ".");
        }
    }

    /**
     * Spustí jedno kolo kvízu
     *
     * @throws IOException pokud nastane chyba při zápisu statistik do souboru
     */

    private static void runQuizCycle() throws IOException {
        List<Question> allQuestions = questionManager.loadQuestions(filename);
        QuizManager quizManager = new QuizManager(questionManager,categoryManager, uiHelper, currentUser, results);
        quizManager.startQuiz(allQuestions,filename);
        results = quizManager.results;
        //currentUser.updateScores(score);
        DataWriter.writeUserStatistics(currentUser);
    }

    /**
     * Po jednom kole kvízu dá uživateli na výběr následující akci
     *
     * @return boolean True pokud má v kvízu pokračovat, False pokud ne (break aby opět vypsal tyto možnosti)
     */

    private static boolean handlePostQuizOptions(){
        while (true) {
            System.out.println(" \n Vyber z možností:");
            System.out.println("1. Pokračovat v kvízu (se stejným souborem)");
            System.out.println("2. Načíst nový soubor s otázkama");
            System.out.println("3. Změnit přezdívku");
            System.out.println("4. Podívat se na statistiky");
            System.out.println("5. Uložit protokol o testu");
            System.out.println("6. Ukončit program");

            int choice = uiHelper.getValidChoice(1,6);

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
                    saveQuizProtocolOption(results);
                    break;
                case 6:
                    System.out.println("Program ukončen");
                    System.exit(0);
                    return false;
                default:
                    break;
            }
        }
        //continue unless the user choosesto exit
    }
    /**
     * Vyžádá si název nového kvízového souboru od uživatele
     */

    private static void loadNewQuizFile(){
        System.out.println("Zadej jméno souboru s otázkama: ");
        filename= scanner.nextLine().trim();
    }

    /**
     * Změní přezdívku aktuálního uživatele a pokusí se načíst existující data pro danou přezdívku
     */
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

    /**
     * Pokusí se načíst data uživatele s danou přezdívkou ze souboru (jde od konce souboru)
     *
     * @param username přezdívka, pro kterou data načíst
     * @return User s načtenými data čí null pokud aková přezdívka nebyla nalezena
     */


    private static User loadUserDataIfExists(String username) {
        Path path = Paths.get(DataWriter.USER_STATS_FILE);
        try {
            List<String> lines = Files.readAllLines(path);
            User lastFoundUser = null;

            for (int i = lines.size() - 4; i >= 0; i--) {       //kazdy blok je dlouhy 4 radky
                if (lines.get(i).trim().equals(username)) {     // username matchuje
                    if (i + 3 < lines.size()) {                 //next 3 lines kde je hledane info
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
    /**
     * Zparsuje statistické informace uživatele z bloku textu souboru a vytvoří User objekt
     * Tato metoda předpokládá, že useBlock list má pravidelný formát:
     * - První řádek obsahuje přezdívku
     * - Druhý řádek obsauje celkový počet kvízů, které byly dokončeny pod danou přezdívkou
     * - Třetí řádek obsahuje nejvyšší dosažené skóre
     * - Čtvrtý řádek obsahuje průměrné skóre
     *
     * @param userBlock list stringů, kde každý reprezentuje řádek dat
     * @return User se zparsovanými daty z posledního kola
     * @throws NumberFormatException pokud parsování čísel neproběhne v pořádku
     * @throws IndexOutOfBoundsException pokud userBlock obsahuje méně než čtyři stringy
     */

    private static User parseUserStatistics(List<String> userBlock) {
        String username = userBlock.get(0).trim();
        User user = new User(username);

        user.setTotalQuizzesTaken(Integer.parseInt(userBlock.get(1).split(": ")[1].trim()));
        user.setHighestScore(Integer.parseInt(userBlock.get(2).split(": ")[1].trim()));
        user.setAverageScore(Double.parseDouble(userBlock.get(3).split(": ")[1].trim()));

        return user;
    }

    private static void saveQuizProtocolOption(List<QuestionResult> results){
        System.out.println("Zadej jméno souboru, do kterého protokol uložit: ");
        String protocolFilename = scanner.nextLine().trim();
        DataWriter.saveQuizProtocol(currentUser, results, protocolFilename, filename);
    }
}
