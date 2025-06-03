package com.example.deliveryapp.dao;

import com.example.deliveryapp.entities.Livreur;
import java.util.List;

/**
 * Data Access Object interface for Livreur entities
 */
public interface ILivreurDAO {
    /**
     * Add a new driver to the database
     * @param livreur The driver to add
     * @throws Exception if operation fails
     */
    void addLivreur(Livreur livreur) throws Exception;
    
    /**
     * Update an existing driver in the database
     * @param livreur The driver to update
     * @throws Exception if operation fails
     */
    void updateLivreur(Livreur livreur) throws Exception;
    
    /**
     * Delete a driver from the database
     * @param id The ID of the driver to delete
     * @throws Exception if operation fails
     */
    void deleteLivreur(int id) throws Exception;
    
    /**
     * Get a driver by ID
     * @param id The driver's ID
     * @return The Livreur object or null if not found
     * @throws Exception if operation fails
     */
    Livreur getLivreurById(int id) throws Exception;
    
    /**
     * Get all drivers from the database
     * @return List of all drivers
     * @throws Exception if operation fails
     */
    List<Livreur> getAllLivreurs() throws Exception;
}