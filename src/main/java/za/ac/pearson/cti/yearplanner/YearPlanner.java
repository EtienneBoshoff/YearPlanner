/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.pearson.cti.yearplanner;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author UBOSHET
 */
public class YearPlanner extends Application {

    private static Stage primaryStage;
    private AnchorPane yearPlannerLayout;
    
    @Override
    public void start(Stage primaryStage) {
        
        YearPlanner.primaryStage = primaryStage;
        YearPlanner.primaryStage.setTitle("Year Planner");
        
        initRootLayout();
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void initRootLayout() {
        
        try {
            // Load layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            // search from the class path root so start with / otherwise it will
            // search from where current package
            loader.setLocation(getClass().getResource("/fxml/YearPlanner.fxml"));

            yearPlannerLayout = (AnchorPane) loader.load();
            
            Scene scene = new Scene(yearPlannerLayout);
            primaryStage.getIcons().add(new Image(getClass().getResource("/images/planner.png").toString()));
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            Logger.getLogger(YearPlanner.class.getName()).log(Level.SEVERE,null , e);
        }
        
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
}
