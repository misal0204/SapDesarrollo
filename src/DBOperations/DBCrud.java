package DBOperations;

import ProcessSap_Java.ProcessSap;
import com.sap.conn.jco.JCoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misael Recinos
 */
public class DBCrud {

    public void insertLotInsp(Statement query, String insplot, String plant, String orderno,
            String inspoints, String material, String batch, String txtlot,
            String txtmat, String createdate) {
        try {
            query.execute("INSERT INTO SAP_LOTE_INSP(INSPLOT,PLANT,ORDER_NO,INSPPOINTS,MATERIAL,BATCH,TXT_LOT,TXT_MAT,CREAT_DAT) VALUES"
                    + "('" + insplot + "','" + plant + "','" + orderno + "','" + inspoints + "','" + material + "','" + batch + "',"
                    + "'" + txtlot + "','" + txtmat + "',TO_DATE('" + createdate + "','YYYY-MM-DD HH24:MI:SS'))");
        } catch (SQLException ex) {

            System.out.println("Error en insertar lote de inspección: " + ex.getMessage());
        }
    }

    public void insertLotOper(Statement query, String insplot, String insoper, String plant, String txt_oper,
            String workcenter, String txt_workc) {
        try {
            query.execute("INSERT INTO SAP_LOTINSP_OPE(INSPLOT,INSPOPER,PLANT,TXT_OPER,WORKCENTER,TXT_WORKC) VALUES"
                    + "('" + insplot + "','" + insoper + "','" + plant + "','" + txt_oper + "','" + workcenter + "','" + txt_workc + "')");
        } catch (SQLException ex) {

            System.out.println("Error en insertar operación de lote de inspección: " + ex.getMessage());
        }

    }

    public void insertOperCaract(Statement query, String insplot, String insoper, String inspchar, String status,
            String char_descr, String char_type) {
        try {
            query.execute("INSERT INTO SAP_OPER_CARACTERISTICAS(INSPLOT,INSPOPER,INSPCHAR,STATUS,CHAR_DESCR,CHAR_TYPE) VALUES"
                    + "('" + insplot + "','" + insoper + "','" + inspchar + "','" + status + "','" + char_descr + "','" + char_type + "')");
        } catch (SQLException ex) {

            System.out.println("Error en insertar operación de lote de inspección: " + ex.getMessage());
        }
    }

    public void displayLotInsp(Statement query) {
        ResultSet result;
        String q = "SELECT * FROM SAP_LOTE_INSP";
        try {
            result = query.executeQuery(q);
            while (result.next()) {
                String lotins = result.getString("INSPLOT");
                System.out.println(lotins);

                new ProcessSap().Bapi_getOperacion(lotins);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBCrud.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JCoException ex) {
            Logger.getLogger(DBCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayOper(Statement query) throws JCoException {
        ResultSet result;
        String q = "SELECT * FROM SAP_LOTINSP_OPE WHERE INSPLOT LIKE '03%'";
        try {
            result = query.executeQuery(q);
            while (result.next()) {
                String lotins = result.getString("INSPLOT");
                String oper = result.getString("INSPOPER");
                System.out.println(lotins
                        + '\t' + oper);

                if (oper.equals("0020")) {

                    new ProcessSap().Bapi_getCaracteristica(lotins, oper);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
