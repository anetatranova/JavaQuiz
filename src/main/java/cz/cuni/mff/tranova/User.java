package cz.cuni.mff.tranova;

/**
 * Tato třída reprezentuje uživatele, ukládá a spravuje uživatelská data.
 */
public class User {
    private final String username;
    private int highestScore;
    private int totalQuizzesTaken;
    private double averageScore;

    /**
     * Konstruktory pro nového uživatele s danou přezdívkou a inicializuje hodnoty související se skórem na 0
     *
     * @param username přezdívka uživatele
     */

    public User(String username) {
        this.username = username;
        this.highestScore = 0;
        this.totalQuizzesTaken = 0;
        this.averageScore = 0.0;
    }

    /**
     * Získá přezdívku uživatele
     *
     * @return přezdívka uživatele
     */
    public String getUsername() {
        return username;
    }
    /**
     * Aktualizuje skóre uživatele
     * Počítá nejvyšší skóre, aktualizuje počet dokončených kvízů a vypočítá průměrné skóre
     *
     * @param score skóre získané v posledním kvízu
     */
    public void updateScores(int score) {
        if (score > highestScore) {
            highestScore = score;
        }
        totalQuizzesTaken++;
        averageScore = ((averageScore * (totalQuizzesTaken - 1)) + score) / totalQuizzesTaken;
        //totalScore += score;
    }
    /**
     * Nastaví počet dokončených kvízů
     *
     * @param totalQuizzesTaken počet dokončených kvízů
     */
    public void setTotalQuizzesTaken(int totalQuizzesTaken){
        this.totalQuizzesTaken = totalQuizzesTaken;
    }
    /**
     * Nastaví nejvyšší dosaženeé skóre
     *
     * @param highestScore nejvyšší dosažené skóre
     */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
    /**
     * Nastaví průměrné skóre napříč všemi kvízy
     *
     * @param averageScore průměrné skóre
     */
    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
    /**
     * Získá nejvyšší dosažené skóre
     *
     * @return nejvyšší dosažené skóre
     */
    public int getHighestScore() {
        return highestScore;
    }
    /**
     * Získá průměrné skóre napříč všemi kvízy
     *
     * @return průměrné skóre
     */

    public double getAverageScore() {
        return averageScore;
    }
    /**
     * Získá počet dokončených kvízů
     *
     * @return počet dokončených kvízů
     */

    public int getQuizzesTaken() {
        return totalQuizzesTaken;
    }
}
