package com.example.deliveryapp.service;

import com.example.deliveryapp.dao.ILivreurDAO;
import com.example.deliveryapp.dao.LivreurDAOImpl;
import com.example.deliveryapp.entities.Livreur;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ILivreurService
 */
public class LivreurServiceImpl implements ILivreurService {
    private final ILivreurDAO livreurDAO;
    
    public LivreurServiceImpl() {
        this.livreurDAO = new LivreurDAOImpl();
    }
    
    @Override
    public void addLivreur(Livreur livreur) throws Exception {
        validateLivreur(livreur);
        livreurDAO.addLivreur(livreur);
    }
    
    @Override
    public void updateLivreur(Livreur livreur) throws Exception {
        validateLivreur(livreur);
        if (livreur.getId() <= 0) {
            throw new Exception("ID du livreur invalide pour la mise à jour");
        }
        livreurDAO.updateLivreur(livreur);
    }
    
    @Override
    public void deleteLivreur(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID du livreur invalide");
        }
        livreurDAO.deleteLivreur(id);
    }
    
    @Override
    public Livreur getLivreurById(int id) {
        try {
            return livreurDAO.getLivreurById(id);
        } catch (Exception e) {
            System.err.println("Error getting livreur by ID: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Livreur> getAllLivreurs() {
        try {
            return livreurDAO.getAllLivreurs();
        } catch (Exception e) {
            System.err.println("Error getting all livreurs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Validate Livreur data
     * @param livreur The livreur to validate
     * @throws Exception if validation fails
     */
    private void validateLivreur(Livreur livreur) throws Exception {
        if (livreur == null) {
            throw new Exception("Les données du livreur ne peuvent pas être nulles");
        }
        
        if (livreur.getNom() == null || livreur.getNom().trim().isEmpty()) {
            throw new Exception("Le nom du livreur est obligatoire");
        }
        
        if (livreur.getPrenom() == null || livreur.getPrenom().trim().isEmpty()) {
            throw new Exception("Le prénom du livreur est obligatoire");
        }
        
        if (livreur.getNom().length() > 255) {
            throw new Exception("Le nom du livreur ne peut pas dépasser 255 caractères");
        }
        
        if (livreur.getPrenom().length() > 255) {
            throw new Exception("Le prénom du livreur ne peut pas dépasser 255 caractères");
        }
        
        if (livreur.getTelephone() != null && livreur.getTelephone().length() > 20) {
            throw new Exception("Le numéro de téléphone ne peut pas dépasser 20 caractères");
        }
    }
}