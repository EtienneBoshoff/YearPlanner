/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.ExcelFormatter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UBOSHET
 */
public class Globals {
    

    public static String CURRENT_SEMESTER = "-1";
    public static String COURSE_YEAR = "-1";
    public static final String PROP_UPLOAD_PARENT_FOLDER = "upload_parent_folder";
    public static final String PROP_OUTPUT_PARENT_FOLDER = "output_parent_folder";
    public static final String PROP_STUDENT_ADDRESS = "student_address";
    public static final String PROP_LEGEND_FILE = "properties/legend_properties.prop";
    public static final String PROPERTIES_FILE = "properties.prop";
    public static boolean PRINTER_POPUP = true;
    public static final String SEMESTER1 = "1";
    public static final String SEMESTER2 = "2";
    public static final String PENDING = "PEN";
    private static Properties properties;
    private static BufferedWriter studentAddressLog;
    
    /*============================ FILE CONSTANTS ====================== */
    public static final String DEFAULT_REGISTERED_STUDENT_FILE_LOCATION = "C:/temp/registered_students";
    /*============================ FILE CONSTANTS ====================== */
    
    /* =========================== EXCEL CELL GLOBALS ================== */
    // Constants reflect the cell number in the excel sheet from evision
    public static final int EXCEL_EVISION_STUDENT = 0;
    public static final int EXCEL_STUDENT_NUMBER = 2;
    public static final int EXCEL_SURNAME = 3;
    public static final int EXCEL_FIRST_NAME = 1;
    public static final int EXCEL_MODULE_CODE = 6;
    public static final int EXCEL_SEMESTER_MARK = 8;
    public static final int EXCEL_EXAM_MARK = 9;
    public static final int EXCEL_FINAL_MARK = 10;
    public static final int EXCEL_RESULT_CODE = 11;
    
    // Constants from ocean excel
    public static final int EXCEL_ADDRESS_FIRST_NAME = 2;
    public static final int EXCEL_ADDRESS_SURNAME = 1;
    public static final int EXCEL_CELL_NUMBER = 9;
    public static final int EXCEL_QUALIFICATION = 4;
    public static final int EXCEL_STUDENT_EMAIL = 10;
    public static final int EXCEL_SPONSOR_NAME = 16;
    public static final int EXCEL_SPONSOR_EMAIL = 17;
    public static final int EXCEL_SPONSOR_CELL_NUMBER = 19;
    public static final int EXCEL_ADDRESS_DV_NUM = 0;
    
    /* =========================== EXCEL CELL GLOBALS ================== */
    
    /**
     * Loads the properties object with the BufferedReader stream pointing to
     * the file containing the properties. If the properties object is null a
     * new properties object is created
     *
     * @return the properties of the Year Planner program
     */
    public static Properties getProperties() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("properties/" + Globals.PROPERTIES_FILE)));

            if (properties == null) {
                properties = new Properties();
            }

            properties.load(br);
        } catch (FileNotFoundException fnfe) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, fnfe);
        } catch (IOException ioe) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ioe);
        }

        return properties;
    }

    /**
     * Stores the BufferedWriter object in the properties object pointing to the
     * file where the properties are stored.
     *
     * @param properties
     */
    public static void setProperties(Properties properties) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("properties/" + Globals.PROPERTIES_FILE));

            getProperties().store(bw, "saved location for uploads");
        } catch (IOException ioe) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ioe);
        }

        properties = Globals.getProperties();
    }

    /**
     * Lazy singleton log to capture all the students that have no address
     * associated with their names
     *
     * @return BufferedWriter studentAddressLog
     */
    public static BufferedWriter openStudentAddressLog() {
        if (studentAddressLog == null) {
            try {
                studentAddressLog = new BufferedWriter(new FileWriter(new File("logs/student_address.log"), true));
                studentAddressLog.newLine();
                studentAddressLog.write(LocalDateTime.now().toLocalDate() + " - The following students have no address associated with their names:");
                studentAddressLog.newLine();
                studentAddressLog.flush();
            } catch (IOException ioe) {
                Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ioe);
            }
        }

        return studentAddressLog;
    }
    
    public static BufferedWriter useStudentAddressLog() {
        return studentAddressLog;
    }

    /**
     * This method closes the buffered reader log file
     */
    public void closeAddressLog() {
        if (studentAddressLog != null) {
            try {
                studentAddressLog.close();
            } catch (IOException ioe) {
                Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ioe);
            }
            studentAddressLog = null;
        }
    }
}
    
