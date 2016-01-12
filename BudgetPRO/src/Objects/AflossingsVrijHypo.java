/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import system.Main;

/**
 *
 * @author Dave van Rijn, IS-103, Studentnummer 500714558
 */
public class AflossingsVrijHypo extends Hypotheek {

    /**
     * Maak een nieuwe aflossingsVrijHypotheek
     * @param id Het id nummer van de aflossingsvrije hypotheek
     * @param restschuld De restschuld van de aflossingsvrije hypotheek
     * @param rente Het rentepercentage van de aflossingsvrije hypotheek
     * @param omschrijving De omschrijving van de aflossingsvrije hypotheek
     */
    public AflossingsVrijHypo(int id, String restschuld, String rente, String omschrijving) {
        super(id, restschuld, rente, omschrijving, "Aflossingsvrij");
    }

    /**
     * Betaal de aflossing van een aflossingsvrije hypotheek
     * @param berekendeMaand De maand die berekend wordt
     */
    public void betaalHerhaling(int berekendeMaand) {
        renteOmschrijving = "Rente van hypotheek #" + id
                + "Omschrijving: " + omschrijving
                + "\nHerhaling van maand " + berekendeMaand;
        String jaar = Integer.toString(Main.huidigJaartal);
        String maand;
        if (berekendeMaand < 10) {
            maand = "0" + berekendeMaand;
        } else {
            maand = Integer.toString(berekendeMaand);
        }
        String datum = jaar + "/" + maand + "/01 00:00:00";
        //Rente betaling
        uitgaand = deciForm.format(berekenRente());
        Wijziging wijziging = new Wijziging(0, vast, inkomend, uitgaand, renteOmschrijving, datum, herhaling, categorie);
        wijziging.nieuweWijziging(false);
    }
}
