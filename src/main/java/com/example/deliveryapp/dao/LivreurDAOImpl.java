package com.example.deliveryapp.dao;

import com.example.deliveryapp.entities.Livreur;
import com.example.deliveryapp.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ILivreurDAO using JDBC
 */
public class LivreurDAOImpl implements ILivreurDAO {
    
    @Override
    public void addLivreur(Livreur livreur) throws Exception {
        String sql = "INSERT INTO livreurs (nom, prenom, telephone) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getTelephone());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating livreur failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livreur.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating livreur failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void updateLivreur(Livreur livreur) throws Exception {
        String sql = "UPDATE livreurs SET nom = ?, prenom = ?, telephone = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getTelephone());
            stmt.setInt(4, livreur.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating livreur failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void deleteLivreur(int id) throws Exception {
        String sql = "DELETE FROM livreurs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting livreur failed, no rows affected.");
            }
        }
    }
    
    @Override
    public Livreur getLivreurById(int id) throws Exception {
        String sql = "SELECT * FROM livreurs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livreur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone")
                    );
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Livreur> getAllLivreurs() throws Exception {
        String sql = "SELECT * FROM livreurs ORDER BY nom, prenom";
        List<Livreur> livreurs = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                livreurs.add(new Livreur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone")
                ));
            }
        }
        
        return livreurs;
    }
}