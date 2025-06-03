package com.example.deliveryapp.dao;

import com.example.deliveryapp.entities.Colis;
import java.time.LocalDate;
import java.util.List;

/**
 * Data Access Object interface for Colis entities
 */
public interface IColisDAO {
    /**
     * Add a new package to the database
     * @param colis The package to add
     * @throws Exception if operation fails
     */
    void addColis(Colis colis) throws Exception;
    
    /**
     * Update an existing package in the database
     * @param colis The package to update
     * @throws Exception if operation fails
     */
    void updateColis(Colis colis) throws Exception;
    
    /**
     * Delete a package from the database
     * @param id The ID of the package to delete
     * @throws Exception if operation fails
     */
    void deleteColis(int id) throws Exception;
    
    /**
     * Get a package by ID
     * @param id The package's ID
     * @return The Colis object or null if not found
     * @throws Exception if operation fails
     */
    Colis getColisById(int id) throws Exception;
    
    /**
     * Get all packages from the database
     * @return List of all packages
     * @throws Exception if operation fails
     */
    List<Colis> getAllColis() throws Exception;
    
    /**
     * Get all packages assigned to a specific driver
     * @param livreurId The driver's ID
     * @return List of packages assigned to the driver
     * @throws Exception if operation fails
     */
    List<Colis> getColisByLivreurId(int livreurId) throws Exception;
    
    /**
     * Get all packages delivered on a specific date
     * @param date The delivery date
     * @return List of packages delivered on the specified date
     * @throws Exception if operation fails
     */
    List<Colis> getColisDeliveredOnDate(LocalDate date) throws Exception;
}