package com.example.deliveryapp.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Utility class for showing JavaFX alert dialogs
 */
public class AlertUtils {
    
    /**
     * Show an information alert
     * @param title Dialog title
     * @param header Header text
     * @param content Content text
     */
    public static void showInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Show a success alert
     * @param title Dialog title
     * @param content Content text
     */
    public static void showSuccess(String title, String content) {
        showInfo(title, "Succès", content);
    }
    
    /**
     * Show an error alert
     * @param title Dialog title
     * @param header Header text
     * @param content Content text
     */
    public static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Show an error alert with exception details
     * @param title Dialog title
     * @param header Header text
     * @param content Content text
     * @param exception Exception to display
     */
    public static void showError(String title, String header, String content, Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();
        
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);
        
        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
    
    /**
     * Show a simple error alert
     * @param content Error message
     */
    public static void showError(String content) {
        showError("Erreur", "Une erreur s'est produite", content);
    }
    
    /**
     * Show a warning alert
     * @param title Dialog title
     * @param header Header text
     * @param content Content text
     */
    public static void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Show a confirmation dialog
     * @param title Dialog title
     * @param header Header text
     * @param content Content text
     * @return true if user clicked OK/Yes, false otherwise
     */
    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Show a simple confirmation dialog
     * @param content Confirmation message
     * @return true if user clicked OK/Yes, false otherwise
     */
    public static boolean showConfirmation(String content) {
        return showConfirmation("Confirmation", "Veuillez confirmer", content);
    }
    
    /**
     * Show delete confirmation dialog
     * @param itemName Name of the item to delete
     * @return true if user confirms deletion
     */
    public static boolean showDeleteConfirmation(String itemName) {
        return showConfirmation("Confirmer la suppression", 
                              "Suppression de " + itemName, 
                              "Êtes-vous sûr de vouloir supprimer cet élément ?\nCette action est irréversible.");
    }
}