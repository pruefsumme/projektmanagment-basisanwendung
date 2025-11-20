package io.csv;

import business.kunde.Kunde;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test-Klasse für den ExportManager
 */
public class ExportManagerTest {
    
    public static void main(String[] args) {
        System.out.println("=== ExportManager Tests ===\n");
        
        testDateinamenGenerierung();
        testCsvExport();
        testUngueltigeZeichen();
        testFehlerbehandlung();
        
        System.out.println("\n=== Alle Tests abgeschlossen ===");
    }
    
    /**
     * Test 1: Dateinamen-Generierung
     */
    private static void testDateinamenGenerierung() {
        System.out.println("Test 1: Dateinamen-Generierung");
        
        ExportManager manager = ExportManager.getInstance();
        
        // Testdaten
        Kunde kunde = new Kunde();
        kunde.setHausnummer(12);
        kunde.setNachname("Müller");
        kunde.setVorname("Max");
        kunde.setTelefonnummer("0123456789");
        kunde.setEmail("max@example.com");
        
        String dateiname = manager.generiereDateiname(kunde);
        String erwartet = "12_Müller.csv";
        
        if (dateiname.equals(erwartet)) {
            System.out.println("✓ Dateiname korrekt: " + dateiname);
        } else {
            System.out.println("✗ FEHLER: Erwartet '" + erwartet + "', erhalten '" + dateiname + "'");
        }
        System.out.println();
    }
    
    /**
     * Test 2: CSV-Export mit echten Daten
     */
    private static void testCsvExport() {
        System.out.println("Test 2: CSV-Export mit Kundendaten");
        
        ExportManager manager = ExportManager.getInstance();
        manager.setExportVerzeichnis("test_exports");
        
        // Testkunde erstellen
        Kunde kunde = new Kunde();
        kunde.setHausnummer(15);
        kunde.setNachname("Schmidt");
        kunde.setVorname("Anna");
        kunde.setTelefonnummer("9876543210");
        kunde.setEmail("anna@example.com");
        
        // CSV-Inhalt simulieren
        String csvInhalt = "Bezeichnung;Preis\n" +
                          "Carport;5000.00\n" +
                          "Markise;1500.00\n" +
                          "Whirlpool;8000.00\n";
        
        String dateiname = manager.generiereDateiname(kunde);
        
        try {
            manager.schreibeCsvDatei(dateiname, csvInhalt);
            
            // Prüfen, ob Datei existiert
            if (manager.dateiExistiert(dateiname)) {
                System.out.println("✓ CSV-Datei erfolgreich erstellt: " + dateiname);
                
                // Inhalt prüfen
                String pfad = manager.getExportVerzeichnis() + File.separator + dateiname;
                String gespeicherterInhalt = new String(Files.readAllBytes(Paths.get(pfad)));
                
                if (gespeicherterInhalt.equals(csvInhalt)) {
                    System.out.println("✓ Dateiinhalt korrekt");
                } else {
                    System.out.println("✗ FEHLER: Dateiinhalt stimmt nicht überein");
                }
            } else {
                System.out.println("✗ FEHLER: Datei wurde nicht erstellt");
            }
            
        } catch (IOException e) {
            System.out.println("✗ FEHLER beim Schreiben: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Test 3: Ungültige Zeichen im Dateinamen
     */
    private static void testUngueltigeZeichen() {
        System.out.println("Test 3: Ungültige Zeichen im Dateinamen");
        
        ExportManager manager = ExportManager.getInstance();
        
        // Kunde mit problematischen Zeichen im Nachnamen
        Kunde kunde = new Kunde();
        kunde.setHausnummer(12);
        kunde.setNachname("Müller:Schmidt/Test");
        kunde.setVorname("Max");
        
        String dateiname = manager.generiereDateiname(kunde);
        
        // Prüfen, ob ungültige Zeichen entfernt wurden
        if (!dateiname.contains("/") && !dateiname.contains(":")) {
            System.out.println("✓ Ungültige Zeichen entfernt: " + dateiname);
        } else {
            System.out.println("✗ FEHLER: Ungültige Zeichen noch vorhanden: " + dateiname);
        }
        System.out.println();
    }
    
    /**
     * Test 4: Fehlerbehandlung
     */
    private static void testFehlerbehandlung() {
        System.out.println("Test 4: Fehlerbehandlung");
        
        ExportManager manager = ExportManager.getInstance();
        
        // Test mit null-Kunde
        try {
            manager.generiereDateiname(null);
            System.out.println("✗ FEHLER: Exception bei null-Kunde erwartet");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception bei null-Kunde korrekt: " + e.getMessage());
        }
        
        // Test mit leerem Nachnamen
        try {
            Kunde kunde = new Kunde();
            kunde.setHausnummer(12);
            kunde.setNachname("");
            manager.generiereDateiname(kunde);
            System.out.println("✗ FEHLER: Exception bei leerem Nachnamen erwartet");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception bei leerem Nachnamen korrekt: " + e.getMessage());
        }
        
        // Test mit ungültiger Hausnummer
        try {
            Kunde kunde = new Kunde();
            kunde.setHausnummer(0);
            kunde.setNachname("Müller");
            manager.generiereDateiname(kunde);
            System.out.println("✗ FEHLER: Exception bei ungültiger Hausnummer erwartet");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception bei ungültiger Hausnummer korrekt: " + e.getMessage());
        }
        System.out.println();
    }
}