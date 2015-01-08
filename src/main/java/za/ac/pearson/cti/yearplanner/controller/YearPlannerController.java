/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.pearson.cti.yearplanner.controller;

import za.ac.pearson.cti.yearplanner.dataaccesslayer.ExcelReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jxl.read.biff.BiffException;
import za.ac.pearson.cti.yearplanner.model.Globals;
import za.ac.pearson.cti.yearplanner.model.Module;
import za.ac.pearson.cti.yearplanner.model.Sponsor;
import za.ac.pearson.cti.yearplanner.model.Student;
import za.ac.pearson.cti.yearplanner.YearPlanner;
import za.ac.pearson.cti.yearplanner.dataaccesslayer.ExcelWriter;

/**
 *
 * @author UBOSHET
 */
public class YearPlannerController implements Initializable {
    
    @FXML
    private TextArea statusArea;
    
    @FXML
    private ChoiceBox<String> yearGroupField;
    
    @FXML
    private ChoiceBox<String> yearSelection;
    
    @FXML
    private ChoiceBox<String> semesterChoiceBox;
    
    @FXML 
    private ProgressBar progressBar;
    
    @FXML 
    private ProgressIndicator taskProgress;
    
    @FXML
    private Button loadTemplateBtn;
    
    @FXML
    private Button loadResultsBtn;
    
    @FXML
    private Button loadPrerequisitesBtn;
    
    @FXML
    private Button selectOutputFolderBtn;
    
    @FXML
    private Button calculateBtn;
    
    @FXML
    private Label logoLbl;
    
    private List<Student> masterAddressList;
    
    private List<Student> currentSelectedYearStudents;
    
    private File template;
    
    private File outputFolder;
    private final String EXCEL_FILE_EXTENTION = ".xls";
    private List<Module> allModules;
    /**
     * This method loads the registered students into the masterAddressList that 
     * will be used to create a year plan from for each student
     */
    @FXML
    private void handleLoadStudents() {
        // Clear status area and start the dialog to choose a file
        statusArea.clear();
        statusArea.setWrapText(true);
        
        // Update the progress of the task
        taskProgress.setProgress(0.0);
        progressBar.setProgress(0.10);
        
        // Choose .xls file NOTE: Cannot be .xlsx since its not supported.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File with Student Information");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 2003 Format", "*.xls"));
        
        File excelFile = fileChooser.showOpenDialog(YearPlanner.getPrimaryStage());
        statusArea.setText("Loading Students into memory...\n\n");
        
        taskProgress.setProgress(0.05);
        
        // Read the addresses of the students into memory
        ExcelReader reader = new ExcelReader(excelFile);
        try {
            if (excelFile != null && reader.openWorkBook()) {
                statusArea.appendText("Found workbook at :\n" + excelFile.getAbsolutePath() + "\n");
                taskProgress.setProgress(0.10);
                statusArea.appendText("Total Rows : " + reader.getTotalRows()+ "\n");
                statusArea.appendText("Total Columns : " + reader.getTotalColumns() + "\n");
                // Calculate progress gaps and reset masterList
                int categorySpaces = reader.getTotalRows() / 9;
                masterAddressList.clear();
                // Read all the addresses and insert them into the master student list.
                for (int i = 1; i < reader.getTotalRows(); i++) {
                    if (i % categorySpaces == 0) {
                        // progress is calculated by taking into account where in the category you are and dividing
                        // by ten to make it < 1.0
                        double progress = i / categorySpaces / 10;
                        taskProgress.setProgress(progress);
                    }
                    // filter excel to only get current and started students
                    if (reader.readCellValue(Globals.EXCEL_STUDENT_STATUS, i).equalsIgnoreCase("CURRENT") 
                            || reader.readCellValue(Globals.EXCEL_STUDENT_STATUS, i).equalsIgnoreCase("STARTED")) {
                        // Get name and Surname
                        String name = reader.readCellValue(Globals.EXCEL_ADDRESS_FIRST_NAME, i);
                        String surname = reader.readCellValue(Globals.EXCEL_ADDRESS_SURNAME, i);
                        // Create registered student
                        Student registeredStudent = new Student(name, surname);
                        // Set registered student's DV number
                        registeredStudent.setStudentNumber(reader.readCellValue(Globals.EXCEL_ADDRESS_DV_NUM, i));
                        // Set registered student's Qualification
                        registeredStudent.setCourse(reader.readCellValue(Globals.EXCEL_QUALIFICATION, i));
                        // Get Cell Number of registered student
                        registeredStudent.setCellNumber("+" + reader.readCellValue(Globals.EXCEL_CELL_NUMBER, i));
                        // Get Email of registered student
                        registeredStudent.setEmailAddress(reader.readCellValue(Globals.EXCEL_STUDENT_EMAIL, i));
                        // Setup sponsor
                        Sponsor studentSponsor = new Sponsor();
                        studentSponsor.setName(reader.readCellValue(Globals.EXCEL_SPONSOR_NAME, i));
                        studentSponsor.setCellNumber("+" + reader.readCellValue(Globals.EXCEL_SPONSOR_CELL_NUMBER, i));
                        studentSponsor.setEmailAdress(reader.readCellValue(Globals.EXCEL_SPONSOR_EMAIL, i));
                        // Add sponsor to student
                        registeredStudent.setSponsor(studentSponsor);

                        masterAddressList.add(registeredStudent);
                    }
                }
                statusArea.appendText("Added : " + masterAddressList.size() + " students to memory...\n");
                /*
                // Just a check must be removed later
                // TODO: Remove this nonsense
                statusArea.appendText("Details of last student Added : \n");
                statusArea.appendText("First Name : " + masterAddressList.get(masterAddressList.size() - 1).getName() + "\n" );
                statusArea.appendText("Surname : " + masterAddressList.get(masterAddressList.size() - 1).getSurname() + "\n");
                statusArea.appendText("DV number : " + masterAddressList.get(masterAddressList.size() - 1).getStudentNumber()+ "\n");
                statusArea.appendText("Registered For : " + masterAddressList.get(masterAddressList.size() - 1).getCourse() + "\n");
                statusArea.appendText("Cell Number : " + masterAddressList.get(masterAddressList.size() - 1).getCellNumber() + "\n");
                statusArea.appendText("Email Address : " + masterAddressList.get(masterAddressList.size() - 1).getEmailAddress() + "\n");
                statusArea.appendText("Sponsor Name : " + masterAddressList.get(masterAddressList.size() - 1).getSponsor().getName() + "\n");
                statusArea.appendText("Sponsor Cell Number : " + masterAddressList.get(masterAddressList.size() - 1).getSponsor().getCellNumber() + "\n");
                statusArea.appendText("Sponsor Email Address : " + masterAddressList.get(masterAddressList.size() - 1).getSponsor().getEmailAdress() + "\n");
                //*/
                
                statusArea.appendText("\n\t<<<Task Completed Loading of Students>>>\n\n");
                
                taskProgress.setProgress(1.0);
                statusArea.appendText("Please Load the Template Now now\n");
                loadTemplateBtn.setDisable(false);
                reader.closeWorkBook();
            } else {
                taskProgress.setProgress(0.0);
                statusArea.appendText("No File Selected Please Select File\n");
            }
            
        } catch (IOException | BiffException ex) {
            Logger.getLogger(YearPlannerController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * This method sets the location of the template that will be used to construct the year planner 
     */
    @FXML
    private void handleLoadTemplate() {
        statusArea.appendText("Loading Chosen Template into memory...\n\n");
        progressBar.setProgress(0.20);
        taskProgress.setProgress(0.10);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please Select The Template For The Year Planner");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 2003 Format", "*.xls"));
        template = fileChooser.showOpenDialog(YearPlanner.getPrimaryStage());
        if (template != null) {
            statusArea.appendText("Chosen Template : " + template.getName() + "\n");      
            taskProgress.setProgress(1.0);
            statusArea.appendText("\n\t<<<Task Completed Loading of Template>>>\n\n");
            statusArea.appendText("Please Load Results Now\n");
            loadResultsBtn.setDisable(false);
        } else {
            statusArea.appendText("No Template Selected.  Please Select a Template\n");
        }
        
    }
    
    @FXML
    private void handleLoadResults() {
        statusArea.appendText("Loading Results of Students into memory...\n\n");
        progressBar.setProgress(0.30);
        taskProgress.setProgress(0.10);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please Select The E-Vision Result Dump For The Year Planner");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 2003 Format", "*.xls"));
        File evisionFile = fileChooser.showOpenDialog(YearPlanner.getPrimaryStage());
        
        ExcelReader reader = new ExcelReader(evisionFile);
        
        try {
            if (evisionFile != null && reader.openWorkBook()) {
                statusArea.appendText("E-Vision File located at : " + evisionFile.getAbsolutePath() + "\n");
                statusArea.appendText("Total Rows : " + reader.getTotalRows() + "\n");
                statusArea.appendText("Total Columns : " + reader.getTotalColumns() + "\n");
                int taskCounter = 0;
                double progressCounter = 0.0;
                int sectionSplit = masterAddressList.size() / 10;
                for (Student student : masterAddressList) {
                    for (int i = 1; i < reader.getTotalRows(); i++) {
                        if (reader.readCellValue(Globals.EXCEL_STUDENT_NUMBER, i).equalsIgnoreCase(student.getStudentNumber())) {
                            Module tempModule = new Module(reader.readCellValue(Globals.EXCEL_MODULE_CODE, i),
                                reader.readCellValue(Globals.EXCEL_FINAL_MARK, i),
                                reader.readCellValue(Globals.EXCEL_RESULT_CODE, i));
                            // Check if the module has no final mark.  If it does not have then set it to zero
                            if (tempModule.getFinalMark().equals("")) {
                                tempModule.setFinalMark("0");
                            }
                            // Figure out in which semester this module is
                            if (tempModule.getModuleCode()
                                    .substring(tempModule.getModuleCode().length() - 2,
                                            tempModule.getModuleCode().length() - 1)
                                    .equals(Globals.SEMESTER2_IDENTIFIER)) {
                                tempModule.setSemester(Globals.SEMESTER2);
                            }
                            else {
                                if (tempModule.getModuleCode()
                                    .substring(tempModule.getModuleCode().length() - 2,
                                            tempModule.getModuleCode().length() - 1)
                                    .equals(Globals.SEMESTER1_IDENTIFIER)) {
                                        tempModule.setSemester(Globals.SEMESTER1);
                                } else {
                                    tempModule.setSemester(Globals.COURSE_YEAR);
                                }
                            }
                            if (reader.readCellValue(Globals.EXCEL_EXAM_MARK, i).equals(""))
                            {
                                tempModule.setExamMark("0");
                            }
                            else {
                                tempModule.setExamMark(reader.readCellValue(Globals.EXCEL_EXAM_MARK, i));  
                            }
                            
                            student.addModule(tempModule);
                        }
                    }
                    taskCounter++;
                    if (taskCounter % sectionSplit == 0) {
                        taskProgress.setProgress((progressCounter + 0.1));
                    }
                }
                
                /*
                statusArea.appendText("\nLast Student "
                        + masterAddressList.get(masterAddressList.size() - 1).getName()
                        + " "
                        + masterAddressList.get(masterAddressList.size() - 1).getSurname()
                        +" had : " 
                        + masterAddressList.get(masterAddressList.size() - 1).getModules().size() 
                        + " modules loaded\n");
                //*/
                
                statusArea.appendText("\n\t<<<Task Completed Loading of Results>>>\n\n");
                statusArea.appendText("Please Load Prerequisites Now\n");
                taskProgress.setProgress(1.0);
                loadPrerequisitesBtn.setDisable(false);
            } else {
                statusArea.appendText("No results were selected.  Please Select Results\n\n");
            }
            
        } catch (IOException | BiffException ex) {
            Logger.getLogger(YearPlannerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    private void handleLoadPrerequisites() {
        statusArea.appendText("Loading Prerequisites into memory...\n\n");
        progressBar.setProgress(0.40);
        
        statusArea.appendText("Locking Group, Year and Semester Choices now.\n");
        taskProgress.setProgress(0.10);
        yearSelection.setDisable(true);
        yearGroupField.setDisable(true);
        semesterChoiceBox.setDisable(true);
        
        if (yearGroupField.getValue().equals("Bachelor of Science in Information Technology")) {
             // Filter all students according to selected year and group
            currentSelectedYearStudents = masterAddressList.stream()
                    .filter(s -> s.getCourse().contains(yearSelection.getValue()) && 
                            s.getCourse().contains(yearGroupField.getValue()))
                    .collect(Collectors.toList());

            statusArea.appendText("Filtered students from " + masterAddressList.size() 
                    + " down to " + currentSelectedYearStudents.size() + "\n");

            statusArea.appendText("\nLoading all year subjects into memory...\n");
            allModules = new ArrayList<>();
            ExcelReader templateReader = new ExcelReader(template);

            try {
                templateReader.openWorkBook();
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.YEAR_ONE_START_ROW, Globals.YEAR_ONE_END_ROW));
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.YEAR_TWO_START_ROW, Globals.YEAR_TWO_END_ROW));
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.YEAR_THREE_START_ROW, Globals.YEAR_THREE_END_ROW)); 
                templateReader.closeWorkBook();
            } catch (IOException | BiffException ex) {
                Logger.getLogger(YearPlannerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            statusArea.appendText("Loaded " + allModules.size() + " modules into memory.\n");

            statusArea.appendText("\nFollowing Co-Requisites found and saved: \n\n");
            // filter on all the coRequisites
            allModules.stream().filter((module) -> (!module.getCoRequisites().equals("")))
                    .forEach((Module module) -> {
                        statusArea.appendText("\tModule " + module.getModuleCode() 
                                + " has co-requisite: " 
                                + module.getCoRequisites() 
                                + "\n");
            });
        }
        
        if (yearGroupField.getValue().equals("Bachelor of Commerce")) {
             // Filter all students according to selected year and group
            currentSelectedYearStudents = masterAddressList.stream()
                    .filter(s -> s.getCourse().contains(yearSelection.getValue()) && 
                            s.getCourse().contains(yearGroupField.getValue()))
                    .collect(Collectors.toList());

            statusArea.appendText("Filtered students from " + masterAddressList.size() 
                    + " down to " + currentSelectedYearStudents.size() + "\n");

            statusArea.appendText("\nLoading all year subjects into memory...\n");
            allModules = new ArrayList<>();
            ExcelReader templateReader = new ExcelReader(template);

            try {
                templateReader.openWorkBook();
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.COMMERCE_YEAR_ONE_START, Globals.COMMERCE_YEAR_ONE_END));
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.COMMERCE_YEAR_TWO_START, Globals.COMMERCE_YEAR_TWO_END));
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.COMMERCE_YEAR_THREE_START, Globals.COMMERCE_YEAR_THREE_END));
                templateReader.closeWorkBook();
            } catch (IOException | BiffException ex) {
                Logger.getLogger(YearPlannerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            statusArea.appendText("Loaded " + allModules.size() + " modules into memory.\n");

            statusArea.appendText("\nFollowing Co-Requisites found and saved: \n\n");
            // filter on all the coRequisites
            allModules.stream().filter((module) -> (!module.getCoRequisites().equals("")))
                    .forEach((Module module) -> {
                        statusArea.appendText("\tModule " + module.getModuleCode() 
                                + " has co-requisite: " 
                                + module.getCoRequisites() 
                                + "\n");
            });
        }
        
        if (yearGroupField.getValue().equals("Higher Certificate in Information Technology")) {
             // Filter all students according to selected year and group
            currentSelectedYearStudents = masterAddressList.stream()
                    .filter(s -> s.getCourse().contains(yearSelection.getValue()) && 
                            s.getCourse().contains(yearGroupField.getValue()))
                    .collect(Collectors.toList());

            statusArea.appendText("Filtered students from " + masterAddressList.size() 
                    + " down to " + currentSelectedYearStudents.size() + "\n");

            statusArea.appendText("\nLoading all year subjects into memory...\n");
            allModules = new ArrayList<>();
            ExcelReader templateReader = new ExcelReader(template);

            try {
                templateReader.openWorkBook();
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.HC_YEAR_ONE_START_ROW, Globals.HC_YEAR_ONE_END_ROW));
                templateReader.closeWorkBook();
            } catch (IOException | BiffException ex) {
                Logger.getLogger(YearPlannerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            statusArea.appendText("Loaded " + allModules.size() + " modules into memory.\n");

            statusArea.appendText("\nFollowing Co-Requisites found and saved: \n\n");
            // filter on all the coRequisites
            allModules.stream().filter((module) -> (!module.getCoRequisites().equals("")))
                    .forEach((Module module) -> {
                        statusArea.appendText("\tModule " + module.getModuleCode() 
                                + " has co-requisite: " 
                                + module.getCoRequisites() 
                                + "\n");
            });
        }
       
        if (yearGroupField.getValue().equals("Higher Certificate in Business Management")) {
             // Filter all students according to selected year and group
            currentSelectedYearStudents = masterAddressList.stream()
                    .filter(s -> s.getCourse().contains(yearSelection.getValue()) && 
                            s.getCourse().contains(yearGroupField.getValue()))
                    .collect(Collectors.toList());

            statusArea.appendText("Filtered students from " + masterAddressList.size() 
                    + " down to " + currentSelectedYearStudents.size() + "\n");

            statusArea.appendText("\nLoading all year subjects into memory...\n");
            allModules = new ArrayList<>();
            ExcelReader templateReader = new ExcelReader(template);

            try {
                templateReader.openWorkBook();
                allModules.addAll(gatherModulesAndPreRequisitesFrom(templateReader, Globals.COM_HC_YEAR_ONE_START_ROW, Globals.COM_HC_YEAR_ONE_END_ROW));
                templateReader.closeWorkBook();
            } catch (IOException | BiffException ex) {
                Logger.getLogger(YearPlannerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            statusArea.appendText("Loaded " + allModules.size() + " modules into memory.\n");

            statusArea.appendText("\nFollowing Co-Requisites found and saved: \n\n");
            // filter on all the coRequisites
            allModules.stream().filter((module) -> (!module.getCoRequisites().equals("")))
                    .forEach((Module module) -> {
                        statusArea.appendText("\tModule " + module.getModuleCode() 
                                + " has co-requisite: " 
                                + module.getCoRequisites() 
                                + "\n");
            });
        }
        
        
        taskProgress.setProgress(1.0);
        statusArea.appendText("\n\t<<<Task Completed Loading of Prerequisites>>>\n\n");
        statusArea.appendText("Please Select Output Folder Now\n");
        selectOutputFolderBtn.setDisable(false);
    }
    
    /**
     * This method gathers the module codes and their co-requisites in the template
     * @param reader the template where the module codes reside
     * @param beginningBoundry where the beginning of the module codes start
     * @param endBoundry where the end of the module codes are
     * @return A list of all the modules for a given year and their co-requisites
     */
    private List<Module> gatherModulesAndPreRequisitesFrom(ExcelReader reader, Integer beginningBoundry, Integer endBoundry) {
        List<Module> moduleList = new ArrayList<>();
        
        for (int i = beginningBoundry; i < endBoundry; i++) {
                Module currentModule = new Module(reader.readCellValue(Globals.TEMPLATE_MODULE_COL, i));
                currentModule.setTemplateRow(i);
                String unformattedPreRequisites = reader.readCellValue(Globals.TEMPLATE_PREREQUISITE_COL, i);
                if (!unformattedPreRequisites.equals("")) {
                    String[] preRequisites = unformattedPreRequisites.split(",");
                    for (String preRequisite : preRequisites) {
                        if (preRequisite != null) {
                            Module preRequisiteModule = new Module(preRequisite);
                            currentModule.addCoRequisite(preRequisiteModule);
                        }
                        
                    }
                }
                moduleList.add(currentModule);
            }
        
        return moduleList;
    }
    
    @FXML
    private void handleSelectOutputFolder() {
        
        progressBar.setProgress(0.50);
        taskProgress.setProgress(0.10);
        
        // Choose folder where the documents will be saved.
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Select The Folder Where You Would Like To Save The YearPlanners To");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        
        outputFolder = fileChooser.showDialog(YearPlanner.getPrimaryStage());
        
        if (outputFolder != null && outputFolder.isDirectory()) {
            statusArea.appendText("Setting up "+ outputFolder.getAbsolutePath() + " to save yearplanners in...\n\n");
            taskProgress.setProgress(1.0);
            statusArea.appendText("\n\t<<<Task Completed Select Output Folder>>>\n\n");
            statusArea.appendText("Ready To Calculate Year Planners\n");
            calculateBtn.setDisable(false);
        } else {
            statusArea.appendText("\nNo Folder Chosen\n");
            statusArea.appendText("Please Choose An Output Folder");
        }
        
    }
    
    @FXML
    private void handleCalculateYearPlanner() {
        double currentProgress;
        statusArea.appendText("Calculating Year Planners\n");
        ExecutorService executor = Executors.newFixedThreadPool(Globals.THREAD_POOL_SIZE);
        for (Student currentSelectedStudent : currentSelectedYearStudents) {
            taskProgress.setProgress(0.0);
            String currentStudentFileName = formatFileNameForStudent(currentSelectedStudent);
            ExcelWriter writer = new ExcelWriter(template, outputFolder.toString() 
                    + "\\" 
                    + currentStudentFileName);
            calculateSubjectStates(currentSelectedStudent);
            addMissingSubjects(currentSelectedStudent);
            calculatePreRequisites(currentSelectedStudent);
            addLastMinuteChanges(currentSelectedStudent);
            writer.setCurrentStudent(currentSelectedStudent);
            Task writingTask = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                   
                    writer.run();
                    ExcelWriter.updateExecutionCounter();
                    //updateProgress(ExcelWriter.getExecutionCounter(), currentSelectedYearStudents.size());
                    return null;
                }
            
            };
            executor.execute(writingTask);
            taskProgress.setProgress(0.5);
            //currentProgress = (studentServed / currentSelectedYearStudents.size());
            //System.out.println("Served " + studentServed + "Students");
            // TODO: Fix broken progress bar
            //taskProgress.progressProperty().bind(writingTask.progressProperty());
            taskProgress.setProgress(1.0);
        }
        
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait patiently
        }
        //taskProgress.setProgress(1.0);
        statusArea.appendText("Created " + ExcelWriter.getExecutionCounter() + " Year Planners." );
        statusArea.appendText("\n\n\t<<<All Year Planners Created>>>");
        progressBar.setProgress(1.0);
        calculateBtn.setDisable(true);
        selectOutputFolderBtn.setDisable(true);
        loadPrerequisitesBtn.setDisable(true);
        loadResultsBtn.setDisable(true);
        loadTemplateBtn.setDisable(true);
        yearSelection.setDisable(false);
        yearGroupField.setDisable(false);
        semesterChoiceBox.setDisable(false);
    }
    
    private void calculatePreRequisites(Student student) {
        student.getModules().stream().filter((module) -> (module.getPreRequisitesList().size() > 0)).forEach((module) -> {
            module.getPreRequisitesList().stream().forEach((preRequisite) -> {
                student.getModules().stream().filter((moduleToCompare) ->
                        (preRequisite.equals(moduleToCompare) && moduleToCompare.getStatus().equals(Globals.REDO))).forEach((_item) ->
                        {
                            module.setStatus(Globals.FAILED_PREREQUISITES);
                        });
            });
        });
    }
    
    // This method adds last minute changes that are made.
    // TODO : Fix this so that it reads the changes from a file.
    private void addLastMinuteChanges(Student student) {
        student.getModules().stream().filter((module) -> (module.getModuleCode().equalsIgnoreCase("C_ITMA121"))).forEach((module) -> {
            module.setSemester(Globals.SEMESTER1);
            if (module.getStatus().equalsIgnoreCase(Globals.TO_DO)) {
                module.setStatus(Globals.REDO);
            }
        });
    }
    
    private void addMissingSubjects(Student student) {
        allModules.stream().filter((module) -> (!student.getModules().contains(module))).map((module) -> {
            // Set module status to to be completed
            module.setStatus(Globals.TO_DO);
            return module;
        }).map((module) -> {
            // Check if semester is set if not set it
            if (module.getSemester().equals("")) {
                if (module.getModuleCode().substring(module.getModuleCode().length() - 2
                        , module.getModuleCode().length() - 1).equals(Globals.SEMESTER2_IDENTIFIER)) {
                    module.setSemester(Globals.SEMESTER2);
                } else {
                    if (module.getModuleCode().substring(module.getModuleCode().length() - 2
                            , module.getModuleCode().length() - 1).equals(Globals.SEMESTER1_IDENTIFIER)) {
                        module.setSemester(Globals.SEMESTER1);
                    } else {
                        module.setSemester(Globals.COURSE_YEAR);
                    } 
                }
            }
            return module;
        }).forEach((module) -> {
            student.addModule(module);
        });
    }
    
    private void calculateSubjectStates(Student student) {
        
        // Go through each module available for the student
        for (Module module : allModules) {
            // Check each student module
            for (Module studentModule : student.getModules()) {
                if (module.getModuleCode()
                        .equalsIgnoreCase(studentModule.getModuleCode())) {
                    //try {
                        if (!studentModule.getFinalMark().equals("")
                                && Double.parseDouble(studentModule.getFinalMark()) >= 50.0
                                && Double.parseDouble(studentModule.getExamMark()) >= 50.0) {
                            studentModule.setStatus(Globals.PASSED);
                            studentModule.setTemplateRow(module.getTemplateRow());

                        } else {
                            // TODO: Figure out the students current study year rather than this nonsense
                            if (!studentModule.getFinalMark().equals("") 
                                    && student.getCourse().contains(yearSelection.getValue())
                                    && (Double.parseDouble(studentModule.getFinalMark()) < 50.0
                                    || Double.parseDouble(studentModule.getExamMark()) < 50.0)) {
                                studentModule.setStatus(Globals.REDO);
                            }
                            else {
                                studentModule.setStatus(Globals.TO_DO);
                            }
                            studentModule.setTemplateRow(module.getTemplateRow());
                        }
                }
            }
        }
        
    }
    
    private String formatFileNameForStudent(Student student) {
        String currentStudentFileName = student.getName() +
                    " " + student.getSurname() +
                    ", " + student.getStudentNumber() +
                    EXCEL_FILE_EXTENTION;
        return currentStudentFileName;
    }
    
    @FXML
    private void handleMouseEnteringButtonArea() {
        YearPlanner.getPrimaryStage().getScene().setCursor(Cursor.HAND);
    }

    @FXML
    private void handleMouseLeavingButtonArea() {
        YearPlanner.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
    }
    /**
     * Initializes the choice box with the semester values
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoLbl.setId("logo-text");
        masterAddressList = new ArrayList<>();
        taskProgress.setProgress(-1.0);
        progressBar.setProgress(-1.0);
        statusArea.setText("Program Ready. \nPlease Load Students.");
        // Dynamically figure out available years
        LocalDate date = LocalDate.now();
        yearSelection.getItems().clear();
        // populate years
        yearSelection.setItems(FXCollections.observableArrayList(
            (date.getYear() + 1) + "" , date.getYear() + "",
            (date.getYear() - 1) + "" , (date.getYear() - 2) + "",
            (date.getYear() - 3) + "", (date.getYear() - 4) + ""));
        // set selection to current year
        yearSelection.setValue(date.getYear() + "");
        yearGroupField.getItems().clear();
        yearGroupField.setItems(FXCollections.observableArrayList(
            "Bachelor of Science in Information Technology", "Bachelor of Commerce", "Higher Certificate in Information Technology",
                "Higher Certificate in Business Management"));
        yearGroupField.setValue("Bachelor of Science in Information Technology");
        // Show different semesters
        semesterChoiceBox.getItems().clear();
        semesterChoiceBox.setItems(FXCollections.observableArrayList(
                "Semester 1"
                , "Semester 2"));
        // if after June
        if (date.getMonthValue() > 5) {
            semesterChoiceBox.setValue(Globals.SEMESTER2);
        } else {
            semesterChoiceBox.setValue(Globals.SEMESTER1);
        }
        
        loadTemplateBtn.setDisable(true);
        loadResultsBtn.setDisable(true);
        loadPrerequisitesBtn.setDisable(true);
        selectOutputFolderBtn.setDisable(true);
        calculateBtn.setDisable(true);
    }
    
    
}
