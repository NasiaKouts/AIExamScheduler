/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

/**
 * Class Grade represents the schedule of a specific Grade.
 * It contains a 2D Array of Pairs where the lessons and teachers are.
 * The rows represents the days and the columns the hours.
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class Grade {
    private int id;
    private Pair[][] schedule;

    /**
     * Constructor
     *
     * @param id the id of the grade
     * @param schedule the 2d array of the grade's schedule
     */
    public Grade(int id, Pair[][] schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    /**
     * Getters and setters for each member variable.
     */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Pair[][] getSchedule() {
        return schedule;
    }
    public void setSchedule(Pair[][] schedule) {
        this.schedule = schedule;
    }

    /**
     * Returns the pair in a specific day-hour
     * @param day the day
     * @param hour the hour
     * @return the pair in the schedule
     */
    public Pair getPair(int day, int hour){
        return schedule[day][hour];
    }

    /**
     * Inserts a pair to a specific location in the schedule
     * @param day the day where the pair will be inserted
     * @param hour the hour where the pair will be inserted
     * @param pair the pair itself
     */
    public void insertPair(int day, int hour, Pair pair){
        this.schedule[day][hour] = pair;
    }

    /**
     * Deletes the pair from a specific location
     * @param day the day where the pair will be deleted
     * @param hour the hour where the pair will be deleted
     */
    public void removePair(int day, int hour){
        this.schedule[day][hour] = null;
    }

    /**
     * Swaps two pairs
     * @param day1 the day1 of the schedule
     * @param hour1 the hour1 of the schedule
     * @param day2 the day2 of the schedule
     * @param hour2 the hour2 of the schedule
     */
    public void swapPair(int day1, int hour1, int day2, int hour2){
        Pair temp = this.schedule[day1][hour1];
        this.schedule[day1][hour1] = this.schedule[day2][hour2];
        this.schedule[day2][hour2] = temp;
    }
}
