/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.pearson.cti.yearplanner.model;

import java.util.List;

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
    private boolean isYearModule;
    private String semester;
    private List<Module> preRequisites;
    private List<Module> coRequisites;

    /* ============== CONSTRUCTORS =========== */
    public Module(String module, String finalMark,
            String status, String semester) {
        this.moduleCode = module;
        this.finalMark = finalMark;
        this.status = status;
        this.isYearModule = (semester.equals("Y"));
        this.semester = semester;

    }

    public Module(String module) {
        this.moduleCode = module;
    }
/* ============== CONSTRUCTORS =========== */
    
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
    
    public String getModuleCode() {
        return this.moduleCode;
    }

    public String getFinalMark() {
        return this.finalMark;
    }

    public String getStatus() {
        return this.status;
    }

    /* Returns semester in String format of YEAR_OF_COURSE/SEMESTER_IT_TAKES_PLACE_IN */
    public String getSemester() {
        return Globals.COURSE_YEAR + "/"
                + (isYearModule ? "YEAR" : "SEM" + this.semester);
    }

    public boolean isYearModule() {
        return this.isYearModule;
    }

    @Override
    public String toString() {
        return ("\nModule: " + this.moduleCode + "\nFinal Mark: " + this.finalMark 
                + "\nStatus:" + this.status);
    }
    
}
