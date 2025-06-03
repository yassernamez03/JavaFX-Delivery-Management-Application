package com.example.deliveryapp.service;

import com.example.deliveryapp.entities.Livreur;
import java.util.List;

/**
 * Service interface for Livreur business logic
 */
public interface ILivreurService {
    /**
     * Add a new driver with validation
     * @param livreur The driver to add
     * @throws Exception if operation fails or validation fails
     */
    void addLivreur(Livreur livreur) throws Exception;
    
    /**
     * Update an existing driver with validation
     * @param livreur The driver to update
     * @throws Exception if operation fails or validation fails
     */
    void updateLivreur(Livreur livreur) throws Exception;
    
    /**
     * Delete a driver
     * @param id The ID of the driver to delete
     * @throws Exception if operation fails
     */
    void deleteLivreur(int id) throws Exception;
    
    /**
     * Get a driver by ID
     * @param id The driver's ID
     * @return The Livreur object or null if not found
     */
    Livreur getLivreurById(int id);
    
    /**
     * Get all drivers
     * @return List of all drivers
     */
    List<Livreur> getAllLivreurs();
}