/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import model.Globals;

/**
 *
 * @author UBOSHET
 */
public class ExcelReader {
    
    
    private Workbook workbook;
    private final File excelFile;
    private Sheet sheet;
    
    public ExcelReader() {
        excelFile = new File(Globals.DEFAULT_REGISTERED_STUDENT_FILE_LOCATION);
    }
    
    public ExcelReader(File excelFile) {
        this.excelFile = excelFile;
    }
    
    /**
     * This method opens the Workbook so that you can start reading values from it
     * It will return false if there is no work book at the file location.
     * @return if the excel file exists in the specified location and can be read from
     * @throws java.io.IOException
     * @throws jxl.read.biff.BiffException
     */
    public Boolean openWorkBook() throws IOException, BiffException {
        workbook = Workbook.getWorkbook(excelFile);
        sheet = workbook.getSheet(0);
        return excelFile.canRead();
    }
    
    /**
     * This method reads a single value contained within the Cell and returns it
     * as a String
     * @param column where the cell is
     * @param row where the cell is
     * @return the value within the cell in column and row as String
     */
    public String readCellValue(int column, int row) {
        Cell cell = sheet.getCell(column, row);
        String cellValue = cell.getContents();
        return cellValue.trim();
    }
    
    /**
     * Gets the total number of rows in the workbook
     * @return total number of rows in the workbook
     */
    public int getTotalRows() {
        return sheet.getRows();
    }
    
    public int getTotalColumns() {
        return sheet.getColumns();
    }
    
    /**
     * Closes the workbook
     */
    public void closeWorkBook() {
        workbook.close();
    }
    
}
