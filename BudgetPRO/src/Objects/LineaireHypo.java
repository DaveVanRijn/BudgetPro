/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import javax.swing.JOptionPane;
import system.Main;

/**
 *
 * @author Dave van Rijn, IS-103, Studentnummer 500714558
 */
public class LineaireHypo extends Hypotheek {

    String aflossing;

    /**
     * Maak een nieuw LineareHypo object
     * @param id Het id nummer van de lineaire hypotheek
     * @param restschuld De restschuld van de lineaire hypotheek
     * @param rente Het rentepercentage van de lineaire hypotheek
     * @param omschrijving De omschrijving van de lineaire hypotheek
     * @param aflossing De aflossing van de lineaire hypotheek
     */
    public LineaireHypo(int id, String restschuld, String rente, String omschrijving, String aflossing) {
        super(id, restschuld, rente, omschrijving, "Lineair");
        this.aflossing = aflossing;
    }

    /**
     * Betaal de rente en omschrijving van een Lineaire hypotheek
     * @param berekendeMaand De maand die berekend wordt
     */
    public void betaalHerhaling(int berekendeMaand) {
        renteOmschrijving = "Rente van hypotheek #" + id
                + "Omschrijving: " + omschrijving
                + "\nHerhaling van maand " + berekendeMaand;
        aflossingOmschrijving = "Aflossing van hypotheek #" + id
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
        double restschuld = Double.parseDouble(this.restschuld);
        double aflossing = Double.parseDouble(this.aflossing);

        if (restschuld > 0) {
            //Rente betaling
            uitgaand = deciForm.format(berekenRente());
            Wijziging wijziging = new Wijziging(0, vast, inkomend, uitgaand, renteOmschrijving, datum, herhaling, categorie);
            wijziging.nieuweWijziging(false);

            //Aflossing betaling
            if ((restschuld - aflossing) <= 0) { //Hypotheek afgelost
                int keuze = JOptionPane.showOptionDialog(null, "Hypotheek #" + id + " is afgelost! Wil je de "
                        + "hypotheek verwijderen uit het systeem?", "Bevestig", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if (keuze == JOptionPane.YES_OPTION) {
                    db.verwijderHypotheek(id);
                    JOptionPane.showMessageDialog(null, "Hypotheek #" + id + " verwijderd.", "Succes", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    db.setRestschuld(deciForm.format(0).replace(",", "."), id);
                    uitgaand = deciForm.format(restschuld);
                    wijziging = new Wijziging(0, vast, inkomend, uitgaand, aflossingOmschrijving, datum, herhaling, categorie);
                    wijziging.nieuweWijziging(false);
                }
            } else {
                db.setRestschuld(deciForm.format(restschuld - aflossing).replace(",", "."), id);
                //Aflossing
                uitgaand = deciForm.format(aflossing);
                wijziging = new Wijziging(0, vast, inkomend, uitgaand, aflossingOmschrijving, datum, herhaling, categorie);
                wijziging.nieuweWijziging(false);
            }
        }
    }

    
    /**
     * Get de aflossing van een Lineaire hypotheek
     * @return De aflossing van de hypotheek (String)
     */
    @Override
    public String getAflossing() {
        return aflossing;
    }

}
