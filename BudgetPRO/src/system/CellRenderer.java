/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Dave van Rijn, Klas IS-103, Studentnummer 500714558
 */
public class CellRenderer extends DefaultTableCellRenderer {
    private static boolean isOverzicht;
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof String) {
            if (isOverzicht) {
                if (column == 3) {
                    String string = (String) value;
                    if (!string.equals("-")) {
                        setText(getHTML(string));
                    }
                }
            } else {
                if (column == 2) {
                    String string = (String) value;
                    if (!string.equals("-")) {
                        setText(getHTML(string));
                    }
                }
            }
        }
        else if (value instanceof Integer){
            if(isOverzicht){
                if(column == 0){
                    String string = Integer.toString((int) (value));
                    setText(getHTMLWhite(string));
                }
            }
        }
        return tableCellRendererComponent;
    }

    private String getHTML(String string) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<span style=\"color: red;\">");
        sb.append(string);
        sb.append("</span>");
        sb.append("</html>");
        return sb.toString();
    }

    private String getHTMLWhite(String string) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<span style=\"color: white;\">");
        sb.append(string);
        sb.append("</span>");
        sb.append("</html>");
        return sb.toString();
    }

    public static void initUI(JTable table, boolean overzicht) {
        isOverzicht = overzicht;
        table.setDefaultRenderer(Object.class, new CellRenderer());
    }
}
