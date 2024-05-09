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

    private List<Question> selectQuestions(List<Question> questions) {
        // Logic to select questions based on some criteria (e.g., categories)
        return questions; // Placeholder
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
            char userAnswer = uiHelper.getValidAnswerFromUser(question.getAnswers().size());

            String correctAnswer = question.getAnswers().get(userAnswer - 'A');
            if (correctAnswer.equalsIgnoreCase(question.getRightAnswer())) {
                correctCount++;
                System.out.println("Correct!");
            } else {
                System.out.println("Incorrect. The correct answer was: " + question.getRightAnswer());
            }
        }
        uiHelper.displayFinalScore(correctCount, questions.size());
    }
}

