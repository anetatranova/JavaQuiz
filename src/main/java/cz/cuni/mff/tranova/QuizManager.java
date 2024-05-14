package cz.cuni.mff.tranova;

import java.util.*;

public class QuizManager {
    private QuestionManager questionManager;
    private CategoryManager categoryManager;
    private UIHelper uiHelper;
    private User currentUser;

    public QuizManager(QuestionManager questionManager, CategoryManager categoryManager, UIHelper uiHelper, User currentUser) {
        this.questionManager = questionManager;
        this.categoryManager = categoryManager;
        this.uiHelper = uiHelper;
        this.currentUser = currentUser;
    }

    public void startQuiz(List<Question> allQuestions, String filename) {
        //List<Question> allQuestions = questionManager.loadQuestions("questions.txt");
        categoryManager.processCategories(allQuestions);
        Map<Integer,String> categories = categoryManager.getCategoryIdMap();

        uiHelper.showCategories(categories, categoryManager);

        List<String> selectedCategories = uiHelper.getUserCategorySelections(categories);
        List<Question> filteredQuestions = questionManager.filterQuestions(allQuestions, selectedCategories);

        int numberOfQuestions = uiHelper.getUserQuestionCount(filteredQuestions.size());
        List<Question> questionsToAnswer = pickQuestions(filteredQuestions,numberOfQuestions);

        int score = runQuiz(questionsToAnswer);

        currentUser.updateScores(score);

        Map<String,Integer[]> categoryResults = getCategoryResults(questionsToAnswer);
        DataWriter.writeQuizResults(currentUser, score, categoryResults, filename);
    }


    private List<Question> pickQuestions(List<Question> questions, int count) {
        Collections.shuffle(questions);
        return questions.subList(0, count);
    }

    private Map<String, Integer[]> getCategoryResults(List<Question> questions) {
        Map<String, Integer[]> results = new HashMap<>();
        for (Question question : questions) {
            String category = question.getCategory();
            Integer[] counts = results.getOrDefault(category, new Integer[]{0, 0});
            if (question.isCorrect()) {
                counts[0]++;
            } else {
                counts[1]++;
            }
            results.put(category, counts);
        }
        return results;
    }

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
                System.out.println("Špatná odpověď, správná odpověď je: " + question.getRightAnswer());
            }
        }
        uiHelper.displayFinalScore(correctCount, questions.size());

        //currentUser.updateScores(correctCount);
        return correctCount;
    }
}



