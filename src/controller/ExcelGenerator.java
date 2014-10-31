/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import model.Globals;
import model.Student;

/**
 *
 * @author UBOSHET
 */
public class ExcelGenerator {
    private WritableSheet sheet;
    private WritableWorkbook workbook;
    private String filePath;
    private Student student;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private Properties legendProperties;
    private static final String LOGO = "images/mgiLogo.png";
    private String[] postalAddress;
    int rowCount_pageBreakTemp = 0;

    public ExcelGenerator(Student student, String filePath) {

        this.student = student;
        this.filePath = filePath;

        try {
            // Prepare the properties object
            BufferedReader br = new BufferedReader(new FileReader(Globals.PROP_LEGEND_FILE));
            legendProperties = new Properties();
            legendProperties.load(br);

            this.filePath = this.filePath + "/Academic Transcript for "
                    + student.getSurname() + ", " + student.getName()
                    + "(Academic Transcript) - " + sf.format(new Date())
                    + ".xls";

            File progressReport = new File(this.filePath);

            // Set the file path for printing
            //student.setReportFilePath(this.filePath);

            WorkbookSettings ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            ws.setSuppressWarnings(true);
            //workbook = Workbook.createWorkbook(new File(this.filePath), ws);
            workbook = Workbook.createWorkbook(progressReport, ws);
            sheet = workbook.createSheet("Sheet1", 0);

            // Set the margins of the page to print correct format
            sheet.getSettings().setLeftMargin(0.33);
            sheet.getSettings().setRightMargin(0.33);
            sheet.getSettings().setBottomMargin(0.55);
            sheet.getSettings().setTopMargin(0.55);
            sheet.getSettings().setFooterMargin(0);

            //Create worksheet
            writeDataSheet();

            String progressReportPath = progressReport.getAbsolutePath();

            student.setReportFilePath(progressReportPath);
        } catch (IOException e) {
            Logger.getLogger(ExcelFormatter.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    private String writeDataSheet() {
        try {
            // Adjust the column sizes
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 13);
            sheet.setColumnView(2, 19);
            sheet.setColumnView(3, 19);
            sheet.setColumnView(4, 7);
            sheet.setColumnView(5, 16);

            // Generic label object that will be overwritten for each cell
            Label label;

            // Sets fonts for different sections of the transcript
            WritableFont calibri6 = new WritableFont(WritableFont.createFont("Calibri"), 6, WritableFont.NO_BOLD);
            WritableFont calibri8 = new WritableFont(WritableFont.createFont("Calibri"), 8, WritableFont.NO_BOLD);
            WritableFont calibri9 = new WritableFont(WritableFont.createFont("Calibri"), 9, WritableFont.NO_BOLD);
            WritableFont calibri9Bold = new WritableFont(WritableFont.createFont("Calibri"), 9, WritableFont.BOLD);
            WritableFont calibri10Bold = new WritableFont(WritableFont.createFont("Calibri"), 10, WritableFont.BOLD); 

            File imageFile = new File(LOGO);
            BufferedImage input = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(input,
                    "PNG", baos);

            sheet.addImage(
                    new WritableImage(6, 0, 2,
                    5, baos.toByteArray()));

            sheet.mergeCells(6, 5, 7, 5);
            WritableCellFormat cf = new WritableCellFormat(calibri6);
            cf.setAlignment(Alignment.CENTRE);
            label = new Label(6, 5, "DoE Reg. Cert. No. 2001/HE07/008", cf);
            sheet.addCell(label);

            int rowCount = 7;
            cf = new WritableCellFormat(calibri9Bold);
            label = new Label(0, rowCount, "Progress Report for: " + student.getEVisionNumber() + "  Ms " + student.getName() + " " + student.getSurname(), cf);
            sheet.addCell(label);

            rowCount = 10;
            cf = new WritableCellFormat(calibri9Bold);
            label = new Label(0, rowCount, "Ms " + student.getName() + " " + student.getSurname(), cf);
            sheet.addCell(label);

            rowCount = 11;
            cf = new WritableCellFormat(calibri8);
            /*
            if (student.getPostalAddress() != null
                    && !student.getPostalAddress().equals("")) {
                postalAddress = student.getPostalAddress().split("_");

                for (String addressLine : postalAddress) {
                    sheet.mergeCells(0, rowCount, 1, rowCount);

                    label = new Label(0, rowCount, addressLine, cf);
                    sheet.addCell(label);
                    rowCount++;
                }
            } else {
                // Students with no associated addresses are written to a log file
                Globals.openStudentAddressLog().write(student.getStudentNumber() + " - "
                        + student.getName() + " " + student.getSurname() + " (" + student.getCourse() + ")");
                Globals.useStudentAddressLog().newLine();
                Globals.useStudentAddressLog().flush();
            }
            */
            rowCount++;
            cf = new WritableCellFormat(calibri9Bold);
            label = new Label(5, rowCount,
                    "Date:",
                    cf);
            sheet.addCell(label);
            
            label = new Label(6, rowCount,
                    new SimpleDateFormat("EEEE dd MMMM yyyy").format(new Date()),
                    cf);
            sheet.addCell(label);
            
            rowCount++;
            cf = new WritableCellFormat(calibri9Bold);
            label = new Label(5, rowCount,
                    "Student No:",
                    cf);
            sheet.addCell(label);
            
            label = new Label(6, rowCount,
                    student.getEVisionNumber(),
                    cf);
            sheet.addCell(label);
            
            rowCount++;
            cf = new WritableCellFormat(calibri9Bold);
            label = new Label(5, rowCount,
                    "ID No:",
                    cf);
            sheet.addCell(label);
            
            label = new Label(6, rowCount,
                    "No ID Number Yet",
                    cf);
            sheet.addCell(label);
            
            rowCount++;
            cf = new WritableCellFormat(calibri9Bold);
            label = new Label(5, rowCount,
                    "PUK No::",
                    cf);
            sheet.addCell(label);
            
            label = new Label(6, rowCount,
                    "No PUK Number Yet",
                    cf);
            sheet.addCell(label);
            workbook.write();
            workbook.close();

        } catch (RowsExceededException ex) {
            Logger.getLogger(ExcelFormatter.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException | IOException ex) {
            Logger.getLogger(ExcelFormatter.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return this.filePath;
    }

    /**
     * Checks if the list will break onto the next page. If it will then a page
     * break is inserted.
     *
     * @param listSize
     * @param rowCount
     * @param tempRowCount
     * @return
     */
    private int addPageBreak(int listSize, int rowCount, int tempRowCount) {

        if (tempRowCount + listSize >= 40) {
            sheet.addRowPageBreak(rowCount);

            tempRowCount = 0;
        }

        return tempRowCount;
    }

    /**
     * Draws a specified border on specified rows and columns
     *
     * @param startCol
     * @param startRow
     * @param endCol
     * @param endRow
     * @param borderType
     * @param borderStyle
     * @param font
     * @throws WriteException
     */
    private void drawBorder(int startCol, int startRow, int endCol, int endRow,
            Border borderType, BorderLineStyle borderStyle)
            throws WriteException {

        // Merge next row and set border on bottom line
        sheet.mergeCells(startCol, startRow, endCol, endRow);
        WritableCellFormat cf = new WritableCellFormat();
        cf.setBorder(borderType, borderStyle);
        Label label = new Label(startCol, startRow, "", cf);
        sheet.addCell(label);
    }

    private void setPrintTitles(int lastRow) {
        // Set the heading of each printout
        sheet.getSettings().setPrintTitles(0, lastRow, 0, 5);
    }

    public String getFile() {
        return this.filePath;
    }
}
