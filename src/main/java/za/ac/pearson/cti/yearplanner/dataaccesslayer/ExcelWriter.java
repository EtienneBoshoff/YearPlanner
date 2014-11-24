/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.pearson.cti.yearplanner.dataaccesslayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import java.util.Locale;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

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
    
    public boolean CreateExcelFileFromTemplate(String newFileName) throws IOException {
        this.excelFile = new File(this.excelFile.getAbsolutePath() + "\\" + newFileName);
        if (this.excelFile.exists()) {
            return false;
        }
        Files.copy(this.template.toPath(), this.excelFile.toPath(), COPY_ATTRIBUTES);
        System.out.println("\nCopied file to:" + excelFile.getAbsolutePath());
        return true;
    }
    
}
