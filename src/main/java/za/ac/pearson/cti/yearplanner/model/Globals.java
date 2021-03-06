/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.pearson.cti.yearplanner.model;

/**
 *
 * @author UBOSHET
 */
public class Globals {
    

    public static String COURSE_YEAR = "Year";
    public static final String SEMESTER1 = "Semester 1";
    public static final String SEMESTER2 = "Semester 2";
    public static final String SEMESTER1_IDENTIFIER = "1";
    public static final String SEMESTER2_IDENTIFIER = "2";
    public static final String PASSED = "P";
    public static final String PASSED_VALUE = "PASSED";
    public static final String FAILED_VALUE = "FAILED";
    public static final String CREDIT_GIVEN_FOR_SUBJECT = "CREDIT";
    public static final String CREDIT_MODULE = "C";
    public static final String REDO = "REDO";
    public static final String FAILED_PREREQUISITES = "FP";
    public static final String TO_DO = "X";
    public static final int THREAD_POOL_SIZE = 10;
    
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
    public static final int EXCEL_FINAL_RESULT = 12;
    
    // Constants from ocean excel
    public static final int EXCEL_STUDENT_STATUS = 6;
    public static final int EXCEL_ADDRESS_FIRST_NAME = 2;
    public static final int EXCEL_ADDRESS_SURNAME = 1;
    public static final int EXCEL_CELL_NUMBER = 9;
    public static final int EXCEL_QUALIFICATION = 4;
    public static final int EXCEL_STUDENT_EMAIL = 10;
    public static final int EXCEL_SPONSOR_NAME = 16;
    public static final int EXCEL_SPONSOR_EMAIL = 17;
    public static final int EXCEL_SPONSOR_CELL_NUMBER = 19;
    public static final int EXCEL_ADDRESS_DV_NUM = 0;
    
    // Constants from Bsc IT Template
    // Rows start from 0 as well
    public static final int YEAR_ONE_START_ROW = 16;
    public static final int YEAR_ONE_END_ROW = 27;
    public static final int YEAR_TWO_START_ROW = 29;
    public static final int YEAR_TWO_END_ROW = 39;
    public static final int YEAR_THREE_START_ROW = 41;
    public static final int YEAR_THREE_END_ROW = 49; 
    public static final int TEMPLATE_MODULE_COL = 2; // Column C
    public static final int TEMPLATE_PREREQUISITE_COL = 3; // Column D
    
    //Constants from BCOM Template
    public static final int COMMERCE_YEAR_ONE_START = 16;
    public static final int COMMERCE_YEAR_ONE_END = 28;
    public static final int COMMERCE_YEAR_TWO_START = 30;
    public static final int COMMERCE_YEAR_TWO_END = 38;
    public static final int COMMERCE_YEAR_THREE_START = 40;
    public static final int COMMERCE_YEAR_THREE_END = 48;
    
    
    // Constants from HC IT
    public static final int HC_YEAR_ONE_START_ROW = 16;
    public static final int HC_YEAR_ONE_END_ROW = 28;
    public static final int HC_BSC_YEAR_ONE_START = 30;
    public static final int HC_BSC_YEAR_ONE_END = 40;
    
    // Constants from Commerce HC
    public static final int COM_HC_YEAR_ONE_START_ROW = 16;
    public static final int COM_HC_YEAR_ONE_END_ROW = 26;
    public static final int HC_BCOM_YEAR_ONE_START = 28;
    public static final int HC_BCOM_YEAR_ONE_END = 40;
    
    // ExcelTemplate Writing
    public static final int DETAILS_COLUMN = 2; // Column C;
    public static final int SURNAME_ROW = 3; // Row 4
    public static final int NAME_ROW = 4; // Row 3
    public static final int NUMBER_ROW = 5;
    public static final int PHONE_ROW = 6;
    public static final int EMAIL_ROW = 7;
    public static final int SPONSOR = 9;
    public static final int SPONSOR_PHONE = 10;
    public static final int SPONSOR_EMAIL = 11;
    public static final int YEAR_MODULE_COLUMN = 6;
    public static final int TEMPLATE_SEMESTER2_COLUMN = 5;
    public static final int TEMPLATE_SEMESTER1_COLUMN = 4;
    
    /* =========================== EXCEL CELL GLOBALS ================== */
    public static int SUBSTRING_START = 6;
    public static int SUBSTRING_END = 6;
    
}
    
