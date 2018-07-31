/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Class AStar, this is the main class of the A* algorithm implementation
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class AStar {

    /* PriorityQueue with the set of States */
    private PriorityQueue<State> sortedOpenSet;

    /* ArrayList of all lessons in the timetable */
    private ArrayList<Lesson> lessons;

    /* The days the schedule will have */
    public static int scheduleDays;

    /* The hours per day the schedule will have */
    public static int scheduleHours;

    /* The number of grades a class has */
    public static int gradesPerClass;

    /* The number of total grades to create the array of grades */
    public static int totalGrades;

    /**
     * Constructor
     *
     * @param lessons the array of the schedule's lessons
     * @param scheduleDays the number of days the schedule has
     * @param scheduleHours the number of hours per day the schedule has
     * @param gradesPerClass the number of grades the classes have
     */
    public AStar(ArrayList<Lesson> lessons, int scheduleDays, int scheduleHours, int gradesPerClass){
        this.lessons = lessons;
        this.sortedOpenSet = new PriorityQueue<>();
        AStar.scheduleDays = scheduleDays;
        AStar.scheduleHours = scheduleHours;
        AStar.gradesPerClass = gradesPerClass;
        AStar.totalGrades = gradesPerClass * 3;
    }

    /**
     * This method starts the algorithm and returns the success
     *
     * @return the success of the algorithm
     */
    public boolean Start(){

        /* Add a full random schedule to the set */
        sortedOpenSet.add(new State(lessons));

        /* Count the steps to the success */
        int steps = 0;

        /* Count the total time to the success */
        long totalTime = 0;

        /* While the algorithm does not surpass 5 minutes continue*/
        while(totalTime < 300000){
            /* Get the start time of the loop */
            long startTime = System.nanoTime();

            steps++;

            /* Gets and remove the state with the lowest score */
            State currentState = sortedOpenSet.remove();

            /* If the State is accepted then the algorithm is done
            * Is Accepted means that the state satisfies all the hard constraints*/
            if(currentState.isAccepted()){
                //printSchedule(currentState);
                System.out.println("SCORESOFT: " + currentState.getSoftScore() + " HARDSOFT: " + currentState.getHardScore() + " Steps: "+ steps);
                System.out.println("TotalTime: " + totalTime + "ms");
                try{
                    Utilities.WriteScheduleToFile(currentState);
                }catch(IOException ex){
                    System.err.print(ex);
                }
                return true;
            }

            /* Swap Lesson-Teacher Pairs of the elements that violate the hard constraints */
            swapHardConstraints(currentState);

            /* Get the end time of the loop */
            long endTime = System.nanoTime();

            /* Add the loopTime to the totalTime */
            totalTime += (endTime - startTime)/(float)1000000;
        }


        System.out.println("TotalTime: " + totalTime);
        return false;
    }

    /**
     * This method is responsible for creating children.
     * Each child is identical copy to its parent,
     * with only 2 elements swapped.
     * @param parentState a state as the parent state
     */
    public void swapHardConstraints(State parentState) {
        //Arraylist which stores all places where a hard constrain is being violated
        ArrayList<HardConstraintErrorInfo> placesToSwap = parentState.getPlacesToSwap();
        if (placesToSwap.size() == 1) {
            State newState = new State(parentState);
            HardConstraintErrorInfo errorPlace = placesToSwap.get(0);

            /*
                For each cell of the schedule, we create a new child,
                with swapped elements, the current cell and the
                unique element (place) where a hard constrain
                has been violated. After inserting this new child
                to the open set of the A star, we also evaluate its
                score.
             */
            for (int d = 0; d < scheduleDays; d++) {
                for (int j = 0; j < scheduleHours; j++) {
                    newState.getGrades()[errorPlace.getGrade()]
                            .swapPair(errorPlace.getDay(), errorPlace.getHour(), d, j);
                    sortedOpenSet.add(newState);
                    newState.evaluateScore();
                }
            }
        }
        else{
            /*
                Select a random element within those who violate hard constrain, meaning
                we select a random element of the placesToSwap arraylist. Lets call this
                element elem1.
                For each cell of the schedule, we create a new child,
                with swapped elements, the current cell and elem1.
                After inserting this new child to the open set of the A star,
                we also evaluate its score.
             */
            Random r = new Random(System.currentTimeMillis());

            int errorPlaceIndex = r.nextInt(placesToSwap.size());
            HardConstraintErrorInfo errorPlace = placesToSwap.get(errorPlaceIndex);

            int grade = errorPlace.getGrade();
            Pair pError = parentState.getGrades()[grade].getSchedule()[errorPlace.getDay()][errorPlace.getHour()];

            while(pError == null){
                errorPlaceIndex = r.nextInt(placesToSwap.size());
                errorPlace = placesToSwap.get(errorPlaceIndex);
                grade = errorPlace.getGrade();
                pError = parentState.getGrades()[grade].getSchedule()[errorPlace.getDay()][errorPlace.getHour()];
            }
            for(int day = 0; day < scheduleDays; day++){
                for(int hour = 0; hour < scheduleHours; hour++){
                    if(parentState.getGrades()[grade].getSchedule()[day][hour]!=null
                            &&(errorPlace.getCategory()==2 || errorPlace.getCategory()==3)
                            && parentState.getGrades()[grade].getSchedule()[day][hour].getTeacher().getId()
                            == pError.getTeacher().getId() ) continue;
                    State newState = new State(parentState);
                    newState.getGrades()[grade]
                            .swapPair(errorPlace.getDay(), errorPlace.getHour(), day, hour);
                    sortedOpenSet.add(newState);

                    newState.evaluateScore();
                }

            }
        }
    }

    /**
     * Getters and setters for each member variable.
     */
    public PriorityQueue<State> getSortedOpenSet() {
        return sortedOpenSet;
    }
    public void setSortedOpenSet(PriorityQueue<State> sortedOpenSet) {
        this.sortedOpenSet = sortedOpenSet;
    }
}