/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

/**
 * Class Teacher, represents the teachers that we have as input from the teachers.txt.
 * This class consists of its member variables and their getters and setters methods.
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class Teacher {
    private int id;
    private String name;
    private int maxHoursPerDay;
    private int maxHoursPerWeek;
    private int hoursTaken;

    /**
     * Constructor
     *
     * @param id teacher's id, as mentioned on teachers.txt file.
     * @param name teacher's name, as mentioned on teachers.txt file.
     * @param maxHoursPerDay max hours that the teacher can teach in total per day, as mentioned on teachers.txt file.
     * @param maxHoursPerWeek max hours that the teacher can teach in total per week, as mentioned on teachers.txt file
     * There is also hoursTaken member variable. This variable holds a value that indicates how many hours has been assigned
     * to the teacher till know, in total, per week. It is initialized to zero, when we create the teacher object.
     **/
    public Teacher(int id, String name, int maxHoursPerDay, int maxHoursPerWeek) {
        this.id = id;
        this.name = name;
        this.maxHoursPerDay = maxHoursPerDay;
        this.maxHoursPerWeek = maxHoursPerWeek;
        this.hoursTaken = 0;
    }

    /**
     * Getters and setters for each member variable.
     */
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHoursPerDay() {
        return maxHoursPerDay;
    }
    public void setMaxHoursPerDay(int maxHoursPerDay) {
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public int getMaxHoursPerWeek() {
        return maxHoursPerWeek;
    }
    public int getHoursTaken() {
        return hoursTaken;
    }

    public void setHoursTaken(int hoursTaken) {
        this.hoursTaken = hoursTaken;
    }
    public void setMaxHoursPerWeek(int maxHoursPerWeek) {
        this.maxHoursPerWeek = maxHoursPerWeek;
    }
}
