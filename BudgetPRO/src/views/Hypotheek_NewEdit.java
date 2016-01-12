/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Objects.AflossingsVrijHypo;
import Objects.AnnuiteitHypo;
import Objects.LineaireHypo;
import Objects.SpaarHypo;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import system.Database;
import system.Main;

/**
 *
 * @author Dave van Rijn, IS-103, Studentnummer 500714558
 */
public class Hypotheek_NewEdit extends javax.swing.JPanel {

    private final int VERWIJDEREN = 1;
    private final int AANPASSEN = 2;
    private int actie;
    private final DecimalFormat deciForm = new DecimalFormat("0.00");
    private int id; //id nummer van de hypotheek
    private Database db = new Database();
    private String omschrijving, soort, aflossing, restschuld, rente, annuiteit, maandelijks;

    /**
     * Creates new form Hypotheek_NewEdit
     */
    public Hypotheek_NewEdit() { //Nieuwe hypotheek
        initComponents();
        setLetterGrootte(Main.getLetterGrootte());
        pnlAnnuiteit.setVisible(false);
        pnlAflossingsVrij.setVisible(false);
        pnlSpaar.setVisible(false);
        btnLinAanpassen.setVisible(false);
        btnLinVerwijderen.setVisible(false);
        txtLinRestschuld.setText("");
        txtLinAflossing.setText("");
        txtLinRente.setText("");
        txtLinOmschrijving.setText("");
        lblSoort.setVisible(false);
    }

    public Hypotheek_NewEdit(int id, String soort) { //Bestaande hypotheek
        initComponents();
        setLetterGrootte(Main.getLetterGrootte());
        ResultSet rs = db.getHypoDetails(id);
        this.id = id;
        actie = VERWIJDEREN;
        btnLineair.setVisible(false);
        btnAnnu.setVisible(false);
        btnAflosvrij.setVisible(false);
        btnSpaar.setVisible(false);
        try {
            while (rs.next()) {
                switch (soort) {
                    case "Lineair":
                        btnLinOpslaan.setVisible(false);
                        pnlAnnuiteit.setVisible(false);
                        pnlAflossingsVrij.setVisible(false);
                        pnlSpaar.setVisible(false);
                        lblSoort.setText("Soort hypotheek: Lineair");
                        txtLinRestschuld.setText(rs.getString(1));
                        txtLinAflossing.setText(rs.getString(2));
                        txtLinRente.setText(rs.getString(3));
                        txtLinOmschrijving.setText(rs.getString(4));

                        txtLinRestschuld.setEditable(false);
                        txtLinAflossing.setEditable(false);
                        txtLinRente.setEditable(false);
                        txtLinOmschrijving.setEditable(false);
                        break;
                    case "Annuiteit":
                        btnAnnOpslaan.setVisible(false);
                        pnlAflossingsVrij.setVisible(false);
                        pnlLineair.setVisible(false);
                        pnlSpaar.setVisible(false);
                        lblSoort.setText("Soort hypotheek: Annuïteit");
                        txtAnnRestschuld.setText(rs.getString(1));
                        txtAnnRente.setText(rs.getString(2));
                        txtAnnAnnuiteit.setText(rs.getString(5));
                        txtAnnOmschrijving.setText(rs.getString(4));

                        txtAnnRestschuld.setEditable(false);
                        txtAnnAnnuiteit.setEditable(false);
                        txtAnnRente.setEditable(false);
                        txtAnnOmschrijving.setEditable(false);
                        break;
                    case "Aflossingsvrij":
                        btnAflOpslaan.setVisible(false);
                        pnlLineair.setVisible(false);
                        pnlAnnuiteit.setVisible(false);
                        pnlSpaar.setVisible(false);
                        lblSoort.setText("Soort hypotheek: Aflossingsvrij");
                        txtAflRestschuld.setText(rs.getString(1));
                        txtAflRente.setText(rs.getString(2));
                        txtAflOmschrijving.setText(rs.getString(4));

                        txtAflRestschuld.setEditable(false);
                        txtAflRente.setEditable(false);
                        txtAflOmschrijving.setEditable(false);
                        break;
                    case "Spaar":
                        btnSpOpslaan.setVisible(false);
                        pnlLineair.setVisible(false);
                        pnlAnnuiteit.setVisible(false);
                        pnlAflossingsVrij.setVisible(false);
                        lblSoort.setText("Soort hypotheek: Spaar");
                        txtSpRestschuld.setText(rs.getString(1));
                        txtSpRente.setText(rs.getString(3));
                        txtSpMaandelijks.setText(rs.getString(5));
                        txtSpOmschrijving.setText(rs.getString(4));
                        
                        txtSpRestschuld.setEditable(false);
                        txtSpRente.setEditable(false);
                        txtSpMaandelijks.setEditable(false);
                        txtSpOmschrijving.setEditable(false);
                }
            }
        } catch (SQLException e) {
            Main.showError("042", e);
        }

    }
    
    private void setLetterGrootte(int grootte){
        Font font = new Font("Tahoma", 0, grootte);
        lblSoort.setFont(font);
        btnAflAanpassen.setFont(font);
        btnAflOpslaan.setFont(font);
        btnAflVerwijderen.setFont(font);
        btnAflosvrij.setFont(font);
        btnAnnAanpassen.setFont(font);
        btnAnnOpslaan.setFont(font);
        btnAnnVerwijderen.setFont(font);
        btnAnnu.setFont(font);
        btnLinOpslaan.setFont(font);
        btnLinAanpassen.setFont(font);
        btnLinVerwijderen.setFont(font);
        btnLineair.setFont(font);
        btnSpAanpassen.setFont(font);
        btnSpOpslaan.setFont(font);
        btnSpVerwijderen.setFont(font);
        btnSpaar.setFont(font);
        jLabel1.setFont(font);
        jLabel2.setFont(font);
        jLabel3.setFont(font);
        jLabel4.setFont(font);
        jLabel5.setFont(font);
        jLabel6.setFont(font);
        jLabel7.setFont(font);
        jLabel8.setFont(font);
        jLabel9.setFont(font);
        jLabel10.setFont(font);
        jLabel11.setFont(font);
        lblLinAflossing.setFont(font);
        lblLinOmschrijving.setFont(font);
        lblLinRente.setFont(font);
        lblLinRestschuld.setFont(font);
        lblSoort.setFont(font);
        txtAflOmschrijving.setFont(font);
        txtAflRente.setFont(font);
        txtAflRestschuld.setFont(font);
        txtAnnAnnuiteit.setFont(font);
        txtAnnOmschrijving.setFont(font);
        txtAnnRente.setFont(font);
        txtAnnRestschuld.setFont(font);
        txtLinAflossing.setFont(font);
        txtLinOmschrijving.setFont(font);
        txtLinRente.setFont(font);
        txtLinRestschuld.setFont(font);
        txtSpMaandelijks.setFont(font);
        txtSpOmschrijving.setFont(font);
        txtSpRente.setFont(font);
        txtSpRente.setFont(font);
        txtSpRestschuld.setFont(font);
    }

    private String checkDecimaal(String string) throws NumberFormatException {
        if (string.contains(",")) {
            string = string.replace(",", ".");
        }
        string = deciForm.format(Double.parseDouble(string)).replace(",", ".");
        return string;
    }

    private void verwijderen() {
        int optie = JOptionPane.showOptionDialog(null, 
                "Weet je zeker dat je deze hypotheek wil verwijderen?", "Bevestigen", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, new Object[]{"Ja", "Nee"}, "Ja");
        if (optie == JOptionPane.YES_OPTION) {
            db.verwijderHypotheek(id);
            JOptionPane.showMessageDialog(null, "De hypotheek is verwijderd.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            Main.setNewPanel(new Hypotheken());
        }
    }

    //<Lineaire hypotheek>------------------------------------------------------
    private void linAanpassen() {
        actie = AANPASSEN;
        btnLinVerwijderen.setText("Opslaan");
        txtLinAflossing.setEditable(true);
        txtLinOmschrijving.setEditable(true);
        txtLinRente.setEditable(true);
        txtLinRestschuld.setEditable(true);
        btnLinAanpassen.setEnabled(false);
    }

    private void linOpslaan() {
        restschuld = txtLinRestschuld.getText();
        rente = txtLinRente.getText();
        aflossing = txtLinAflossing.getText();
        omschrijving = txtLinOmschrijving.getText();
        try {
            restschuld = checkDecimaal(restschuld);
            rente = checkDecimaal(rente);
            aflossing = checkDecimaal(aflossing);
            LineaireHypo hypo = new LineaireHypo(id, restschuld, rente, omschrijving, aflossing);
            hypo.wijzigHypo();
            JOptionPane.showMessageDialog(null, "De hypotheek is aangepast.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            btnLinVerwijderen.setText("Verwijderen");
            btnLinAanpassen.setEnabled(true);
            txtLinAflossing.setEditable(false);
            txtLinOmschrijving.setEditable(false);
            txtLinRente.setEditable(false);
            txtLinRestschuld.setEditable(false);
            actie = VERWIJDEREN;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Voer enkel bedragen in de bedrag velden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void linNieuw() {
        restschuld = txtLinRestschuld.getText();
        rente = txtLinRente.getText();
        aflossing = txtLinAflossing.getText();
        omschrijving = txtLinOmschrijving.getText();
        if (restschuld.isEmpty() || rente.isEmpty() || aflossing.isEmpty()
                || omschrijving.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "Geen van de velden mogen leeggelaten worden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                restschuld = checkDecimaal(restschuld);
                rente = checkDecimaal(rente);
                aflossing = checkDecimaal(aflossing);
                LineaireHypo hypo = new LineaireHypo(0, restschuld, rente, omschrijving, aflossing);
                hypo.nieuweHypo();
                JOptionPane.showMessageDialog(null,
                        "De hypotheek is toegevoegd.", "Succes", 
                        JOptionPane.INFORMATION_MESSAGE);
                txtLinRestschuld.setText("");
                txtLinAflossing.setText("");
                txtLinRente.setText("");
                txtLinOmschrijving.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                        "Voer enkel bedragen in de bedrag velden!", "Fout",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //</Lineaire hypotheek>-----------------------------------------------------

    //<Annuïteit hypotheek>-----------------------------------------------------
    private void annAanpassen() {
        actie = AANPASSEN;
        btnAnnVerwijderen.setText("Opslaan");
        txtAnnOmschrijving.setEditable(true);
        txtAnnRente.setEditable(true);
        txtAnnRestschuld.setEditable(true);
        txtAnnAnnuiteit.setEditable(true);
        btnAnnAanpassen.setEnabled(false);
    }

    private void annOpslaan() {
        restschuld = txtAnnRestschuld.getText();
        rente = txtAnnRente.getText();
        annuiteit = txtAnnAnnuiteit.getText();
        omschrijving = txtAnnOmschrijving.getText();
        try {
            restschuld = checkDecimaal(restschuld);
            rente = checkDecimaal(rente);
            annuiteit = checkDecimaal(annuiteit);
            AnnuiteitHypo hypo = new AnnuiteitHypo(id, restschuld, rente, omschrijving, annuiteit);
            hypo.wijzigHypo();
            JOptionPane.showMessageDialog(null, 
                    "De hypotheek is aangepast.", "Succes", 
                    JOptionPane.INFORMATION_MESSAGE);
            btnAnnVerwijderen.setText("Verwijderen");
            btnAnnAanpassen.setEnabled(true);
            txtAnnAnnuiteit.setEditable(false);
            txtAnnOmschrijving.setEditable(false);
            txtAnnRente.setEditable(false);
            txtAnnRestschuld.setEditable(false);
            actie = VERWIJDEREN;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Voer enkel bedragen in de bedrag velden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annNieuw() {
        restschuld = txtAnnRestschuld.getText();
        rente = txtAnnRente.getText();
        annuiteit = txtAnnAnnuiteit.getText();
        omschrijving = txtAnnOmschrijving.getText();
        if (restschuld.isEmpty() || rente.isEmpty() || annuiteit.isEmpty()
                || omschrijving.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "Geen van de velden mogen leeggelaten worden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                restschuld = checkDecimaal(restschuld);
                rente = checkDecimaal(rente);
                annuiteit = checkDecimaal(annuiteit);
                AnnuiteitHypo hypo = new AnnuiteitHypo(0, restschuld, rente, omschrijving, annuiteit);
                hypo.nieuweHypo();
                JOptionPane.showMessageDialog(null, 
                        "De hypotheek is toegevoegd.", "Succes", 
                        JOptionPane.INFORMATION_MESSAGE);
                txtAnnRestschuld.setText("");
                txtAnnAnnuiteit.setText("");
                txtAnnRente.setText("");
                txtAnnOmschrijving.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                        "Voer enkel bedragen in de bedrag velden!", "Fout",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //</Annuïteit hypotheek>----------------------------------------------------

    //<Aflossingsvrij hypotheek>------------------------------------------------
    private void aflAanpassen() {
        actie = AANPASSEN;
        btnAflVerwijderen.setText("Opslaan");
        txtAflOmschrijving.setEditable(true);
        txtAflRente.setEditable(true);
        txtAflRestschuld.setEditable(true);
        btnAflAanpassen.setEnabled(false);
    }

    private void aflOpslaan() {
        restschuld = txtAflRestschuld.getText();
        rente = txtAflRente.getText();
        omschrijving = txtAflOmschrijving.getText();
        try {
            restschuld = checkDecimaal(restschuld);
            rente = checkDecimaal(rente);
            AflossingsVrijHypo hypo = new AflossingsVrijHypo(id, restschuld, rente, omschrijving);
            hypo.wijzigHypo();
            JOptionPane.showMessageDialog(null, 
                    "De hypotheek is aangepast.", "Succes", 
                    JOptionPane.INFORMATION_MESSAGE);
            btnAnnVerwijderen.setText("Verwijderen");
            btnAflAanpassen.setEnabled(true);
            txtAflOmschrijving.setEditable(false);
            txtAflRente.setEditable(false);
            txtAflRestschuld.setEditable(false);
            actie = VERWIJDEREN;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Voer enkel bedragen in de bedrag velden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aflNieuw() {
        restschuld = txtAflRestschuld.getText();
        rente = txtAflRente.getText();
        omschrijving = txtAflOmschrijving.getText();
        if (restschuld.isEmpty() || rente.isEmpty() || omschrijving.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "Geen van de velden mogen leeggelaten worden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                restschuld = checkDecimaal(restschuld);
                rente = checkDecimaal(rente);
                AflossingsVrijHypo hypo = new AflossingsVrijHypo(0, restschuld, rente, omschrijving);
                hypo.nieuweHypo();
                JOptionPane.showMessageDialog(null, 
                        "De hypotheek is toegevoegd.", "Succes", 
                        JOptionPane.INFORMATION_MESSAGE);
                txtAflRestschuld.setText("");
                txtAflRente.setText("");
                txtAflOmschrijving.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                        "Voer enkel bedragen in de bedrag velden!", "Fout",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //</Aflossingsvrij hypotheek>-----------------------------------------------

    //<Spaar hypotheek>---------------------------------------------------------
    private void spAanpassen() {
        actie = AANPASSEN;
        btnSpVerwijderen.setText("Opslaan");
        txtSpOmschrijving.setEditable(true);
        txtSpRestschuld.setEditable(true);
        txtSpRente.setEditable(true);
        txtSpMaandelijks.setEditable(true);
        btnSpAanpassen.setEnabled(false);
    }

    private void spOpslaan() {
        restschuld = txtSpRestschuld.getText();
        rente = txtSpRente.getText();
        maandelijks = txtSpMaandelijks.getText();
        omschrijving = txtSpOmschrijving.getText();
        try {
            restschuld = checkDecimaal(restschuld);
            rente = checkDecimaal(rente);
            maandelijks = checkDecimaal(maandelijks);
            SpaarHypo hypo = new SpaarHypo(id, restschuld, rente, omschrijving,
                    maandelijks);
            hypo.wijzigHypo();
            JOptionPane.showMessageDialog(null, 
                    "De hypotheek is aangepast.", "Succes", 
                    JOptionPane.INFORMATION_MESSAGE);
            btnSpVerwijderen.setText("Verwijderen");
            btnSpAanpassen.setEnabled(true);
            txtSpOmschrijving.setEditable(false);
            txtSpRente.setEditable(false);
            txtSpRestschuld.setEditable(false);
            txtSpMaandelijks.setEditable(false);
            actie = VERWIJDEREN;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Voer enkel bedragen in de bedrag velden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void spNieuw() {
        restschuld = txtSpRestschuld.getText();
        rente = txtSpRente.getText();
        maandelijks = txtSpMaandelijks.getText();
        omschrijving = txtSpOmschrijving.getText();
        if (restschuld.isEmpty() || rente.isEmpty() || omschrijving.isEmpty() || maandelijks.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "Geen van de velden mogen leeggelaten worden!", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                restschuld = checkDecimaal(restschuld);
                rente = checkDecimaal(rente);
                maandelijks = checkDecimaal(maandelijks);
                SpaarHypo hypo = new SpaarHypo(0, restschuld, rente, omschrijving,
                        maandelijks);
                hypo.nieuweHypo();
                JOptionPane.showMessageDialog(null, 
                        "De hypotheek is toegevoegd.", "Succes", 
                        JOptionPane.INFORMATION_MESSAGE);
                txtSpRestschuld.setText("");
                txtSpRente.setText("");
                txtSpMaandelijks.setText("");
                txtSpOmschrijving.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                        "Voer enkel bedragen in de bedrag velden!", "Fout",
                        JOptionPane.ERROR_MESSAGE);
            }
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

        btngrp = new javax.swing.ButtonGroup();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pnlLineair = new javax.swing.JPanel();
        lblLinAflossing = new javax.swing.JLabel();
        lblLinRente = new javax.swing.JLabel();
        lblLinRestschuld = new javax.swing.JLabel();
        lblLinOmschrijving = new javax.swing.JLabel();
        txtLinRestschuld = new javax.swing.JTextField();
        txtLinAflossing = new javax.swing.JTextField();
        txtLinRente = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLinOmschrijving = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        btnLinOpslaan = new javax.swing.JButton();
        btnLinVerwijderen = new javax.swing.JButton();
        btnLinAanpassen = new javax.swing.JButton();
        pnlAnnuiteit = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAnnRestschuld = new javax.swing.JTextField();
        txtAnnAnnuiteit = new javax.swing.JTextField();
        txtAnnRente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAnnOmschrijving = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        btnAnnAanpassen = new javax.swing.JButton();
        btnAnnOpslaan = new javax.swing.JButton();
        btnAnnVerwijderen = new javax.swing.JButton();
        pnlAflossingsVrij = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtAflRestschuld = new javax.swing.JTextField();
        txtAflRente = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAflOmschrijving = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        btnAflOpslaan = new javax.swing.JButton();
        btnAflVerwijderen = new javax.swing.JButton();
        btnAflAanpassen = new javax.swing.JButton();
        pnlSpaar = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtSpRestschuld = new javax.swing.JTextField();
        txtSpMaandelijks = new javax.swing.JTextField();
        txtSpRente = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSpOmschrijving = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        btnSpOpslaan = new javax.swing.JButton();
        btnSpVerwijderen = new javax.swing.JButton();
        btnSpAanpassen = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lblSoort = new javax.swing.JLabel();
        btnAflosvrij = new javax.swing.JRadioButton();
        btnAnnu = new javax.swing.JRadioButton();
        btnLineair = new javax.swing.JRadioButton();
        btnSpaar = new javax.swing.JRadioButton();

        lblLinAflossing.setText("Aflossing");

        lblLinRente.setText("Rentepercentage");

        lblLinRestschuld.setText("Restschuld");

        lblLinOmschrijving.setText("Omschrijving");

        txtLinRestschuld.setText("Restschuld");
        txtLinRestschuld.setNextFocusableComponent(txtLinAflossing);
        txtLinRestschuld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLinRestschuldActionPerformed(evt);
            }
        });

        txtLinAflossing.setText("Aflossing");
        txtLinAflossing.setNextFocusableComponent(txtLinRente);

        txtLinRente.setText("Rentepercentage");
        txtLinRente.setNextFocusableComponent(txtLinOmschrijving);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtLinOmschrijving.setColumns(20);
        txtLinOmschrijving.setLineWrap(true);
        txtLinOmschrijving.setRows(5);
        txtLinOmschrijving.setWrapStyleWord(true);
        txtLinOmschrijving.setNextFocusableComponent(txtAflRestschuld);
        txtLinOmschrijving.setPreferredSize(new java.awt.Dimension(166, 94));
        jScrollPane1.setViewportView(txtLinOmschrijving);

        btnLinOpslaan.setText("Opslaan");
        btnLinOpslaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinOpslaanActionPerformed(evt);
            }
        });

        btnLinVerwijderen.setText("Verwijderen");
        btnLinVerwijderen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinVerwijderenActionPerformed(evt);
            }
        });

        btnLinAanpassen.setText("Aanpassen");
        btnLinAanpassen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinAanpassenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnLinAanpassen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnLinVerwijderen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(btnLinOpslaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLinAanpassen)
                    .addComponent(btnLinVerwijderen))
                .addGap(18, 18, 18)
                .addComponent(btnLinOpslaan))
        );

        javax.swing.GroupLayout pnlLineairLayout = new javax.swing.GroupLayout(pnlLineair);
        pnlLineair.setLayout(pnlLineairLayout);
        pnlLineairLayout.setHorizontalGroup(
            pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLineairLayout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addGroup(pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlLineairLayout.createSequentialGroup()
                        .addGroup(pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLinRestschuld, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(lblLinAflossing, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(lblLinRente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblLinOmschrijving, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLinAflossing, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLinRente, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLinRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(266, 266, 266))
        );

        pnlLineairLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, txtLinAflossing, txtLinRente, txtLinRestschuld});

        pnlLineairLayout.setVerticalGroup(
            pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLineairLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLinRestschuld)
                    .addComponent(txtLinRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLinAflossing)
                    .addComponent(txtLinAflossing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLinRente)
                    .addComponent(txtLinRente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlLineairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLinOmschrijving)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jLabel1.setText("Annuïteit");

        jLabel2.setText("Restschuld");

        jLabel3.setText("Rentepercentage");

        jLabel4.setText("Omschrijving");

        txtAnnRestschuld.setText("Restschuld");
        txtAnnRestschuld.setNextFocusableComponent(txtAnnAnnuiteit);
        txtAnnRestschuld.setPreferredSize(new java.awt.Dimension(166, 20));

        txtAnnAnnuiteit.setText("Annuïteit");
        txtAnnAnnuiteit.setNextFocusableComponent(txtAnnRente);
        txtAnnAnnuiteit.setPreferredSize(new java.awt.Dimension(166, 20));

        txtAnnRente.setText("Rentepercentage");
        txtAnnRente.setNextFocusableComponent(txtAnnOmschrijving);
        txtAnnRente.setPreferredSize(new java.awt.Dimension(166, 20));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtAnnOmschrijving.setColumns(20);
        txtAnnOmschrijving.setLineWrap(true);
        txtAnnOmschrijving.setRows(5);
        txtAnnOmschrijving.setWrapStyleWord(true);
        txtAnnOmschrijving.setNextFocusableComponent(txtAnnRestschuld);
        txtAnnOmschrijving.setPreferredSize(new java.awt.Dimension(166, 94));
        jScrollPane2.setViewportView(txtAnnOmschrijving);

        btnAnnAanpassen.setText("Aanpassen");
        btnAnnAanpassen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnAanpassenActionPerformed(evt);
            }
        });

        btnAnnOpslaan.setText("Opslaan");
        btnAnnOpslaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnOpslaanActionPerformed(evt);
            }
        });

        btnAnnVerwijderen.setText("Verwijderen");
        btnAnnVerwijderen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnVerwijderenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnAnnAanpassen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnAnnVerwijderen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(btnAnnOpslaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAnnAanpassen)
                    .addComponent(btnAnnVerwijderen))
                .addGap(18, 18, 18)
                .addComponent(btnAnnOpslaan))
        );

        javax.swing.GroupLayout pnlAnnuiteitLayout = new javax.swing.GroupLayout(pnlAnnuiteit);
        pnlAnnuiteit.setLayout(pnlAnnuiteitLayout);
        pnlAnnuiteitLayout.setHorizontalGroup(
            pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAnnuiteitLayout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addGroup(pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlAnnuiteitLayout.createSequentialGroup()
                        .addGroup(pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAnnuiteitLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAnnuiteitLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtAnnRente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAnnuiteitLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtAnnRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAnnuiteitLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtAnnAnnuiteit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(266, 266, 266))
        );

        pnlAnnuiteitLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4});

        pnlAnnuiteitLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane2, txtAnnAnnuiteit, txtAnnRente, txtAnnRestschuld});

        pnlAnnuiteitLayout.setVerticalGroup(
            pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAnnuiteitLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAnnRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtAnnAnnuiteit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAnnRente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAnnuiteitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jLabel5.setText("Restschuld");

        jLabel6.setText("Rentepercentage");

        jLabel7.setText("Omschrijving");

        txtAflRestschuld.setText("Restschuld");
        txtAflRestschuld.setNextFocusableComponent(txtAflRente);
        txtAflRestschuld.setPreferredSize(new java.awt.Dimension(166, 20));

        txtAflRente.setText("Rentepercentage");
        txtAflRente.setNextFocusableComponent(txtAflOmschrijving);
        txtAflRente.setPreferredSize(new java.awt.Dimension(166, 20));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtAflOmschrijving.setColumns(20);
        txtAflOmschrijving.setLineWrap(true);
        txtAflOmschrijving.setRows(5);
        txtAflOmschrijving.setWrapStyleWord(true);
        txtAflOmschrijving.setNextFocusableComponent(txtAflRestschuld);
        txtAflOmschrijving.setPreferredSize(new java.awt.Dimension(166, 94));
        jScrollPane3.setViewportView(txtAflOmschrijving);

        btnAflOpslaan.setText("Opslaan");
        btnAflOpslaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAflOpslaanActionPerformed(evt);
            }
        });

        btnAflVerwijderen.setText("Verwijderen");
        btnAflVerwijderen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAflVerwijderenActionPerformed(evt);
            }
        });

        btnAflAanpassen.setText("Aanpassen");
        btnAflAanpassen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAflAanpassenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnAflAanpassen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnAflVerwijderen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(btnAflOpslaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAflAanpassen)
                    .addComponent(btnAflVerwijderen))
                .addGap(18, 18, 18)
                .addComponent(btnAflOpslaan))
        );

        javax.swing.GroupLayout pnlAflossingsVrijLayout = new javax.swing.GroupLayout(pnlAflossingsVrij);
        pnlAflossingsVrij.setLayout(pnlAflossingsVrijLayout);
        pnlAflossingsVrijLayout.setHorizontalGroup(
            pnlAflossingsVrijLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAflossingsVrijLayout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addGroup(pnlAflossingsVrijLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlAflossingsVrijLayout.createSequentialGroup()
                        .addGroup(pnlAflossingsVrijLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAflossingsVrijLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAflossingsVrijLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtAflRente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAflossingsVrijLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txtAflRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(266, 266, 266))
        );

        pnlAflossingsVrijLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel5, jLabel6, jLabel7});

        pnlAflossingsVrijLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane3, txtAflRente, txtAflRestschuld});

        pnlAflossingsVrijLayout.setVerticalGroup(
            pnlAflossingsVrijLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAflossingsVrijLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(pnlAflossingsVrijLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAflRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAflossingsVrijLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtAflRente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAflossingsVrijLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );

        jLabel8.setText("Restschuld");
        jLabel8.setPreferredSize(new java.awt.Dimension(52, 20));

        jLabel9.setText("Rentepercentage");

        jLabel10.setText("Maandelijkse kosten");

        jLabel11.setText("Omschrijving");

        txtSpRestschuld.setText("Restschuld");

        txtSpMaandelijks.setText("Maandeliijkse kosten");
        txtSpMaandelijks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSpMaandelijksActionPerformed(evt);
            }
        });

        txtSpRente.setText("Rentepercentage");

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtSpOmschrijving.setColumns(20);
        txtSpOmschrijving.setLineWrap(true);
        txtSpOmschrijving.setRows(5);
        txtSpOmschrijving.setWrapStyleWord(true);
        txtSpOmschrijving.setPreferredSize(new java.awt.Dimension(166, 94));
        jScrollPane4.setViewportView(txtSpOmschrijving);

        btnSpOpslaan.setText("Opslaan");
        btnSpOpslaan.setPreferredSize(new java.awt.Dimension(89, 23));
        btnSpOpslaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpOpslaanActionPerformed(evt);
            }
        });

        btnSpVerwijderen.setText("Verwijderen");
        btnSpVerwijderen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpVerwijderenActionPerformed(evt);
            }
        });

        btnSpAanpassen.setText("Aanpassen");
        btnSpAanpassen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpAanpassenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnSpAanpassen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnSpVerwijderen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addComponent(btnSpOpslaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSpAanpassen)
                    .addComponent(btnSpVerwijderen))
                .addGap(18, 18, 18)
                .addComponent(btnSpOpslaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlSpaarLayout = new javax.swing.GroupLayout(pnlSpaar);
        pnlSpaar.setLayout(pnlSpaarLayout);
        pnlSpaarLayout.setHorizontalGroup(
            pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpaarLayout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addGroup(pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlSpaarLayout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlSpaarLayout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(18, 18, 18)
                            .addComponent(txtSpRente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlSpaarLayout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(18, 18, 18)
                            .addComponent(txtSpMaandelijks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlSpaarLayout.createSequentialGroup()
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtSpRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(260, 260, 260))
        );

        pnlSpaarLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel8, jLabel9});

        pnlSpaarLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane4, txtSpMaandelijks, txtSpRente, txtSpRestschuld});

        pnlSpaarLayout.setVerticalGroup(
            pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpaarLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSpRestschuld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtSpMaandelijks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSpRente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlSpaarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        pnlSpaarLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel8, jLabel9});

        pnlSpaarLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtSpMaandelijks, txtSpRente, txtSpRestschuld});

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlLineair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlAnnuiteit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlAflossingsVrij, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlSpaar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlLineair, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlAnnuiteit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlAflossingsVrij, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlSpaar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1.setLayer(pnlLineair, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlAnnuiteit, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlAflossingsVrij, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlSpaar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lblSoort.setText("Soort Hypotheek");

        btngrp.add(btnAflosvrij);
        btnAflosvrij.setText("Aflossingsvrij");
        btnAflosvrij.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAflosvrijActionPerformed(evt);
            }
        });

        btngrp.add(btnAnnu);
        btnAnnu.setText("Annuïteit");
        btnAnnu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnuActionPerformed(evt);
            }
        });

        btngrp.add(btnLineair);
        btnLineair.setSelected(true);
        btnLineair.setText("Lineair");
        btnLineair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLineairActionPerformed(evt);
            }
        });

        btngrp.add(btnSpaar);
        btnSpaar.setText("Spaar");
        btnSpaar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpaarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnLineair)
                .addGap(14, 14, 14)
                .addComponent(btnAflosvrij)
                .addGap(14, 14, 14)
                .addComponent(btnAnnu)
                .addGap(14, 14, 14)
                .addComponent(btnSpaar)
                .addGap(32, 32, 32)
                .addComponent(lblSoort)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnLineair))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAflosvrij)
                    .addComponent(btnAnnu)))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSpaar)
                    .addComponent(lblSoort)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLinAanpassenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinAanpassenActionPerformed
        linAanpassen();
    }//GEN-LAST:event_btnLinAanpassenActionPerformed

    private void btnLinVerwijderenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinVerwijderenActionPerformed
        if (actie == VERWIJDEREN) {
            verwijderen();
        } else if (actie == AANPASSEN) {
            linOpslaan();
        }
    }//GEN-LAST:event_btnLinVerwijderenActionPerformed

    private void btnAnnAanpassenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnAanpassenActionPerformed
        annAanpassen();
    }//GEN-LAST:event_btnAnnAanpassenActionPerformed

    private void btnAnnVerwijderenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnVerwijderenActionPerformed
        if (actie == VERWIJDEREN) {
            verwijderen();
        } else if (actie == AANPASSEN) {
            annOpslaan();
        }
    }//GEN-LAST:event_btnAnnVerwijderenActionPerformed

    private void btnAflAanpassenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAflAanpassenActionPerformed
        aflAanpassen();
    }//GEN-LAST:event_btnAflAanpassenActionPerformed

    private void btnAflVerwijderenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAflVerwijderenActionPerformed
        if (actie == VERWIJDEREN) {
            verwijderen();
        } else if (actie == AANPASSEN) {
            aflOpslaan();
        }
    }//GEN-LAST:event_btnAflVerwijderenActionPerformed

    private void btnLinOpslaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinOpslaanActionPerformed
        linNieuw();
    }//GEN-LAST:event_btnLinOpslaanActionPerformed

    private void btnAnnOpslaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnOpslaanActionPerformed
        annNieuw();
    }//GEN-LAST:event_btnAnnOpslaanActionPerformed

    private void btnAflOpslaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAflOpslaanActionPerformed
        aflNieuw();
    }//GEN-LAST:event_btnAflOpslaanActionPerformed

    private void btnLineairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLineairActionPerformed
        pnlLineair.setVisible(true);
        txtLinRestschuld.setText("");
        txtLinAflossing.setText("");
        txtLinRente.setText("");
        txtLinOmschrijving.setText("");
        pnlAnnuiteit.setVisible(false);
        pnlAflossingsVrij.setVisible(false);

        btnLinAanpassen.setVisible(false);
        btnLinVerwijderen.setVisible(false);
        btnLinOpslaan.setVisible(true);
    }//GEN-LAST:event_btnLineairActionPerformed

    private void btnAflosvrijActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAflosvrijActionPerformed
        pnlAflossingsVrij.setVisible(true);
        txtAflRestschuld.setText("");
        txtAflRente.setText("");
        txtAflOmschrijving.setText("");
        pnlAnnuiteit.setVisible(false);
        pnlLineair.setVisible(false);

        btnAflAanpassen.setVisible(false);
        btnAflVerwijderen.setVisible(false);
        btnAflOpslaan.setVisible(true);
    }//GEN-LAST:event_btnAflosvrijActionPerformed

    private void btnAnnuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnuActionPerformed
        pnlAnnuiteit.setVisible(true);
        txtAnnRestschuld.setText("");
        txtAnnAnnuiteit.setText("");
        txtAnnRente.setText("");
        txtAnnOmschrijving.setText("");
        pnlAflossingsVrij.setVisible(false);
        pnlLineair.setVisible(false);

        btnAnnAanpassen.setVisible(false);
        btnAnnVerwijderen.setVisible(false);
        btnAnnOpslaan.setVisible(true);
    }//GEN-LAST:event_btnAnnuActionPerformed

    private void btnSpaarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpaarActionPerformed
        pnlSpaar.setVisible(true);
        txtSpRestschuld.setText("");
        txtSpMaandelijks.setText("");
        txtSpRente.setText("");
        txtSpOmschrijving.setText("");
        pnlAflossingsVrij.setVisible(false);
        pnlLineair.setVisible(false);
        pnlAnnuiteit.setVisible(false);

        btnSpAanpassen.setVisible(false);
        btnSpVerwijderen.setVisible(false);
        btnSpOpslaan.setVisible(true);
    }//GEN-LAST:event_btnSpaarActionPerformed

    private void txtSpMaandelijksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSpMaandelijksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpMaandelijksActionPerformed

    private void btnSpAanpassenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpAanpassenActionPerformed
        spAanpassen();
    }//GEN-LAST:event_btnSpAanpassenActionPerformed

    private void btnSpVerwijderenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpVerwijderenActionPerformed
        if (actie == VERWIJDEREN) {
            verwijderen();
        } else if (actie == AANPASSEN) {
            spOpslaan();
        }
    }//GEN-LAST:event_btnSpVerwijderenActionPerformed

    private void btnSpOpslaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpOpslaanActionPerformed
        spNieuw();
    }//GEN-LAST:event_btnSpOpslaanActionPerformed

    private void txtLinRestschuldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLinRestschuldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLinRestschuldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAflAanpassen;
    private javax.swing.JButton btnAflOpslaan;
    private javax.swing.JButton btnAflVerwijderen;
    private javax.swing.JRadioButton btnAflosvrij;
    private javax.swing.JButton btnAnnAanpassen;
    private javax.swing.JButton btnAnnOpslaan;
    private javax.swing.JButton btnAnnVerwijderen;
    private javax.swing.JRadioButton btnAnnu;
    private javax.swing.JButton btnLinAanpassen;
    private javax.swing.JButton btnLinOpslaan;
    private javax.swing.JButton btnLinVerwijderen;
    private javax.swing.JRadioButton btnLineair;
    private javax.swing.JButton btnSpAanpassen;
    private javax.swing.JButton btnSpOpslaan;
    private javax.swing.JButton btnSpVerwijderen;
    private javax.swing.JRadioButton btnSpaar;
    private javax.swing.ButtonGroup btngrp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblLinAflossing;
    private javax.swing.JLabel lblLinOmschrijving;
    private javax.swing.JLabel lblLinRente;
    private javax.swing.JLabel lblLinRestschuld;
    private javax.swing.JLabel lblSoort;
    private javax.swing.JPanel pnlAflossingsVrij;
    private javax.swing.JPanel pnlAnnuiteit;
    private javax.swing.JPanel pnlLineair;
    private javax.swing.JPanel pnlSpaar;
    private javax.swing.JTextArea txtAflOmschrijving;
    private javax.swing.JTextField txtAflRente;
    private javax.swing.JTextField txtAflRestschuld;
    private javax.swing.JTextField txtAnnAnnuiteit;
    private javax.swing.JTextArea txtAnnOmschrijving;
    private javax.swing.JTextField txtAnnRente;
    private javax.swing.JTextField txtAnnRestschuld;
    private javax.swing.JTextField txtLinAflossing;
    private javax.swing.JTextArea txtLinOmschrijving;
    private javax.swing.JTextField txtLinRente;
    private javax.swing.JTextField txtLinRestschuld;
    private javax.swing.JTextField txtSpMaandelijks;
    private javax.swing.JTextArea txtSpOmschrijving;
    private javax.swing.JTextField txtSpRente;
    private javax.swing.JTextField txtSpRestschuld;
    // End of variables declaration//GEN-END:variables
}
