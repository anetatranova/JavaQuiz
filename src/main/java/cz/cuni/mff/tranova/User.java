package cz.cuni.mff.tranova;

public class User {
    private String username;
    private int highestScore;
    private int totalQuizzesTaken;
    private double averageScore;
    //private String highestScoringCategory;
    private int totalScore;


    public User(String username) {
        this.username = username;
        this.highestScore = 0;
        this.totalQuizzesTaken = 0;
        this.averageScore = 0.0;
        //this.highestScoringCategory ="";
        this.totalScore = 0;
    }

    public String getUsername() {
        return username;
    }

    public void updateScores(int score) {
        if (score > highestScore) {
            highestScore = score;
            //highestScoringCategory = category;
        }
        totalQuizzesTaken++;
        averageScore = ((averageScore * (totalQuizzesTaken - 1)) + score) / totalQuizzesTaken;
        totalScore += score;
    }

    public void setTotalScore(int totalScore){
        this.totalScore = totalScore;
    }
    public void setTotalQuizzesTaken(int totalQuizzesTaken){
        this.totalQuizzesTaken = totalQuizzesTaken;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getTotalScore(){return totalScore;}
    public int getHighestScore() {
        return highestScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public int getQuizzesTaken() {
        return totalQuizzesTaken;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
