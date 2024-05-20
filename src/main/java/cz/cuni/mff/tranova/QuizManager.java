package cz.cuni.mff.tranova;

import java.io.IOException;
import java.util.*;

/**
 * Tato třída organizuje flow kvízu - od výběru kategorií a otázek až k výpočtu skóre a zápisu výsledků
 */

public class QuizManager {
    private QuestionManager questionManager;
    private CategoryManager categoryManager;
    private UIHelper uiHelper;
    private User currentUser;

    /**
     * Konstruktory pro QuizManager instanci
     *
     * @param questionManager pomocná třída pro získání a zpracovávání otázek
     * @param categoryManager pomocná trída pro práci s kategoriemi
     * @param uiHelper pomocná třída pro správu uživatelksého rozhraní
     * @param currentUser aktuální uživatel (reprezentován přezdívkou)
     */

    public QuizManager(QuestionManager questionManager, CategoryManager categoryManager, UIHelper uiHelper, User currentUser) {
        this.questionManager = questionManager;
        this.categoryManager = categoryManager;
        this.uiHelper = uiHelper;
        this.currentUser = currentUser;
    }

    /**
     * Začíná kvíz načtením otázek, výběrem kategorií, filtrováním otázek a spuštěním kvízu
     * Aktualizuje skŕe a zapíše jej do souboru
     *
     * @param allQuestions seznam všech otázek z načteného souboru
     * @param filename název souboru, ze kterého jsou otázky načteny
     *
     * @throws IOException pokud nastne chyba při zápisu výsledků do souboru
     */
    public void startQuiz(List<Question> allQuestions, String filename) throws IOException {
        //List<Question> allQuestions = questionManager.loadQuestions("questions.txt");
        categoryManager.processCategories(allQuestions);
        Map<Integer,String> categories = categoryManager.getCategoryIdMap();

        uiHelper.showCategories(categories, categoryManager);

        List<String> selectedCategories = uiHelper.getUserCategorySelections(categories);
        List<Question> filteredQuestions = questionManager.filterQuestions(allQuestions, selectedCategories);

        int numberOfQuestions= uiHelper.getUserQuestionCount(filteredQuestions.size());
        List<Question> questionsToAnswer = pickQuestions(filteredQuestions,numberOfQuestions);

        int score = runQuiz(questionsToAnswer);

        currentUser.updateScores(score);

        Map<String,Integer[]> categoryResults = getCategoryResults(questionsToAnswer);
        DataWriter.writeQuizResults(currentUser, score, categoryResults, filename);
    }

    /**
     * Otázky zamíchá a pak vybere podmnožinu z nich
     *
     * @param questions seznam otázek
     * @param count velikost podmonožinu (počet otázek)
     * @return seznam náhodně vybraných otázek se zadaným počtem
     */

    private List<Question> pickQuestions(List<Question> questions, int count) {
        Collections.shuffle(questions);

        return questions.subList(0, count);
    }

    /**
     * Vypočítá výsledky kvízu podle kategorie
     *
     * @param questions seznam otázek, ktereé byly v kvízu zodpovězeny
     * @return mapu kategorií a počet správně a špatně zodpovězených otázek v nich
     */

    private Map<String, Integer[]> getCategoryResults(List<Question> questions) {
        Map<String, Integer[]> results = new HashMap<>();
        for (Question question : questions) {
            String category = question.getCategory();
            Integer[] counts = results.getOrDefault(category,new Integer[]{0, 0});
            if (question.isCorrect()) {
                counts[0]++;
            } else {
                counts[1]++;
            }
            results.put(category, counts);
        }
        return results;
    }

    /**
     * Rozběhne kvíz, zobrazí otázky a zachycuje odpovědi uživatele
     * Spočítá sprácně zodpovězené otázky
     *
     * @param questions seznam otázek v kvízu
     * @return počet správně zodpovězených otázek
     */

    private int runQuiz(List<Question> questions) {
        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            uiHelper.displayQuestionAndOptions(question, i + 1, questions.size());
            String userAnswerLabel = uiHelper.getValidAnswerFromUser(question.getAnswers().size());

            int userAnswerIndex = UIHelper.generateAnswerLabels(question.getAnswers().size()).indexOf(userAnswerLabel);


            String userSelectedAnswer = question.getAnswers().get(userAnswerIndex);
            question.setUserAnswer(userSelectedAnswer);

            if (userSelectedAnswer.equalsIgnoreCase(question.getRightAnswer())) {
                correctCount++;
                System.out.println("Správná odpověď!");
            } else {
                System.out.println("Špatná odpověď, správná odpověď je: " +question.getRightAnswer());
            }
        }
        uiHelper.displayFinalScore(correctCount,questions.size());

        //currentUser.updateScores(correctCount);
        return correctCount;
    }
}



