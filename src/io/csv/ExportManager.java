package io.csv;

import business.kunde.Kunde;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Zentrale Klasse für den Export von Daten in CSV-Dateien.
 */
public class ExportManager {
    
    private static ExportManager instance;
    
    // Standardverzeichnis für Exporte
    private String exportVerzeichnis = "exports";
    
    /**
     * Privater Konstruktor für Singleton-Pattern
     */
    private ExportManager() {
        // Exportverzeichnis erstellen, falls nicht vorhanden
        File dir = new File(exportVerzeichnis);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Gibt die einzige Instanz des ExportManagers zurück
     * @return ExportManager-Instanz
     */
    public static ExportManager getInstance() {
        if (instance == null) {
            instance = new ExportManager();
        }
        return instance;
    }
    
    /**
     * Setzt das Exportverzeichnis
     * @param verzeichnis Pfad zum Exportverzeichnis
     */
    public void setExportVerzeichnis(String verzeichnis) {
        this.exportVerzeichnis = verzeichnis;
        File dir = new File(verzeichnis);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Gibt das aktuelle Exportverzeichnis zurück
     * @return Pfad zum Exportverzeichnis
     */
    public String getExportVerzeichnis() {
        return this.exportVerzeichnis;
    }
    
    /**
     * Generiert den Dateinamen nach dem Schema: Hausnummer_Nachname.csv
     * Entfernt ungültige Zeichen aus dem Dateinamen
     * @param kunde Kunde-Objekt
     * @return Formatierter Dateiname
     * @throws IllegalArgumentException wenn Kunde null ist oder Pflichtfelder fehlen
     */
    public String generiereDateiname(Kunde kunde) {
        if (kunde == null) {
            throw new IllegalArgumentException("Kunde darf nicht null sein");
        }
        
        int hausnummer = kunde.getHausnummer();
        String nachname = kunde.getNachname();
        
        if (hausnummer <= 0) {
            throw new IllegalArgumentException("Hausnummer muss größer als 0 sein");
        }
        
        if (nachname == null || nachname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nachname darf nicht leer sein");
        }
        
        // Entferne ungültige Zeichen für Dateinamen (Windows und Unix)
        String hausnummerStr = String.valueOf(hausnummer);
        nachname = bereinigeDateinamenTeil(nachname);
        
        return hausnummerStr + "_" + nachname + ".csv";
    }
    
    /**
     * Entfernt ungültige Zeichen aus einem Dateinamen-Teil
     * @param teil Teil des Dateinamens
     * @return Bereinigter String
     */
    private String bereinigeDateinamenTeil(String teil) {
        // Entferne ungültige Zeichen: \ / : * ? " < > |
        return teil.replaceAll("[\\\\/:*?\"<>|]", "")
                   .trim();
    }
    
    /**
     * Exportiert Sonderwünsche eines Kunden in eine CSV-Datei
     * @param kunde Kunde-Objekt
     * @param csvInhalt CSV-formatierter String mit den Sonderwünschen
     * @param dateiname Name der zu erstellenden Datei
     * @throws IOException bei Schreibfehlern
     */
    public void exportiereSonderwuensche(Kunde kunde, String csvInhalt, String dateiname) 
            throws IOException {
        String pfad = exportVerzeichnis + File.separator + dateiname;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pfad))) {
            writer.write(csvInhalt);
        }
    }
    
    /**
     * Schreibt eine CSV-Datei
     * @param dateiname Name der Datei
     * @param inhalt Inhalt der Datei
     * @throws IOException bei Schreibfehlern
     */
    public void schreibeCsvDatei(String dateiname, String inhalt) throws IOException {
        String pfad = exportVerzeichnis + File.separator + dateiname;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pfad))) {
            writer.write(inhalt);
        }
    }
    
    /**
     * Prüft, ob eine Datei im Exportverzeichnis existiert
     * @param dateiname Name der zu prüfenden Datei
     * @return true, wenn die Datei existiert
     */
    public boolean dateiExistiert(String dateiname) {
        File datei = new File(exportVerzeichnis + File.separator + dateiname);
        return datei.exists();
    }
}