/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

import java.util.ArrayList;

/**
 * Class Constraints, this class checks and returns the violations of the schedule
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class Constraints{
    public static ArrayList<Lesson> lessons;
    public static ArrayList<Teacher> teachers;

    /**
     * Hard Constraint
     * This method checks if a teacher teach above the max hours per day
     * @param grades the schedules of the grades
     * @return Array of HardConstraintErrorInfo where there is a violation
     */
    public ArrayList<HardConstraintErrorInfo> checkMaxTeacherHoursPerDay(Grade[] grades) {
        ArrayList<HardConstraintErrorInfo> errors = new ArrayList<>();
        for(Teacher teacher : teachers){
            for(int day = 0; day < AStar.scheduleDays; day++){
                int total = 0;
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    for(Grade grade : grades){
                        Pair pair = grade.getPair(day,hour);
                        if(pair != null && pair.getTeacher().getId() == teacher.getId()){
                            total++;
                            if(total > teacher.getMaxHoursPerDay()){
                                errors.add(new HardConstraintErrorInfo(day, hour, grade.getId(), 1));
                            }
                        }
                    }
                }
            }
        }
        return errors;
    }

    /**
     * Hard Constraint
     * This method checks if a teacher teach above the max hours per week
     * @param grades the schedules of the grades
     * @return Array of HardConstraintErrorInfo where there is a violation
     */
    public ArrayList<HardConstraintErrorInfo> checkMaxTeacherHoursPerWeek(Grade[] grades) {
        ArrayList<HardConstraintErrorInfo> errors = new ArrayList<>();
        for(Teacher teacher : teachers){
            int total = 0;
            for(int day = 0; day < AStar.scheduleDays; day++){
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    for(Grade grade : grades){
                        Pair pair = grade.getPair(day,hour);
                        if(pair != null && pair.getTeacher().getId() == teacher.getId()){
                            total++;
                            if(total > teacher.getMaxHoursPerWeek()){
                                errors.add(new HardConstraintErrorInfo(day, hour, grade.getId(), 2));
                            }
                        }
                    }
                }
            }
        }
        return errors;
    }

    /**
     * Hard Constraint
     * This method checks if a lessons is teached by a professor who is not teaching this lesson
     * @param grades the schedules of the grades
     * @return Array of HardConstraintErrorInfo where there is a violation
     */
    public ArrayList<HardConstraintErrorInfo> checkLessonTeachers(Grade[] grades){
        ArrayList<HardConstraintErrorInfo> errors = new ArrayList<>();
        for(Grade grade : grades){
            for (int day = 0; day < AStar.scheduleDays ; day++){
                for (int hour = 0; hour < AStar.scheduleHours; hour++){
                    Pair pair = grade.getPair(day,hour);
                    if(pair!=null){
                        if(!pair.getLesson().findTeacher(pair.getTeacher().getId())){
                            errors.add(new HardConstraintErrorInfo(day, hour, grade.getId(), 3));
                        }
                    }
                }
            }
        }
        return errors;
    }

    /**
     * Hard Constraint
     * This method checks is the teacher is found in above 1 grade at the same day-hour time.
     * @param grades the schedules of the grades
     * @return Array of HardConstraintErrorInfo where there is a violation
     */
    public ArrayList<HardConstraintErrorInfo> checkTeacherInSameHour(Grade[] grades){
        ArrayList<HardConstraintErrorInfo> errors= new ArrayList<>();
        for (int hour = 0; hour < AStar.scheduleHours; hour++){
            for(int day = 0; day < AStar.scheduleDays; day++){
                ArrayList<Integer> teachersInThisTimeslot = new ArrayList<>();
                for(Grade grade : grades){
                    Pair pair= grade.getPair(day,hour);
                    if(pair != null){
                        if(!teachersInThisTimeslot.isEmpty() && teachersInThisTimeslot.contains(pair.getTeacher().getId())){
                            errors.add(new HardConstraintErrorInfo(day, hour, grade.getId(),4));
                        }
                        teachersInThisTimeslot.add(pair.getTeacher().getId());
                    }
                }
            }
        }
        return errors;
    }

    /**
     * Soft Constraint
     * This method checks if there is an empty slot between the grade's teaching hours
     * Referenced as Constraint 1 in the project description
     * @param grades the schedules of the grades
     * @return the score increase by one for every violation
     */
    public int checkIfNotEmptySlotBetweenTeachingHours(Grade[] grades){
        int errorScore=0;
        for(Grade grade : grades) {
            for (int day = 0; day < AStar.scheduleDays; day++) {
                int emptyHours = 0;
                for (int hour = 0; hour < AStar.scheduleHours; hour++) {
                    if (grade.getSchedule()[day][hour] == null) {
                        emptyHours++;
                    }else{
                        errorScore += emptyHours;
                        emptyHours = 0;
                    }
                }
            }
        }
        return errorScore;
    }

    /**
     * Soft Constraint
     * This method checks if the teacher teach two hours in sequence
     * Referenced as Constraint 2 in the project description
     * @param grades the schedules of the grades
     * @return the score increase by one for every violation
     */
    public int checkIfTwoHoursInSequencePerTeacher(Grade[] grades){
        int errorScore=0;
        for(Teacher teacher : teachers){
            for(Grade grade : grades){
                for(int day = 0; day < AStar.scheduleDays; day++){
                    int continuousHours=0;
                    for(int hour = 0; hour < AStar.scheduleHours; hour++){
                        Pair pair = grade.getPair(day, hour);
                        if(pair != null && pair.getTeacher().getId() == teacher.getId()){
                                continuousHours++;
                                if(continuousHours > 2){
                                    errorScore++;
                                };
                        }
                        else{
                            continuousHours = 0;
                        }
                    }
                }
            }
        }
        return errorScore;
    }

    /**
     * Soft Constraint
     * This method checks if the grade's hours per day are normalized
     * Referenced as Constraint 3 in the project description
     * @param grades the schedules of the grades
     * @return the score increase by one for every violation
     */
    public int checkNormalizationGradePerDay(Grade[] grades){
        int errorScore=0;
        for(Grade grade : grades){
            int[] hourPerDay = new int[AStar.scheduleDays];
            for(int i = 0; i < hourPerDay.length; i++){
                hourPerDay[i] = 0;
            }

            int totalHours = 0;
            for(int day = 0; day < AStar.scheduleDays; day++){
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    if(grade.getPair(day,hour)!=null){
                        hourPerDay[day]++;
                    }
                }
                totalHours += hourPerDay[day];
            }
            float mean = totalHours/(float)5;
            for(int i=0; i<5; i++){
                int difference = (int)(Math.abs(hourPerDay[i]-mean));
                if(difference>=1){
                    errorScore+=difference;
                }
            }
        }
        return errorScore;
    }

    /**
     * Soft Constraint
     * This method checks if the grade's lesson hours per week are normalized
     * Referenced as Constraint 4 in the project description
     * @param grades the schedules of the grades
     * @return the score increase by one for every violation
     */
    public int checkNormalizationLessonPerGrade(Grade[] grades) {
        int errorScore=0;
        for (Grade grade : grades) {
            //Storing the values for lesson per day.
            int[][] lessonHoursForWeek = new int[lessons.size()][AStar.scheduleDays];
            float[] meanPerLesson = new float[lessons.size()];

            for(int day = 0; day < AStar.scheduleDays; day++){
                //Pair pair=new Pair(teachers.get(0), lessons.get(0));  //just a dummy assigment
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    Pair pair=grade.getPair(day,hour);
                    if(pair!=null) {
                        lessonHoursForWeek[pair.getLesson().getId()-1][day]++;
                    }
                }
            }

            for(int i = 0; i < lessons.size(); i++){
                for(int j = 0; j < AStar.scheduleDays; j++){
                    meanPerLesson[i] += lessonHoursForWeek[i][j];
                }
                meanPerLesson[i] = meanPerLesson[i] / (float)5;

                for(int day = 0; day < AStar.scheduleDays; day++){
                    int difference= (int)(Math.abs(lessonHoursForWeek[i][day] - meanPerLesson[i]));
                    //For each lesson if there is a difference in one more than 1 increase error
                    if(difference>=1){
                        errorScore += difference;
                    }
                }
            }
        }
        return errorScore;
    }

    /**
     * Soft Constraint
     * This method checks if the teacher's teaching hours are normalized per week
     * Referenced as Constraint 5 in the project description
     * @param grades the schedules of the grades
     * @return the score increase by one for every violation
     */
    public int checkNormalizationTeachingHoursOfTeacher(Grade[] grades){
        int errorScore = 0;
        float totalTeachingsHours = 0;
        int[] hoursPerTeacher= new int[teachers.size()];
        for(Grade grade : grades){
            //Pair pair=new Pair(teachers.get(0), lessons.get(0)); //dummy
            for(int day = 0; day < AStar.scheduleDays; day++){
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    Pair pair=grade.getPair(day,hour);
                    if(pair!=null){
                        hoursPerTeacher[pair.getTeacher().getId()-1]++;
                    }
                }
            }
        }


        for(int t=0; t<teachers.size(); t++) {
            totalTeachingsHours += hoursPerTeacher[t];
        }

        totalTeachingsHours = totalTeachingsHours /(float)teachers.size();

        for(int t=0; t<teachers.size(); t++) {
            int difference = (int)(Math.abs(hoursPerTeacher[t]-totalTeachingsHours));
            if(difference>=1){
                errorScore += difference;
            }
        }

        return errorScore;
    }
}

