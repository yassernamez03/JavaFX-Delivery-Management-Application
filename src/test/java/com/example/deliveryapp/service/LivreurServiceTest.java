package com.example.deliveryapp.service;

import com.example.deliveryapp.entities.Livreur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LivreurService
 * Note: These are basic validation tests. Full integration tests would require database setup.
 */
public class LivreurServiceTest {
    
    private LivreurServiceImpl livreurService;
    
    @BeforeEach
    void setUp() {
        livreurService = new LivreurServiceImpl();
    }
    
    @Test
    void testValidateLivreur_ValidData() {
        // Create a valid livreur
        Livreur livreur = new Livreur("Dupont", "Jean", "0123456789");
        
        // This should not throw an exception
        assertDoesNotThrow(() -> {
            // We can't directly test private validate method, 
            // but we can test that valid data doesn't cause issues
            assertNotNull(livreur.getNom());
            assertNotNull(livreur.getPrenom());
            assertFalse(livreur.getNom().trim().isEmpty());
            assertFalse(livreur.getPrenom().trim().isEmpty());
        });
    }
    
    @Test
    void testLivreurCreation() {
        Livreur livreur = new Livreur("Martin", "Pierre", "0987654321");
        
        assertEquals("Martin", livreur.getNom());
        assertEquals("Pierre", livreur.getPrenom());
        assertEquals("0987654321", livreur.getTelephone());
        assertEquals("Pierre Martin", livreur.getFullName());
        assertEquals("Pierre Martin", livreur.toString());
    }
    
    @Test
    void testLivreurWithId() {
        Livreur livreur = new Livreur(1, "Durand", "Marie", "0147258369");
        
        assertEquals(1, livreur.getId());
        assertEquals("Durand", livreur.getNom());
        assertEquals("Marie", livreur.getPrenom());
        assertEquals("0147258369", livreur.getTelephone());
    }
    
    @Test
    void testEmptyTelephone() {
        Livreur livreur = new Livreur("Test", "User", "");
        
        assertEquals("", livreur.getTelephone());
        assertEquals("User Test", livreur.getFullName());
    }
    
    @Test
    void testNullTelephone() {
        Livreur livreur = new Livreur("Test", "User", null);
        
        assertNull(livreur.getTelephone());
        assertEquals("User Test", livreur.getFullName());
    }
}