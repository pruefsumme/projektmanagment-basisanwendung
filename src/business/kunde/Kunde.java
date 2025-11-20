package business.kunde;

/**
 * Klasse, welche einen Kunden beschreibt.
 */
public class Kunde {
    
    // eindeutige ID des Kunden
    private int id;
    // NEW: Hausnummer des zugeordneten Hauses
    private Integer hausnummer;
    // Vorname des Kunden
    private String vorname;
    // Nachname des Kunden
    private String nachname;
    // Telefonnummer des Kunden
    private String telefon;
    // E-Mail des Kunden
    private String email;
    
    /**
     * erzeugt ein Kunde-Objekt
     * @param id eindeutige ID
     * @param hausnummer Hausnummer (kann null sein)
     * @param vorname Vorname
     * @param nachname Nachname
     * @param telefon Telefonnummer
     * @param email E-Mail
     */
    public Kunde(int id, Integer hausnummer, String vorname, String nachname,
                 String telefon, String email) {
        this.id = id;
        this.hausnummer = hausnummer;
        this.vorname = vorname;
        this.nachname = nachname;
        this.telefon = telefon;
        this.email = email;
    }
    
    public int getId() {
        return id;
    }
    
    // NEW: Getter für Hausnummer
    public Integer getHausnummer() {
        return hausnummer;
    }
    
    public String getVorname() {
        return vorname;
    }
    
    public String getNachname() {
        return nachname;
    }
    
    public String getTelefon() {
        return telefon;
    }
    
    public String getEmail() {
        return email;
    }
}