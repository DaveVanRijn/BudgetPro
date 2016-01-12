/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import enums.Gebruiker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;
import system.CellRenderer;
import system.Database;
import java.io.FileOutputStream;
import java.util.Date;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import system.Main;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class Overzicht extends javax.swing.JPanel {

    /**
     * Creates new form Overzicht
     */
    DateFormat form = new SimpleDateFormat("dd-MM-yyyy"); //date format voor pfd titel
    String whereClaus = "WHERE `gebruiker_rekeningnummer` = ?"; //Begin van WHERE String
    String maandKeuze = null; //Maand filter
    String soortKeuze = null; //Soort filter
    String vastVariabelKeuze = null; //Vast/Variabel filter
    String categorieKeuze = null; //Categorie filter
    ArrayList<String> voorwaarden = new ArrayList<>(); //List van delen String van WHERE String
    int records = 0; //Aantal records dat word laten zien in de tabel
    String date = form.format(new Date()); //datum voor pdf titel
    double inkomend = 0; //Totaal inkomend bedrag van de tabel
    double uitgaand = 0; //Totaal uitgaand bedrag van de tabel
    private static final Font redFont = new Font(Font.FontFamily.UNDEFINED, 12,
            Font.NORMAL, BaseColor.RED); //font voor uitgaande bedragen
    private final DecimalFormat DeciFormat = new DecimalFormat("0.00"); //Format voor decimale getallen
    private String catsTotaal;
    ArrayList<String> categorien;
    ArrayList<Double> bedragen;

    public Overzicht() {
        Database db = new Database();
        initComponents();
        setLetterGrootte(Main.getLetterGrootte());
        vulTabel(db.getWijzigingen());
        CellRenderer.initUI(tblWijzigingen, true);
        tblWijzigingen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int id = (int) target.getValueAt(row, 0);
                    Main.showDetails(new WijzigingDetail(id, true), true);
                }
            }
        });
        totaalCategorie();
    }

    private void setLetterGrootte(int grootte) {
        java.awt.Font font = new java.awt.Font("Tahoma", 0, grootte);
        btnPDF.setFont(font);
        btnVasteInvoeren.setFont(font);
        cmbCategorie.setFont(font);
        cmbInUit.setFont(font);
        cmbMaand.setFont(font);
        cmbVastVariabel.setFont(font);
        jLabel1.setFont(font);
        lblAantalWijzigingen.setFont(font);
        lblCategorie.setFont(font);
        lblInUit.setFont(font);
        lblInkomend.setFont(font);
        lblMaand.setFont(font);
        lblUitgaand.setFont(font);
        lblVastVariabel.setFont(font);
        if (grootte == 19) {
            tblWijzigingen.setFont(new java.awt.Font("Tahoma", 0, 24));
        } else {
            tblWijzigingen.setFont(new java.awt.Font("Tahoma", 0, 16));
        }
    }

    /**
     * Filter de tabel op bepaalde eisen
     *
     * @param refresh Flags wether or not the table is refeshed
     */
    public void filterTabel(boolean refresh) {
        int scrollLocation = 0;
        if (refresh) {
            scrollLocation = jScrollPane1.getVerticalScrollBar().getValue();
        }
        Database db = new Database();
        //Maand filter
        Calendar cal = Calendar.getInstance();
        String year = Integer.toString(cal.get(Calendar.YEAR));
        String maand = null;
        if (!cmbMaand.getSelectedItem().toString().equals("Alles")) {
            switch (cmbMaand.getSelectedItem().toString()) {
                case "Januari":
                    maand = "01";
                    break;
                case "Februari":
                    maand = "02";
                    break;
                case "Maart":
                    maand = "03";
                    break;
                case "April":
                    maand = "04";
                    break;
                case "Mei":
                    maand = "05";
                    break;
                case "Juni":
                    maand = "06";
                    break;
                case "Juli":
                    maand = "07";
                    break;
                case "Augustus":
                    maand = "08";
                    break;
                case "September":
                    maand = "09";
                    break;
                case "Oktober":
                    maand = "10";
                    break;
                case "November":
                    maand = "11";
                    break;
                case "December":
                    maand = "12";
                    break;
            }
            maandKeuze = "`datum` >= '" + year + "-" + maand + "-01' AND"
                    + " `datum` < '" + year + "-" + Integer.toString(Integer.parseInt(maand) + 1)
                    + "-01'";
            voorwaarden.add(maandKeuze);
        } else {
            if (maandKeuze != null) {
                voorwaarden.remove(maandKeuze);
                maandKeuze = null;
            }
        }

        //In/Uit filter
        if (!cmbInUit.getSelectedItem().toString().equals("Alles")) {
            switch (cmbInUit.getSelectedItem().toString()) {
                case "In":
                    soortKeuze = "`in` != '-'";
                    break;
                case "Uit":
                    soortKeuze = "`uit` != '-'";
                    break;
            }
            voorwaarden.add(soortKeuze);
        } else {
            if (soortKeuze != null) {
                voorwaarden.remove(soortKeuze);
                soortKeuze = null;
            }
        }

        //Vast/Variabel filter
        if (!cmbVastVariabel.getSelectedItem().toString().equals("Alles")) {
            vastVariabelKeuze = "`soort` = '" + cmbVastVariabel.getSelectedItem().toString() + "'";
            voorwaarden.add(vastVariabelKeuze);
        } else {
            if (vastVariabelKeuze != null) {
                voorwaarden.remove(vastVariabelKeuze);
                vastVariabelKeuze = null;
            }
        }

        //Categorie filter
        if (!cmbCategorie.getSelectedItem().toString().equals("Alles")) {
            categorieKeuze = "`categorie_naam` = '" + cmbCategorie.getSelectedItem().toString() + "'";
            voorwaarden.add(categorieKeuze);
        } else {
            if (categorieKeuze != null) {
                voorwaarden.remove(categorieKeuze);
                categorieKeuze = null;
            }
        }

        //Geen filter
        if (maandKeuze == null && soortKeuze == null && vastVariabelKeuze == null
                && categorieKeuze == null) {
            vulTabel(db.getWijzigingen());
        } else {
            for (String voorwaarden1 : voorwaarden) {
                whereClaus += " AND " + voorwaarden1;
            }
        }
        vulTabel(db.filterWijzigingen(whereClaus));
        if (refresh) {
            jScrollPane1.getVerticalScrollBar().setValue(scrollLocation);
        }
        whereClaus = "WHERE `gebruiker_rekeningnummer` = ?";
        voorwaarden.clear();
    }

    /**
     * Vul de tabel met alle wijzigingen
     *
     * @param rs
     */
    public void vulTabel(ResultSet rs) {
        try {
            while (tblWijzigingen.getRowCount() > 0) {
                ((DefaultTableModel) tblWijzigingen.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) tblWijzigingen.getModel()).insertRow(rs.getRow() - 1, row);
            }
            rs.close();
        } catch (SQLException e) {
            Main.showError("046", e);
        }
        try {
            lblAantalWijzigingen.setText("Aantal wijzigingen: " + tblWijzigingen.getRowCount());
            inkomend = 0;
            uitgaand = 0;
            for (int i = 0; i < tblWijzigingen.getRowCount(); i++) {
                String temp = (String) tblWijzigingen.getValueAt(i, 2);
                if (!temp.equals("-")) {
                    if (temp.contains(",")) {
                        temp = temp.replace(",", ".");
                    }
                    inkomend += Double.parseDouble(temp);
                }
                temp = (String) tblWijzigingen.getValueAt(i, 3);
                if (!temp.equals("-")) {
                    if (temp.contains(",")) {
                        temp = temp.replace(",", ".");
                    }
                    uitgaand += Double.parseDouble(temp);
                }
            }

            lblInkomend.setText("Totaal inkomend: \u20ac" + DeciFormat.format(inkomend));
            lblUitgaand.setForeground(Color.RED);
            lblUitgaand.setText("Totaal uitgaand: \u20ac" + DeciFormat.format(uitgaand));
        } catch (NumberFormatException e) {
            Main.showError("047", e);
        }
        totaalCategorie();
    }

    private void totaalCategorie() {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> cat = new ArrayList<>();
        String huidig;
        double bedrag = 0;

        builder.append("\nInkomende categorieën:");
        for (int i = 0; i < tblWijzigingen.getRowCount(); i++) {
            if (!cat.contains(huidig = tblWijzigingen.getValueAt(i, 5).toString()) && !(tblWijzigingen.getValueAt(i, 2).toString()).equals("-")) {
                cat.add(huidig);
                for (int j = i; j < tblWijzigingen.getRowCount(); j++) {
                    if (tblWijzigingen.getValueAt(j, 5).toString().equals(huidig)
                            && !tblWijzigingen.getValueAt(j, 2).toString().equals("-")) {
                        bedrag += Double.parseDouble(tblWijzigingen.getValueAt(j, 2).toString().replace(",", "."));
                    }
                }
                builder.append("\n");
                builder.append(huidig);
                builder.append(": \u20ac");
                builder.append(DeciFormat.format(bedrag));

            }
            bedrag = 0;
        }
        cat.clear();
        builder.append("\n\nUitgaande categorieën:");
        for (int i = 0; i < tblWijzigingen.getRowCount(); i++) {
            if (!cat.contains(huidig = tblWijzigingen.getValueAt(i, 5).toString()) && !(tblWijzigingen.getValueAt(i, 3).toString()).equals("-")) {
                cat.add(huidig);
                for (int j = i; j < tblWijzigingen.getRowCount(); j++) {
                    if (tblWijzigingen.getValueAt(j, 5).toString().equals(huidig)
                            && !tblWijzigingen.getValueAt(j, 3).toString().equals("-")) {
                        bedrag += Double.parseDouble(tblWijzigingen.getValueAt(j, 3).toString().replace(",", "."));
                    }
                }
                builder.append("\n");
                builder.append(huidig);
                builder.append(": \u20ac");
                builder.append(DeciFormat.format(bedrag));
            }
            bedrag = 0;
        }
        cat.clear();
        catsTotaal = builder.toString();
        builder.setLength(0);
    }

    /**
     * Maak een pdf bestand exporteer de tabel naar de pdf
     */
    private void naarPDF() {
        //Optionpane voor titel
        JTextField txtTitle = new JTextField(15);
        JLabel Title = new JLabel("Enter a Title:");
        txtTitle.setAlignmentX(LEFT_ALIGNMENT);
        Title.setAlignmentX(LEFT_ALIGNMENT);
        Box box = new Box(BoxLayout.PAGE_AXIS);
        box.add(Title);
        box.add(txtTitle);
        JPanel panel = new JPanel();
        panel.add(box, BorderLayout.WEST);

        String FILE = null;

        JFileChooser f = new JFileChooser();
        f.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        f.setAcceptAllFileFilterUsed(false);
        int retrival = f.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            FILE = f.getSelectedFile().getPath() + ".pdf";
        }
        if (FILE != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(FILE));
                document.open();
                addText(document);
                addPDFTable(document);
                JOptionPane.showMessageDialog(null,
                        "Exporteren geslaagd.", "Succes",
                        JOptionPane.INFORMATION_MESSAGE);
                int keuze = JOptionPane.showOptionDialog(null, "Wil je het overzicht openen?",
                        "Openen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, new Object[]{"Ja", "Nee"}, "Ja");
                if (keuze == JOptionPane.YES_OPTION) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(new File(FILE));
                }
            } catch (FileNotFoundException | DocumentException e) {
                Main.showError("049", e);
            } catch (IOException e) {
                Main.showError("049", e);
            }
        }
    }

    /**
     * Voeg de tabel toe aan het pfd bestand
     *
     * @param doc document dat word aangemaakt
     */
    private void addPDFTable(Document doc) {
        try {
            PdfPTable table = new PdfPTable(5);
            table.setWidths(new float[]{1.3f, 1, 1, 1, 2.5f});
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell("Datum");
            table.addCell("In");
            table.addCell("Uit");
            table.addCell("Status");
            table.addCell("Categorie");
            table.getDefaultCell().setBackgroundColor(null);

            for (int i = 0; i < tblWijzigingen.getRowCount(); i++) {
                String string3;
                PdfPCell obj3;
                PdfPCell obj1 = new PdfPCell(new Phrase(tblWijzigingen.getValueAt(i, 1).toString()));
                PdfPCell obj2 = new PdfPCell(new Phrase(tblWijzigingen.getValueAt(i, 2).toString()));
                if ((string3 = tblWijzigingen.getValueAt(i, 3).toString()).equals("-")) {
                    obj3 = new PdfPCell(new Phrase(string3));
                } else {
                    obj3 = new PdfPCell(new Phrase(string3, redFont));
                }
                PdfPCell obj4 = new PdfPCell(new Phrase(tblWijzigingen.getValueAt(i, 4).toString()));
                PdfPCell obj5 = new PdfPCell(new Phrase(tblWijzigingen.getValueAt(i, 5).toString()));

                obj1.setFixedHeight(36f);
                obj2.setFixedHeight(36f);
                obj3.setFixedHeight(36f);
                obj4.setFixedHeight(36f);
                obj5.setFixedHeight(36f);

                table.addCell(obj1);
                table.addCell(obj2);
                table.addCell(obj3);
                table.addCell(obj4);
                table.addCell(obj5);
            }
            doc.add(table);
        } catch (Exception e) {
            Main.showError("050", e);
        }
        doc.close();
    }

    private void addText(Document doc) {
        try {
            Paragraph par = new Paragraph();
            Paragraph par1 = new Paragraph();
            par1.setSpacingAfter(40);
            par.setAlignment(Element.ALIGN_CENTER);
            par.setSpacingAfter(20);

            String budgetString = "\u20ac" + DeciFormat.format(Gebruiker.getBudget());
            Phrase budget;
            if (Gebruiker.getBudget() < 0) {
                budget = new Phrase(budgetString, redFont);
            } else {
                budget = new Phrase(budgetString);
            }
            String keuzes = "Maand: " + cmbMaand.getSelectedItem().toString() + "\nIn/Uit: "
                    + cmbInUit.getSelectedItem().toString() + "\nVast/Variabel: "
                    + cmbVastVariabel.getSelectedItem().toString() + "\nCategorie: "
                    + cmbCategorie.getSelectedItem().toString() + "\nTotaal inkomend: \u20ac"
                    + DeciFormat.format(inkomend) + "\nTotaal uitgaand: ";
            Phrase uitgaandPh = new Phrase("\u20ac" + DeciFormat.format(uitgaand) + "\n", redFont);
            String Gegevens = "Datum: " + date
                    + "\nGebruiker: " + Gebruiker.getVolledigeNaam()
                    + "\nRekeningnummer: " + Gebruiker.getRekeningnummer()
                    + "\nHuidig saldo: ";
            par.add(Gegevens);
            par.add(budget);
            par1.add(keuzes);
            par1.add(uitgaandPh);
            par1.add(catsTotaal);
            doc.add(par);
            doc.add(par1);
        } catch (Exception e) {
            Main.showError("051", e);
        }
    }

    //Build pie chart
    private JPanel createPiePanel() {
        JFreeChart pieChart = createPieChart(createPieDataset());
        return new ChartPanel(pieChart);
    }

    private JFreeChart createPieChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                null,
                dataset,
                true,
                true,
                false
        );

        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);
        plot.setSectionOutlinesVisible(false);
        plot.setNoDataMessage("Ongeldige data");

        return chart;
    }

    private PieDataset createPieDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        categorien = new ArrayList<>();
        bedragen = new ArrayList<>();
        String huidig;
        double bedrag = 0;

        for (int i = 0; i < tblWijzigingen.getRowCount(); i++) {
            if (!categorien.contains(huidig = tblWijzigingen.getValueAt(i, 5).toString())) {
                categorien.add(huidig);
                for (int j = i; j < tblWijzigingen.getRowCount(); j++) {
                    String string;
                    if (tblWijzigingen.getValueAt(j, 5).toString().equals(huidig)) {
                        if (!(string = tblWijzigingen.getValueAt(j, 2).toString()).equals("-")) {
                            bedrag += Double.parseDouble(string.replace(",", "."));
                        } else if (!(string = tblWijzigingen.getValueAt(j, 3).toString()).equals("-")) {
                            bedrag += Double.parseDouble(string.replace(",", "."));
                        }
                    }
                }
                bedragen.add(bedrag);
                bedrag = 0;
            }

        }

        for (int i = 0; i < categorien.size(); i++) {
            dataset.setValue(categorien.get(i), bedragen.get(i));
        }
        return dataset;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblWijzigingen = new javax.swing.JTable();
        cmbMaand = new javax.swing.JComboBox();
        cmbInUit = new javax.swing.JComboBox();
        cmbVastVariabel = new javax.swing.JComboBox();
        cmbCategorie = new javax.swing.JComboBox();
        btnPDF = new javax.swing.JButton();
        lblAantalWijzigingen = new javax.swing.JLabel();
        lblMaand = new javax.swing.JLabel();
        lblInUit = new javax.swing.JLabel();
        lblVastVariabel = new javax.swing.JLabel();
        lblCategorie = new javax.swing.JLabel();
        lblInkomend = new javax.swing.JLabel();
        lblUitgaand = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnVasteInvoeren = new javax.swing.JButton();
        btnGrafiek = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 500));

        tblWijzigingen.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblWijzigingen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID#", "Datum", "In", "Uit", "Status", "Categorie"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblWijzigingen.setToolTipText("Dubbelklik op invoer voor details");
        tblWijzigingen.setRowHeight(30);
        tblWijzigingen.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblWijzigingen);
        if (tblWijzigingen.getColumnModel().getColumnCount() > 0) {
            tblWijzigingen.getColumnModel().getColumn(0).setResizable(false);
            tblWijzigingen.getColumnModel().getColumn(1).setResizable(false);
            tblWijzigingen.getColumnModel().getColumn(2).setResizable(false);
            tblWijzigingen.getColumnModel().getColumn(3).setResizable(false);
            tblWijzigingen.getColumnModel().getColumn(4).setResizable(false);
            tblWijzigingen.getColumnModel().getColumn(5).setResizable(false);
        }

        cmbMaand.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Alles", "Januari", "Februari", "Maart", "April", "Mei", "Juni", "Juli", "Augustus", "September", "Oktober", "November", "December" }));
        cmbMaand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMaandActionPerformed(evt);
            }
        });

        cmbInUit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alles", "In", "Uit" }));
        cmbInUit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbInUitActionPerformed(evt);
            }
        });

        cmbVastVariabel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alles", "Vast", "Variabel" }));
        cmbVastVariabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbVastVariabelActionPerformed(evt);
            }
        });

        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alles", "Diversen" }));
        cmbCategorie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategorieActionPerformed(evt);
            }
        });

        btnPDF.setText("Naar PDF");
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });

        lblAantalWijzigingen.setText("Aantal wijzigingen");

        lblMaand.setText("Maand");

        lblInUit.setText("In/Uit");

        lblVastVariabel.setText("Vast/Variabel");

        lblCategorie.setText("Categorie");

        lblInkomend.setText("Inkomend");

        lblUitgaand.setText("Uitgaand");

        jLabel1.setText("Dubbelklik op een invoer voor details.");

        btnVasteInvoeren.setText("Vaste Invoeren");
        btnVasteInvoeren.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVasteInvoerenActionPerformed(evt);
            }
        });

        btnGrafiek.setText("Grafiek");
        btnGrafiek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrafiekActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbMaand, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaand))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbInUit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInUit))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbVastVariabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblVastVariabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblCategorie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnVasteInvoeren)
                                .addGap(18, 18, 18)
                                .addComponent(btnPDF))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAantalWijzigingen)
                        .addGap(18, 18, 18)
                        .addComponent(lblInkomend)
                        .addGap(18, 18, 18)
                        .addComponent(lblUitgaand)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGrafiek)))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMaand)
                            .addComponent(lblInUit)
                            .addComponent(lblVastVariabel)
                            .addComponent(lblCategorie)))
                    .addComponent(jLabel1))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMaand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbInUit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbVastVariabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPDF)
                    .addComponent(btnVasteInvoeren))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAantalWijzigingen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblInkomend)
                    .addComponent(lblUitgaand)
                    .addComponent(btnGrafiek))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbMaandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMaandActionPerformed
        filterTabel(false);
    }//GEN-LAST:event_cmbMaandActionPerformed

    private void cmbInUitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbInUitActionPerformed
        switch (cmbInUit.getSelectedItem().toString()) {
            case "In":
                cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("in", true)));
                break;
            case "Uit":
                cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(Main.getCategorieen("uit", true)));
                break;
            default:
                cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Alles", "Diversen"}));
                break;
        }
        filterTabel(false);
    }//GEN-LAST:event_cmbInUitActionPerformed

    private void cmbVastVariabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbVastVariabelActionPerformed
        filterTabel(false);
    }//GEN-LAST:event_cmbVastVariabelActionPerformed

    private void cmbCategorieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategorieActionPerformed
        filterTabel(false);
    }//GEN-LAST:event_cmbCategorieActionPerformed

    private void btnPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFActionPerformed
        naarPDF();
    }//GEN-LAST:event_btnPDFActionPerformed

    private void btnVasteInvoerenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVasteInvoerenActionPerformed
        Main.setNewPanel(new vasteWijzigingen());
    }//GEN-LAST:event_btnVasteInvoerenActionPerformed

    private void btnGrafiekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrafiekActionPerformed
        Main.showDetails(new Chart(createPiePanel(), categorien, bedragen), false);
    }//GEN-LAST:event_btnGrafiekActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGrafiek;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnVasteInvoeren;
    private javax.swing.JComboBox cmbCategorie;
    private javax.swing.JComboBox cmbInUit;
    private javax.swing.JComboBox cmbMaand;
    private javax.swing.JComboBox cmbVastVariabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAantalWijzigingen;
    private javax.swing.JLabel lblCategorie;
    private javax.swing.JLabel lblInUit;
    private javax.swing.JLabel lblInkomend;
    private javax.swing.JLabel lblMaand;
    private javax.swing.JLabel lblUitgaand;
    private javax.swing.JLabel lblVastVariabel;
    private javax.swing.JTable tblWijzigingen;
    // End of variables declaration//GEN-END:variables
}
