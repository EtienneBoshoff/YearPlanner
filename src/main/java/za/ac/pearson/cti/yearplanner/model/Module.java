/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.pearson.cti.yearplanner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * An important note on prerequisites and co-requisites
 * 
 * PREREQUISITES -> Required that the student completed this module before
 *                  student is allowed to attempt current module
 * 
 * COREQUISITES -> Required that student has attempted this module before 
 *                 student is allowed to attempt current module
 * 
 * BOTH ARE LOADED FROM THE PREREQUISITE AND COREQUISITE EXCEL FILE
 */
public class Module {
    
    private final String moduleCode;
    private String finalMark;
    private String status;
    private String semester;
    private String examMark;
    private List<Module> preRequisites;
    private List<Module> coRequisites;
    private int templateRow;

    /* ============== CONSTRUCTORS =========== */
    public Module(String module, String finalMark,
            String status) {
        this.moduleCode = module;
        this.finalMark = finalMark;
        this.status = status;
        this.semester = "";
        this.preRequisites = new ArrayList<>();
        this.coRequisites = new ArrayList<>();
    }

    public Module(String module) {
        this.moduleCode = module;
        this.preRequisites = new ArrayList<>();
        this.coRequisites = new ArrayList<>();
        this.semester = "";
    }
/* ============== CONSTRUCTORS =========== */
    
    public List<Module> getCoRequisitesList() {
        return coRequisites;
    }
    
    public void setTemplateRow(Integer templateRow) {
        this.templateRow = templateRow;
    }
    
    public void setFinalMark(String finalMark) {
        this.finalMark = finalMark;
    }
    
    public void setExamMark(String examMark) {
        this.examMark = examMark;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public void setPreRequisites(List<Module> preRequisites) {
        this.preRequisites = preRequisites;
    }
    
    public void setCoRequisites(List<Module> coRequisites) {
        this.coRequisites = coRequisites;
    }
    
    public void addPreRequisite(Module preRequisiteModule) {
        preRequisites.add(preRequisiteModule);
    }
    
    public void addCoRequisite(Module coRequisiteModule) {
        coRequisites.add(coRequisiteModule);
    }
    
    public String getExamMark() {
        return examMark;
    }
    
    public String getModuleCode() {
        return this.moduleCode;
    }

    public String getFinalMark() {
        return this.finalMark;
    }

    public String getStatus() {
        return this.status;
    }
    
    public int getTemplateRow() {
        return templateRow;
    }

    /* Returns semester in String format of YEAR_OF_COURSE/SEMESTER_IT_TAKES_PLACE_IN */
    public String getSemester() {
        return semester;
    }
    
    public List<Module> getPreRequisitesList() {
        return preRequisites;
    }
    
    public String getCoRequisites() {
        String resultString = "";
        resultString = coRequisites.stream()
                .filter((module) -> (module.getModuleCode() != null))
                .map((module) -> module.getModuleCode() + " ")
                .reduce(resultString, String::concat);
        return resultString;
    }

    @Override
    public String toString() {
        return ("\nModule: " + this.moduleCode + "\nFinal Mark: " + this.finalMark 
                + "\nStatus:" + this.status);
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Module other = (Module) obj;
        if (!Objects.equals(this.moduleCode, other.moduleCode)) {
            return false;
        }
        return true;
    }
    


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.moduleCode);
        return hash;
    }


    
}
