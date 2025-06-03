package com.example.deliveryapp.controller;

import com.example.deliveryapp.entities.Livreur;
import com.example.deliveryapp.service.ILivreurService;
import com.example.deliveryapp.service.LivreurServiceImpl;
import com.example.deliveryapp.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Livreur management
 */
public class LivreurController implements Initializable {
    
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telephoneField;
    
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button cancelButton;
    @FXML private Button refreshButton;
    @FXML private Button deleteButton;
    
    @FXML private TableView<Livreur> livreurTable;
    @FXML private TableColumn<Livreur, Integer> idColumn;
    @FXML private TableColumn<Livreur, String> nomColumn;
    @FXML private TableColumn<Livreur, String> prenomColumn;
    @FXML private TableColumn<Livreur, String> telephoneColumn;
    
    private ILivreurService livreurService;
    private ObservableList<Livreur> livreurList;
    private Livreur selectedLivreur;
    private boolean isEditMode = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize service
        livreurService = new LivreurServiceImpl();
        
        // Initialize table
        setupTable();
        
        // Load data
        loadLivreurs();
        
        // Setup form state
        setupFormState();
        
        // Setup event handlers
        setupEventHandlers();
    }
    
    /**
     * Setup table columns and properties
     */
    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        
        livreurList = FXCollections.observableArrayList();
        livreurTable.setItems(livreurList);
        
        // Make table responsive
        livreurTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    /**
     * Setup form initial state
     */
    private void setupFormState() {
        updateButton.setDisable(true);
        setEditMode(false);
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Table selection
        livreurTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedLivreur = newValue;
                if (newValue != null) {
                    populateForm(newValue);
                    updateButton.setDisable(false);
                    deleteButton.setDisable(false);
                } else {
                    clearForm();
                    updateButton.setDisable(true);
                    deleteButton.setDisable(true);
                }
            }
        );
        
        // Double-click to edit
        livreurTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectedLivreur != null) {
                setEditMode(true);
            }
        });
    }
    
    /**
     * Load all livreurs from database
     */
    private void loadLivreurs() {
        try {
            livreurList.clear();
            livreurList.addAll(livreurService.getAllLivreurs());
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Chargement",
                "Impossible de charger les livreurs",
                "Une erreur s'est produite lors du chargement des données.",
                e
            );
        }
    }
    
    /**
     * Populate form with livreur data
     */
    private void populateForm(Livreur livreur) {
        nomField.setText(livreur.getNom());
        prenomField.setText(livreur.getPrenom());
        telephoneField.setText(livreur.getTelephone());
    }
    
    /**
     * Clear form fields
     */
    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
        selectedLivreur = null;
        setEditMode(false);
    }
    
    /**
     * Set edit mode state
     */
    private void setEditMode(boolean editMode) {
        isEditMode = editMode;
        addButton.setDisable(editMode);
        updateButton.setDisable(!editMode || selectedLivreur == null);
        
        if (editMode) {
            nomField.requestFocus();
        }
    }
    
    /**
     * Create livreur from form data
     */
    private Livreur createLivreurFromForm() {
        if (isEditMode && selectedLivreur != null) {
            return new Livreur(
                selectedLivreur.getId(),
                nomField.getText().trim(),
                prenomField.getText().trim(),
                telephoneField.getText().trim()
            );
        } else {
            return new Livreur(
                nomField.getText().trim(),
                prenomField.getText().trim(),
                telephoneField.getText().trim()
            );
        }
    }
    
    /**
     * Validate form data
     */
    private boolean validateForm() {
        if (nomField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Validation", "Champ requis", "Le nom est obligatoire.");
            nomField.requestFocus();
            return false;
        }
        
        if (prenomField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Validation", "Champ requis", "Le prénom est obligatoire.");
            prenomField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // ===== EVENT HANDLERS =====
    
    @FXML
    private void handleAddLivreur() {
        if (!validateForm()) return;
        
        try {
            Livreur newLivreur = createLivreurFromForm();
            livreurService.addLivreur(newLivreur);
            
            AlertUtils.showSuccess("Succès", "Livreur ajouté avec succès.");
            loadLivreurs();
            clearForm();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur d'Ajout",
                "Impossible d'ajouter le livreur",
                e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleUpdateLivreur() {
        if (!validateForm() || selectedLivreur == null) return;
        
        try {
            Livreur updatedLivreur = createLivreurFromForm();
            livreurService.updateLivreur(updatedLivreur);
            
            AlertUtils.showSuccess("Succès", "Livreur modifié avec succès.");
            loadLivreurs();
            clearForm();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Modification",
                "Impossible de modifier le livreur",
                e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleDeleteLivreur() {
        if (selectedLivreur == null) {
            AlertUtils.showWarning("Sélection", "Aucun livreur sélectionné", "Veuillez sélectionner un livreur à supprimer.");
            return;
        }
        
        if (!AlertUtils.showDeleteConfirmation("livreur \"" + selectedLivreur.getFullName() + "\"")) {
            return;
        }
        
        try {
            livreurService.deleteLivreur(selectedLivreur.getId());
            
            AlertUtils.showSuccess("Succès", "Livreur supprimé avec succès.");
            loadLivreurs();
            clearForm();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Suppression",
                "Impossible de supprimer le livreur",
                "Une erreur s'est produite lors de la suppression.\n" +
                "Le livreur peut avoir des colis associés.",
                e
            );
        }
    }
    
    @FXML
    private void handleCancel() {
        clearForm();
    }
    
    @FXML
    private void handleRefresh() {
        loadLivreurs();
        clearForm();
        AlertUtils.showInfo("Actualisation", "Données actualisées", "La liste des livreurs a été rechargée.");
    }
    
    /**
     * Get all livreurs for external use (e.g., ComboBox)
     */
    public ObservableList<Livreur> getLivreurList() {
        return livreurList;
    }
}