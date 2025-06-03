package com.example.deliveryapp.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX Application class
 */
public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/MainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            
            // Load CSS stylesheet
            scene.getStylesheets().add(MainApp.class.getResource("/css/styles.css").toExternalForm());
            
            stage.setTitle("Gestion de Livraison de Colis");
            stage.setScene(scene);
            stage.setMinWidth(1000);
            stage.setMinHeight(700);
            
            // Set application icon (optional)
            try {
                Image icon = new Image(MainApp.class.getResourceAsStream("/images/app-icon.png"));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                // Icon not found, continue without it
                System.out.println("Application icon not found, continuing without icon.");
            }
            
            stage.show();
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public static void main(String[] args) {
        launch();
    }
}