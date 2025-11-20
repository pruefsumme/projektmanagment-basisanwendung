package business.grundriss;

import java.util.*;

/**
 * Klasse, welche die Sonderwünsche zu den Grundriss-Varianten für die GUI hält.
 * (in-memory; muss später mit DB-Zugriff ersetzt werden)
 */
public class GrundrissModel {
    
    // Liste aller möglichen Sonderwünsche (Grundriss-Varianten) (muss später aus DB geladen werden)
    private final List<Sonderwunsch> sonderwuensche = new ArrayList<>();
    // aktuell ausgewählte Sonderwünsche (IDs) (muss später kundenspezifisch aus DB kommen)
    private final Set<Integer> ausgewaehlteIds = new HashSet<>();
    
    /**
     * erzeugt ein GrundrissModel-Objekt und initialisiert die Sonderwunschliste.
     * (Hardcodiert; muss später durch SELECT auf Tabelle Sonderwunsch ersetzt werden)
     */
    public GrundrissModel() {
        // Initialisierung gem. Pflichtenheft /F20/ (später entfernen und dynamisch laden)
        sonderwuensche.add(new Sonderwunsch(1, "Wand zur Abtrennung der Küche vom Essbereich", 300));
        sonderwuensche.add(new Sonderwunsch(2, "Tür in der Wand zwischen Küche und Essbereich", 300));
        sonderwuensche.add(new Sonderwunsch(3, "Großes Zimmer im OG statt zwei kleinen Zimmern", 0));
        sonderwuensche.add(new Sonderwunsch(4, "Abgetrennter Treppenraum im DG", 890));
        sonderwuensche.add(new Sonderwunsch(5, "Vorrichtung eines Bades im DG", 990));
        sonderwuensche.add(new Sonderwunsch(6, "Ausführung eines Bades im DG", 8990));
    }
    
    /**
     * gibt alle möglichen Sonderwünsche heraus
     * @return List<Sonderwunsch>, Liste aller Sonderwünsche
     */
    public List<Sonderwunsch> getSonderwuensche() {
        return Collections.unmodifiableList(sonderwuensche);
    }
    
    /**
     * prüft, ob ein Sonderwunsch bereits ausgewählt ist
     * @param id ID des Sonderwunsches
     * @return boolean, true wenn ausgewählt
     */
    public boolean istAusgewaehlt(int id) {
        return ausgewaehlteIds.contains(id);
    }
    
    /**
     * setzt die aktuell ausgewählten Sonderwünsche (in-memory)
     * @param ids int[], IDs der ausgewählten Sonderwünsche
     * (später durch Persistierung ersetzen)
     */
    public void setAusgewaehlte(int[] ids) {
        ausgewaehlteIds.clear();
        for (int id : ids) {
            ausgewaehlteIds.add(id);
        }
    }
}