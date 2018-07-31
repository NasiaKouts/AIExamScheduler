/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Class Utilities is used to read and deserialize the input data and
 * write the final schedule to an output .txt file
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */

public class Utilities {

    /**
     * Read the teachers file
     */
    public static ArrayList<Teacher> readTeachers(){
        ArrayList<Teacher> teachers = new ArrayList<>();

        /* Initialize the reader */
        Reader reader = new Reader("data/teachers.txt");
        String line;
        int countLines = 0;
		/* While more lines, read */
        while((line=reader.readLine()) != null){
            countLines++;
            if(countLines == 1) continue;
			/* Trim line by " */
            StringTokenizer st = new StringTokenizer(line.trim(), " ");
			/* Initialize class instance variables */
            int i = 0;
            int id = 0;
            int maxPerDay = 0;
            int maxPerWeek = 0;
            String name = null;
			/* While line not ended, read */
            while(st.hasMoreTokens()){
                String temp = st.nextToken().trim();
				/* If line is empty, continue to the next token */
				/* When is going to read the unavailable date, accept null values */
                if(temp.equals("")) continue;

                i++;
				/* First token is the id, second the name, third the date */
                switch(i){
                    case 1:{ id = Integer.parseInt(temp); break;}
                    case 2:{ name = temp; break; }
                    case 3:{ maxPerDay = Integer.parseInt(temp); break; }
                    case 4:{ maxPerWeek = Integer.parseInt(temp); break; }
                }
            }

			/* Add the instance to the arraylist */
            teachers.add(new Teacher(id, name, maxPerDay, maxPerWeek));
        }
        return teachers;
    }

    /**
     * Read the lessons file
     */
    public static ArrayList<Lesson> readLessons(ArrayList<Teacher> Teachers) {
        ArrayList<Lesson> lessons = new ArrayList<>();

        /* Initialize the reader */
        Reader reader = new Reader("data/lessons.txt");
        String line;
        int countLines = 0;
		/* While more lines, read */
        while((line=reader.readLine()) != null){
            countLines++;
            if(countLines == 1) continue;
			/* Trim line by " */
            StringTokenizer st = new StringTokenizer(line.trim(), " ");
			/* Initialize class instance variables */
            int i = 0, id = 0;
            ArrayList<Integer> hours_per_class = new ArrayList<>();
            ArrayList<Teacher> lessonTeachers = new ArrayList<>();
            String name = null;
			/* While line not ended, read */
            while(st.hasMoreTokens()){
                String temp = st.nextToken().trim();
				/* If line is empty, continue to the next token */
                if(temp.equals("")) continue;
				/* First token is the id, second the name, third the instructor name
				   fourth the department name, fifth the semester, sixth the capacity*/
                i++;
                switch(i){
                    case 1:{ id = Integer.parseInt(temp); break;}
                    case 2:{ name = temp; break; }
                    case 3:{
                        StringTokenizer st2 = new StringTokenizer(temp, ",");
                        while(st2.hasMoreTokens()){
                            String temp2 = st2.nextToken().trim();
                            if(temp2.equals("")) continue;
                            hours_per_class.add(Integer.parseInt(temp2));
                        }
                        break;
                    }
                    case 4:{
                        StringTokenizer st2 = new StringTokenizer(temp, ",");
                        while(st2.hasMoreTokens()){
                            String temp2 = st2.nextToken().trim();
                            if(temp2.equals("")) continue;

                            int teacherId = Integer.parseInt(temp2);
                            Teacher tempTeacher = null;
                            for(int j = 0; j < Teachers.size(); j++){
                                if(teacherId == Teachers.get(j).getId()){
                                    tempTeacher = Teachers.get(j);
                                }
                            }

                            if(tempTeacher != null){
                                lessonTeachers.add(tempTeacher);
                            }
                        }
                        break;
                    }
                }
            }
            lessons.add(new Lesson(id, name, hours_per_class, lessonTeachers));
        }

        return lessons;
    }

    /**
     *
     * @param state gets the state to write to the file
     * @throws IOException
     */
    public static void WriteScheduleToFile(State state) throws IOException {

        /* Get the path */
        Path file = Paths.get("data/schedule.txt");

        if(!Files.exists(file)){
            /* If the file is not exists, create new */
            Files.createFile(file);
        }else{
            /* If the file exists, clear it */
            PrintWriter writer = new PrintWriter(file.toString(), "UTF-8");
            writer.print("");
            writer.close();
        }

        /* Initialize the writer */
        PrintWriter writer = new PrintWriter(file.toString(), "UTF-8");
        for(int grade = 0; grade < AStar.totalGrades; grade++){
            /* Get ClassId */
            int classId = grade / AStar.gradesPerClass;

            /* Write GradeId(e.g. A1) */
            writer.println(getCharForNumber(classId + 1) + ((grade % AStar.gradesPerClass) + 1) + ":");
            for(int day = 0; day < AStar.scheduleDays; day++){
                writer.print("   Day: " + day);
                for(int hour = 0; hour < AStar.scheduleHours; hour++){
                    /* Print the lesson-teacher for every day, hour in the schedule */
                    Pair pair = state.getGrades()[grade].getSchedule()[day][hour];
                    /* If the pair is null, then there is no lesson and no teacher */
                    if(pair == null){
                        writer.print(" | No lesson no teacher");
                        continue;
                    }
                    writer.print(" | " + pair.getLesson().getName() + " , " +pair.getTeacher().getName());
                }
                writer.println();
            }
        }
        writer.close();
    }

    /**
     * Makes an integer to a letter
     * @param i integer
     * @return char
     */
    private static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }
}
