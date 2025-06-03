package com.example.deliveryapp.util;

import com.example.deliveryapp.entities.Colis;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for exporting data to CSV format
 */
public class CSVExporter {
    private static final String CSV_SEPARATOR = ",";
    private static final String CSV_HEADER = "Colis ID,Destinataire,Adresse,Date Envoi,Date Livraison,Nom Livreur,Prénom Livreur";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Export a list of Colis to CSV format
     * @param colisList List of packages to export
     * @return CSV content as string
     */
    public String exportColisToCSV(List<Colis> colisList) {
        StringBuilder csvContent = new StringBuilder();
        
        // Add header
        csvContent.append(CSV_HEADER).append("\n");
        
        // Add data rows
        for (Colis colis : colisList) {
            csvContent.append(formatColisToCSVRow(colis)).append("\n");
        }
        
        return csvContent.toString();
    }
    
    /**
     * Export packages delivered today to a CSV file
     * @param colisList List of packages to export
     * @param filename Filename for the CSV file
     * @return Path to the created CSV file
     * @throws IOException if file writing fails
     */
    public String exportColisToCSVFile(List<Colis> colisList, String filename) throws IOException {
        String csvContent = exportColisToCSV(colisList);
        
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(csvContent);
        }
        
        return filename;
    }
    
    /**
     * Export packages delivered today to a CSV file with automatic filename
     * @param colisList List of packages to export
     * @return Path to the created CSV file
     * @throws IOException if file writing fails
     */
    public String exportDeliveredTodayToFile(List<Colis> colisList) throws IOException {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String filename = "deliveries_" + date + ".csv";
        return exportColisToCSVFile(colisList, filename);
    }
    
    /**
     * Format a single Colis object to a CSV row
     * @param colis The package to format
     * @return CSV row as string
     */
    private String formatColisToCSVRow(Colis colis) {
        StringBuilder row = new StringBuilder();
        
        // Colis ID
        row.append(colis.getId()).append(CSV_SEPARATOR);
        
        // Destinataire (escape commas and quotes)
        row.append(escapeCsvField(colis.getDestinataire())).append(CSV_SEPARATOR);
        
        // Adresse (escape commas and quotes)
        row.append(escapeCsvField(colis.getAdresse())).append(CSV_SEPARATOR);
        
        // Date Envoi
        row.append(colis.getDateEnvoi() != null ? colis.getDateEnvoi().format(DATE_FORMATTER) : "").append(CSV_SEPARATOR);
        
        // Date Livraison
        row.append(colis.getDateLivraison() != null ? colis.getDateLivraison().format(DATE_FORMATTER) : "").append(CSV_SEPARATOR);
        
        // Nom Livreur
        String nomLivreur = (colis.getLivreur() != null) ? colis.getLivreur().getNom() : "";
        row.append(escapeCsvField(nomLivreur)).append(CSV_SEPARATOR);
        
        // Prénom Livreur
        String prenomLivreur = (colis.getLivreur() != null) ? colis.getLivreur().getPrenom() : "";
        row.append(escapeCsvField(prenomLivreur));
        
        return row.toString();
    }
    
    /**
     * Escape special characters in CSV fields
     * @param field The field to escape
     * @return Escaped field
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        
        // If field contains comma, newline, or quote, wrap in quotes and escape internal quotes
        if (field.contains(",") || field.contains("\n") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        
        return field;
    }
}