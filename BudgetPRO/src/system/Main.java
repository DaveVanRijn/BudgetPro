/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import enums.Gebruiker;
import views.Login;
import views.Overzicht;
import views.Startpage;
import views.WijzigingDetail;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import views.Hypotheek_NewEdit;
import views.Hypotheken;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    private static Main mainFrame;
    private static JFrame popup;
    private final ArrayList<JPanel> panels;
    DateFormat form = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date(); //Huidige datum
    private final static DecimalFormat decForm = new DecimalFormat("0.00");
    public static int huidigJaartal = 2015;
    public static final String path = Paths.get("").toAbsolutePath().toString();
    private static int letterGrootte = 12;
    private static File file = null;

    public Main() {
        URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
        file = new File(location.getPath().replace("BudgetPRO.jar", "style.txt").substring(1));
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                letterGrootte = Integer.parseInt(reader.readLine());
                reader.close();
            } catch (FileNotFoundException e) {
                Main.showError("55", e);
            } catch (IOException | NumberFormatException e) {
                Main.showError("55", e);
            }
        } else {
            try {
                PrintWriter out = new PrintWriter(file, "UTF-8");
                out.print("12");
                out.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                Main.showError("55", e);
            }
        }
        initComponents();
        Database db = new Database();
        if (Calendar.getInstance().get(Calendar.YEAR) > huidigJaartal) {
            db.resetAlleMaanden();
            huidigJaartal = Calendar.getInstance().get(Calendar.YEAR);
        }
        btnTerug.setEnabled(false);
        btnUitloggen.setEnabled(false);
        panels = new ArrayList<>();
        setVisible(true);
        pnlMain.setLayout(new BorderLayout());
        JPanel panel = (JPanel) new Login();
        pnlMain.setSize(panel.getPreferredSize());
        pnlMain.add(panel, BorderLayout.CENTER);
        setTitle("BudgetPro");
        lblRekeningNR.setVisible(false);
        lblSaldo.setVisible(false);
        lblDatum.setText(form.format(date));
        pack();
        setLocationRelativeTo(null);
    }

    public static void setLetterGrootte(int grootte) {
        Font font = new Font("Tahoma", 0, grootte);
        mainFrame.btnTerug.setFont(font);
        mainFrame.btnUitloggen.setFont(font);
        mainFrame.btnUpdate.setFont(font);
        mainFrame.lblDatum.setFont(font);
        mainFrame.lblRekeningNR.setFont(font);
        mainFrame.lblSaldo.setFont(font);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
    }

    /**
     * Zet een nieuw scherm zichtbaar met constructor van dat scherm
     *
     * @param o Object dat gemaakt word met de constructor van het
     * debestreffende scherm
     */
    public static void setNewPanel(Object o) {
        mainFrame.pnlMain.removeAll();
        try {
            JPanel panel = (JPanel) o;
            mainFrame.panels.add(panel);
            mainFrame.pnlMain.add(panel, BorderLayout.CENTER);
            mainFrame.revalidate();
            mainFrame.pnlMain.repaint();

        } catch (Exception e) {
            showError("035", e);
        }
        if (o instanceof Login) {
            mainFrame.btnTerug.setEnabled(false);
            mainFrame.btnUitloggen.setEnabled(false);
            mainFrame.lblRekeningNR.setVisible(false);
            mainFrame.lblSaldo.setVisible(false);
            mainFrame.btnUpdate.setVisible(true);
        } else {
            mainFrame.btnTerug.setEnabled(true);
            mainFrame.btnUitloggen.setEnabled(true);
            mainFrame.lblRekeningNR.setVisible(true);
            mainFrame.lblSaldo.setVisible(true);
            mainFrame.btnUpdate.setVisible(false);
        }
        if (o instanceof Startpage) {
            mainFrame.btnTerug.setEnabled(false);
        }
        if (o instanceof Overzicht || o instanceof Hypotheken) {
            JPanel panel = mainFrame.panels.get(mainFrame.panels.size() - 2);
            if (panel instanceof WijzigingDetail || panel instanceof Hypotheek_NewEdit) {
                mainFrame.panels.remove(mainFrame.panels.size() - 2);
                mainFrame.panels.remove(mainFrame.panels.size() - 2);
            }
        }
        if (mainFrame.lblSaldo.isVisible()) {
            if (Gebruiker.getBudget() < 0.00) {
                mainFrame.lblSaldo.setForeground(Color.RED);
            } else {
                mainFrame.lblSaldo.setForeground(Color.BLACK);
            }
        }
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
    }

    /**
     * Zet het vorige scherm zichtbaar
     */
    public static void setPreviousPanel() {
        mainFrame.pnlMain.removeAll();
        mainFrame.pnlMain.repaint();
        try {
            Class clazz = mainFrame.panels.get(mainFrame.panels.size() - 2).getClass();
            JPanel panel = (JPanel) clazz.newInstance();
            mainFrame.pnlMain.add(panel, BorderLayout.CENTER);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.revalidate();
            mainFrame.pnlMain.revalidate();
            if (panel instanceof Login) {
                mainFrame.btnTerug.setEnabled(false);
                mainFrame.btnUitloggen.setEnabled(false);
                mainFrame.lblRekeningNR.setVisible(false);
                mainFrame.lblSaldo.setVisible(false);
            } else {
                mainFrame.btnTerug.setEnabled(true);
                mainFrame.btnUitloggen.setEnabled(true);
                mainFrame.lblRekeningNR.setVisible(true);
                mainFrame.lblSaldo.setVisible(true);
            }
            if (panel instanceof Startpage) {
                mainFrame.btnTerug.setEnabled(false);
            }
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null);
            mainFrame.panels.remove(mainFrame.panels.remove(mainFrame.panels.size() - 1));
        } catch (InstantiationException | IllegalAccessException e) {
            showError("036", e);
        }
        setLabels();
    }

    public static void setLabels() {
        mainFrame.lblRekeningNR.setText(Integer.toString(Gebruiker.getRekeningnummer()));
        mainFrame.lblSaldo.setText("Saldo: \u20ac" + decForm.format(Gebruiker.getBudget()));
        if (mainFrame.lblSaldo.isVisible()) {
            if (Gebruiker.getBudget() < 0) {
                if (decForm.format(Gebruiker.getBudget()).equals("-0.00")
                        || decForm.format(Gebruiker.getBudget()).equals("-0,00")) {
                    Gebruiker.setBudget(0.00);
                    setLabels();
                } else {
                    mainFrame.lblSaldo.setForeground(Color.RED);
                }
            } else {
                mainFrame.lblSaldo.setForeground(Color.BLACK);
            }
        }
    }

    public static void showError(String number, Exception e) {
        String error = "Error " + number + ":\n" + e.getMessage() + "\n";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            error += e.getStackTrace()[i].toString() + "\n";
        }
        ScrollPane pane = new ScrollPane();
        pane.setSize(40, 200);
        JTextArea txtError = new JTextArea(10, 100);
        txtError.setText(error);
        txtError.setEditable(false);

        JLabel Title = new JLabel("Er is iets fout gegaan, zie hieronder voor de details.");
        JLabel exit = new JLabel("Het programma zal na het klikken op OK sluiten.");
        txtError.setAlignmentX(LEFT_ALIGNMENT);
        Title.setAlignmentX(CENTER_ALIGNMENT);
        exit.setAlignmentX(CENTER_ALIGNMENT);
        pane.add(txtError);
        Box box = new Box(BoxLayout.PAGE_AXIS);
        box.add(Box.createHorizontalGlue());
        box.add(Box.createHorizontalStrut(400));
        box.add(Box.createVerticalStrut(15));
        box.add(pane);
        box.add(Box.createVerticalStrut(15));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Title);
        panel.add(box, BorderLayout.WEST);
        panel.add(exit);
        JOptionPane.showMessageDialog(null, panel, "Fout", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    public static String[] getCategorieen(String inUit, boolean overzicht) {
        Database db = new Database();
        ResultSet rs = db.getCategorieen(inUit);
        String[] cat = null;
        int i = 0;

        try {
            rs.last();
            if (overzicht) {
                cat = new String[rs.getRow() + 2];
                cat[0] = "Alles";
                i++;
            } else {
                cat = new String[rs.getRow() + 1];
            }
            rs.beforeFirst();
            while (rs.next()) {
                cat[i] = rs.getString(1);
                i++;
            }
            cat[i] = "Diversen";
        } catch (SQLException e) {
            showError("037", e);
        }

        return cat;
    }

    public static void showDetails(Object o, boolean vanafOverzicht) {
        popup = new JFrame();
        popup.setTitle("Details");
        JPanel panel = (JPanel) o;
        popup.add(panel);
        popup.pack();
        popup.setLocationRelativeTo(null);

        popup.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {

                closePopup(vanafOverzicht);
            }
        });
        popup.setVisible(true);
    }

    public static void closePopup(boolean vanafOverzicht) {
        if(vanafOverzicht){
            try{
                ((Overzicht) mainFrame.pnlMain.getComponent(0)).filterTabel(true);
            } catch (Exception e){
                
            }
        }
        popup.dispose();
    }

    public static void newLettergrootte(int grootte) {
        letterGrootte = grootte;
        setLetterGrootte(Main.getLetterGrootte());
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        try {
            PrintWriter out = new PrintWriter(file, "UTF-8");
            out.print(grootte);
            out.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            Main.showError("56", e);
        }
    }

    public static int getLetterGrootte() {
        return letterGrootte;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        lblRekeningNR = new javax.swing.JLabel();
        lblSaldo = new javax.swing.JLabel();
        sep1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        btnTerug = new javax.swing.JButton();
        btnUitloggen = new javax.swing.JButton();
        lblDatum = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusable(false);

        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlHeader.setMinimumSize(new java.awt.Dimension(0, 65));

        lblIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/geldbiljetten.jpg"))); // NOI18N
        lblIcon.setText("jLabel1");

        lblRekeningNR.setText("Rekeningnummer");
        lblRekeningNR.setFocusable(false);

        lblSaldo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSaldo.setText("Saldo");
        lblSaldo.setFocusable(false);

        btnTerug.setText("Terug");
        btnTerug.setFocusable(false);
        btnTerug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerugActionPerformed(evt);
            }
        });

        btnUitloggen.setText("Uitloggen");
        btnUitloggen.setFocusable(false);
        btnUitloggen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUitloggenActionPerformed(evt);
            }
        });

        lblDatum.setText("Datum");
        lblDatum.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(lblDatum))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnTerug)
                .addGap(9, 9, 9)
                .addComponent(btnUitloggen))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblDatum)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTerug)
                    .addComponent(btnUitloggen)))
        );

        btnUpdate.setText("Update");
        btnUpdate.setFocusable(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addComponent(lblIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRekeningNR)
                    .addComponent(lblSaldo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(sep1)
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHeaderLayout.createSequentialGroup()
                        .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIcon)
                            .addGroup(pnlHeaderLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblRekeningNR)
                                    .addComponent(btnUpdate))
                                .addGap(5, 5, 5)
                                .addComponent(lblSaldo)))
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(sep1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTerugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerugActionPerformed
        setPreviousPanel();
    }//GEN-LAST:event_btnTerugActionPerformed

    private void btnUitloggenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUitloggenActionPerformed
        setNewPanel(new Login());
        Gebruiker.resetGebruiker();
    }//GEN-LAST:event_btnUitloggenActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int keuze = JOptionPane.showOptionDialog(null, "Als u het programma wil bijwerken, moet BudgetPro gesloten worden."
                + "\n\nWilt u doorgaan?", "Bevestig", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Doorgaan", "Annuleren"}, "Doorgaan");
        if (keuze == JOptionPane.YES_OPTION) {
            URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
            String curPath = location.getPath().replace("BudgetPRO.jar", "UpdateBudgetPro.jar").replace("/", File.separator).substring(1);
            try {

                Runtime.getRuntime().exec("cmd /c java -jar " + curPath);
                System.exit(0);
            } catch (IOException e) {
                showError("038", e);
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException e) {
            showError("039", e);
        } catch (IllegalAccessException | UnsupportedLookAndFeelException e) {
            showError("040", e);
        }

        EventQueue.invokeLater(() -> {
            mainFrame = new Main();
            setLetterGrootte(getLetterGrootte());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTerug;
    private javax.swing.JButton btnUitloggen;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblRekeningNR;
    private javax.swing.JLabel lblSaldo;
    public javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JSeparator sep1;
    // End of variables declaration//GEN-END:variables
}
