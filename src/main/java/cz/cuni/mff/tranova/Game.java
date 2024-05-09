package cz.cuni.mff.tranova;

import java.util.*;
import java.util.stream.Collectors;


public class Game {

    private static QuestionManager questionManager;
    private static CategoryManager categoryManager;
    private static Scanner scanner;
1
    public Game(){
        questionManager = new QuestionManager();
        this.categoryManager = new CategoryManager();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args){
        QuestionManager questionManager = new QuestionManager();
        CategoryManager categoryManager = new CategoryManager();
        UIHelper uiHelper = new UIHelper();
        QuizManager quizManager = new QuizManager(questionManager, categoryManager, uiHelper);
        quizManager.startQuiz();
    }
}
