package com.example.deliveryapp.dao;

import com.example.deliveryapp.entities.Colis;
import com.example.deliveryapp.entities.Livreur;
import com.example.deliveryapp.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IColisDAO using JDBC
 */
public class ColisDAOImpl implements IColisDAO {
    
    @Override
    public void addColis(Colis colis) throws Exception {
        String sql = "INSERT INTO colis (destinataire, adresse, dateEnvoi, livre, dateLivraison, livreur_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, colis.getDestinataire());
            stmt.setString(2, colis.getAdresse());
            stmt.setDate(3, Date.valueOf(colis.getDateEnvoi()));
            stmt.setBoolean(4, colis.isLivre());
            stmt.setDate(5, colis.getDateLivraison() != null ? Date.valueOf(colis.getDateLivraison()) : null);
            stmt.setObject(6, colis.getLivreurId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating colis failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    colis.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating colis failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public void updateColis(Colis colis) throws Exception {
        String sql = "UPDATE colis SET destinataire = ?, adresse = ?, dateEnvoi = ?, livre = ?, dateLivraison = ?, livreur_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, colis.getDestinataire());
            stmt.setString(2, colis.getAdresse());
            stmt.setDate(3, Date.valueOf(colis.getDateEnvoi()));
            stmt.setBoolean(4, colis.isLivre());
            stmt.setDate(5, colis.getDateLivraison() != null ? Date.valueOf(colis.getDateLivraison()) : null);
            stmt.setObject(6, colis.getLivreurId());
            stmt.setInt(7, colis.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating colis failed, no rows affected.");
            }
        }
    }
    
    @Override
    public void deleteColis(int id) throws Exception {
        String sql = "DELETE FROM colis WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting colis failed, no rows affected.");
            }
        }
    }
    
    @Override
    public Colis getColisById(int id) throws Exception {
        String sql = "SELECT c.*, l.nom as livreur_nom, l.prenom as livreur_prenom, l.telephone as livreur_telephone " +
                    "FROM colis c LEFT JOIN livreurs l ON c.livreur_id = l.id WHERE c.id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToColis(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Colis> getAllColis() throws Exception {
        String sql = "SELECT c.*, l.nom as livreur_nom, l.prenom as livreur_prenom, l.telephone as livreur_telephone " +
                    "FROM colis c LEFT JOIN livreurs l ON c.livreur_id = l.id ORDER BY c.dateEnvoi DESC";
        List<Colis> colisList = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                colisList.add(mapResultSetToColis(rs));
            }
        }
        
        return colisList;
    }
    
    @Override
    public List<Colis> getColisByLivreurId(int livreurId) throws Exception {
        String sql = "SELECT c.*, l.nom as livreur_nom, l.prenom as livreur_prenom, l.telephone as livreur_telephone " +
                    "FROM colis c LEFT JOIN livreurs l ON c.livreur_id = l.id WHERE c.livreur_id = ? ORDER BY c.dateEnvoi DESC";
        List<Colis> colisList = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, livreurId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    colisList.add(mapResultSetToColis(rs));
                }
            }
        }
        
        return colisList;
    }
    
    @Override
    public List<Colis> getColisDeliveredOnDate(LocalDate date) throws Exception {
        String sql = "SELECT c.*, l.nom as livreur_nom, l.prenom as livreur_prenom, l.telephone as livreur_telephone " +
                    "FROM colis c LEFT JOIN livreurs l ON c.livreur_id = l.id WHERE c.dateLivraison = ? ORDER BY c.id";
        List<Colis> colisList = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    colisList.add(mapResultSetToColis(rs));
                }
            }
        }
        
        return colisList;
    }
    
    /**
     * Helper method to map ResultSet to Colis object
     */
    private Colis mapResultSetToColis(ResultSet rs) throws SQLException {
        Colis colis = new Colis();
        colis.setId(rs.getInt("id"));
        colis.setDestinataire(rs.getString("destinataire"));
        colis.setAdresse(rs.getString("adresse"));
        
        Date dateEnvoi = rs.getDate("dateEnvoi");
        if (dateEnvoi != null) {
            colis.setDateEnvoi(dateEnvoi.toLocalDate());
        }
        
        colis.setLivre(rs.getBoolean("livre"));
        
        Date dateLivraison = rs.getDate("dateLivraison");
        if (dateLivraison != null) {
            colis.setDateLivraison(dateLivraison.toLocalDate());
        }
        
        Object livreurIdObj = rs.getObject("livreur_id");
        if (livreurIdObj != null) {
            colis.setLivreurId((Integer) livreurIdObj);
            
            // Create Livreur object if data is available
            String livreurNom = rs.getString("livreur_nom");
            if (livreurNom != null) {
                Livreur livreur = new Livreur(
                    colis.getLivreurId(),
                    livreurNom,
                    rs.getString("livreur_prenom"),
                    rs.getString("livreur_telephone")
                );
                colis.setLivreur(livreur);
            }
        }
        
        return colis;
    }
}