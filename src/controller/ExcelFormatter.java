/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import model.Globals;
import model.Module;
import model.Student;

/**
 *
 * @author UBOSHET
 */
public class ExcelFormatter {

    private static final int MODULE_CODE = 0;
    private static final int MODULE_DESCRIPTION = 1;
    private Sheet data = null;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    private Map<String, String> address = new HashMap<>();
    private String currentCourse;
    private static ArrayList<String> noAddress = new ArrayList<>();

    /**
     * Creates Workbook and calls methods to load known types and load the
     * addresses of each student from external files
     *
     * @param inputFile
     * @param currentCourse
     */
    public void setupWorksheet(String inputFile, String currentCourse) {
        this.currentCourse = currentCourse;

        try {
            File inputWorkbook = new File(inputFile);
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            this.data = w.getSheet(0);
            loadKnownTypes();
            loadAddress();
        } catch (IOException | BiffException ex) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadKnownTypes() {
        try {
            File inputWorkbook = new File("KnownTypes.xls");
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet knownTypes = w.getSheet(0);

            for (int i = 1; i < knownTypes.getRows(); i++) {
                types.add(knownTypes.getCell(0, i).getContents() + "_" + knownTypes.getCell(1, i).getContents());
            }

        } catch (IOException | BiffException ex) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads all postal address of all students into a map with their student
     * numbers as the key
     */
    private void loadAddress() {
        try {
            /* Obtain the file path for the student address excel workbook
             * from the properties file
             */
            String studentAddressPath = Globals.PROP_STUDENT_ADDRESS;

            File inputWorkbook = new File(studentAddressPath);
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet studentAddress = w.getSheet(0);

            // Loop through all student records
            for (int i = 1; i < studentAddress.getRows(); i++) {

                // Start each address with an empty string
                String addressStr = "";

                // Loop through all possible address lines
                for (int j = 9; j < 13; j++) {
                    // Check that an entry was made into the specific address line
                    if (studentAddress.getCell(j, i).getContents() != null
                            && !studentAddress.getCell(j, i).getContents().equals("")) {
                        // Append the address to the total address
                        addressStr = addressStr
                                + studentAddress.getCell(j, i).getContents()
                                + "_";
                    }
                }

                // Append the last line without a '_' suffix
                addressStr = addressStr + studentAddress.getCell(13, i).getContents();

                // Replace '\' with empty spaces
                addressStr = addressStr.replace("\\", "");

                // Add the address to the address map
                address.put(
                        studentAddress.getCell(8, i).getContents().substring(2),
                        addressStr);
            }

        } catch (IOException | BiffException ex) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * QUICK FIX Find address for a specific student from their first name and
     * surname and then add this address to the address map
     *
     * @param Name
     * @param Surname
     */
    private String loadAddress(String firstName, String surname, String studentNum) {
        String postalAddress = "";
        try {
            /* Obtain the file path for the student address excel workbook
             * from the properties file
             */
            String studentAddressPath = Globals.PROP_STUDENT_ADDRESS;

            File inputWorkbook = new File(studentAddressPath);
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet studentAddress = w.getSheet(0);

            // Loop through all student records
            for (int i = 1; i < studentAddress.getRows(); i++) {
                if (studentAddress.getCell(0, i).getContents().equalsIgnoreCase(firstName)
                        && studentAddress.getCell(1, i).getContents().equalsIgnoreCase(surname)) {

                    // Start each address with an empty string
                    String addressStr = "";

                    // Loop through all possible address lines
                    for (int j = 9; j < 13; j++) {
                        // Check that an entry was made into the specific address line
                        if (studentAddress.getCell(j, i).getContents() != null
                                && !studentAddress.getCell(j, i).getContents().equals("")) {
                            // Append the address to the total address
                            addressStr = addressStr
                                    + studentAddress.getCell(j, i).getContents()
                                    + "_";
                        }
                    }

                    // Append the last line without a '_' suffix
                    addressStr = addressStr + studentAddress.getCell(13, i).getContents();

                    // Replace '\' with empty spaces
                    addressStr = addressStr.replace("\\", "");

                    // Add the address to the address map
                    address.remove(studentNum);
                    address.put(studentNum, addressStr);

                    postalAddress = addressStr;
                }
            }

        } catch (IOException | BiffException ex) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return postalAddress;
    }

    private boolean checkIfKnown(String moduleCode, String mark) {

        return types.contains(moduleCode + "_" + mark);
    }

    //Use this structure to get the informations that you need
    public void displayResults() {
        for (Student stud : students) {
            /*
            if (stud.getPostalAddress() == null) {
                noAddress.add(stud.getStudentNumber());
            }
            */
            System.out.println("Number: " + stud.getStudentNumber());
            System.out.println("Name: " + stud.getName());
            System.out.println("Surname: " + stud.getSurname());
            System.out.println("Course: " + stud.getCourse());
            //System.out.println("Address: " + stud.getPostalAddress());

            stud.getModules().stream().map((module) -> {
                String[] moduleComponents = module.getModuleCode().split("_");
                System.out.println("Module code: " + moduleComponents[MODULE_CODE]);
                System.out.println("Module des: " + moduleComponents[MODULE_DESCRIPTION]);
                System.out.println("Final Mark: " + module.getFinalMark());
                System.out.println("Status: " + module.getStatus());
                return module;
            });
            System.out.println("");
            System.out.println("");
        }
    }

    public void readRawData2() {
        if (data != null) {
            for (int studCount = 1; studCount < data.getRows(); studCount++) {
                Cell studentNumber = data.getCell(Globals.EXCEL_STUDENT_NUMBER, studCount);
                System.out.println("studentNumber: " + studentNumber.getContents());
                if (!studentNumber.getContents().equals("")) {
                    String studentNum = studentNumber.getContents().substring(3, 12);
                    Cell eVisionNumber = data.getCell(Globals.EXCEL_EVISION_STUDENT, studCount);
                    Cell studentName = data.getCell(Globals.EXCEL_FIRST_NAME, studCount);
                    Cell studentSurname = data.getCell(Globals.EXCEL_SURNAME, studCount);
                    String postalAddress = address.get(studentNum);
                    if (postalAddress == null || postalAddress.equals("")) {
                        postalAddress = loadAddress(studentName.getContents(), studentSurname.getContents(), studentNum);
                    }

                    ArrayList<Module> modules = new ArrayList<>();

                    while (studentNumber.getContents().equals(data.getCell(Globals.EXCEL_STUDENT_NUMBER, studCount).getContents())) {
                        int modCount = studCount;
                        
                        Cell module_code = data.getCell(Globals.EXCEL_MODULE_CODE, modCount);
                        //Cell module_name = data.getCell(Globals.EXCEL_MODULE_NAME, modCount);

                        String finalMark = data.getCell(Globals.EXCEL_FINAL_MARK, modCount).getContents();
                        String status = data.getCell(Globals.EXCEL_RESULT_CODE, modCount).getContents();

                        modules.add(new Module(
                                module_code.getContents()
                                + " (" + Globals.COURSE_YEAR + ")",
                                //+ "_" + module_name.getContents(),
                                finalMark, status,
                                "WORK OUT WHICH SEMESTER"));
                        studCount++;
                    }

                   // students.add(new Student(eVisionNumber.getContents(), studentNumber.getContents(),
                     //       studentName.getContents(),
                      //      studentSurname.getContents(), currentCourse,
                        //    "NO COURSE ID YET", postalAddress, modules, ""));
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Generates excel sheets for all students
     * @param filePath where to save the excel files created
     */
    public void createExcel(String filePath) {

        // Generate an excel sheet for each student
        students.stream().forEach((student) -> {
            ExcelGenerator ex = new ExcelGenerator(student, filePath);
        });
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }
}
