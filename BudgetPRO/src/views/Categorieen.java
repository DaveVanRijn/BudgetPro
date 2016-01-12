/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.BorderLayout;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import system.Database;
import system.Main;

/**
 *
 * @author Dave van Rijn, IS-103, Studentnummer 500714558
 */
public class Categorieen extends javax.swing.JPanel {

    String naam;
    String inUit;
    private final int ALLES = 1;
    private final int IN = 2;
    private final int UIT = 3;
    private int filter = ALLES;

    /**
     * Creates new form Categorieen
     */
    public Categorieen() {
        Database db = new Database();
        initComponents();
        setLetterGrootte(Main.getLetterGrootte());
        vulTabel(db.getCategorieen());
        tblCategorie.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    naam = (String) target.getValueAt(row, 0);
                    inUit = (String) target.getValueAt(row, 1);
                    btnVerwijderen.setEnabled(true);
                    btnAanpassen.setEnabled(true);
                } else if (e.getClickCount() == 2) {
                    aanpassen(naam, inUit);
                    btnAanpassen.setEnabled(false);
                    btnVerwijderen.setEnabled(false);
                }
            }
        });
    }

    private void vulTabel(ResultSet rs) {
        try {
            while (tblCategorie.getRowCount() > 0) {
                ((DefaultTableModel) tblCategorie.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) tblCategorie.getModel()).insertRow(rs.getRow() - 1, row);
            }
            rs.close();
        } catch (SQLException e) {
            Main.showError("041", e);
        }
    }

    private void setLetterGrootte(int grootte) {
        Font font = new Font("Tahoma", 0, grootte);
        rbtnIn.setFont(font);
        rbtnUit.setFont(font);
        rbtnAlles.setFont(font);
        jLabel1.setFont(font);
        btnNieuw.setFont(font);
        btnAanpassen.setFont(font);
        btnVerwijderen.setFont(font);
        if (grootte == 19) {
            tblCategorie.setFont(new Font("Tahoma", 0, 24));
        } else {
            tblCategorie.setFont(new Font("Tahoma", 0, 16));
        }
    }

    private void aanpassen(String naam, String inUit) {
        Database db = new Database();
        JPanel panel = new JPanel();
        JTextField txtNaam = new JTextField(15);
        txtNaam.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        JLabel Title = new JLabel("Naam:");
        Title.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        JRadioButton in = new JRadioButton("In");
        in.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        JRadioButton uit = new JRadioButton("Uit");
        uit.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        ButtonGroup group = new ButtonGroup();
        group.add(in);
        group.add(uit);
        txtNaam.setAlignmentX(LEFT_ALIGNMENT);
        Title.setAlignmentX(LEFT_ALIGNMENT);
        Box box = new Box(BoxLayout.PAGE_AXIS);

        if (inUit.equals("in")) {
            in.setSelected(true);
        } else {
            uit.setSelected(true);
        }
        txtNaam.setText(naam);

        box.add(Title);
        box.add(txtNaam);
        box.add(Box.createVerticalStrut(15));
        box.add(in);
        box.add(uit);
        panel.add(box, BorderLayout.CENTER);

        boolean succes;
        do {
            int optie = JOptionPane.showOptionDialog(null, panel, "Aanpassen",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Ok", "Annuleren"}, "Ok");

            if (optie == JOptionPane.OK_OPTION) {
                if (txtNaam.getText().isEmpty()) {
                    succes = false;
                    JOptionPane.showMessageDialog(null, "Vul een naam voor de categorie in!", "Fout", JOptionPane.ERROR_MESSAGE);
                } else {
                    succes = true;
                    String naamNieuw = txtNaam.getText();
                    String inUitNieuw = (group.getSelection() == in.getModel()) ? "in" : "uit";
                    db.verwijderCategorie(naam, inUit);
                    db.addCategorie(naamNieuw, inUit);
                    vulTabel(db.getCategorieen(inUitNieuw));
                    switch (inUitNieuw) {
                        case "in":
                            rbtnIn.setSelected(true);
                            break;
                        case "uit":
                            rbtnUit.setSelected(true);
                            break;
                    }
                    JOptionPane.showMessageDialog(null, "De categorie is aangepast.", "Succes", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                succes = true;
            }
        } while (!succes);
    }

    private void verwijderen(String naam, String inUit) {
        int optie = JOptionPane.showOptionDialog(null,
                "Weet je zeker dat je categorie '" + naam + "' wil verwijderen?",
                "Bevestigen", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Ja", "Nee"},
                "Ja");
        if (optie == JOptionPane.YES_OPTION) {
            Database db = new Database();
            db.verwijderCategorie(naam, inUit);
            JOptionPane.showMessageDialog(null, "Categorie '" + naam + "' is verwijderd", "Succes", JOptionPane.INFORMATION_MESSAGE);

            ButtonModel model = btngrpInUit.getSelection();
            if (model == rbtnIn.getModel()) {
                vulTabel(db.getCategorieen("in"));
            } else if (model == rbtnUit.getModel()) {
                vulTabel(db.getCategorieen("uit"));
            } else {
                vulTabel(db.getCategorieen());
            }
        }
    }

    private void nieuw() {
        Database db = new Database();
        JPanel panel = new JPanel();
        JTextField txtNaam = new JTextField(15);
        txtNaam.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        JLabel Title = new JLabel("Naam:");
        Title.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        JRadioButton in = new JRadioButton("In");
        in.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        JRadioButton uit = new JRadioButton("Uit");
        uit.setFont(new Font("Tahoma", 0, Main.getLetterGrootte()));
        ButtonGroup group = new ButtonGroup();
        group.add(in);
        group.add(uit);
        txtNaam.setAlignmentX(LEFT_ALIGNMENT);
        Title.setAlignmentX(LEFT_ALIGNMENT);
        Box box = new Box(BoxLayout.PAGE_AXIS);
        switch (filter) {
            case ALLES:
                uit.setSelected(true);
                break;
            case IN:
                in.setSelected(true);
                break;
            case UIT:
                uit.setSelected(true);
                break;
        }

        box.add(Title);
        box.add(txtNaam);
        box.add(Box.createVerticalStrut(15));
        box.add(in);
        box.add(uit);
        panel.add(box, BorderLayout.CENTER);

        boolean succes;
        do {
            int optie = JOptionPane.showOptionDialog(null, panel,
                    "Nieuw", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Ok", "Annuleren"}, "Ok");
            if (optie == JOptionPane.OK_OPTION) {
                if (txtNaam.getText().isEmpty()) {
                    succes = false;
                    JOptionPane.showMessageDialog(null, "Vul een naam voor de categorie in!", "Fout", JOptionPane.ERROR_MESSAGE);
                } else {
                    succes = true;
                    String naamNieuw = txtNaam.getText();
                    String inUitNieuw = (group.getSelection() == in.getModel()) ? "in" : "uit";
                    db.addCategorie(naamNieuw, inUitNieuw);
                    switch (inUitNieuw) {
                        case "in":
                            rbtnIn.setSelected(true);
                            break;
                        case "uit":
                            rbtnUit.setSelected(true);
                            break;
                    }
                    vulTabel(db.getCategorieen(inUitNieuw));
                    JOptionPane.showMessageDialog(null, "De categorie is toegevoegd.", "Succes", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                succes = true;
            }
        } while (!succes);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategorie = new javax.swing.JTable();
        btnAanpassen = new javax.swing.JButton();
        btnVerwijderen = new javax.swing.JButton();
        rbtnIn = new javax.swing.JRadioButton();
        rbtnUit = new javax.swing.JRadioButton();
        rbtnAlles = new javax.swing.JRadioButton();
        btnNieuw = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        tblCategorie.setAutoCreateRowSorter(true);
        tblCategorie.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblCategorie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Naam", "In/Uit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCategorie.setRowHeight(30);
        tblCategorie.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblCategorie);
        if (tblCategorie.getColumnModel().getColumnCount() > 0) {
            tblCategorie.getColumnModel().getColumn(0).setResizable(false);
            tblCategorie.getColumnModel().getColumn(1).setResizable(false);
        }

        btnAanpassen.setText("Aanpassen");
        btnAanpassen.setEnabled(false);
        btnAanpassen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAanpassenActionPerformed(evt);
            }
        });

        btnVerwijderen.setText("Verwijderen");
        btnVerwijderen.setEnabled(false);
        btnVerwijderen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerwijderenActionPerformed(evt);
            }
        });

        btngrpInUit.add(rbtnIn);
        rbtnIn.setText("Inkomend");
        rbtnIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnInActionPerformed(evt);
            }
        });

        btngrpInUit.add(rbtnUit);
        rbtnUit.setText("Uitgaand");
        rbtnUit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnUitActionPerformed(evt);
            }
        });

        btngrpInUit.add(rbtnAlles);
        rbtnAlles.setSelected(true);
        rbtnAlles.setText("Alles");
        rbtnAlles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAllesActionPerformed(evt);
            }
        });

        btnNieuw.setText("Nieuw");
        btnNieuw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNieuwActionPerformed(evt);
            }
        });

        jLabel1.setText("Selecteer een categorie om aan te passen/verwijderen of klik op 'Nieuw' om een nieuwe categorie toe te voegen.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNieuw)
                .addGap(18, 18, 18)
                .addComponent(btnAanpassen)
                .addGap(18, 18, 18)
                .addComponent(btnVerwijderen)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(rbtnIn)
                        .addGap(18, 18, 18)
                        .addComponent(rbtnUit)
                        .addGap(18, 18, 18)
                        .addComponent(rbtnAlles))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtnIn)
                    .addComponent(rbtnUit)
                    .addComponent(rbtnAlles))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAanpassen)
                    .addComponent(btnNieuw)
                    .addComponent(btnVerwijderen))
                .addGap(36, 36, 36))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rbtnInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnInActionPerformed
        Database db = new Database();
        vulTabel(db.getCategorieen("in"));
        filter = IN;
    }//GEN-LAST:event_rbtnInActionPerformed

    private void rbtnUitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnUitActionPerformed
        Database db = new Database();
        vulTabel(db.getCategorieen("uit"));
        filter = UIT;
    }//GEN-LAST:event_rbtnUitActionPerformed

    private void rbtnAllesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAllesActionPerformed
        Database db = new Database();
        vulTabel(db.getCategorieen());
        filter = ALLES;
    }//GEN-LAST:event_rbtnAllesActionPerformed

    private void btnNieuwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNieuwActionPerformed
        nieuw();
        btnAanpassen.setEnabled(false);
        btnVerwijderen.setEnabled(false);
    }//GEN-LAST:event_btnNieuwActionPerformed

    private void btnAanpassenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAanpassenActionPerformed
        aanpassen(naam, inUit);
        btnAanpassen.setEnabled(false);
        btnVerwijderen.setEnabled(false);
    }//GEN-LAST:event_btnAanpassenActionPerformed

    private void btnVerwijderenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerwijderenActionPerformed
        verwijderen(naam, inUit);
        btnAanpassen.setEnabled(false);
        btnVerwijderen.setEnabled(false);
    }//GEN-LAST:event_btnVerwijderenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAanpassen;
    private javax.swing.JButton btnNieuw;
    private javax.swing.JButton btnVerwijderen;
    private javax.swing.ButtonGroup btngrpInUit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbtnAlles;
    private javax.swing.JRadioButton rbtnIn;
    private javax.swing.JRadioButton rbtnUit;
    private javax.swing.JTable tblCategorie;
    // End of variables declaration//GEN-END:variables
}
