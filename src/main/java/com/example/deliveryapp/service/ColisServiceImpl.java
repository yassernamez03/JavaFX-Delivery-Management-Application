package com.example.deliveryapp.service;

import com.example.deliveryapp.dao.IColisDAO;
import com.example.deliveryapp.dao.ColisDAOImpl;
import com.example.deliveryapp.entities.Colis;
import com.example.deliveryapp.util.CSVExporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IColisService
 */
public class ColisServiceImpl implements IColisService {
    private final IColisDAO colisDAO;
    private final CSVExporter csvExporter;
    
    public ColisServiceImpl() {
        this.colisDAO = new ColisDAOImpl();
        this.csvExporter = new CSVExporter();
    }
    
    @Override
    public void addColis(Colis colis) throws Exception {
        validateColis(colis);
        colisDAO.addColis(colis);
    }
    
    @Override
    public void updateColis(Colis colis) throws Exception {
        validateColis(colis);
        if (colis.getId() <= 0) {
            throw new Exception("ID du colis invalide pour la mise à jour");
        }
        colisDAO.updateColis(colis);
    }
    
    @Override
    public void deleteColis(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID du colis invalide");
        }
        colisDAO.deleteColis(id);
    }
    
    @Override
    public Colis getColisById(int id) {
        try {
            return colisDAO.getColisById(id);
        } catch (Exception e) {
            System.err.println("Error getting colis by ID: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Colis> getAllColis() {
        try {
            return colisDAO.getAllColis();
        } catch (Exception e) {
            System.err.println("Error getting all colis: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public void assignColisToLivreur(int colisId, int livreurId) throws Exception {
        if (colisId <= 0) {
            throw new Exception("ID du colis invalide");
        }
        if (livreurId <= 0) {
            throw new Exception("ID du livreur invalide");
        }
        
        Colis colis = colisDAO.getColisById(colisId);
        if (colis == null) {
            throw new Exception("Colis non trouvé");
        }
        
        colis.setLivreurId(livreurId);
        colisDAO.updateColis(colis);
    }
    
    @Override
    public List<Colis> getColisByLivreur(int livreurId) {
        try {
            return colisDAO.getColisByLivreurId(livreurId);
        } catch (Exception e) {
            System.err.println("Error getting colis by livreur: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public void markColisAsDelivered(int colisId) throws Exception {
        if (colisId <= 0) {
            throw new Exception("ID du colis invalide");
        }
        
        Colis colis = colisDAO.getColisById(colisId);
        if (colis == null) {
            throw new Exception("Colis non trouvé");
        }
        
        if (colis.isLivre()) {
            throw new Exception("Ce colis est déjà marqué comme livré");
        }
        
        colis.setLivre(true);
        colis.setDateLivraison(LocalDate.now());
        colisDAO.updateColis(colis);
    }
    
    @Override
    public List<Colis> getColisDeliveredToday() {
        try {
            return colisDAO.getColisDeliveredOnDate(LocalDate.now());
        } catch (Exception e) {
            System.err.println("Error getting colis delivered today: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public String exportDeliveredTodayToCSV() throws Exception {
        List<Colis> deliveredToday = getColisDeliveredToday();
        return csvExporter.exportColisToCSV(deliveredToday);
    }
    
    /**
     * Validate Colis data
     * @param colis The colis to validate
     * @throws Exception if validation fails
     */
    private void validateColis(Colis colis) throws Exception {
        if (colis == null) {
            throw new Exception("Les données du colis ne peuvent pas être nulles");
        }
        
        if (colis.getDestinataire() == null || colis.getDestinataire().trim().isEmpty()) {
            throw new Exception("Le destinataire du colis est obligatoire");
        }
        
        if (colis.getAdresse() == null || colis.getAdresse().trim().isEmpty()) {
            throw new Exception("L'adresse du colis est obligatoire");
        }
        
        if (colis.getDateEnvoi() == null) {
            throw new Exception("La date d'envoi du colis est obligatoire");
        }
        
        if (colis.getDestinataire().length() > 255) {
            throw new Exception("Le nom du destinataire ne peut pas dépasser 255 caractères");
        }
        
        // Check if delivery date is valid when package is marked as delivered
        if (colis.isLivre() && colis.getDateLivraison() == null) {
            colis.setDateLivraison(LocalDate.now());
        }
        
        // Ensure delivery date is not before send date
        if (colis.getDateLivraison() != null && colis.getDateLivraison().isBefore(colis.getDateEnvoi())) {
            throw new Exception("La date de livraison ne peut pas être antérieure à la date d'envoi");
        }
    }
}