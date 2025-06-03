package com.example.deliveryapp.service;

import com.example.deliveryapp.entities.Colis;
import java.util.List;

/**
 * Service interface for Colis business logic
 */
public interface IColisService {
    /**
     * Add a new package with validation
     * @param colis The package to add
     * @throws Exception if operation fails or validation fails
     */
    void addColis(Colis colis) throws Exception;
    
    /**
     * Update an existing package with validation
     * @param colis The package to update
     * @throws Exception if operation fails or validation fails
     */
    void updateColis(Colis colis) throws Exception;
    
    /**
     * Delete a package
     * @param id The ID of the package to delete
     * @throws Exception if operation fails
     */
    void deleteColis(int id) throws Exception;
    
    /**
     * Get a package by ID
     * @param id The package's ID
     * @return The Colis object or null if not found
     */
    Colis getColisById(int id);
    
    /**
     * Get all packages
     * @return List of all packages
     */
    List<Colis> getAllColis();
    
    /**
     * Assign a package to a driver
     * @param colisId The package ID
     * @param livreurId The driver ID
     * @throws Exception if operation fails
     */
    void assignColisToLivreur(int colisId, int livreurId) throws Exception;
    
    /**
     * Get all packages assigned to a specific driver
     * @param livreurId The driver ID
     * @return List of packages assigned to the driver
     */
    List<Colis> getColisByLivreur(int livreurId);
    
    /**
     * Mark a package as delivered
     * @param colisId The package ID
     * @throws Exception if operation fails
     */
    void markColisAsDelivered(int colisId) throws Exception;
    
    /**
     * Get all packages delivered today
     * @return List of packages delivered today
     */
    List<Colis> getColisDeliveredToday();
    
    /**
     * Export packages delivered today to CSV format
     * @return CSV content as string
     * @throws Exception if operation fails
     */
    String exportDeliveredTodayToCSV() throws Exception;
}