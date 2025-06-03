package com.example.deliveryapp.controller;

import com.example.deliveryapp.entities.Colis;
import com.example.deliveryapp.entities.Livreur;
import com.example.deliveryapp.service.IColisService;
import com.example.deliveryapp.service.ColisServiceImpl;
import com.example.deliveryapp.service.ILivreurService;
import com.example.deliveryapp.service.LivreurServiceImpl;
import com.example.deliveryapp.util.AlertUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for Colis management
 */
public class ColisController implements Initializable {
    
    @FXML private TextField destinataireField;
    @FXML private TextArea adresseField;
    @FXML private DatePicker dateEnvoiPicker;
    @FXML private ComboBox<Livreur> livreurComboBox;
    
    @FXML private Button addColisButton;
    @FXML private Button updateColisButton;
    @FXML private Button cancelColisButton;
    @FXML private Button refreshColisButton;
    @FXML private Button deleteColisButton;
    @FXML private Button assignButton;
    @FXML private Button markDeliveredButton;
    @FXML private Button exportButton;
    
    @FXML private TableView<Colis> colisTable;
    @FXML private TableColumn<Colis, Integer> colisIdColumn;
    @FXML private TableColumn<Colis, String> destinataireColumn;
    @FXML private TableColumn<Colis, String> adresseColumn;
    @FXML private TableColumn<Colis, LocalDate> dateEnvoiColumn;
    @FXML private TableColumn<Colis, String> statutColumn;
    @FXML private TableColumn<Colis, LocalDate> dateLivraisonColumn;
    @FXML private TableColumn<Colis, String> livreurColumn;
    
    private IColisService colisService;
    private ILivreurService livreurService;
    private ObservableList<Colis> colisList;
    private ObservableList<Livreur> livreurList;
    private Colis selectedColis;
    private boolean isEditMode = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize services
        colisService = new ColisServiceImpl();
        livreurService = new LivreurServiceImpl();
        
        // Initialize table and form
        setupTable();
        setupForm();
        
        // Load data
        loadData();
        
        // Setup form state
        setupFormState();
        
        // Setup event handlers
        setupEventHandlers();
    }
    
    /**
     * Setup table columns and properties
     */
    private void setupTable() {
        colisIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        destinataireColumn.setCellValueFactory(new PropertyValueFactory<>("destinataire"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        dateEnvoiColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnvoi"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statutText"));
        dateLivraisonColumn.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        livreurColumn.setCellValueFactory(new PropertyValueFactory<>("livreurName"));
        
        // Format date columns
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateEnvoiColumn.setCellFactory(column -> new TableCell<Colis, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });
        
        dateLivraisonColumn.setCellFactory(column -> new TableCell<Colis, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText("");
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });
        
        // Style status column
        statutColumn.setCellFactory(column -> new TableCell<Colis, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("Livré".equals(status)) {
                        setStyle("-fx-text-fill: #198754; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        colisList = FXCollections.observableArrayList();
        colisTable.setItems(colisList);
        
        // Make table responsive
        colisTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    /**
     * Setup form components
     */
    private void setupForm() {
        // Setup date picker
        dateEnvoiPicker.setValue(LocalDate.now());
        
        // Setup ComboBox
        livreurList = FXCollections.observableArrayList();
        livreurComboBox.setItems(livreurList);
        livreurComboBox.setConverter(new StringConverter<Livreur>() {
            @Override
            public String toString(Livreur livreur) {
                return livreur != null ? livreur.getFullName() : "";
            }
            
            @Override
            public Livreur fromString(String string) {
                return null; // Not needed for display-only ComboBox
            }
        });
        
        // Add placeholder for empty selection
        livreurComboBox.setPromptText("Sélectionnez un livreur (optionnel)");
    }
    
    /**
     * Load all data
     */
    private void loadData() {
        loadColis();
        loadLivreurs();
    }
    
    /**
     * Load all colis from database
     */
    private void loadColis() {
        try {
            colisList.clear();
            colisList.addAll(colisService.getAllColis());
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Chargement",
                "Impossible de charger les colis",
                "Une erreur s'est produite lors du chargement des données.",
                e
            );
        }
    }
    
    /**
     * Load all livreurs for ComboBox
     */
    private void loadLivreurs() {
        try {
            livreurList.clear();
            livreurList.addAll(livreurService.getAllLivreurs());
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Chargement",
                "Impossible de charger les livreurs",
                "Une erreur s'est produite lors du chargement des livreurs.",
                e
            );
        }
    }
    
    /**
     * Setup form initial state
     */
    private void setupFormState() {
        updateColisButton.setDisable(true);
        setEditMode(false);
        updateActionButtonsState();
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Table selection
        colisTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedColis = newValue;
                if (newValue != null) {
                    populateForm(newValue);
                    updateColisButton.setDisable(false);
                } else {
                    clearForm();
                    updateColisButton.setDisable(true);
                }
                updateActionButtonsState();
            }
        );
        
        // Double-click to edit
        colisTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectedColis != null) {
                setEditMode(true);
            }
        });
    }
    
    /**
     * Update action buttons state based on selection
     */
    private void updateActionButtonsState() {
        boolean hasSelection = selectedColis != null;
        boolean isDelivered = hasSelection && selectedColis.isLivre();
        
        assignButton.setDisable(!hasSelection || isDelivered);
        markDeliveredButton.setDisable(!hasSelection || isDelivered);
        deleteColisButton.setDisable(!hasSelection);
    }
    
    /**
     * Populate form with colis data
     */
    private void populateForm(Colis colis) {
        destinataireField.setText(colis.getDestinataire());
        adresseField.setText(colis.getAdresse());
        dateEnvoiPicker.setValue(colis.getDateEnvoi());
        
        // Set livreur in ComboBox
        if (colis.getLivreur() != null) {
            livreurComboBox.setValue(colis.getLivreur());
        } else {
            livreurComboBox.setValue(null);
        }
    }
    
    /**
     * Clear form fields
     */
    private void clearForm() {
        destinataireField.clear();
        adresseField.clear();
        dateEnvoiPicker.setValue(LocalDate.now());
        livreurComboBox.setValue(null);
        selectedColis = null;
        setEditMode(false);
    }
    
    /**
     * Set edit mode state
     */
    private void setEditMode(boolean editMode) {
        isEditMode = editMode;
        addColisButton.setDisable(editMode);
        updateColisButton.setDisable(!editMode || selectedColis == null);
        
        if (editMode) {
            destinataireField.requestFocus();
        }
    }
    
    /**
     * Create colis from form data
     */
    private Colis createColisFromForm() {
        Colis colis;
        if (isEditMode && selectedColis != null) {
            colis = new Colis(
                selectedColis.getId(),
                destinataireField.getText().trim(),
                adresseField.getText().trim(),
                dateEnvoiPicker.getValue(),
                selectedColis.isLivre(),
                selectedColis.getDateLivraison(),
                selectedColis.getLivreurId()
            );
        } else {
            colis = new Colis(
                destinataireField.getText().trim(),
                adresseField.getText().trim(),
                dateEnvoiPicker.getValue()
            );
        }
        
        // Set livreur if selected
        Livreur selectedLivreur = livreurComboBox.getValue();
        if (selectedLivreur != null) {
            colis.setLivreur(selectedLivreur);
        }
        
        return colis;
    }
    
    /**
     * Validate form data
     */
    private boolean validateForm() {
        if (destinataireField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Validation", "Champ requis", "Le destinataire est obligatoire.");
            destinataireField.requestFocus();
            return false;
        }
        
        if (adresseField.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Validation", "Champ requis", "L'adresse est obligatoire.");
            adresseField.requestFocus();
            return false;
        }
        
        if (dateEnvoiPicker.getValue() == null) {
            AlertUtils.showWarning("Validation", "Champ requis", "La date d'envoi est obligatoire.");
            dateEnvoiPicker.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // ===== EVENT HANDLERS =====
    
    @FXML
    private void handleAddColis() {
        if (!validateForm()) return;
        
        try {
            Colis newColis = createColisFromForm();
            colisService.addColis(newColis);
            
            AlertUtils.showSuccess("Succès", "Colis ajouté avec succès.");
            loadColis();
            clearForm();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur d'Ajout",
                "Impossible d'ajouter le colis",
                e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleUpdateColis() {
        if (!validateForm() || selectedColis == null) return;
        
        try {
            Colis updatedColis = createColisFromForm();
            colisService.updateColis(updatedColis);
            
            AlertUtils.showSuccess("Succès", "Colis modifié avec succès.");
            loadColis();
            clearForm();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Modification",
                "Impossible de modifier le colis",
                e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleDeleteColis() {
        if (selectedColis == null) {
            AlertUtils.showWarning("Sélection", "Aucun colis sélectionné", "Veuillez sélectionner un colis à supprimer.");
            return;
        }
        
        if (!AlertUtils.showDeleteConfirmation("colis pour \"" + selectedColis.getDestinataire() + "\"")) {
            return;
        }
        
        try {
            colisService.deleteColis(selectedColis.getId());
            
            AlertUtils.showSuccess("Succès", "Colis supprimé avec succès.");
            loadColis();
            clearForm();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Suppression",
                "Impossible de supprimer le colis",
                e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleAssignToDriver() {
        if (selectedColis == null) {
            AlertUtils.showWarning("Sélection", "Aucun colis sélectionné", "Veuillez sélectionner un colis.");
            return;
        }
        
        if (selectedColis.isLivre()) {
            AlertUtils.showWarning("Colis Livré", "Colis déjà livré", "Ce colis est déjà marqué comme livré.");
            return;
        }
        
        Livreur selectedLivreur = livreurComboBox.getValue();
        if (selectedLivreur == null) {
            AlertUtils.showWarning("Sélection", "Aucun livreur sélectionné", "Veuillez sélectionner un livreur dans la liste.");
            livreurComboBox.requestFocus();
            return;
        }
        
        try {
            colisService.assignColisToLivreur(selectedColis.getId(), selectedLivreur.getId());
            
            AlertUtils.showSuccess("Succès", 
                "Colis assigné avec succès au livreur " + selectedLivreur.getFullName() + ".");
            loadColis();
            updateActionButtonsState();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur d'Assignation",
                "Impossible d'assigner le colis au livreur",
                e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleMarkAsDelivered() {
        if (selectedColis == null) {
            AlertUtils.showWarning("Sélection", "Aucun colis sélectionné", "Veuillez sélectionner un colis.");
            return;
        }
        
        if (selectedColis.isLivre()) {
            AlertUtils.showWarning("Colis Livré", "Colis déjà livré", "Ce colis est déjà marqué comme livré.");
            return;
        }
        
        if (!AlertUtils.showConfirmation(
            "Marquer comme livré",
            "Confirmer la livraison",
            "Êtes-vous sûr de vouloir marquer ce colis comme livré ?\n" +
            "La date de livraison sera définie à aujourd'hui.")) {
            return;
        }
        
        try {
            colisService.markColisAsDelivered(selectedColis.getId());
            
            AlertUtils.showSuccess("Succès", "Colis marqué comme livré avec succès.");
            loadColis();
            updateActionButtonsState();
            
        } catch (Exception e) {
            AlertUtils.showError(
                "Erreur de Livraison",
                "Impossible de marquer le colis comme livré",
                e.getMessage()
            );
        }
    }
    
    @FXML
    private void handleCancel() {
        clearForm();
    }
    
    @FXML
    private void handleRefresh() {
        loadData();
        clearForm();
        AlertUtils.showInfo("Actualisation", "Données actualisées", "La liste des colis a été rechargée.");
    }
    
    @FXML
    private void handleExportCSV() {
        // Show confirmation
        if (!AlertUtils.showConfirmation(
            "Exporter les livraisons",
            "Exporter les livraisons du jour",
            "Voulez-vous exporter les livraisons d'aujourd'hui au format CSV ?")) {
            return;
        }
        
        // Disable export button during operation
        exportButton.setDisable(true);
        exportButton.setText("Export en cours...");
        
        // Create background task for CSV export
        Task<String> exportTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                // Get today's deliveries
                var deliveredToday = colisService.getColisDeliveredToday();
                
                if (deliveredToday.isEmpty()) {
                    throw new Exception("Aucune livraison trouvée pour aujourd'hui.");
                }
                
                // Generate CSV content
                String csvContent = colisService.exportDeliveredTodayToCSV();
                
                // Write to file
                String filename = "livraisons_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".csv";
                try (FileWriter writer = new FileWriter(filename)) {
                    writer.write(csvContent);
                }
                
                return filename;
            }
        };
        
        exportTask.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                exportButton.setDisable(false);
                exportButton.setText("Exporter Livraisons du Jour");
                
                String filename = exportTask.getValue();
                AlertUtils.showSuccess(
                    "Export Réussi",
                    "Fichier CSV créé avec succès :\n" + filename
                );
            });
        });
        
        exportTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                exportButton.setDisable(false);
                exportButton.setText("Exporter Livraisons du Jour");
                
                Throwable exception = exportTask.getException();
                AlertUtils.showError(
                    "Erreur d'Export",
                    "Impossible d'exporter les données",
                    exception.getMessage()
                );
            });
        });
        
        // Run task in background thread
        Thread exportThread = new Thread(exportTask);
        exportThread.setDaemon(true);
        exportThread.start();
    }
}