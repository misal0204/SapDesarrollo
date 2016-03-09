package sapdesarrollo;

import InterfacesUsuario.FijarResultados;
import InterfacesUsuario.LiberarLotes;
import InterfacesUsuario.PanelMonitor;
import InterfacesUsuario.Principal;
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

    public static void main(String[] args) throws JCoException, SQLException {

        //new ProcessSap().Bapi_getOperacion("030000000166");
        //new ProcessSap().Bapi_getCaracteristica("030000000166", "0020");
        
        //new ProcessSap().Bapi_getStatusLotInspDE();
        //new ProcessSap().Bapi_getStatusLotInsp();
        //new ProcessSap().Bapi_getLotInspeccion();
        new ProcessSap().Bapi_setDE();
        //new ProcessSap().Bapi_getStatusRecord();
        
        
        /*try {
            ConnectionSAP.ConnectSap.exeFunctionCall();
        } catch (JCoException ex) {
            Logger.getLogger(SapDesarrollo.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error en conexión con SAP: " + ex.getMessage());
        }*/

        //new ProcessSap().getInspOper();
        //new ProcessSap().Bapi_getCaracteristica("030000000450", "0020");
        
        //LiberarLotes ll=new LiberarLotes();
        //ll.setVisible(true);
        
        //new ProcessSap().Bapi_set2();
       /* 
        try {
         new ProcessSap().Bapi_getLotInspeccion();
         //new ProcessSap().getLotInsp();
         } catch (SQLException ex) {
         Logger.getLogger(SapDesarrollo.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        //new ProcessSap().Bapi_getIns("30000000515","0020","0010");
        //new ProcessSap().Bapi_getResult();
        //new ProcessSap().Bapi_set2();
        //Principal p=new Principal();
        //p.setVisible(true);
        //FijarResultados fr=new FijarResultados();
        //fr.setVisible(true);

    }
}
