/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.text.DecimalFormat;
import java.util.ArrayList;
import system.Database;
import system.Main;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class Herhaling {

    private String datum = null;
    private String in = null;
    private String uit = null;
    private String herhaling = null;
    private final String soort = "Vast";
    private String omschrijving = null;
    private String categorie = null;
    private final ArrayList<Integer> maanden = new ArrayList<>();
    DecimalFormat form = new DecimalFormat("0.00");
    private final int huidigeMaand;

    public Herhaling(String datum, String in, String uit, String herhaling,
            String omschrijving, String categorie, int maand) {
        this.datum = datum;
        this.in = in;
        this.uit = uit;
        this.herhaling = herhaling;
        this.omschrijving = omschrijving;
        this.categorie = categorie;
        huidigeMaand = maand;
    }

    public void berekenHerhaling() {
        Database db = new Database();
        String[] temp = datum.split("-");
        int maand = Integer.parseInt(temp[1]);
        int dag = Integer.parseInt(temp[2]);
        String nieuweHerhaling = null;
        switch (herhaling) {
            case "Elke maand":
                for (int i = 1; i <= 11; i++) {
                    maanden.add(i);
                }
                nieuweHerhaling = "Maandelijks";
                break;
            case "Elke 3 maanden":
                int maandPlus = maand;
                int maandMin = maand;
                do {
                    maanden.add(maandPlus);
                    maandPlus += 3;
                } while (maandPlus <= 11);
                do {
                    maanden.add(maandMin);
                    maandMin -= 3;
                } while (maandMin >= 0);
                maanden.add(maand - 12);
                nieuweHerhaling = "Elk kwartaal";
                break;
            case "Elk half jaar":
                maanden.add(maand);
                if (maand + 6 <= 11) {
                    maanden.add(maand + 6);
                } else {
                    maanden.add(maand - 6);
                }
                nieuweHerhaling = "Half jaarlijks";
                break;
            case "Elk jaar":
                maanden.add(maand);
                nieuweHerhaling = "Jaarlijks";
                break;
        }
        if (maanden.contains(huidigeMaand)) {
            String nieuwDatum = Integer.toString(Main.huidigJaartal) + "/" + Integer.toString(huidigeMaand)
                    + "/" + Integer.toString(dag) + " " + "00:00:00";
            String omschrijvingN = omschrijving + "\nHerhaling maand: " + huidigeMaand;
            Wijziging wijziging = new Wijziging(0, soort, in, uit, omschrijvingN, nieuwDatum,
                    nieuweHerhaling, categorie);
            wijziging.nieuweWachtlijst();
            

        }

    }
}
