/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */

package com.ai.ExamScheduler;
import java.util.ArrayList;

/**
 * Class Main
 * This is the main executable of A* Algorithm
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class main {
    public static void main(String[] args){
        ArrayList<Teacher> teachers = Utilities.readTeachers();
        ArrayList<Lesson> lessons = Utilities.readLessons(teachers);

        Constraints.teachers = teachers;
        Constraints.lessons = lessons;
        State.teachers = new ArrayList<>(teachers);

        AStar alg = new AStar(lessons, 5, 7, 3);
        alg.Start();
    }
}
