package cz.cuni.mff.tranova;

import java.util.*;
import java.util.stream.Collectors;

import static cz.cuni.mff.tranova.Game.*;

public class QuizManager {
    private QuestionManager questionManager;
    private CategoryManager categoryManager;
    private UIHelper uiHelper;

    public QuizManager(QuestionManager questionManager, CategoryManager categoryManager, UIHelper uiHelper) {
        this.questionManager = questionManager;
        this.categoryManager = categoryManager;
        this.uiHelper = uiHelper;
    }

    public void startQuiz() {
        List<Question> allQuestions = questionManager.loadQuestions("questions.txt");
        categoryManager.processCategories(allQuestions);
        Map<Integer,String> categories = categoryManager.getCategoryIdMap();

        uiHelper.showCategories(categories, categoryManager);

        List<String> selectedCategories = uiHelper.getUserCategorySelections(categories);
        List<Question> filteredQuestions = questionManager.filterQuestions(allQuestions, selectedCategories);

        int numberOfQuestions = uiHelper.getUserQuestionCount(filteredQuestions.size());
        List<Question> questionsToAnswer = pickQuestions(filteredQuestions,numberOfQuestions);

        runQuiz(questionsToAnswer);
    }

    private List<Question> pickQuestions(List<Question> questions, int count) {
        Collections.shuffle(questions);
        return questions.subList(0, count);
    }

    private void runQuiz(List<Question> questions) {
        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            uiHelper.displayQuestionAndOptions(question, i + 1, questions.size());
            String userAnswerLabel = uiHelper.getValidAnswerFromUser(question.getAnswers().size());

            // Obtain the correct index of the user's answer based on the label
            int userAnswerIndex = UIHelper.generateAnswerLabels(question.getAnswers().size()).indexOf(userAnswerLabel);

            // Retrieve the user's selected answer using the index
            String userSelectedAnswer = question.getAnswers().get(userAnswerIndex);

            if (userSelectedAnswer.equalsIgnoreCase(question.getRightAnswer())) {
                correctCount++;
                System.out.println("Správná odpověď!");
            } else {
                System.out.println("Špatná odpověď, správná odpověď je: " + question.getRightAnswer());
            }
        }
        uiHelper.displayFinalScore(correctCount, questions.size());
    }

}



