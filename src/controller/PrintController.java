/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Desktop;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Student;

/**
 *
 * @author UBOSHET
 */
public class PrintController {
    public void printStudents(Student student) {
        try {
            Desktop.getDesktop().print(new File(student.getReportFilePath()));
        } catch (Exception ex) {
            Logger.getLogger(PrintController.class.getName()).log(Level.SEVERE, null, ex);

            System.out.println("Cannot print Student report");
        }
    }
}
