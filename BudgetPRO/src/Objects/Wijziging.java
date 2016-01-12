/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import system.Database;
import system.Main;

/**
 *
 * @author Dave van Rijn, IS-103, Studentnummer 500714558
 */
public class Wijziging {

    private final int id;
    private final String soort; //vast/variabel
    private String in, uit; //in- en uitgaande bedragen
    private String omschrijving;
    private String datum;
    private String herhaling;
    private final String categorie;
    private final DecimalFormat deciForm = new DecimalFormat("0.00");
    private final Database db = new Database();

    /**
     * Maak een nieuw wijziging object
     * @param id Het id nummer van de wijziging
     * @param soort Het soort van de wijziging (vast/variabel)
     * @param in Het inkomende bedrag
     * @param uit Het uitgaande bedrag
     * @param omschrijving De omschrijving van de wijziging
     * @param datum De datum van de wijziging
     * @param herhaling De frequentie van de herhaling
     * @param categorie De categorie van de wijziging
     */
    public Wijziging(int id, String soort, String in, String uit, String omschrijving,
            String datum, String herhaling, String categorie) {
        this.id = id;
        this.soort = soort;
        this.in = checkBedrag(in);
        this.uit = checkBedrag(uit);
        this.omschrijving = omschrijving;
        this.datum = datum;
        this.herhaling = herhaling;
        this.categorie = categorie;
    }

    /**
     * Controleer een String of het een bedrag is, vervangt een komma met een punt.
     * @param bedrag Het bedrag dat gecontroleerd wordt
     * @return Het gecontroleerde en omgezette bedrag.
     * @exception NumberFormatException: Als param bedrag geen getal is.
     */
    private String checkBedrag(String bedrag) {
        if (!bedrag.equals("-")) {
            try {
                if (bedrag.contains(",")) {
                    bedrag = bedrag.replace(",", ".");
                }
                bedrag = deciForm.format(Double.parseDouble(bedrag));
                bedrag = bedrag.replace(",", ".");
            } catch (NumberFormatException e) {
                Main.showError("001", e);
            }
        }

        return bedrag;
    }

    /**
     * Voert een nieuwe invoer in in de database, laat alleen een bevestiging 
     * zien als param message true is. 
     * @param message Of er een bevestiging moet worden gegeven van het invoeren
     */
    public void nieuweWijziging(boolean message) {
        double bedrag = 0;
        db.nieuweWijziging(this);
        if (!in.equals("-")) {
            bedrag = Double.parseDouble(in);
        } else if (!uit.equals("-")) {
            bedrag = Double.parseDouble(uit) * -1;
        }
        db.setNieuwSaldo(bedrag);
        if (message) {
            JOptionPane.showMessageDialog(null, "De invoer is doorgevoerd.");
        }
    }

    /**
     * Maakt een nieuwe wachtlijst entry in de database
     */
    public void nieuweWachtlijst() {
        db.nieuweWachtlijst(this);
    }

    /**
     * Wijzigt een wijziging in de database
     */
    public void wijzigInvoer() {
        db.wijzigWijziging(this);
    }

    /**
     * Get het soort van de wijziging
     * @return Het soort van de wijziging (vast/variabel) (String)
     */
    public String getSoort() {
        return soort;
    }

    /**
     * Get het inkomende bedrag van een invoer
     * @return het inkomende bedrag (String)
     */
    public String getIn() {
        return in;
    }

    /**
     * Get het uitgaande bedrag van een invoer
     * @return Het uitgaande bedrag (String)
     */
    public String getUit() {
        return uit;
    }

    /**
     * Get de omschrijving van een invoer
     * @return De omscgrijving van de invoer (String)
     */
    public String getOmschrijving() {
        return omschrijving;
    }

    /**
     * Get de datum van een invoer
     * @return de datum van de invoer (String)
     */
    public String getDatum() {
        return datum;
    }

    /**
     * Get de herhaling van een wijziging
     * @return De herhaling van de wijziging (String)
     */
    public String getHerhaling() {
        return herhaling;
    }

    /**
     * Get de categorie van een wijziging
     * @return De categorie van de wijziging (String)
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Get het id nummer van een wijziging
     * @return Het id nummer van de wijziging (int)
     */
    public int getId() {
        return id;
    }

}
