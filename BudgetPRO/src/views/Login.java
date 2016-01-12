/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import enums.Gebruiker;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import Objects.AflossingsVrijHypo;
import Objects.AnnuiteitHypo;
import system.Database;
import Objects.Herhaling;
import Objects.LineaireHypo;
import Objects.Wijziging;
import java.awt.Font;
import system.Main;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class Login extends javax.swing.JPanel {

    /**
     * Creates new form Login
     */
    private final Color grey = new Color(153, 153, 153); //Kleur voor invulvelden tips
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private int rekeningnummer;
    private double huidigSaldo;
    ArrayList<Integer> maanden = new ArrayList<>(); //Lijst met de maanden
    ArrayList<String> uitgevoerd = new ArrayList<>(); //Lijst me ja of nee voor de maanden (uitgevoerd of niet)

    public Login() {
        initComponents();
        setLetterGrootte(Main.getLetterGrootte());
        final String key = "ENTER";
        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login();
            }
        };
        txtRekeningnummer.getInputMap().put(KeyStroke.getKeyStroke(key), key);
        txtRekeningnummer.getActionMap().put(key, enterAction);
    }
    
    private void setLetterGrootte(int grootte){
        Font font = new Font("Tahoma", 0, grootte);
        btnInloggen.setFont(font);
        btnRegistreren.setFont(font);
        lblAchternaam.setFont(font);
        lblHuidigSaldo.setFont(font);
        lblRekeningnummer.setFont(font);
        lblRekeningnummerN.setFont(font);
        lblTussenvoegsel.setFont(font);
        lblVoorletters.setFont(font);
        tabInlog.setFont(font);
        txtAchternaam.setFont(font);
        txtHuidigSaldo.setFont(font);
        txtRekeningnummer.setFont(font);
        txtRekeningnummerN.setFont(font);
        txtTussenvoegsel.setFont(font);
        txtVoorletters.setFont(font);
    }

    /**
     * Inloggen in het programma
     */
    private void Login() {
        try {
            if (!txtRekeningnummer.getText().isEmpty()) {
                rekeningnummer = Integer.parseInt(txtRekeningnummer.getText());
            } else if (!txtRekeningnummerN.getText().equals("Rekeningnummer")) {
                rekeningnummer = Integer.parseInt(txtRekeningnummerN.getText());
            }
            Gebruiker.resetGebruiker();
            Database db = new Database();
            db.login(rekeningnummer);

            if (Gebruiker.isIngelogd()) {
                boolean succes = berekenHerhalingen();
                if (succes) {
                    procesWachtlijst();
                    Main.setNewPanel(new Startpage());
                    Main.setLabels();
                }
            } else {
                JOptionPane.showMessageDialog(null, 
                        "Dit rekeningnummer is niet geregistreerd!", "Fout",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Het rekeningnummer bestaat alleen uit getallen!", "Fout", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Registreer een nieuwe gebruiker
     */
    private void Registreer() {
        Database db = new Database();
        voorletters = txtVoorletters.getText();
        tussenvoegsel = txtTussenvoegsel.getText();
        if (tussenvoegsel.equals("Tussenvoegsel")) {
            tussenvoegsel = null;
        }
        achternaam = txtAchternaam.getText();
        String message = "Gebruiker " + voorletters + " ";
        if (tussenvoegsel != null) {
            message += tussenvoegsel + " ";
        }
        message += achternaam + " geregistreerd";
        db.login(rekeningnummer);

        if (!Gebruiker.isIngelogd()) {
            db.registreer(voorletters, tussenvoegsel, achternaam, rekeningnummer, huidigSaldo);
            int confirm = JOptionPane.showOptionDialog(null, message, 
                    "Geregistreerd", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.OK_OPTION) {
                Login();
            }
        } else {
            JOptionPane.showMessageDialog(null, 
                    "Dit rekeningnummer is al geregistreerd!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
            Gebruiker.resetGebruiker();
        }
    }

    private boolean berekenHerhalingen() {
        Database db = new Database();
        int huidigeMaand = Calendar.getInstance().get(Calendar.MONTH) + 1;
        ResultSet rss = db.getMaandHerhalingen(huidigeMaand);
        ResultSet rs = db.getHerhalingen();
        boolean succes;
        try {
            while (rss.next()) {
                if (rss.getString(2).equals("n")) {
                    ResultSet hypo = db.getHypotheek();
                    //Gewone herhalingen
                    while (rs.next()) {
                        Herhaling herhaal = new Herhaling(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4), rs.getString(5),
                                rs.getString(6), rss.getInt(1));
                        herhaal.berekenHerhaling();
                    }
                    rs.beforeFirst();

                    //Hypotheek herhalingen
                    while (hypo.next()) {
                        switch(hypo.getString(5)){
                            case "Lineair":
                                LineaireHypo linHypo = new LineaireHypo(hypo.getInt(1), hypo.getString(3), hypo.getString(4), hypo.getString(7), hypo.getString(2));
                                linHypo.betaalHerhaling(rss.getInt(1));
                                break;
                            case "Annuiteit":
                                AnnuiteitHypo annHypo = new AnnuiteitHypo(hypo.getInt(1), hypo.getString(3), hypo.getString(4), hypo.getString(7), hypo.getString(6));
                                annHypo.betaalHerhaling(rss.getInt(1));
                                break;
                            case "Aflossingsvrij":
                                AflossingsVrijHypo aflHypo = new AflossingsVrijHypo(hypo.getInt(1), hypo.getString(3), hypo.getString(4), hypo.getString(7));
                                aflHypo.betaalHerhaling(rss.getInt(1));
                                break;
                        }
                    }
                    hypo.beforeFirst();
                }
                db.setMaandBerekend(rss.getInt(1));
            }

            succes = true;
        } catch (SQLException | NullPointerException e) {
            Main.showError("044", e);
            succes = false;
        }

        return succes;
    }
    
    private void procesWachtlijst(){
        Database db = new Database();
        ResultSet rs = db.getWachtlijst();
        try{
            while(rs.next()){
                String in = rs.getString(2);
                String uit = rs.getString(3);
                double bedrag = 0;
                if(!in.equals("-")){
                    bedrag = Double.parseDouble(in);
                } else if (!uit.equals("-")){
                    bedrag = Double.parseDouble(uit) * -1;
                }
                
                Wijziging wijziging = new Wijziging(0, "Vast", in, uit,
                rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(8));
                wijziging.nieuweWijziging(false);
                db.verwijderWachtlijst(rs.getInt(1));
            }
        } catch (SQLException e){
            Main.showError("045", e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabInlog = new javax.swing.JTabbedPane();
        pnlBestaande = new javax.swing.JPanel();
        lblRekeningnummer = new javax.swing.JLabel();
        txtRekeningnummer = new javax.swing.JTextField();
        btnInloggen = new javax.swing.JButton();
        pnlNieuwe = new javax.swing.JPanel();
        lblVoorletters = new javax.swing.JLabel();
        lblTussenvoegsel = new javax.swing.JLabel();
        lblAchternaam = new javax.swing.JLabel();
        lblRekeningnummerN = new javax.swing.JLabel();
        lblHuidigSaldo = new javax.swing.JLabel();
        txtVoorletters = new javax.swing.JTextField();
        txtRekeningnummerN = new javax.swing.JTextField();
        txtHuidigSaldo = new javax.swing.JTextField();
        txtAchternaam = new javax.swing.JTextField();
        txtTussenvoegsel = new javax.swing.JTextField();
        btnRegistreren = new javax.swing.JButton();

        tabInlog.setFocusable(false);

        lblRekeningnummer.setText("Rekeningnummer");
        lblRekeningnummer.setFocusable(false);

        btnInloggen.setText("Inloggen");
        btnInloggen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInloggenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBestaandeLayout = new javax.swing.GroupLayout(pnlBestaande);
        pnlBestaande.setLayout(pnlBestaandeLayout);
        pnlBestaandeLayout.setHorizontalGroup(
            pnlBestaandeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBestaandeLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addGroup(pnlBestaandeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnInloggen)
                    .addGroup(pnlBestaandeLayout.createSequentialGroup()
                        .addComponent(lblRekeningnummer)
                        .addGap(18, 18, 18)
                        .addComponent(txtRekeningnummer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(133, Short.MAX_VALUE))
        );
        pnlBestaandeLayout.setVerticalGroup(
            pnlBestaandeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBestaandeLayout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addGroup(pnlBestaandeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRekeningnummer)
                    .addComponent(txtRekeningnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnInloggen)
                .addContainerGap(107, Short.MAX_VALUE))
        );

        tabInlog.addTab("Bestaande gebruiker", pnlBestaande);

        lblVoorletters.setText("Voorletters");

        lblTussenvoegsel.setText("Tussenvoegsel");

        lblAchternaam.setText("Achternaam");

        lblRekeningnummerN.setText("Rekeningnummer");

        lblHuidigSaldo.setText("Huidig saldo");

        txtVoorletters.setForeground(new java.awt.Color(153, 153, 153));
        txtVoorletters.setText("Voorletters");
        txtVoorletters.setNextFocusableComponent(txtTussenvoegsel);
        txtVoorletters.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtVoorlettersFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVoorlettersFocusLost(evt);
            }
        });

        txtRekeningnummerN.setForeground(new java.awt.Color(153, 153, 153));
        txtRekeningnummerN.setText("Rekeningnummer");
        txtRekeningnummerN.setNextFocusableComponent(txtHuidigSaldo);
        txtRekeningnummerN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRekeningnummerNFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRekeningnummerNFocusLost(evt);
            }
        });

        txtHuidigSaldo.setForeground(new java.awt.Color(153, 153, 153));
        txtHuidigSaldo.setText("Huidig saldo");
        txtHuidigSaldo.setNextFocusableComponent(txtVoorletters);
        txtHuidigSaldo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHuidigSaldoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHuidigSaldoFocusLost(evt);
            }
        });

        txtAchternaam.setForeground(new java.awt.Color(153, 153, 153));
        txtAchternaam.setText("Achternaam");
        txtAchternaam.setNextFocusableComponent(txtRekeningnummerN);
        txtAchternaam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAchternaamFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAchternaamFocusLost(evt);
            }
        });

        txtTussenvoegsel.setForeground(new java.awt.Color(153, 153, 153));
        txtTussenvoegsel.setText("Tussenvoegsel");
        txtTussenvoegsel.setNextFocusableComponent(txtAchternaam);
        txtTussenvoegsel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTussenvoegselFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTussenvoegselFocusLost(evt);
            }
        });

        btnRegistreren.setText("Registreren");
        btnRegistreren.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrerenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNieuweLayout = new javax.swing.GroupLayout(pnlNieuwe);
        pnlNieuwe.setLayout(pnlNieuweLayout);
        pnlNieuweLayout.setHorizontalGroup(
            pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNieuweLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistreren)
                    .addGroup(pnlNieuweLayout.createSequentialGroup()
                        .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblVoorletters)
                            .addComponent(lblTussenvoegsel)
                            .addComponent(lblAchternaam)
                            .addComponent(lblRekeningnummerN)
                            .addComponent(lblHuidigSaldo))
                        .addGap(18, 18, 18)
                        .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRekeningnummerN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTussenvoegsel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVoorletters, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAchternaam, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHuidigSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(133, Short.MAX_VALUE))
        );
        pnlNieuweLayout.setVerticalGroup(
            pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNieuweLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVoorletters)
                    .addComponent(txtVoorletters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTussenvoegsel)
                    .addComponent(txtTussenvoegsel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAchternaam)
                    .addComponent(txtAchternaam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRekeningnummerN)
                    .addComponent(txtRekeningnummerN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlNieuweLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHuidigSaldo)
                    .addComponent(txtHuidigSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnRegistreren)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        tabInlog.addTab("Nieuwe gebruiker", pnlNieuwe);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabInlog, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabInlog)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtVoorlettersFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVoorlettersFocusGained
        if (txtVoorletters.getText().equals("Voorletters")) {
            txtVoorletters.setText(null);
            txtVoorletters.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtVoorlettersFocusGained

    private void txtVoorlettersFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVoorlettersFocusLost
        if (txtVoorletters.getText().isEmpty()) {
            txtVoorletters.setForeground(grey);
            txtVoorletters.setText("Voorletters");
        }
    }//GEN-LAST:event_txtVoorlettersFocusLost

    private void txtTussenvoegselFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTussenvoegselFocusGained
        if (txtTussenvoegsel.getText().equals("Tussenvoegsel")) {
            txtTussenvoegsel.setText(null);
            txtTussenvoegsel.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtTussenvoegselFocusGained

    private void txtTussenvoegselFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTussenvoegselFocusLost
        if (txtTussenvoegsel.getText().isEmpty()) {
            txtTussenvoegsel.setForeground(grey);
            txtTussenvoegsel.setText("Tussenvoegsel");
        }
    }//GEN-LAST:event_txtTussenvoegselFocusLost

    private void txtAchternaamFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAchternaamFocusGained
        if (txtAchternaam.getText().equals("Achternaam")) {
            txtAchternaam.setText(null);
            txtAchternaam.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtAchternaamFocusGained

    private void txtAchternaamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAchternaamFocusLost
        if (txtAchternaam.getText().isEmpty()) {
            txtAchternaam.setForeground(grey);
            txtAchternaam.setText("Achternaam");
        }
    }//GEN-LAST:event_txtAchternaamFocusLost

    private void txtRekeningnummerNFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRekeningnummerNFocusGained
        if (txtRekeningnummerN.getText().equals("Rekeningnummer")) {
            txtRekeningnummerN.setText(null);
            txtRekeningnummerN.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtRekeningnummerNFocusGained

    private void txtRekeningnummerNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRekeningnummerNFocusLost
        if (txtRekeningnummerN.getText().isEmpty()) {
            txtRekeningnummerN.setForeground(grey);
            txtRekeningnummerN.setText("Rekeningnummer");
        }
    }//GEN-LAST:event_txtRekeningnummerNFocusLost

    private void txtHuidigSaldoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHuidigSaldoFocusGained
        if (txtHuidigSaldo.getText().equals("Huidig saldo")) {
            txtHuidigSaldo.setText(null);
            txtHuidigSaldo.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtHuidigSaldoFocusGained

    private void txtHuidigSaldoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHuidigSaldoFocusLost
        if (txtHuidigSaldo.getText().isEmpty()) {
            txtHuidigSaldo.setForeground(grey);
            txtHuidigSaldo.setText("Huidig saldo");
        }
    }//GEN-LAST:event_txtHuidigSaldoFocusLost

    private void btnRegistrerenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrerenActionPerformed
        boolean correctIngevuld;
        try {
            rekeningnummer = Integer.parseInt(txtRekeningnummerN.getText());
            correctIngevuld = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Het rekeningnummer bestaat alleen "
                    + "uit getallen!", "Fout", JOptionPane.ERROR_MESSAGE);;
            correctIngevuld = false;
        }
        if (correctIngevuld) {
            try {
                huidigSaldo = Double.parseDouble(txtHuidigSaldo.getText());
                correctIngevuld = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Het huidige saldo bestaat alleen "
                        + "uit (decimale) getallen!", "Fout", 
                        JOptionPane.ERROR_MESSAGE);
                correctIngevuld = false;
            }
        }
        if (correctIngevuld) {
            Registreer();
        }
    }//GEN-LAST:event_btnRegistrerenActionPerformed

    private void btnInloggenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInloggenActionPerformed
        Login();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }//GEN-LAST:event_btnInloggenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInloggen;
    private javax.swing.JButton btnRegistreren;
    private javax.swing.JLabel lblAchternaam;
    private javax.swing.JLabel lblHuidigSaldo;
    private javax.swing.JLabel lblRekeningnummer;
    private javax.swing.JLabel lblRekeningnummerN;
    private javax.swing.JLabel lblTussenvoegsel;
    private javax.swing.JLabel lblVoorletters;
    private javax.swing.JPanel pnlBestaande;
    private javax.swing.JPanel pnlNieuwe;
    private javax.swing.JTabbedPane tabInlog;
    private javax.swing.JTextField txtAchternaam;
    private javax.swing.JTextField txtHuidigSaldo;
    private javax.swing.JTextField txtRekeningnummer;
    private javax.swing.JTextField txtRekeningnummerN;
    private javax.swing.JTextField txtTussenvoegsel;
    private javax.swing.JTextField txtVoorletters;
    // End of variables declaration//GEN-END:variables
}
