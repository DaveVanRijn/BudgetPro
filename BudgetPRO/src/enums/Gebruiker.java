/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class Gebruiker {
    private final String voorletters, tussenvoegsel, achternaam;
    private final int rekeningnummer;
    private double budget;
    private static Gebruiker huidigeGebruiker = null;
    
    /**
     * Maak een nieuw gebruiker object
     * @param voorletters De voorletters van de gebruiker
     * @param tussenvoegsel Het tussenvoegsel van de gebruiker
     * @param achternaam De achternaam van de gebruiker
     * @param rekeningnummer Het rekekningnummer van de gebruiker
     * @param budget Het saldo van de gebruiker
     */
    private Gebruiker(String voorletters, String tussenvoegsel, String achternaam, 
            int rekeningnummer, double budget){
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.rekeningnummer = rekeningnummer;
        this.budget = budget;  
    }
    
    /**
     * Zet de huidige gebruiker van het systeem naar de echte gebruiker
     * @param voorletters De voorletters van de gebruiker
     * @param tussenvoegsel Het tussenvoegsel van de gebruiker
     * @param achternaam De achternaam van de gebruiker
     * @param rekeningnummer Het rekekningnummer van de gebruiker
     * @param budget Het saldo van de gebruiker
     */
    public static void setGebruiker(String voorletters, String tussenvoegsel, String achternaam, 
            int rekeningnummer, double budget){
        huidigeGebruiker = new Gebruiker(voorletters, tussenvoegsel, achternaam, rekeningnummer, budget);
    }
    
    /**
     * Get de huidige gebruiker
     * @return De gebruiker (Gebruiker)
     */
    public static Gebruiker getGebruiker(){
        return huidigeGebruiker;
    }
    
    /**
     * Get de voorletters van de huidige gebruiker
     * @return De voorletters van de huidige gebruiker (String)
     */
    public static String getVoorletters(){
        return getGebruiker().voorletters;
    }
    
    /**
     * Get het tussenvoegsel van de huidige gebruiker
     * @return het tussenvoegsel van huidige de gebruiker (String)
     */
    public static String getTussenvoegsel(){
        return getGebruiker().tussenvoegsel;
    }
     /**
      * Get de achternaam van de huidige gebruiker
      * @return De achternaam van de huidige gebruiker (String)
      */
    public static String getAchternaam(){
        return getGebruiker().achternaam;
    }
    
    /**
     * Get de volledige naam van de huidige gebruiker
     * @return Voornaam + tussenvoegsel + achternaam (String)
     */
    public static String getVolledigeNaam(){
        String naam = getGebruiker().voorletters + " ";
        if(getGebruiker().tussenvoegsel != null){
            naam += getGebruiker().tussenvoegsel + " ";
        }
        naam += getGebruiker().achternaam;
        
        return naam;
    }
    
    /**
     * Get het rekeningnummer van de huidige gebruiker
     * @return Het rekeningnummer van de huidige gebruiker (int)
     */
    public static int getRekeningnummer(){
        return getGebruiker().rekeningnummer;
    }
    
    /**
     * Get het saldo van de huidige gebruiker
     * @return Het saldo van de huidige gebruiker (double)
     */
    public static double getBudget(){
        return getGebruiker().budget;
    }
    
    /**
     * Set het saldo van de huidige gebruiker
     * @param budget Het nieuwe saldo van de huidige gebruiker
     */
    public static void setBudget(double budget){
        getGebruiker().budget = budget;
    }
    
    /**
     * Check of er een gebruiker is ingelogd
     * @return Er is een huidige gebruiker (Boolean)
     */
    public static boolean isIngelogd(){
        return huidigeGebruiker!= null;
    }
    
    /**
     * Reset de huidige gebruiker
     */
    public static void resetGebruiker(){
        huidigeGebruiker = null;
    }
        
}

