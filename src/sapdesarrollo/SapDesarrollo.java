package sapdesarrollo;

import ProcessSap_Java.ProcessSap;
import com.sap.conn.jco.JCoException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misael Recinos
 */
public class SapDesarrollo {

    public static void main(String[] args) {
        try {
            ConnectionSAP.ConnectSap.exeFunctionCall();
        } catch (JCoException ex) {
            Logger.getLogger(SapDesarrollo.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error en conexión con SAP: " + ex.getMessage());
        }

        ProcessSap.Bapi_getLotInspeccion();

    }
}
