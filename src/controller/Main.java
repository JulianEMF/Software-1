/**
 * Author: Julian Montoya Franco
 * Student ID: 001364434
 * email: jmon301@wgu.edu
 * The Javadocs are located in the folder "javadocs"
 */

/** This application manages an inventory system that allows to create parts and products that can be associated. */
/**
 FUTURE ENHANCEMENT:
 One possible enhancement is to add mathematical operations to the application. One that can check how many parts need to be ordered based on how many parts each product needs.
 As well as finding the total sum of prices, parts in stock, etc.
 */

package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** The main class is the entry point for the application */
public class Main extends Application {

    /** This method creates the primary stage and brings the Main Screen, the first scene
     * @param primaryStage
     * */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
        primaryStage.setTitle("Main Form");
        primaryStage.setScene(new Scene(root, 950, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
