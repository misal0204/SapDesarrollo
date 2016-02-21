package sapdesarrollo;

import ProcessSap_Java.ProcessSap;
import com.sap.conn.jco.JCoException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misael Recinos
 */
public class SapDesarrollo {

    public static void main(String[] args) throws JCoException {
        try {
            ConnectionSAP.ConnectSap.exeFunctionCall();
        } catch (JCoException ex) {
            Logger.getLogger(SapDesarrollo.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error en conexión con SAP: " + ex.getMessage());
        }

        try {
            new ProcessSap().Bapi_getLotInspeccion();
            //ProcessSap.Bapi_getOperacion("30000000450");
            //ProcessSap.Bapi_getCaracteristica("30000000450", "0020");
        } catch (SQLException ex) {
            Logger.getLogger(SapDesarrollo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
