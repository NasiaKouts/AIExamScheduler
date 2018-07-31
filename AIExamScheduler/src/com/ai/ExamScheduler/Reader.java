/* Katopodis Antonios - 3140076, Koutsopoulou Athanasia Maria - 3140092, Chasakis Dionisios - 3140219 */
package com.ai.ExamScheduler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class Reader, this class is a helper class to read from files
 *
 * @author Katopodis Antonios - 3140076
 * @author Koutsopoulou Athanasia Maria - 3140092
 * @author Chasakis Dionisios - 3140219
 */
public class Reader {
    /* Variable Declaration */
    private File f;
    private BufferedReader reader;

    /**
     * Constructor
     *
     * @param file path to file
     */
    public Reader(String file) {
        try {
			/* Get the file path and try to open it */
            f = new File(file);
			/* File may not exists, then create it */
            if (!f.exists()) {
                f.createNewFile();
            }
			/* Initialize BufferedReader */
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        } catch (NullPointerException e) {
            System.err.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/* Getters and Setters */

    /**
     * Returns the file opened by reader
     *
     * @return file opened by reader
     */
    public File getFile() {
        return f;
    }

    /**
     * Sets the file opened by reader
     *
     * @param f file opened by reader
     */
    public void setFile(File f) {
        this.f = f;
    }

    /**
     * Returns the BufferedReader
     *
     * @return BufferedReader
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * Sets the BufferedReader
     *
     * @param reader BufferedReader
     */
    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Close Reader when job is done
     */
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Error closing file!");
        }
    }

    /**
     * ReadLine Shortcut
     */
    public String readLine() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            System.out.println("Error getting file line!");
        }
        return line;
    }
}