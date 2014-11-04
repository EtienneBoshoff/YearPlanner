/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author UBOSHET
 */
public class Student {
    
    private String eVisionNumber;
    private String studentNumber;
    private String name;
    private String surname;
    private String course;
    private ArrayList<Module> modules = new ArrayList<>();
    private String cellNumber;
    private String emailAddress;
    private Sponsor sponsor;
    private String advisor;

    public Student(String eVisionNumber, String studentNumber, String name, String surname, String course,
            String cellNumber, String emailAddress, Sponsor sponsor,
            String advisor, ArrayList<Module> modules) {
        this.eVisionNumber = eVisionNumber;
        this.studentNumber = studentNumber;
        this.name = name;
        this.surname = surname;
        this.course = course;

        this.modules = modules;
        this.cellNumber = cellNumber;
        this.emailAddress = emailAddress;
        this.sponsor = sponsor;
        this.advisor = advisor;
    }
    
    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.modules = new ArrayList<>();
    }
    
    public void addModule(Module module) {
        this.modules.add(module);
    }

    public void seteVisionNumber(String eVisionNumber) {
        this.eVisionNumber = eVisionNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getEVisionNumber() {
        return this.eVisionNumber;
    }
    public String getStudentNumber() {
        return this.studentNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getCourse() {
        return this.course;
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    @Override
    public String toString() {
        return ("\nNumber: " + this.studentNumber + "\nName: " + this.name + "\nSurname: "
                + this.surname + "\nModules:" + this.modules);
    }

}
