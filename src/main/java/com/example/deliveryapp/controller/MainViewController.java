package com.example.deliveryapp.controller;

import com.example.deliveryapp.util.AlertUtils;
import com.example.deliveryapp.util.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller for the application
 */
public class MainViewController implements Initializable {
    
    @FXML
    private TabPane mainTabPane;
    
    @FXML
    private Label statusLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Test database connection on startup
        testDatabaseConnection();
        
        // Set initial status
        updateStatus("Application initialisée - Prêt");
    }
    
    /**
     * Test database connection and show status
     */
    private void testDatabaseConnection() {
        try {
            if (DatabaseManager.testConnection()) {
                updateStatus("Connexion à la base de données établie");
            } else {
                updateStatus("Erreur de connexion à la base de données");
                AlertUtils.showWarning(
                    "Connexion Base de Données",
                    "Problème de connexion",
                    "Impossible de se connecter à la base de données MySQL.\n" +
                    "Veuillez vérifier que MySQL est en cours d'exécution et que la base de données 'delivery_app' existe.\n\n" +
                    "Instructions:\n" +
                    "1. Démarrez MySQL\n" +
                    "2. Créez la base de données: CREATE DATABASE delivery_app;\n" +
                    "3. Exécutez les scripts de création des tables\n" +
                    "4. Vérifiez les paramètres de connexion dans DatabaseManager.java"
                );
            }
        } catch (Exception e) {
            updateStatus("Erreur de connexion à la base de données");
            AlertUtils.showError(
                "Erreur de Connexion",
                "Erreur lors du test de connexion",
                "Une erreur s'est produite lors du test de connexion à la base de données.",
                e
            );
        }
    }
    
    /**
     * Update status label
     * @param message Status message
     */
    public void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
    
    /**
     * Get the main tab pane
     * @return TabPane
     */
    public TabPane getMainTabPane() {
        return mainTabPane;
    }
}