package gce.textanalyzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This TextAnalyzer program implements an application that reads a file from
 * a given URL and outputs statistics about the words found in that file. It
 * outputs the total number of words found in the file and the frequencies of
 * unique words, sorted by the most frequently used word in descending order.
 * <p>
 * Course: CEN 3024C-27021 Software Development I
 * Instructor: Dr. Lisa Macon
 *
 * @author Guillermo Castaneda Echegaray
 * @version 1.10
 * @since 2020-01-11
 */
public class TextAnalyzer extends Application {

    /**
     * Entry point of the {@code TextAnalyzer} application.
     *
     * @param args the input arguments
     */
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
            System.out.println("It seems that JavaFX is not properly installed in your system.\n\n" +
                    "Program cannot continue. Exiting.");
        }
    }
}
