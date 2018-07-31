/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

import java.util.ArrayList;
import java.util.Random;

/**
 * State class represents a state of the problem.
 * Our problem's state appears as an array of size equal to the number of different grades.
 * This are elements are 2d arrays representing the schedule of the specific grade.
 *
 * Also the state holds some info required for the algorithm, such as:
 * hard score --> score calculated based on the hard constraints
 * soft score --> score calculated based on the soft constraints
 * arraylist of HardConstraintErrorInfo --> objects that help us indicate the elements that
 * isAccepted --> true if the state is an accpeted one, otherwise false
 * violate hard conditions in order to move them to different cell.
 *
 * <u>NOTE:</u> both hard and soft scores are calculated with the same idea.
 *              For each element that violates the constraint being checked,
 *              the score value is being increased by one.
 *
 * An accepted state is one that does not violate any hard constraint. Thus, hard score will be zero.
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class State implements Comparable<State>{
    private int hardScore;
    private int softScore;
    private boolean isAccepted;

    private Grade[] grades;

    private ArrayList<HardConstraintErrorInfo> placesToSwap;

    public static ArrayList<Teacher> teachers;

    /**
     * Constructor
     *
     * Creates a new random state.
     * @param lessons arraylist of all lessons
     */
    public State(ArrayList<Lesson> lessons){
        this.grades = new Grade[AStar.totalGrades];

        for(int i = 0; i < grades.length; i++){
            //classId is our index, that help us get the corresponding hours per class for a specific lesson
            int classId = i/3;

            /*
               Into the arraylist lessonsToBeTeachToClass we add each lesson that is going to be teached to this class
               times the hours that it has to be teached to it. That means we have duplicates.
             */
            ArrayList<Lesson> lessonsToBeTeachToClass = new ArrayList<>();
            for(Lesson lesson : lessons){
                for(int k = 0; k < lesson.getHoursPerClass().get(classId); k++){
                        lessonsToBeTeachToClass.add(lesson);
                }
            }

            Pair[][] schedule = new Pair[AStar.scheduleDays][AStar.scheduleHours];

            /*
               We fill the schedule array of the current grade, timeslot by timeslot,
               choosing each time a random lesson to insert, from the lessonsToBeTeachToClass arraylist.
               After that, we remove this lesson from the arraylist.

               Also we choose, randomly a teacher within those who can teach this lesson, in order to insert him
               as well. NOTE that, we double check the teacher selected before inserting him to the schedule.
               This check has to do with making sure that no teacher is going to be inserted into the schedule
               if he has already reached the max hours per week that he can teach. Thats why each time a teacher
               is being inserted to the schedule we increase his hoursTaken by one. When hourseTaken reach maxHoursPerWeek
               the teacher can't be inserted to another cell.

               When the lessonsToBeTeachToClass arraylist is finally empty, we finished with this grade's schedule
               and we go one to the next grade. (Outer outer loop, grades)
             */
            Outer_loop:
            for(int hour = 0; hour < AStar.scheduleHours; hour++){
                for(int day = 0; day < AStar.scheduleDays; day++){
                    if(lessonsToBeTeachToClass.isEmpty()){
                        break Outer_loop;
                    }
                    Random r = new Random(System.currentTimeMillis());

                    int lessonIndex = r.nextInt(lessonsToBeTeachToClass.size());
                    Lesson lessonToBeAdded = lessonsToBeTeachToClass.get(lessonIndex);

                    int teacherIndex = r.nextInt(lessonToBeAdded.getTeachers().size());
                    Teacher teacherToBeAdded = lessonToBeAdded.getTeachers().get(teacherIndex);

                    Teacher localTeacher = null;
                    for(int t = 0; t < teachers.size(); t++){
                        if(teachers.get(t).getId() == teacherToBeAdded.getId()){
                            localTeacher = teachers.get(t);
                            break;
                        }
                    }

                    boolean allTeachersAreFull = true;
                    for(Teacher teacher : lessonToBeAdded.getTeachers()){
                        if(teacher.getHoursTaken() < teacher.getMaxHoursPerWeek()){
                            allTeachersAreFull = false;
                        }
                    }

                    while(localTeacher.getHoursTaken() >= localTeacher.getMaxHoursPerWeek() && !allTeachersAreFull){
                        int before = teacherIndex;
                        teacherIndex++;

                        if(teacherIndex >= lessonToBeAdded.getTeachers().size()){
                            teacherIndex = 0;
                        }
                        teacherToBeAdded = lessonToBeAdded.getTeachers().get(teacherIndex);

                        for(int t = 0; t < teachers.size(); t++){
                            if(teachers.get(t).getId() == teacherToBeAdded.getId()){
                                localTeacher = teachers.get(t);
                                break;
                            }
                        }
                        if(before == teacherIndex) break;
                     }

                    schedule[day][hour] = new Pair(teacherToBeAdded, lessonToBeAdded);
                    localTeacher.setHoursTaken(localTeacher.getHoursTaken()+1);

                    lessonsToBeTeachToClass.remove(lessonToBeAdded);
                }
            }

            grades[i] = new Grade(i, schedule);
        }
        // evaluateScore for the just created new random state
        evaluateScore();
    }

    /**
     * Copy constructor
     *
     * This constructors actually takes as input a parent state
     * and copy it in order to create a child.
     * After this the method called the copy constructor is going to
     * make swaps to the new child state.
     * @param newState state going to be copy
     */
    public State(State newState){
        this.grades = new Grade[AStar.totalGrades];
        for(int grade = 0; grade < newState.getGrades().length; grade++){
            Pair[][] schedule = new Pair[AStar.scheduleDays][AStar.scheduleHours];
            for(int day = 0; day < AStar.scheduleDays; day++){
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    schedule[day][hour]=newState.getGrades()[grade].getSchedule()[day][hour];
                }
            }
            this.grades[grade] = new Grade(grade, schedule);
        }

        evaluateScore();
    }

    /**
     * This method, calculates both hard score and soft score.
     * Also, calls the checkHardConstraints, and in that way,
     * the ArrayList<HardConstraintErrorInfo> placesToSwap,
     * and the isAccepted variable are being updated.
     */
    public void evaluateScore(){
        Constraints cons = new Constraints();

        this.hardScore = 0;
        this.softScore = 0;
        softScore += cons.checkIfNotEmptySlotBetweenTeachingHours(grades);
        softScore += cons.checkIfTwoHoursInSequencePerTeacher(grades);
        softScore += cons.checkNormalizationGradePerDay(grades);
        softScore += cons.checkNormalizationLessonPerGrade(grades);
        softScore += cons.checkNormalizationTeachingHoursOfTeacher(grades);

        this.isAccepted = checkHardConstraints();
    }

    /**
     * This method calculates the hard score,
     * and fills the ArrayList<HardConstraintErrorInfo> placesToSwap.
     * @return true if there is no hard constraint violation,
     *          otherwise returns false.
     */
    public boolean checkHardConstraints(){
        Constraints constraint= new Constraints();
        ArrayList<HardConstraintErrorInfo> lessonError = constraint.checkLessonTeachers(grades);
        ArrayList<HardConstraintErrorInfo> teacherError = constraint.checkTeacherInSameHour(grades);
        ArrayList<HardConstraintErrorInfo> maxPerDayError = constraint.checkMaxTeacherHoursPerDay(grades);
        ArrayList<HardConstraintErrorInfo> maxPerWeekError = constraint.checkMaxTeacherHoursPerWeek(grades);
        if(lessonError.isEmpty()
                && teacherError.isEmpty()
                && maxPerDayError.isEmpty()
                && maxPerWeekError.isEmpty()){
            return true;
        }

        placesToSwap = new ArrayList<>();
        placesToSwap.addAll(lessonError);
        placesToSwap.addAll(teacherError);
        placesToSwap.addAll(maxPerDayError);
        placesToSwap.addAll(maxPerWeekError);

        this.hardScore += lessonError.size();
        this.hardScore += teacherError.size();
        this.hardScore += maxPerDayError.size();
        this.hardScore += maxPerWeekError.size();

        return false;
    }

    /**
     * Prints the state to the console
     */
    public void print(){
        for(int grade = 0; grade < AStar.totalGrades; grade++){
            System.out.println("Grade: "+grade);
            for(int day = 0; day < AStar.scheduleDays; day++){
                System.out.print("  Day: |" + day);
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    Pair pair = this.grades[grade].getSchedule()[day][hour];
                    if(pair == null){
                        System.out.print(" { No lesson no teacher }");
                        continue;
                    }
                    System.out.print(" ( " + pair.getLesson().getName() + " , " +pair.getTeacher().getName() + " )");
                }
                System.out.println();
            }
        }
    }

    /**
     * Getters and setters for the member variables
     */
    public int getHardScore() {
        return hardScore;
    }
    public void setHardScore(int hardScore) {
        this.hardScore = hardScore;
    }

    public int getSoftScore() {
        return softScore;
    }
    public void setSoftScore(int softScore) {
        this.softScore = softScore;
    }

    public boolean isAccepted() {
        return isAccepted;
    }
    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public Grade[] getGrades() {
        return grades;
    }
    public void setGrades(Grade[] grades) {
        this.grades = grades;
    }

    public ArrayList<HardConstraintErrorInfo> getPlacesToSwap() {
        return placesToSwap;
    }
    public void setPlacesToSwap(ArrayList<HardConstraintErrorInfo> placesToSwap) {
        this.placesToSwap = placesToSwap;
    }

    /**
     * Schedule compareTo Override
     * We consider < each state that has less hard score.
     * If the states that we want to compare have the same hard score,
     * then we consider < the one with the less soft score.
     * @param x ScheduleState to compare with
     */
    @Override
    public int compareTo(State x) {
        if(this.hardScore < x.hardScore) return -1;
        else if(this.hardScore > x.hardScore) return 1;
        else{
            if(this.softScore < x.softScore) return -1;
            else return 1;
        }
    }
}
