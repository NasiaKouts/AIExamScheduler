/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler.tests;

import com.ai.ExamScheduler.*;
import java.util.ArrayList;

import org.junit.*;

/**
 * Class AStarTest, this test class, tests the implementation of the
 * A* algorithm 10 times and returns the total times and scores
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class AStarTest {
    @Test
    public void RunAStarAlgorithm(){
        boolean alwaysTrue = true;
        for(int i = 0; i < 1; i++){
            ArrayList<Teacher> teachers = Utilities.readTeachers();
            ArrayList<Lesson> lessons = Utilities.readLessons(teachers);

            Constraints.teachers = teachers;
            Constraints.lessons = lessons;
            State.teachers = new ArrayList<>(teachers);

            AStar algorithm = new AStar(lessons, 5, 7, 3);
            alwaysTrue = algorithm.Start();

            System.out.println("Time "+ (i+1) + " Completed!");

            if(!alwaysTrue) break;
        }

        Assert.assertTrue(alwaysTrue);
    }
}
