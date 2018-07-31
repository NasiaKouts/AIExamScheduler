/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

import java.util.ArrayList;

/**
 * Class Teacher, represents the teachers that we have as input from the teachers.txt.
 * This class consists of its member variables and their getters and setters methods.
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class Lesson {

    /* Class Variables Declaration */
    private int id;
    private String name;
    private ArrayList<Integer> hoursPerClass;
    private ArrayList<Teacher> teachers;

    /**
     * Constructor
     *
     * @param id teacher's id, as mentioned on lessons.txt file.
     * @param name teacher's name, as mentioned on lessons.txt file.
     * @param hoursPerClass Arraylist filled with hours that the lesson has to be teached,
     *                             in each grade of each class as mentioned on lessons.txt file.
     * @param teachers Arraylist filled with the teachers that are capable of teaching the lesson,
     *                        as mentioned on lessons.txt file.
     **/
    public Lesson(int id, String name, ArrayList<Integer> hoursPerClass, ArrayList<Teacher> teachers) {
        this.id = id;
        this.name = name;
        this.hoursPerClass = hoursPerClass;
        this.teachers = teachers;
    }

    /**
     * This method checks if the input teacher's id is one of the teachers related to the lesson,
     * one of the teachers who can teach the lesson.
     * @param id the teacher's id.
     * @return true if the teacher can teach the lesson,
     *          otherwise false.
     */
    public boolean findTeacher(int id){
        for(Teacher t: teachers){
            if(t.getId()==id){
                return true;
            }
        }
        return false;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getHoursPerClass() {
        return hoursPerClass;
    }
    public void setHoursPerClass(ArrayList<Integer> hoursPerClass) {
        this.hoursPerClass = hoursPerClass;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }
    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }
}
