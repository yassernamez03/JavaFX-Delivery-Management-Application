package com.example.deliveryapp.entities;

import java.time.LocalDate;

/**
 * Entity class representing a package (Colis)
 */
public class Colis {
    private int id;
    private String destinataire;
    private String adresse;
    private LocalDate dateEnvoi;
    private boolean livre;
    private LocalDate dateLivraison;
    private Integer livreurId;
    private Livreur livreur; // For convenience, populated by service layer

    // Default constructor
    public Colis() {}

    // Constructor without ID (for new packages)
    public Colis(String destinataire, String adresse, LocalDate dateEnvoi) {
        this.destinataire = destinataire;
        this.adresse = adresse;
        this.dateEnvoi = dateEnvoi;
        this.livre = false;
    }

    // Full constructor
    public Colis(int id, String destinataire, String adresse, LocalDate dateEnvoi,
                 boolean livre, LocalDate dateLivraison, Integer livreurId) {
        this.id = id;
        this.destinataire = destinataire;
        this.adresse = adresse;
        this.dateEnvoi = dateEnvoi;
        this.livre = livre;
        this.dateLivraison = dateLivraison;
        this.livreurId = livreurId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDate dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public boolean isLivre() {
        return livre;
    }

    public void setLivre(boolean livre) {
        this.livre = livre;
    }

    public LocalDate getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Integer getLivreurId() {
        return livreurId;
    }

    public void setLivreurId(Integer livreurId) {
        this.livreurId = livreurId;
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        if (livreur != null) {
            this.livreurId = livreur.getId();
        }
    }

    public String getStatutText() {
        return livre ? "Livré" : "Non livré";
    }

    public String getLivreurName() {
        return livreur != null ? livreur.getFullName() : "";
    }
}