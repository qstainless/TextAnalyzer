/*
 * @author Guillermo Castaneda Echegaray
 * @version 1.0
 * @course CEN 3024C-27021 Software Development I
 * @instructor Dr. Lisa Macon
 * GUI for Text analyzer.
 */
package com.gce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/TextAnalyzerUI.fxml"));
            primaryStage.setTitle("Text Analyzer");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
