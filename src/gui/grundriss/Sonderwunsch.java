package business.grundriss;

/**
 * Klasse, welche einen Sonderwunsch zu den Grundriss-Varianten beschreibt.
 */
public class Sonderwunsch {
    
    // eindeutige ID des Sonderwunsches
    private final int id;
    // Beschreibung des Sonderwunsches
    private final String beschreibung;
    // Preis des Sonderwunsches
    private final int preis;
    
    /**
     * erzeugt ein Sonderwunsch-Objekt
     * @param id ID des Sonderwunsches
     * @param beschreibung Beschreibung des Sonderwunsches
     * @param preis Preis des Sonderwunsches in Euro
     */
    public Sonderwunsch(int id, String beschreibung, int preis) {
        this.id = id;
        this.beschreibung = beschreibung;
        this.preis = preis;
    }
    
    /**
     * gibt die ID des Sonderwunsches heraus
     * @return int, ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * gibt die Beschreibung des Sonderwunsches heraus
     * @return String, Beschreibung
     */
    public String getBeschreibung() {
        return beschreibung;
    }
    
    /**
     * gibt den Preis des Sonderwunsches heraus
     * @return int, Preis in Euro
     */
    public int getPreis() {
        return preis;
    }
}