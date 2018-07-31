/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

/**
 * Pair class represents pairings that have been created in order to be inserted into the schedule.
 * Due to the way we create the pairs each pair is valid. Meaning that the teacher can teach the lesson.
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class Pair {
    private Teacher teacher;
    private Lesson lesson;

    /**
     * Constructor
     *
     * @param teacher teacher object
     * @param lesson lesson object
     */
    public Pair(Teacher teacher, Lesson lesson) {
        this.teacher = teacher;
        this.lesson = lesson;
    }

    /**
     * Copy constructor
     * @param newPair pair to be copy
     */
    public Pair(Pair newPair){
        this(newPair.getTeacher(), newPair.getLesson());
    }

    /**
     * Getters and setters of member variables
     */
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Lesson getLesson() {
        return lesson;
    }
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
