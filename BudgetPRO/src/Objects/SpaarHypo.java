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
public class SpaarHypo extends Hypotheek {

    private final String totaalBedragS;
    private final double totaalBedrag;
    private final double premie;

    /**
     * Maakt een nieuw SpaarHypo object
     * @param id Het id nummer van de spaarhypotheek
     * @param restschuld De restschuld van de spaarhypotheek
     * @param rente Het rentepercentage van de spaarhypotheek
     * @param omschrijving De omschrijving van de spaarhypotheek
     * @param totaalBedrag Het totale maandelijkse bedrag van de spaarhypotheek
     */
    public SpaarHypo(int id, String restschuld, String rente, String omschrijving,
            String totaalBedrag) {
        super(id, restschuld, rente, omschrijving, "Spaar");
        this.totaalBedragS = totaalBedrag;
        this.totaalBedrag = Double.parseDouble(totaalBedrag);
        premie = berekenPremie();
    }

    /**
     * Bereken de premie van een spaarhypotheek
     * @return De premie van de spaarhypotheek (double)
     */
    private double berekenPremie() {
        return totaalBedrag - berekenRente();
    }

    /**
     * Betaal het totale maandelijkse bedrag van de spaarhypotheek
     * @param berekendeMaand De maand die berekend wordt
     */
    public void betaalHerhaling(int berekendeMaand) {
        String spaarOmschrijving = "Betaling premie en rente van maand #" + berekendeMaand
                + "\nBetaalde rente: \u20ac" + rente
                + "\nBetaalde premie: \u20ac + " + premie;
        String jaar = Integer.toString(Main.huidigJaartal);
        String maand;
        if (berekendeMaand < 10) {
            maand = "0" + berekendeMaand;
        } else {
            maand = Integer.toString(berekendeMaand);
        }
        String datum = jaar + "/" + maand + "/01 00:00:00";
        
        uitgaand = deciForm.format(totaalBedrag);
        Wijziging wijziging = new Wijziging(0, vast, inkomend, uitgaand, spaarOmschrijving, datum, herhaling, categorie);
        wijziging.nieuweWijziging(false);
    }
    
    /**
     * Get het totale maandelijkse bedrag van een spaarhypotheek
     * @return Het totale bedrag van de spaarhypotheek (String)
     */
    @Override
    public String getAnnuiteit(){
        return totaalBedragS;
    }
}
