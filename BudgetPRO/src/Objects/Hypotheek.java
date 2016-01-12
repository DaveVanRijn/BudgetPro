/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.text.DecimalFormat;
import system.Database;

/**
 *
 * @author Dave van Rijn, IS-103, Studentnummer 500714558
 */
public class Hypotheek {

    String soort;
    int id;
    String restschuld, rente;
    final String inkomend = "-";
    final String vast = "Vast";
    final String categorie = "Hypotheek";
    final String herhaling = "Maandelijks";
    String omschrijving;
    String uitgaand;
    final DecimalFormat deciForm = new DecimalFormat("0.00");
    String renteOmschrijving;
    String aflossingOmschrijving;
    Database db = new Database();

    /**
     * Maak een nieuwe hypotheek
     * @param id Het id nummer van de hypotheek
     * @param restschuld De restschuld van de hypotheek
     * @param rente Het rentepercentage van de hypotheek
     * @param omschrijving De omschrijving van de hypotheek
     * @param soort Het soort hypotheek
     */
    public Hypotheek(int id, String restschuld, String rente,
            String omschrijving, String soort) {
        this.id = id;
        this.restschuld = restschuld;
        this.rente = rente;
        this.omschrijving = omschrijving;
        this.soort = soort;
    }

    /**
     * Bereken de rente van een hypotheek
     * @return Rente van de hypotheek (Double)
     */
    protected double berekenRente() {
        double maandRente = ((Double.parseDouble(rente) / 12) / 100);
        return (Double.parseDouble(restschuld) * maandRente);
    }
    
    /**
     * Maak een nieuwe hypotheek in de database
     */
    public void nieuweHypo(){
        db.nieuweHypotheek(this);
    }

    /**
     * Wijzig een hypotheek in de database
     */
    public void wijzigHypo(){
        db.wijzigHypotheek(this);
    }

    /**
     * Get het soort van de hypotheek
     * @return Het soort van de hypotheek (String)
     */
    public String getSoort() {
        return soort;
    }

    /**
     * Get de restschuld van een hypotheek
     * @return De restschuld (String)
     */
    public String getRestschuld() {
        return restschuld;
    }

    /**
     * Get het rentepercentage van een hypotheek
     * @return Het rentepercentage (String)
     */
    public String getRente() {
        return rente;
    }

    /**
     * Get de omschrijving van een hypotheek
     * @return De omschrijving van de hypotheek (String)
     */
    public String getOmschrijving() {
        return omschrijving;
    }

    /**\
     * Get de aflossing van een hypotheek
     * @return De aflossing van de hypotheek (String)
     */
    public String getAflossing() {
        return null;
    }
    
    /**
     * Get de annuiteit van een hypotheek
     * @return De annuiteit van de hypotheek (String)
     */
    public String getAnnuiteit(){
        return null;
    }

    /**
     * Get het id nummer van een hypotheek
     * @return Het id nummer van de hypotheek (int)
     */
    public int getId() {
        return id;
    }
    
    

}
