/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Objects.Wijziging;
import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import system.Database;
import system.DateLabelFormatter;
import system.Main;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class WijzigingDetail extends javax.swing.JPanel {

    /**
     * Creates new form WijzigingDetail
     */
    int wijzigingId = -1, jaar, maand, dag;
    String datum, soort, in, uit, categorie, omschrijving, herhaling;
    double inOud, uitOud, inNieuw, uitNieuw;
    final DecimalFormat deciform = new DecimalFormat("0.00");
    JDatePickerImpl datePicker = null;
    JDatePanelImpl datePanel = null;
    DateFormat form = new SimpleDateFormat("dd/MM/yyyy");
    SqlDateModel model = new SqlDateModel();
    String temp; //String voor temporary usage
    boolean isOverzicht;
    String tijd = null;

    public WijzigingDetail() {
        initComponents();
        //Voeg datepicker toe
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setSize(120, 30);
        pnlDatum.add(datePicker);
        //Datepicker toegevoegd
        setLetterGrootte(Main.getLetterGrootte());
        btnOpslaan.setVisible(false);
    }

    public WijzigingDetail(int id, boolean isOverzicht) {
        this();
        Database db = new Database();
        this.isOverzicht = isOverzicht;

        ResultSet rs = db.getWijzigingDetails(id);
        try {
            while (rs.next()) {
                soort = rs.getString(1);
                in = rs.getString(2);
                uit = rs.getString(3);
                categorie = rs.getString(4);
                omschrijving = rs.getString(5);
                datum = rs.getString(6);
                String[] tempS = datum.split("-");
                jaar = Integer.parseInt(tempS[0]);
                maand = Integer.parseInt(tempS[1]) - 1;
                dag = Integer.parseInt(tempS[2].split(" ")[0]);
                if (soort.equals("Vast")) {
                    herhaling = rs.getString(7);
                    switch (rs.getString(7)) {
                        case "Maandelijks":
                            herhaling = "Elke maand";
                            break;
                        case "3 maanden":
                            herhaling = "Elke 3 maanden";
                            break;
                        case "Half jaarlijks":
                            herhaling = "Elk half jaar";
                            break;
                        case "Jaarlijks":
                            herhaling = "Elk jaar";
                            break;
                        default:
                            herhaling = rs.getString(7);
                    }
                }
                wijzigingId = rs.getInt(8);
            }
        } catch (SQLException e) {
            Main.showError("053", e);
        }
        model.setDate(jaar, maand, dag);
        model.setSelected(true);
        txtIn.setText(this.in);
        if (!this.in.equals("-")) {
            if (this.in.contains(",")) {
                this.in = in.replace(",", ".");
            }
            inOud = Double.parseDouble(this.in);
        }
        if (!this.uit.equals("-")) {
            txtUit.setForeground(Color.RED);
            txtUit.setDisabledTextColor(Color.RED);
            if (this.uit.contains(",")) {
                this.uit = this.uit.replace(",", ".");
            }
            uitOud = Double.parseDouble(this.uit);
        }
        txtUit.setText(this.uit);
        cmbStatus.setSelectedItem(soort);
        if (soort.equals("Vast")) {
            cmbHerhaling.setVisible(true);
            lblHerhaling.setVisible(true);
            cmbHerhaling.setSelectedItem(herhaling);
        } else {
            cmbHerhaling.setVisible(false);
            lblHerhaling.setVisible(false);
        }
        if (!txtIn.getText().equals("-") && txtUit.getText().equals("-")) {
            cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("in", false)));
        } else if (txtIn.getText().equals("-") && !txtUit.getText().equals("-")) {
            cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("uit", false)));

        }
        cmbCategorie.setSelectedItem(categorie);
        txtOmschrijving.setText(omschrijving);
        if (categorie.equals("Hypotheek")) {
            btnWijzigen.setEnabled(false);
            btnVerwijderen.setEnabled(false);
        }

    }
    
    private void setLetterGrootte(int grootte){
        Font font = new Font("Tahoma", 0, grootte);
        btnOpslaan.setFont(font);
        btnVerwijderen.setFont(font);
        btnWijzigen.setFont(font);
        cmbCategorie.setFont(font);
        cmbHerhaling.setFont(font);
        cmbStatus.setFont(font);
        datePanel.setFont(font);
        datePicker.setFont(font);
        jLabel1.setFont(font);
        lblCategorie.setFont(font);
        lblDatum.setFont(font);
        lblHerhaling.setFont(font);
        lblIn.setFont(font);
        lblOmschrijving.setFont(font);
        lblUit.setFont(font);
        txtIn.setFont(font);
        txtOmschrijving.setFont(font);
        txtUit.setFont(font);
    }

    private void wijzig() {
        boolean succes = true;
        Database db = new Database();
        if (tijd == null) {
            tijd = " " + datum.split(" ")[1];
        }
        datum = datePicker.getModel().getValue().toString();
        in = txtIn.getText();
        uit = txtUit.getText();
        soort = cmbStatus.getSelectedItem().toString();
        categorie = cmbCategorie.getSelectedItem().toString();
        if (cmbHerhaling.isVisible()) {
            String herhalingCmb= cmbHerhaling.getSelectedItem().toString();
            switch(herhalingCmb){
                case "Elke maand":
                    herhaling = "Maandelijks";
                    break;
                case "Elke 3 maanden":
                    herhaling = "3 maanden";
                    break;
                case "Elk half jaar":
                    herhaling = "Half jaarlijks";
                    break;
                case "Elk jaar":
                    herhaling = "Jaarlijks";
                    break;
            }
        } else {
            herhaling = "";
        }
        omschrijving = txtOmschrijving.getText();

        if (!txtIn.getText().equals("-") && !txtUit.getText().equals("-")) {
            JOptionPane.showMessageDialog(null, 
                    "'In' en 'Uit' kunnen niet beiden een bedrag bevatten!",
                    "Fout", JOptionPane.ERROR_MESSAGE);
            succes = false;
        }

        if (succes) {
            try {
                double verschil = 0;
                if (!in.equals("-")) {
                    if (in.contains(",")) {
                        in = in.replace(",", ".");
                    }
                    inNieuw = Double.parseDouble(in);
                    in = deciform.format(inNieuw);
                    in = in.replace(",", ".");
                    verschil = inNieuw - inOud;
                }
                if (!uit.equals("-")) {
                    if (uit.contains(",")) {
                        uit = uit.replace(",", ".");
                    }
                    uitNieuw = Double.parseDouble(uit);
                    uit = deciform.format(uitNieuw);
                    uit = uit.replace(",", ".");
                    verschil = (uitNieuw - uitOud) * -1;
                }
                Wijziging wijziging = new Wijziging(wijzigingId, soort, in, uit, omschrijving, datum + tijd, herhaling, categorie);
                wijziging.wijzigInvoer();
                if (isOverzicht) {
                    db.setNieuwSaldo(verschil);
                }
                JOptionPane.showMessageDialog(null, 
                        "De wijziging is aangepast.", "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                btnWijzigen.setEnabled(true);
                btnOpslaan.setVisible(false);
                btnVerwijderen.setVisible(true);
                txtIn.setEnabled(false);
                txtUit.setEnabled(false);
                cmbStatus.setEnabled(false);
                cmbCategorie.setEnabled(false);
                txtOmschrijving.setEnabled(false);
                inOud = inNieuw;
                uitOud = uitNieuw;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                        "Het bedrag mag alleen uit (decimale) "
                        + "getallen bestaan!", "Fout", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verwijder() {
        double bedrag = 0;
        if (!in.equals("-")) {
            if (in.contains(",")) {
                in = in.replace(",", ".");
            }
            bedrag = Double.parseDouble(in) * -1;
        } else if (!uit.equals("-")) {
            if (uit.contains(",")) {
                uit = uit.replace(",", ".");
            }
            bedrag = Double.parseDouble(uit);
        }
        Database db = new Database();
        int option = JOptionPane.showOptionDialog(null, 
                "Weet je zeker dat je deze wijziging wil verwijderen?",
                "Bevestigen", JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, new Object[]{"Ja", "Nee"}, "Ja");
        if (option == JOptionPane.YES_OPTION) {
            db.verwijderWijziging(wijzigingId);
            if (isOverzicht) {
                db.setNieuwSaldo(bedrag);
            }
            JOptionPane.showMessageDialog(null, 
                    "De wijziging is verwijderd", "Succes", 
                    JOptionPane.INFORMATION_MESSAGE);
            Main.closePopup(isOverzicht);
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

        jPanel1 = new javax.swing.JPanel();
        lblDatum = new javax.swing.JLabel();
        pnlDatum = new javax.swing.JPanel();
        lblIn = new javax.swing.JLabel();
        lblUit = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblCategorie = new javax.swing.JLabel();
        lblHerhaling = new javax.swing.JLabel();
        txtIn = new javax.swing.JTextField();
        txtUit = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox();
        cmbCategorie = new javax.swing.JComboBox();
        cmbHerhaling = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtOmschrijving = new javax.swing.JTextArea();
        lblOmschrijving = new javax.swing.JLabel();
        btnWijzigen = new javax.swing.JButton();
        btnVerwijderen = new javax.swing.JButton();
        btnOpslaan = new javax.swing.JButton();

        lblDatum.setText("Datum");
        lblDatum.setPreferredSize(new java.awt.Dimension(70, 30));

        pnlDatum.setPreferredSize(new java.awt.Dimension(120, 30));

        javax.swing.GroupLayout pnlDatumLayout = new javax.swing.GroupLayout(pnlDatum);
        pnlDatum.setLayout(pnlDatumLayout);
        pnlDatumLayout.setHorizontalGroup(
            pnlDatumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        pnlDatumLayout.setVerticalGroup(
            pnlDatumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        lblIn.setText("In");
        lblIn.setPreferredSize(new java.awt.Dimension(70, 30));

        lblUit.setText("Uit");
        lblUit.setPreferredSize(new java.awt.Dimension(70, 30));

        jLabel1.setText("Status");
        jLabel1.setPreferredSize(new java.awt.Dimension(50, 30));

        lblCategorie.setText("Categorie");
        lblCategorie.setPreferredSize(new java.awt.Dimension(70, 30));

        lblHerhaling.setText("Herhaling");
        lblHerhaling.setPreferredSize(new java.awt.Dimension(70, 30));

        txtIn.setText("In tekst");
        txtIn.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtIn.setEnabled(false);
        txtIn.setNextFocusableComponent(txtUit);
        txtIn.setPreferredSize(new java.awt.Dimension(59, 30));
        txtIn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInFocusLost(evt);
            }
        });

        txtUit.setText("Uit tekst");
        txtUit.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtUit.setEnabled(false);
        txtUit.setNextFocusableComponent(txtOmschrijving);
        txtUit.setPreferredSize(new java.awt.Dimension(59, 30));
        txtUit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUitFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUitFocusLost(evt);
            }
        });

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Vast", "Variabel" }));
        cmbStatus.setEnabled(false);
        cmbStatus.setPreferredSize(new java.awt.Dimension(100, 30));
        cmbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusActionPerformed(evt);
            }
        });

        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Verzekering", "Hypotheek", "Eiland", "Auto", "Boodschappen", "Hobby", "Gemeente", "Belasting", "Onderhoud huis" ,"Diversen" }));
        cmbCategorie.setEnabled(false);
        cmbCategorie.setPreferredSize(new java.awt.Dimension(100, 30));

        cmbHerhaling.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Elke maand", "Elke 3 maanden", "Elk half jaar", "Elk jaar" }));
        cmbHerhaling.setEnabled(false);
        cmbHerhaling.setPreferredSize(new java.awt.Dimension(100, 30));

        txtOmschrijving.setColumns(20);
        txtOmschrijving.setLineWrap(true);
        txtOmschrijving.setRows(5);
        txtOmschrijving.setText("Omschrijving tekst\n");
        txtOmschrijving.setWrapStyleWord(true);
        txtOmschrijving.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtOmschrijving.setEnabled(false);
        txtOmschrijving.setNextFocusableComponent(txtIn);
        jScrollPane2.setViewportView(txtOmschrijving);

        lblOmschrijving.setText("Omschrijving");
        lblOmschrijving.setPreferredSize(new java.awt.Dimension(70, 30));

        btnWijzigen.setText("Wijzigen");
        btnWijzigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWijzigenActionPerformed(evt);
            }
        });

        btnVerwijderen.setText("Verwijderen");
        btnVerwijderen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerwijderenActionPerformed(evt);
            }
        });

        btnOpslaan.setText("Opslaan");
        btnOpslaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpslaanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDatum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblUit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(20, 20, 20)))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblCategorie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblHerhaling, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                        .addGap(26, 26, 26)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, 112, Short.MAX_VALUE)
                            .addComponent(cmbCategorie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbHerhaling, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblOmschrijving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(130, 130, 130)))
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnWijzigen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnVerwijderen)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnOpslaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(4, 4, 4)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblOmschrijving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lblIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(lblUit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pnlDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txtIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(txtUit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblHerhaling, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbHerhaling, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnWijzigen)
                    .addComponent(btnVerwijderen)
                    .addComponent(btnOpslaan))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        if (cmbStatus.getSelectedItem().equals("Vast")) {
            lblHerhaling.setVisible(true);
            cmbHerhaling.setVisible(true);
            cmbHerhaling.setSelectedItem(herhaling);
        } else {
            lblHerhaling.setVisible(false);
            cmbHerhaling.setVisible(false);
        }
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void btnWijzigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWijzigenActionPerformed
        txtIn.setEnabled(true);
        txtUit.setEnabled(true);
        cmbStatus.setEnabled(true);
        cmbCategorie.setEnabled(true);
        txtOmschrijving.setEnabled(true);
        btnVerwijderen.setVisible(false);
        btnOpslaan.setVisible(true);
        btnWijzigen.setEnabled(false);
    }//GEN-LAST:event_btnWijzigenActionPerformed

    private void btnOpslaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpslaanActionPerformed
        wijzig();
    }//GEN-LAST:event_btnOpslaanActionPerformed

    private void btnVerwijderenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerwijderenActionPerformed
        verwijder();
    }//GEN-LAST:event_btnVerwijderenActionPerformed

    private void txtInFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInFocusGained
        temp = txtIn.getText();
        txtIn.setText("");
    }//GEN-LAST:event_txtInFocusGained

    private void txtInFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInFocusLost
        if (txtIn.getText().isEmpty()) {
            if (txtUit.getText().equals("-")) {
                txtIn.setText(temp);
            } else {
                txtIn.setText("-");
                cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("uit", false)));
            }
        }
    }//GEN-LAST:event_txtInFocusLost

    private void txtUitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUitFocusGained
        temp = txtUit.getText();
        txtUit.setText("");
    }//GEN-LAST:event_txtUitFocusGained

    private void txtUitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUitFocusLost
        if (txtUit.getText().isEmpty()) {
            if (txtIn.getText().equals("-")) {
                txtUit.setText(temp);
            } else {
                txtUit.setText("-");
                cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("in", false)));
            }
        }
    }//GEN-LAST:event_txtUitFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpslaan;
    private javax.swing.JButton btnVerwijderen;
    private javax.swing.JButton btnWijzigen;
    private javax.swing.JComboBox cmbCategorie;
    private javax.swing.JComboBox cmbHerhaling;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCategorie;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblHerhaling;
    private javax.swing.JLabel lblIn;
    private javax.swing.JLabel lblOmschrijving;
    private javax.swing.JLabel lblUit;
    private javax.swing.JPanel pnlDatum;
    private javax.swing.JTextField txtIn;
    private javax.swing.JTextArea txtOmschrijving;
    private javax.swing.JTextField txtUit;
    // End of variables declaration//GEN-END:variables
}
