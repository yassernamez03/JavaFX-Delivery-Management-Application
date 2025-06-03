package com.example.deliveryapp.entities;

/**
 * Entity class representing a delivery driver (Livreur)
 */
public class Livreur {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;

    // Default constructor
    public Livreur() {}

    // Constructor without ID (for new drivers)
    public Livreur(String nom, String prenom, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    // Constructor with ID (for existing drivers)
    public Livreur(int id, String nom, String prenom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }

    public String getFullName() {
        return prenom + " " + nom;
    }
}