package cz.cuni.mff.tranova;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Game {

    private static ArrayList<Question> questions = new ArrayList<Question>();

    private static void init(){
        try {
            List<String> lines = Files.readAllLines(Paths.get(Game.class.getClassLoader().getResource("questions.txt").toURI()));

            for (int i = 0; i < lines.size(); i += 5){
                Question q = new Question(lines.get(i), lines.get(i+1), lines.get(i+2), lines.get(i+3), lines.get(i+4));
                questions.add(q);
            }
        }
        catch (Exception e){
            System.out.println("couldnt read questions.txt");
            System.exit(-1);
        }
    }

    private static void loop() {
        Scanner scanner = new Scanner(System.in);

        while (questions.size() > 0){
            Question q = questions.remove(0);
            System.out.println(q.text);

            for (int i = 0; i < q.answers.length; i++){ //number of question + actual question
                System.out.println(i + " " + q.answers[i]);
            }

            int input = scanner.nextInt();

            if (input< 0 || input > q.answers.length - 1){ //check if input is in valid range
                System.out.println("invalid input");
                System.exit(-2); //either exit or continue and show next question
            }

            //input valid
            if (q.rightAnswer.equals(q.answers[input])){
                System.out.println("right");
            }
            else {
                System.out.println("wrong");
            }
        }
    }



    public static void main(String[] args){
        init();
        loop();
    }
}