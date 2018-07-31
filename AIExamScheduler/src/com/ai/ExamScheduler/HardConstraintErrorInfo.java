/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

/**
 * HardConstraintErrorInfo class represents an wrong element position into the state.
 * The class contains all the needed info in order to be able to overcome this
 * wrong element placement. Its member variables are:
 * day, hour, grade --> each of them is a pointer to indicate the position of the error.
 * category --> type indicating the type of hard constraint violated in the position given of the above 3 group values.
 *
 * <u>NOTE</u>
 *      CATEGORY 1: TeacherHoursPerDay
 *      CATEGORY 2: TeacherHoursPerWeek
 *      CATEGORY 3: LessonTeachers
 *      CATEGORY 4: TeacherInSameHour
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class HardConstraintErrorInfo {
    private int day;
    private int hour;
    private int grade;
    private int category;

    /**
     * Constructor
     *
     * @param day index of day
     * @param hour index of hour
     * @param grade index of grade
     * @param category category type
     */
    public HardConstraintErrorInfo(int day, int hour, int grade, int category){
        this.day=day;
        this.hour=hour;
        this.grade=grade;
        this.category=category;
    }

    /**
     * Getters and setters for each member variables
     */
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() { return hour; }
    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
}
