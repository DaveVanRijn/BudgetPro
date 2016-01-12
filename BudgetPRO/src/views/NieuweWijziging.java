/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Objects.Wijziging;
import enums.Gebruiker;
import java.awt.BorderLayout;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Font;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
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
public class NieuweWijziging extends javax.swing.JPanel {

    /**
     * Creates new form Wijziging
     */
    String soort; //Vast of variabel
    String status; //In of Uit
    String bedragString; //Bedrag in een String
    double bedrag; //Het in- of uitgaande bedrag
    String in, uit; //Ingaand bedrag in String, Uitgaand bedrag in String
    String categorie; //Categorie in String
    String omschrijving; //Omschrijving
    Date datum; //Datum van datePicker
    String herhaling; //De herhaling in een String
    int id; //ID van de wijziging
    double rente; //Rente percentage
    JDatePickerImpl datePicker = null; //datePicker
    JDatePanelImpl datePanel = null; //Panel van de datePicker
    DateFormat form = new SimpleDateFormat("dd/MM/yyyy"); //Format van de datum uit de datePicker
    DecimalFormat doubleFormat = new DecimalFormat("0.00"); //Format for double numbers
    boolean succes; //controle van de bedragen

    public NieuweWijziging() {
        initComponents();
        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("uit", false)));
        rbtnUit.setSelected(true);
        rbtnVariabel.setSelected(true);
        //Voeg datepicker toe
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setSize(120, 30);
        pnlDatePicker.add(datePicker);
        setLetterGrootte(Main.getLetterGrootte());
        lblHerhaling.setVisible(false);
        cmbHerhaling.setVisible(false);
    }

    private void setLetterGrootte(int grootte) {
        Font font = new Font("Tahoma", 0, grootte);
        btnAnnuleren.setFont(font);
        btnBevestigen.setFont(font);
        btnNieuwCat.setFont(font);
        btnOk.setFont(font);
        btnWijzigen.setFont(font);
        cmbCategorie.setFont(font);
        cmbHerhaling.setFont(font);
        datePanel.setFont(font);
        datePicker.setFont(font);
        lblBedrag.setFont(font);
        lblCategorie.setFont(font);
        lblDatum.setFont(font);
        lblDetails.setFont(font);
        lblHerhaling.setFont(font);
        lblOmschrijving.setFont(font);
        rbtnIn.setFont(font);
        rbtnUit.setFont(font);
        rbtnVariabel.setFont(font);
        rbtnVast.setFont(font);
        txtBedrag.setFont(font);
        txtDetails.setFont(font);
        txtOmschrijving.setFont(font);
        this.setPreferredSize(this.getPreferredSize());
        this.validate();
    }

    /**
     *
     * @param string Het bedrag in een String
     * @param decimaal Of het bedrag uit 2 decimalen moet bestaan
     * @return Bij succes: het bedrag in deci format in een String, bij geen
     * succes: originele String
     */
    private String checkDecimaal(String string, boolean decimaal) {
        String tempor = string;
        double temp;
        try {
            if (string.contains(",")) {
                string = string.replace(",", ".");
            }
            temp = Double.parseDouble(string);
            if (decimaal) {
                string = doubleFormat.format(temp);
                string = string.replace(",", ".");
            } else {
                string = Double.toString(temp);
            }
            succes = true;
        } catch (NumberFormatException e) {
            succes = false;
            JOptionPane.showMessageDialog(null,
                    "Gebruik alleen (decimale) getallen voor de bedragen!",
                    "Fout", JOptionPane.ERROR_MESSAGE);
        }
        if (succes) {
            return string;
        } else {
            return tempor;
        }
    }

    private void nieuwCat() {
        Database db = new Database();
        JPanel panel = new JPanel();
        JTextField txtNaam = new JTextField(15);
        JLabel Title = new JLabel("Naam:");
        JRadioButton in = new JRadioButton("In");
        JRadioButton uit = new JRadioButton("Uit");
        ButtonGroup group = new ButtonGroup();
        uit.setSelected(true);
        group.add(in);
        group.add(uit);
        txtNaam.setAlignmentX(LEFT_ALIGNMENT);
        Title.setAlignmentX(LEFT_ALIGNMENT);
        Box box = new Box(BoxLayout.PAGE_AXIS);

        box.add(Title);
        box.add(txtNaam);
        box.add(Box.createVerticalStrut(15));
        box.add(in);
        box.add(uit);
        panel.add(box, BorderLayout.CENTER);

        boolean Succes;
        do {
            int optie = JOptionPane.showOptionDialog(null, panel, "Nieuw",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Ok", "Annuleren"}, "Ok");
            if (optie == JOptionPane.OK_OPTION) {
                if (txtNaam.getText().isEmpty()) {
                    Succes = false;
                    JOptionPane.showMessageDialog(null,
                            "Vul een naam voor de categorie in!", "Fout",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Succes = true;
                    String naamNieuw = txtNaam.getText();
                    String inUitNieuw = (group.getSelection() == in.getModel()) ? "in" : "uit";
                    db.addCategorie(naamNieuw, inUitNieuw);
                    if (btngrpInUit.getSelection() == rbtnIn.getModel()) {
                        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("in", false)));
                    } else {
                        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("uit", false)));
                    }
                    JOptionPane.showMessageDialog(null,
                            "De categorie is toegevoegd.", "Succes",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                Succes = true;
            }
        } while (!Succes);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngrpInUit = new javax.swing.ButtonGroup();
        btngrpVastVar = new javax.swing.ButtonGroup();
        lblBedrag = new javax.swing.JLabel();
        lblDatum = new javax.swing.JLabel();
        lblOmschrijving = new javax.swing.JLabel();
        lblCategorie = new javax.swing.JLabel();
        txtBedrag = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOmschrijving = new javax.swing.JTextArea();
        cmbCategorie = new javax.swing.JComboBox();
        lblHerhaling = new javax.swing.JLabel();
        cmbHerhaling = new javax.swing.JComboBox();
        btnAnnuleren = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        pnlDatePicker = new javax.swing.JPanel();
        rbtnIn = new javax.swing.JRadioButton();
        rbtnUit = new javax.swing.JRadioButton();
        rbtnVast = new javax.swing.JRadioButton();
        rbtnVariabel = new javax.swing.JRadioButton();
        btnNieuwCat = new javax.swing.JButton();
        pnlDetails = new javax.swing.JPanel();
        lblDetails = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDetails = new javax.swing.JTextArea();
        btnWijzigen = new javax.swing.JButton();
        btnBevestigen = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(800, 500));

        lblBedrag.setText("Bedrag");

        lblDatum.setText("Datum");

        lblOmschrijving.setText("Omschrijving");

        lblCategorie.setText("Categorie");

        txtOmschrijving.setColumns(20);
        txtOmschrijving.setLineWrap(true);
        txtOmschrijving.setRows(5);
        txtOmschrijving.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtOmschrijving);

        cmbCategorie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategorieActionPerformed(evt);
            }
        });

        lblHerhaling.setText("Herhaling");

        cmbHerhaling.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Elke maand", "Elke 3 maanden", "Elk half jaar", "Elk jaar" }));

        btnAnnuleren.setText("Annuleren");
        btnAnnuleren.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnulerenActionPerformed(evt);
            }
        });

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        pnlDatePicker.setMinimumSize(new java.awt.Dimension(100, 20));
        pnlDatePicker.setPreferredSize(new java.awt.Dimension(120, 30));

        javax.swing.GroupLayout pnlDatePickerLayout = new javax.swing.GroupLayout(pnlDatePicker);
        pnlDatePicker.setLayout(pnlDatePickerLayout);
        pnlDatePickerLayout.setHorizontalGroup(
            pnlDatePickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        pnlDatePickerLayout.setVerticalGroup(
            pnlDatePickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        btngrpInUit.add(rbtnIn);
        rbtnIn.setText("In");
        rbtnIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnInActionPerformed(evt);
            }
        });

        btngrpInUit.add(rbtnUit);
        rbtnUit.setText("Uit");
        rbtnUit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnUitActionPerformed(evt);
            }
        });

        btngrpVastVar.add(rbtnVast);
        rbtnVast.setText("Vast");
        rbtnVast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnVastActionPerformed(evt);
            }
        });

        btngrpVastVar.add(rbtnVariabel);
        rbtnVariabel.setText("Variabel");
        rbtnVariabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnVariabelActionPerformed(evt);
            }
        });

        btnNieuwCat.setText("Nieuwe Categorie");
        btnNieuwCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNieuwCatActionPerformed(evt);
            }
        });

        lblDetails.setText("Details");

        txtDetails.setEditable(false);
        txtDetails.setColumns(20);
        txtDetails.setLineWrap(true);
        txtDetails.setRows(5);
        txtDetails.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtDetails);

        btnWijzigen.setText("Wijzigen");
        btnWijzigen.setEnabled(false);
        btnWijzigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWijzigenActionPerformed(evt);
            }
        });

        btnBevestigen.setText("Bevestigen");
        btnBevestigen.setEnabled(false);
        btnBevestigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBevestigenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDetailsLayout = new javax.swing.GroupLayout(pnlDetails);
        pnlDetails.setLayout(pnlDetailsLayout);
        pnlDetailsLayout.setHorizontalGroup(
            pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailsLayout.createSequentialGroup()
                        .addComponent(btnWijzigen)
                        .addGap(18, 18, 18)
                        .addComponent(btnBevestigen))
                    .addComponent(lblDetails, javax.swing.GroupLayout.Alignment.CENTER))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDetailsLayout.setVerticalGroup(
            pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailsLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblDetails)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnWijzigen)
                    .addComponent(btnBevestigen))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBedrag)
                    .addComponent(lblCategorie)
                    .addComponent(lblOmschrijving)
                    .addComponent(lblDatum)
                    .addComponent(lblHerhaling))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbHerhaling, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNieuwCat)
                            .addComponent(cmbCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBedrag, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbtnUit)
                                    .addComponent(rbtnIn))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbtnVast)
                                    .addComponent(rbtnVariabel))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAnnuleren)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 403, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(68, 68, 68))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbtnIn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbtnUit))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbtnVast)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbtnVariabel)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(lblBedrag))
                            .addComponent(txtBedrag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(lblCategorie))
                            .addComponent(cmbCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNieuwCat)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOmschrijving)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDatum)
                            .addComponent(pnlDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbHerhaling, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblHerhaling))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAnnuleren)
                            .addComponent(btnOk))))
                .addGap(66, 66, 66))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        succes = true;
        String temp = checkDecimaal(txtBedrag.getText(), true);

        if (succes) {
            bedragString = temp;
            bedrag = Double.parseDouble(bedragString);
            if (btngrpInUit.getSelection() == rbtnIn.getModel()) {
                in = bedragString;
                uit = "-";
            } else {
                uit = bedragString;
                in = "-";
            }
            categorie = cmbCategorie.getSelectedItem().toString();
            omschrijving = txtOmschrijving.getText();
            if (datePicker.getModel().getValue() != null) {
                datum = (Date) datePicker.getModel().getValue();
            } else {
                JOptionPane.showMessageDialog(null, "Kies een datum!", "Fout",
                        JOptionPane.ERROR_MESSAGE);
            }
            soort = (btngrpVastVar.getSelection() == rbtnVast.getModel()) ? "Vast" : "Variabel";
            status = (btngrpInUit.getSelection() == rbtnIn.getModel()) ? "In" : "Uit";

            //Tekst dat in detailweergave word laten zien
            String details
                    = "Rekeningnummer: " + Integer.toString(Gebruiker.getRekeningnummer())
                    + "\n\nSoort: " + soort
                    + "\nStatus: " + status;
            if (cmbHerhaling.isVisible()) {
                herhaling = cmbHerhaling.getSelectedItem().toString();
                details += "\nHerhaling: " + herhaling;
            } else {
                herhaling = "-";
            }
            details += "\nBedrag: \u20ac" + bedragString
                    + "\nCategorie: " + categorie
                    + "\nOmschrijving: " + omschrijving
                    + "\nDatum: " + form.format(datum);
            txtDetails.setText(details);
            btnOk.setEnabled(false);
            rbtnIn.setEnabled(false);
            rbtnUit.setEnabled(false);
            rbtnVast.setEnabled(false);
            rbtnVariabel.setEnabled(false);
            txtBedrag.setEnabled(false);
            cmbCategorie.setEnabled(false);
            txtOmschrijving.setEnabled(false);
            if (cmbHerhaling.isVisible()) {
                cmbHerhaling.setEnabled(false);
            }
            btnBevestigen.setEnabled(true);
            btnWijzigen.setEnabled(true);

        }

    }//GEN-LAST:event_btnOkActionPerformed

    private void btnWijzigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWijzigenActionPerformed
        btnBevestigen.setEnabled(false);
        btnWijzigen.setEnabled(false);
        rbtnIn.setEnabled(true);
        rbtnUit.setEnabled(true);
        rbtnVast.setEnabled(true);
        rbtnVariabel.setEnabled(true);
        txtBedrag.setEnabled(true);
        cmbCategorie.setEnabled(true);
        txtOmschrijving.setEnabled(true);
        btnOk.setEnabled(true);
        if (cmbHerhaling.isVisible()) {
            cmbHerhaling.setEnabled(true);
        }
        txtDetails.setText("");
    }//GEN-LAST:event_btnWijzigenActionPerformed

    private void btnBevestigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBevestigenActionPerformed
        String tijd = " " + new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        Wijziging wijziging = new Wijziging(0, soort, in, uit, omschrijving, datum.toString() + tijd, herhaling, categorie);
        wijziging.nieuweWijziging(true);
        if (btngrpInUit.getSelection() == rbtnUit.getModel()) {
            bedrag = bedrag * -1;
        }
        txtBedrag.setText(null);
        rbtnUit.setSelected(true);
        rbtnVariabel.setSelected(true);
        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("uit", false)));
        cmbCategorie.setSelectedIndex(0);
        txtOmschrijving.setText(null);
        cmbHerhaling.setEnabled(true);
        cmbHerhaling.setVisible(false);
        txtDetails.setText(null);
        btnBevestigen.setEnabled(false);
        btnOk.setEnabled(true);
        rbtnIn.setEnabled(true);
        rbtnUit.setEnabled(true);
        rbtnVast.setEnabled(true);
        rbtnVariabel.setEnabled(true);
        txtBedrag.setEnabled(true);
        cmbCategorie.setEnabled(true);
        txtOmschrijving.setEnabled(true);
        btnWijzigen.setEnabled(false);
    }//GEN-LAST:event_btnBevestigenActionPerformed

    private void cmbCategorieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategorieActionPerformed

    }//GEN-LAST:event_cmbCategorieActionPerformed

    private void btnAnnulerenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnulerenActionPerformed
        Main.setPreviousPanel();
    }//GEN-LAST:event_btnAnnulerenActionPerformed

    private void rbtnInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnInActionPerformed
        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("in", false)));
    }//GEN-LAST:event_rbtnInActionPerformed

    private void rbtnUitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnUitActionPerformed
        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("uit", false)));
    }//GEN-LAST:event_rbtnUitActionPerformed

    private void rbtnVastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnVastActionPerformed
        lblHerhaling.setVisible(true);
        cmbHerhaling.setVisible(true);
    }//GEN-LAST:event_rbtnVastActionPerformed

    private void rbtnVariabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnVariabelActionPerformed
        lblHerhaling.setVisible(false);
        cmbHerhaling.setVisible(false);
    }//GEN-LAST:event_rbtnVariabelActionPerformed

    private void btnNieuwCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNieuwCatActionPerformed
        nieuwCat();
    }//GEN-LAST:event_btnNieuwCatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnnuleren;
    private javax.swing.JButton btnBevestigen;
    private javax.swing.JButton btnNieuwCat;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnWijzigen;
    private javax.swing.ButtonGroup btngrpInUit;
    private javax.swing.ButtonGroup btngrpVastVar;
    private javax.swing.JComboBox cmbCategorie;
    private javax.swing.JComboBox cmbHerhaling;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBedrag;
    private javax.swing.JLabel lblCategorie;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblDetails;
    private javax.swing.JLabel lblHerhaling;
    private javax.swing.JLabel lblOmschrijving;
    private javax.swing.JPanel pnlDatePicker;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JRadioButton rbtnIn;
    private javax.swing.JRadioButton rbtnUit;
    private javax.swing.JRadioButton rbtnVariabel;
    private javax.swing.JRadioButton rbtnVast;
    private javax.swing.JTextField txtBedrag;
    private javax.swing.JTextArea txtDetails;
    private javax.swing.JTextArea txtOmschrijving;
    // End of variables declaration//GEN-END:variables
}
