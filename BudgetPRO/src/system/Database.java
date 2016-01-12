/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import Objects.Hypotheek;
import Objects.Wijziging;
import enums.Gebruiker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class Database {

    private Connection connection;

    /**
     * Maak connectie met de database 
     */
    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb"
                    + "?zeroDateTimeBehavior=convertToNull",
                    "root", "root"
            );
        } catch (ClassNotFoundException | SQLException e) {
            Main.showError("002", e);
        }
    }

    /**
     * Log in met een rekeningnummer
     * @param rekeningnummer Het rekeningnummer van de gebruiker
     */
    public void login(int rekeningnummer) {
        try {
            String sql = "SELECT `voorletters`, `tussenvoegsel`, `achternaam`, `saldo`"
                    + "FROM `gebruiker`"
                    + "WHERE `rekeningnummer` = ?;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rekeningnummer);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return;
            }

            Gebruiker.setGebruiker(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rekeningnummer,
                    rs.getDouble(4)
            );
        } catch (SQLException e) {
            Main.showError("003", e);
        }
    }

    /**
     * Registreer een nieuwe gebruiker
     * @param voorletters De voorletters van de nieuwe gebruiker
     * @param tussenvoegsel Het tussenvoegsel van de nieuwe gebruiker
     * @param achternaam De achternaam van de nieuwe gebruiker
     * @param rekeningnummer Het rekeningnummer van de nieuwe gebruiker
     * @param huidigSaldo het huidige saldo van de nieuwe gebruiker
     */
    public void registreer(String voorletters, String tussenvoegsel, String achternaam,
            int rekeningnummer, double huidigSaldo) {
        try {
            String sql = "INSERT INTO `gebruiker` (`voorletters`, `tussenvoegsel`,"
                    + "`achternaam`, `rekeningnummer`,`saldo`)"
                    + "VALUES(?, ?, ?, ?, ?);";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, voorletters);
            stmt.setString(2, tussenvoegsel);
            stmt.setString(3, achternaam);
            stmt.setInt(4, rekeningnummer);
            stmt.setDouble(5, huidigSaldo);
            stmt.executeUpdate();

            for (int i = 1; i <= 12; i++) {
                sql = "INSERT INTO `maand_has_gebruiker` VALUES(" + i + ", ?, 'n');";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, rekeningnummer);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            Main.showError("004", e);
        }
    }

    /**
     * get de 5 meest recente wijzigingen
     * @return ResultSet met de 5 wijzigingen
     */
    public ResultSet getRecenteWijzigingen() {
        ResultSet rs = null;
        String sql = "SELECT DATE_FORMAT(`datum`, '%Y-%m-%d'), `in`, `uit`, `soort`, `categorie_naam` FROM `wijziging` "
                + "WHERE `gebruiker_rekeningnummer` = ? ORDER BY `datum` DESC Limit 5;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("005", e);
        }
        return rs;
    }

    /**
     * Voer een nieuwe wijziging in in de database
     * @param wijziging We wijziging die doorgevoerd moet worden
     */
    public void nieuweWijziging(Wijziging wijziging) {
        String sql = "INSERT INTO `wijziging` (`soort`, `in`, `uit`, `omschrijving`, "
                + "`datum`, `herhaling`, `gebruiker_rekeningnummer`, `categorie_naam`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, wijziging.getSoort());
            stmt.setString(2, wijziging.getIn());
            stmt.setString(3, wijziging.getUit());
            stmt.setString(4, wijziging.getOmschrijving());
            stmt.setString(5, wijziging.getDatum());
            stmt.setString(6, wijziging.getHerhaling());
            stmt.setInt(7, Gebruiker.getRekeningnummer());
            stmt.setString(8, wijziging.getCategorie());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("006", e);
        }
    }

    /**
     * Wijzig het saldo in de database
     * @param wijziging  De wijziging in het saldo
     */
    public void setNieuwSaldo(Double wijziging) {
        Double nieuwSaldo = Gebruiker.getBudget() + wijziging;
        String sql = "UPDATE `gebruiker` SET `saldo` = ? WHERE `rekeningnummer` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, nieuwSaldo);
            stmt.setInt(2, Gebruiker.getRekeningnummer());
            stmt.executeUpdate();
            Gebruiker.setBudget(nieuwSaldo);
            Main.setLabels();
        } catch (SQLException e) {
            Main.showError("007", e);
        }
    }

    /**
     * Get alle wijzigingen van de huidige gebruiker
     * @return Alle wijzigingen van de gebruiker (ResultSet)
     */
    public ResultSet getWijzigingen() {
        ResultSet rs = null;
        String sql = "SELECT `id`, DATE_FORMAT(`datum`, '%Y-%m-%d'), `in`, `uit`, `soort`, `categorie_naam` FROM `wijziging` "
                + "WHERE `gebruiker_rekeningnummer` = ? ORDER BY `datum` DESC;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("008", e);
        }
        return rs;
    }

    /**
     * Get wijzigingen, gefilterd door de keuzes van de gebruiker
     * @param where De filter keuzes van de gebruiker in String
     * @return Alle wijzigingen met filter (ResultSet)
     */
    public ResultSet filterWijzigingen(String where) {
        ResultSet rs = null;
        String sql = "SELECT `id`, DATE_FORMAT(`datum`, '%Y-%m-%d'), `in`, `uit`, `soort`, `categorie_naam` "
                + "FROM `wijziging` " + where + " ORDER BY `datum` DESC;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("009", e);
        }
        return rs;
    }

    /**
     * Get de details van een wijziging
     * @param id Het id nummer van de wijziging
     * @return Alle details van de wijziging (ResultSet)
     */
    public ResultSet getWijzigingDetails(int id) {
        ResultSet rs = null;
        String sql = "SELECT `soort`, `in`, `uit`, `categorie_naam`, `omschrijving`,"
                + "`datum`, `herhaling`, `id` FROM `wijziging` WHERE `id` = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("010", e);
        }
        return rs;
    }

    /**
     * Pas een wijziging in de database aan
     * @param wijziging De wijziging die aangepast moet worden
     */
    public void wijzigWijziging(Wijziging wijziging) {
        String sql = "UPDATE `wijziging` SET `datum` = ?, `in` = ?, `uit` = ?"
                + ", `soort` = ?, `categorie_naam` = ?, `herhaling` = ?"
                + ", `omschrijving` = ? WHERE `id` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, wijziging.getDatum());
            stmt.setString(2, wijziging.getIn());
            stmt.setString(3, wijziging.getUit());
            stmt.setString(4, wijziging.getSoort());
            stmt.setString(5, wijziging.getCategorie());
            stmt.setString(6, wijziging.getHerhaling());
            stmt.setString(7, wijziging.getOmschrijving());
            stmt.setInt(8, wijziging.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("011", e);
        }
    }

    /**
     * Verwijder een wijziging uit de database
     * @param id Het id nummer van de wijziging die verwijderd moet worden
     */
    public void verwijderWijziging(int id) {
        String sql = "DELETE FROM `wijziging` WHERE `id` = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("012", e);
        }
    }

    /**
     * Get alle wijzigingen met een herhaling
     * @return Alle wijzigingen met een herhaling (ResultSet)
     */
    public ResultSet getHerhalingen() {
        ResultSet rs = null;
        String sql = "SELECT DATE_FORMAT(`datum`, '%Y-%m-%d'), `in`, `uit`, `herhaling`, "
                + "`omschrijving`, `categorie_naam` , `id` FROM `wijziging` "
                + "WHERE `soort` = 'Vast' AND `gebruiker_rekeningnummer` = ? "
                + "AND `herhaling` != 'Maandelijks' AND `herhaling` != 'Elk kwartaal' "
                + "AND `herhaling` != 'Half jaarlijks' AND `herhaling` != 'Jaarlijks'";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("013", e);
        }
        return rs;
    }

    /**
     * Get de maanden en of ze al herhaald zijn
     * @param huidigeMaand De huidige maand, grens van welke maanden worden opgehaald
     * @return De maanden tot de grens met de herhaling (ResultSet)
     */
    public ResultSet getMaandHerhalingen(int huidigeMaand) {
        ResultSet rs = null;
        String sql = "SELECT `maand_id`, `herhaald` FROM `maand_has_gebruiker` "
                + "WHERE `gebruiker_rekeningnummer` = ? AND `maand_id` <= ? ORDER BY `maand_id`;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            stmt.setInt(2, huidigeMaand);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("014", e);
        }
        return rs;
    }

    /**
     * Set een maand op berekend
     * @param maand De maand die op berekend moet worden gezet
     */
    public void setMaandBerekend(int maand) {
        String sql = "UPDATE `maand_has_gebruiker` SET `herhaald` = 'y' "
                + "WHERE `gebruiker_rekeningnummer` = ? AND `maand_id` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            stmt.setInt(2, maand);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("015", e);
        }
    }

    /**
     * Set alle maanden op niet herhaald
     */
    public void resetAlleMaanden() {
        String sql = "UPDATE `maand` SET `herhaald` = 'n'";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            Main.showError("016", e);
        }
    }

    /**
     * Wijzig een bestaande gebruiker
     * @param voorletters Nieuwe voorletters
     * @param tussenvoegsel Nieuw tussenvoegsel
     * @param achternaam Nieuwe achternaam
     * @param rekeningnummer Nieuw rekeningnummer
     * @param saldo Nieuw saldo
     */
    public void wijzigGebruiker(String voorletters, String tussenvoegsel,
            String achternaam, int rekeningnummer, double saldo) {
        String sql = "UPDATE `gebruiker` SET `voorletters` = ?, `tussenvoegsel` = ?, "
                + "`achternaam` = ?, `rekeningnummer` = ?, `saldo` = ? "
                + "WHERE `rekeningnummer` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, voorletters);
            stmt.setString(2, tussenvoegsel);
            stmt.setString(3, achternaam);
            stmt.setInt(4, rekeningnummer);
            stmt.setDouble(5, saldo);
            stmt.setInt(6, Gebruiker.getRekeningnummer());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("017", e);
        }
    }

    /**
     * Voeg een nieuwe hypotheek toe
     * @param hypo De hypotheek die wordt toegevoegd
     */
    public void nieuweHypotheek(Hypotheek hypo) {
        String sql = "INSERT INTO `hypotheek`(`gebruiker_rekeningnummer`, `aflossing`, `restschuld`, "
                + "`rente`, `omschrijving`, `soort`, `annuiteit`) VALUES("
                + "?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            stmt.setString(2, hypo.getAflossing());
            stmt.setString(3, hypo.getRestschuld());
            stmt.setString(4, hypo.getRente());
            stmt.setString(5, hypo.getOmschrijving());
            stmt.setString(6, hypo.getSoort());
            stmt.setString(7, hypo.getAnnuiteit());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("018", e);
        }
    }

    /**
     * Get de hypotheken voor 
     * @return 
     */
    public ResultSet getHypotheekTabel() {
        ResultSet rs = null;
        String sql = "SELECT `id`, `soort`, `restschuld` FROM `hypotheek` "
                + "WHERE `gebruiker_rekeningnummer` = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("019", e);
        }
        return rs;
    }

    public ResultSet getHypotheek() {
        ResultSet rs = null;
        String sql = "SELECT `id`, `aflossing`, `restschuld`, `rente`, `soort`, `annuiteit`, `omschrijving` FROM `hypotheek`"
                + " WHERE `gebruiker_rekeningnummer` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());

            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("020", e);
        }
        return rs;
    }

    public void setRestschuld(String restschuld, int id) {
        String sql = "UPDATE `hypotheek` SET `restschuld` = ? WHERE `id` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, restschuld);
            stmt.setInt(2, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("021", e);
        }
    }

    public ResultSet getHypoOverzicht() {
        ResultSet rs = null;
        String sql = "SELECT `id`, `restschuld`, `aflossing`, `rente`, `omschrijving` FROM `hypotheek` "
                + "WHERE `gebruiker_rekeningnummer` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());

            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("022", e);
        }
        return rs;
    }

    public ResultSet getHypoDetails(int id) {
        String sql = "SELECT `restschuld`, `aflossing`, `rente`, `omschrijving`, "
                + "`annuiteit` FROM `hypotheek` WHERE `id` = ?;";
        ResultSet rs = null;
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

        } catch (SQLException e) {
            Main.showError("023", e);
        }
        return rs;
    }

    public void wijzigHypotheek(Hypotheek hypo) {
        String sql;
        PreparedStatement stmt;

        try {
            switch (hypo.getSoort()) {
                case "Lineair":
                    sql = "UPDATE `hypotheek` SET `omschrijving` = ?, `restschuld` = ?, "
                            + "`aflossing` = ?, `rente` = ? WHERE `id` = ?;";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, hypo.getOmschrijving());
                    stmt.setString(2, hypo.getRestschuld());
                    stmt.setString(3, hypo.getAflossing());
                    stmt.setString(4, hypo.getRente());
                    stmt.setInt(5, hypo.getId());
                    stmt.executeUpdate();
                    break;
                case "Annuiteit":
                    sql = "UPDATE `hypotheek` SET  `omschrijving` = ?, `restschuld` = ?, "
                            + "`rente` = ?, `annuiteit` = ? WHERE `id` = ?;";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, hypo.getOmschrijving());
                    stmt.setString(2, hypo.getRestschuld());
                    stmt.setString(3, hypo.getRente());
                    stmt.setString(4, hypo.getAnnuiteit());
                    stmt.setInt(5, hypo.getId());
                    stmt.executeUpdate();
                    break;
                case "Aflossingsvrij":
                    sql = "UPDATE `hypotheek` SET `omschrijving` = ?, `restschuld` = ?, "
                            + "`rente` = ? WHERE `id` = ?;";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, hypo.getOmschrijving());
                    stmt.setString(2, hypo.getRestschuld());
                    stmt.setString(3, hypo.getRente());
                    stmt.setInt(4, hypo.getId());
                    stmt.executeUpdate();
                    break;
            }
        } catch (SQLException e) {
            Main.showError("024", e);
        }
    }

    public void verwijderHypotheek(int id) {
        String sql = "DELETE FROM `hypotheek` WHERE `id` = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("025", e);
        }
    }

    public ResultSet getCategorieen(String inUit) {
        ResultSet rs = null;
        String sql = "SELECT `categorie_naam`, `in_uit` FROM `gebruiker_has_categorie` WHERE `in_uit` = ? AND `gebruiker_rekeningnummer` = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, inUit);
            stmt.setInt(2, Gebruiker.getRekeningnummer());
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("026", e);
        }
        return rs;
    }

    public ResultSet getCategorieen() {
        ResultSet rs = null;
        String sql = "SELECT `categorie_naam`, `in_uit` FROM `gebruiker_has_categorie` WHERE `gebruiker_rekeningnummer` = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("027", e);
        }
        return rs;
    }

    public void addCategorie(String naam, String inUit) {
        String Sql = "INSERT IGNORE INTO `categorie` VALUES(?);";
        String sql = "INSERT INTO `gebruiker_has_categorie` VALUES(?, ?, ?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement Stmt = connection.prepareStatement(Sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());
            stmt.setString(2, naam);
            stmt.setString(3, inUit);

            Stmt.setString(1, naam);

            Stmt.executeUpdate();
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("028", e);
        }
    }

    public void verwijderCategorie(String naam, String inUit) {
        String sql = "DELETE FROM `gebruiker_has_categorie` WHERE `categorie_naam` = ? AND `in_uit` = ? AND `gebruiker_rekeningnummer` = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, naam);
            stmt.setString(2, inUit);
            stmt.setInt(3, Gebruiker.getRekeningnummer());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("029", e);
        }
    }

    public void wijzigCategorie(String naam, String inUit, String naamNieuw, String inUitNieuw) {
        String SQL = "INSERT IGNORE INTO `categorie` VALUES(?)";
        String sql = "UPDATE `gebruiker_has_categorie` SET `categorie_naam` = ?, `in_uit` = ? WHERE `categorie_naam` = ? "
                + "AND `in_uit` = ? AND `gebruiker_rekeningnummer` = ?";

        try {
            PreparedStatement STMT = connection.prepareStatement(SQL);
            PreparedStatement stmt = connection.prepareStatement(sql);
            STMT.setString(1, naam);

            stmt.setString(1, naamNieuw);
            stmt.setString(2, inUitNieuw);
            stmt.setString(3, naam);
            stmt.setString(4, inUit);
            stmt.setInt(5, Gebruiker.getRekeningnummer());

            STMT.executeUpdate();
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("030", e);
        }
    }

    public ResultSet[] totaalCategorie() {
        ResultSet[] sets = new ResultSet[2];
        String sql = "SELECT `categorie_naam`, SUM(`in`) FROM `wijziging` WHERE `in` != '-' GROUP BY `categorie_naam`;";

        try {
            Statement stmt = connection.createStatement();
            Statement stm = connection.createStatement();
            sets[0] = stmt.executeQuery(sql);

            sql = "SELECT `categorie_naam`, SUM(`uit`) FROM `wijziging` WHERE `uit` != '-' GROUP BY `categorie_naam`;";
            sets[1] = stm.executeQuery(sql);
        } catch (SQLException e) {
            Main.showError("031", e);
        }
        return sets;
    }

    public void nieuweWachtlijst(Wijziging wijziging) {
        String sql = "INSERT INTO `wachtlijst` (`in`, `uit`, `omschrijving`, "
                + "`datum`, `herhaling`, `gebruiker_rekeningnummer`, `categorie_naam`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, wijziging.getIn());
            stmt.setString(2, wijziging.getUit());
            stmt.setString(3, wijziging.getOmschrijving());
            stmt.setString(4, wijziging.getDatum());
            stmt.setString(5, wijziging.getHerhaling());
            stmt.setInt(6, Gebruiker.getRekeningnummer());
            stmt.setString(7, wijziging.getCategorie());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("032", e);
        }
    }

    public ResultSet getWachtlijst() {
        ResultSet rs = null;
        String sql = "SELECT * FROM `wachtlijst` WHERE `gebruiker_rekeningnummer` = ? AND `datum` <= current_date();";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Gebruiker.getRekeningnummer());

            rs = stmt.executeQuery();
        } catch (SQLException e) {
            Main.showError("033", e);
        }
        return rs;
    }

    public void verwijderWachtlijst(int id) {
        String sql = "DELETE FROM `wachtlijst` WHERE `id` = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.showError("034", e);
        }
    }

}
