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
public class AnnuiteitHypo extends Hypotheek {

    final String annuiteit;
    double aflossing;

    /**
     * Maak een nieuw AnnuiteitHypo object
     * @param id Het id nummer van de annuiteitshypotheek
     * @param restschuld De restschuld van de annuiteitshypotheek
     * @param rente Het rentepercentage van de annuiteitshypotheek
     * @param omschrijving De omschrijving van de annuiteitshypotheek
     * @param annuiteit De annuiteit van de annuiteitshypotheek
     */
    public AnnuiteitHypo(int id, String restschuld, String rente, String omschrijving, String annuiteit) {
        super(id, restschuld, rente, omschrijving, "Annuiteit");
        this.annuiteit = annuiteit;
        aflossing = Double.parseDouble(annuiteit) - berekenRente();
    }

    /**
     * Betaal de aflossing en rente van een annuiteithypotheek
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
        if (restschuld > 0) {
            int nieuwId = -1;

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
                    nieuwId++;
                    uitgaand = deciForm.format(restschuld);
                    wijziging = new Wijziging(0, vast, inkomend, uitgaand, aflossingOmschrijving, datum, herhaling, categorie);
                    wijziging.nieuweWijziging(false);
                }
            } else {
                db.setRestschuld(deciForm.format(restschuld - aflossing).replace(",", "."), id);
                //Aflossing
                nieuwId++;
                uitgaand = deciForm.format(aflossing);
                wijziging = new Wijziging(0, vast, inkomend, uitgaand, aflossingOmschrijving, datum, herhaling, categorie);
                wijziging.nieuweWijziging(false);
            }
        }
    }
    
    /**
     * Get de annuiteit van een annuiteit hypotheek
     * @return De annuiteit (String)
     */
    @Override
    public String getAnnuiteit(){
        return annuiteit;
    }

}
