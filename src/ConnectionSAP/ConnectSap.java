package ConnectionSAP;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 *
 * @author Misael Recinos
 */
public class ConnectSap {

    public final static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";
    private final static String jco_ashost = "10.10.100.52";
    private final static String jco_asrouter = "/H/168.234.192.205/S/3299";
    private final static String jco_sysnr = "00";
    private final static String jco_client = "241";
    private final static String jco_user = "jrecinos";//molinosqm02 - //jrecinos - // nguzman
    private final static String jco_passwd = "Manager$15";//barreritas$00 - // Manager$15 - //minimi10
    private final static String jco_lang = "ES";
    private final static String jco_pool_capacity = "3";
    private final static String jco_peak_limit = "10";

    private final static String funcionConexion = "STFC_CONNECTION";

    static {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, jco_ashost);
        connectProperties.setProperty(DestinationDataProvider.JCO_SAPROUTER, jco_asrouter);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, jco_sysnr);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, jco_client);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, jco_user);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, jco_passwd);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, jco_lang);
        //Numero máximo de connection que puede ser abierto el destino
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, jco_pool_capacity);

        //Número máximo de conexiones activas
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, jco_peak_limit);

        createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);
    }

    public static void createDataFile(String name, String suffix, Properties properties) {
        File cfg = new File(name + "." + suffix);
        if (!cfg.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(cfg, false);
                properties.store(fos, "Creación de archivo exitoso");
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create the destination " + cfg.getName(), e);
            }
        }
    }

    public static void exeFunctionCall() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction(funcionConexion);

        if (function == null) {
            throw new RuntimeException("STFC_CONNECTION not found in SAP.");
        }
        //Se recupera el importparmeterList() y se fija un valor
        function.getImportParameterList().setValue("REQUTEXT", "Conexión realizada con exíto");

        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
            return;
        }
        System.out.println("STFC_CONNECTION finished: ");
        System.out.println("Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
        System.out.println("Response: " + function.getExportParameterList().getString("RESPTEXT"));
        System.out.println();
    }
}
