package DBOperations;

import java.sql.SQLException;
import java.sql.Statement;

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
                    + "'" + txtlot + "','"+txtmat+"',TO_DATE('"+createdate+"','YYYY-MM-DD HH24:MI:SS'))");
        } catch (SQLException ex) {

            System.out.println("Error en insertar lote de inspección: " + ex.getMessage());
        }
    }
}
