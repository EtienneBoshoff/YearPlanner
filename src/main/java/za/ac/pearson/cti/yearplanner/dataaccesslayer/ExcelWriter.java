/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.pearson.cti.yearplanner.dataaccesslayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import za.ac.pearson.cti.yearplanner.model.Globals;
import za.ac.pearson.cti.yearplanner.model.Module;
import za.ac.pearson.cti.yearplanner.model.Student;

/**
 *
 * @author UBOSHET
 */
public class ExcelWriter {
    
    private final File template;
    
    private File excelFile;

    public ExcelWriter(File template, File outputFile) {
        this.template = template;
        this.excelFile = outputFile;
    }
    
    public ExcelWriter(File template, String absolutePathOfOutputFile) {
        this.template = template;
        this.excelFile = new File(absolutePathOfOutputFile);
    }

    public void setExcelFile(File newFile) {
        this.excelFile = newFile;
    }
    
    public boolean CreateNewExcelFile() throws IOException, WriteException {
        if (this.excelFile.exists()) {
            return false;
        }
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setLocale(Locale.ENGLISH);
        WritableWorkbook workbook = Workbook.createWorkbook(excelFile, workbookSettings);
        workbook.createSheet("Year Planner", 0);
        workbook.close();
        return true;
    }
    
    public boolean CreateExcelFileFromTemplate() throws IOException {
        if (this.excelFile.exists()) {
            return false;
        }
        Files.copy(this.template.toPath(), this.excelFile.toPath(), COPY_ATTRIBUTES);
        return true;
    }

    public void FillInStudentDetails(Student currentSelectedStudent) throws IOException, WriteException, BiffException {
        Workbook workbook = Workbook.getWorkbook(excelFile.getAbsoluteFile());
        WritableWorkbook copy = Workbook.createWorkbook(excelFile.getAbsoluteFile(), workbook);
        WritableSheet sheet = copy.getSheet(0);
        // Set font
        WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Calibri"), 18, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat cellFormat = new WritableCellFormat(wfontStatus);
        cellFormat.setWrap(true);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        // Surname
        Label textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SURNAME_ROW,  
                        currentSelectedStudent.getSurname(), cellFormat ); // Column | Row
        WritableCell cellToFill = (WritableCell) textToEnter;
        // Name
        textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.NAME_ROW, 
                        currentSelectedStudent.getName(), cellFormat);
        WritableCell nameCell = (WritableCell) textToEnter;
        // phone
        textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.PHONE_ROW, 
                        currentSelectedStudent.getCellNumber(), cellFormat);
        WritableCell phoneCell = (WritableCell) textToEnter;
        // student number
        textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.NUMBER_ROW, 
                        currentSelectedStudent.getStudentNumber(), cellFormat);
        WritableCell studentNumberCell = (WritableCell) textToEnter;
        // student email
        textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.NAME_ROW, 
                        currentSelectedStudent.getEmailAddress(), cellFormat);
        WritableCell studentEmail = (WritableCell) textToEnter;
        // Sponsor Name
        textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SPONSOR, 
                        currentSelectedStudent.getSponsor().getName(), cellFormat);
        WritableCell sponsorCell = (WritableCell) textToEnter;
        // Sponsor number
        textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SPONSOR_PHONE, 
                        currentSelectedStudent.getSponsor().getCellNumber(), cellFormat);
        WritableCell sponsorCellNumber = (WritableCell) textToEnter;
        // Sponsor email
        textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SPONSOR_EMAIL, 
                        currentSelectedStudent.getSponsor().getEmailAdress(), cellFormat);
        WritableCell sponsorEmail = (WritableCell) textToEnter;
        
        // Add to sheet
        sheet.addCell(sponsorEmail);
        sheet.addCell(studentEmail);
        sheet.addCell(sponsorCellNumber);
        sheet.addCell(sponsorCell);
        sheet.addCell(studentNumberCell);
        sheet.addCell(phoneCell);
        sheet.addCell(cellToFill);
        sheet.addCell(nameCell);
        
        // Write and close all
        copy.write();
        workbook.close();
        copy.close();
    }

    // TODO: Remove the column row and replace with module code implementation
    public void WritePassSymbolAt(Integer column, Integer row) throws IOException, BiffException, WriteException {
        Workbook workbook = Workbook.getWorkbook(excelFile.getAbsoluteFile());
        WritableWorkbook copy = Workbook.createWorkbook(excelFile.getAbsoluteFile(), workbook);
        WritableSheet sheet = copy.getSheet(0);
        // Set font
        WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Calibri"), 18, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat cellFormat = new WritableCellFormat(wfontStatus);
        cellFormat.setWrap(true);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        Label textToEnter = new Label(column, row,  
                        "P", cellFormat ); // Column | Row
        WritableCell cellToFill = (WritableCell) textToEnter;
        sheet.addCell(cellToFill);
        workbook.close();
        copy.write();
        copy.close();
    }

    public void WriteREDOSymbolAt(int column, int row) throws IOException, BiffException, WriteException {
        Workbook workbook = Workbook.getWorkbook(excelFile.getAbsoluteFile());
        WritableWorkbook copy = Workbook.createWorkbook(excelFile.getAbsoluteFile(), workbook);
        WritableSheet sheet = copy.getSheet(0);
        // Set font
        WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Calibri"), 18, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat cellFormat = new WritableCellFormat(wfontStatus);
        cellFormat.setWrap(true);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        Label textToEnter = new Label(column, row,  
                        "REDO", cellFormat ); // Column | Row
        WritableCell cellToFill = (WritableCell) textToEnter;
        sheet.addCell(cellToFill);
        workbook.close();
        copy.write();
        copy.close();
    }

    public void WriteToBeCompletedSymbolAt(int column, int row) throws IOException, BiffException, WriteException {
        Workbook workbook = Workbook.getWorkbook(excelFile.getAbsoluteFile());
        WritableWorkbook copy = Workbook.createWorkbook(excelFile.getAbsoluteFile(), workbook);
        WritableSheet sheet = copy.getSheet(0);
        // Set font
        WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Calibri"), 18, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat cellFormat = new WritableCellFormat(wfontStatus);
        cellFormat.setWrap(true);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        Label textToEnter = new Label(column, row,  
                        "X", cellFormat ); // Column | Row
        WritableCell cellToFill = (WritableCell) textToEnter;
        sheet.addCell(cellToFill);
        workbook.close();
        copy.write();
        copy.close();
    }

    public void WriteYearPlan(Student currentSelectedStudent) {
        if (this.excelFile.exists()) {
            return;
        }
        try {

            Files.copy(this.template.toPath(), this.excelFile.toPath(), COPY_ATTRIBUTES);
            
            Workbook workbook = Workbook.getWorkbook(excelFile.getAbsoluteFile());
            WritableWorkbook copy = Workbook.createWorkbook(excelFile.getAbsoluteFile(), workbook);
            WritableSheet sheet = copy.getSheet(0);
            // Set font
            WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Calibri"), 18, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat cellFormat = new WritableCellFormat(wfontStatus);
            cellFormat.setWrap(true);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            
            // Surname
            Label textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SURNAME_ROW,
                    currentSelectedStudent.getSurname(), cellFormat ); // Column | Row
            WritableCell cellToFill = (WritableCell) textToEnter;
            // Name
            textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.NAME_ROW,
                    currentSelectedStudent.getName(), cellFormat);
            WritableCell nameCell = (WritableCell) textToEnter;
            // phone
            textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.PHONE_ROW,
                    currentSelectedStudent.getCellNumber(), cellFormat);
            WritableCell phoneCell = (WritableCell) textToEnter;
            // student number
            textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.NUMBER_ROW,
                    currentSelectedStudent.getStudentNumber(), cellFormat);
            WritableCell studentNumberCell = (WritableCell) textToEnter;
            // student email
            textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.NAME_ROW,
                    currentSelectedStudent.getEmailAddress(), cellFormat);
            WritableCell studentEmail = (WritableCell) textToEnter;
            // Sponsor Name
            textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SPONSOR,
                    currentSelectedStudent.getSponsor().getName(), cellFormat);
            WritableCell sponsorCell = (WritableCell) textToEnter;
            // Sponsor number
            textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SPONSOR_PHONE,
                    currentSelectedStudent.getSponsor().getCellNumber(), cellFormat);
            WritableCell sponsorCellNumber = (WritableCell) textToEnter;
            // Sponsor email
            textToEnter = new Label(Globals.DETAILS_COLUMN, Globals.SPONSOR_EMAIL,
                    currentSelectedStudent.getSponsor().getEmailAdress(), cellFormat);
            WritableCell sponsorEmail = (WritableCell) textToEnter;
            
            for (Module module : currentSelectedStudent.getModules()) {
                if (module.getSemester().equals("Semester 1")) {
                    Label moduleText = new Label(Globals.TEMPLATE_SEMESTER1_COLUMN, module.getTemplateRow(),  
                        module.getStatus(), cellFormat ); // Column | Row
                    WritableCell moduleCellToFill = (WritableCell) moduleText;
                    sheet.addCell(moduleCellToFill);
                } else {
                    if (module.getSemester().equals("Semester 2")) {
                        Label moduleText = new Label(Globals.TEMPLATE_SEMESTER2_COLUMN, module.getTemplateRow(),  
                            module.getStatus(), cellFormat ); // Column | Row
                        WritableCell moduleCellToFill = (WritableCell) moduleText;
                        sheet.addCell(moduleCellToFill);
                    }
                    else {
                        Label moduleText = new Label(Globals.YEAR_MODULE_COLUMN, module.getTemplateRow(),  
                            module.getStatus(), cellFormat ); // Column | Row
                        WritableCell moduleCellToFill = (WritableCell) moduleText;
                        sheet.addCell(moduleCellToFill);
                    }
                }
                
            }
            

            // Add to sheet
            sheet.addCell(sponsorEmail);
            sheet.addCell(studentEmail);
            sheet.addCell(sponsorCellNumber);
            sheet.addCell(sponsorCell);
            sheet.addCell(studentNumberCell);
            sheet.addCell(phoneCell);
            sheet.addCell(cellToFill);
            sheet.addCell(nameCell);
            
            // Write and close all
            copy.write();
            workbook.close();
            copy.close();
        } catch (IOException | WriteException | BiffException ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
