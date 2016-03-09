package ProcessSap_Java;

import ConnectionDB.DBConnect;
import ConnectionSAP.ConnectSap;
import DBOperations.DBCrud;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misael Recinos
 */
public class ProcessSap {

    DBConnect db;
    Connection con;
    Statement q;
    ResultSet result;

    public void getLotInsp() {
        db = new DBConnect();
        con = db.Connect();
        try {
            q = con.createStatement();
            new DBCrud().displayLotInsp(q);

        } catch (SQLException ex) {
            Logger.getLogger(ProcessSap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getInspOper() {
        db = new DBConnect();
        con = db.Connect();
        try {
            q = con.createStatement();
            new DBCrud().displayOper(q);

        } catch (SQLException ex) {
            Logger.getLogger(ProcessSap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JCoException ex) {
            Logger.getLogger(ProcessSap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Obtener lotes de inspección (Función )
    public void Bapi_getLotInspeccion() throws SQLException {
        db = new DBConnect();
        con = db.Connect();
        try {

            q = con.createStatement();

            JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
            JCoFunction function = destination.getRepository().getFunction("BAPI_INSPLOT_GETLIST");

            function.getImportParameterList().setValue("MAX_ROWS", "0");
            function.getImportParameterList().setValue("MATERIAL", "000000000035000019");
            //function.getImportParameterList().setValue("CREAT_DAT","20150801");
            if (function == null) {
                throw new RuntimeException("BAPI_INSPLOT_GETLIST  not found in SAP.");
            }
            try {
                function.execute(destination);
            } catch (AbapException e) {
                System.out.println("Error en bapi: " + e.toString());
                return;
            }

            JCoTable codes = function.getTableParameterList().getTable("INSPLOT_LIST");
            for (int i = 0; i < codes.getNumRows(); i++) {
                codes.setRow(i);

                String insplot = codes.getString("INSPLOT"), plant = codes.getString("PLANT"), orden = codes.getString("ORDER_NO"),
                        inspoints = codes.getString("INSPPOINTS"), material = codes.getString("MATERIAL"), batch = codes.getString("BATCH"),
                        txtlot = codes.getString("TXT_LOT"), txtmaterial = codes.getString("TXT_MAT"), creatdat = codes.getString("CREAT_DAT");

                if (insplot.substring(0, 2).equals("03")) {
                    System.out.println("Lote de inspeccion: " + insplot
                            + " || " + "Planta: " + plant
                            + " || " + "Orden: " + orden
                            + " || " + "Puntos insp: " + inspoints
                            + " || " + "Material: " + material
                            + " || " + "Lote: " + batch
                            + " || " + "Texto Breve: " + txtlot
                            + " || " + "Texto de material: " + txtmaterial
                            + " || " + "Fecha creacion: " + creatdat);

                    //new DBCrud().insertLotInsp(q, insplot, plant, orden, inspoints, material, batch, txtlot, txtmaterial, creatdat);
                }
            }

        } catch (JCoException ex) {
            System.err.println("Error en lista de inspeccion: " + ex.getMessage());
        }
        con.close();
        q.close();
    }

    public void Bapi_getOperacion(String lotInsp) throws JCoException, SQLException {
        db = new DBConnect();
        con = db.Connect();
        try {

            q = con.createStatement();

            JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
            JCoFunction function = destination.getRepository().getFunction("BAPI_INSPOPER_GETLIST");

            function.getImportParameterList().setValue("INSPLOT", lotInsp);

            if (function == null) {
                throw new RuntimeException("BAPI_INSPOPER_GETLIST  not found in SAP.");
            }
            try {
                function.execute(destination);
            } catch (AbapException e) {
                System.out.println("Error en bapi BAPI_INSPOPER_GETLIST: " + e.toString());
                return;
            }

            JCoTable codes = function.getTableParameterList().getTable("INSPOPER_LIST");
            for (int i = 0; i < codes.getNumRows(); i++) {
                codes.setRow(i);

                String inspoper = codes.getString("INSPOPER"), plant = codes.getString("PLANT"), txt_oper = codes.getString("TXT_OPER"), workcenter = codes.getString("WORKCENTER"), txt_workc = codes.getString("TXT_WORKC");

                System.out.println("Operaciones de Lot. Inspección: " + inspoper
                        + '\t' + "Planta: " + plant
                        + '\t' + "Txt breve: " + txt_oper
                        + '\t' + "Puesto de trabajo: " + workcenter
                        + '\t' + "Txt puesto de trab.: " + txt_workc);

                //new DBCrud().insertLotOper(q, lotInsp, inspoper, plant, txt_oper, workcenter, txt_workc);
            }
        } catch (SQLException ex) {
            System.err.println("Error en operaciones de inspección: " + ex.getMessage());
        }

        con.close();
        q.close();
    }

    public void Bapi_getCaracteristica(String insplot, String operacion) throws JCoException, SQLException {

        db = new DBConnect();
        con = db.Connect();
        q = con.createStatement();
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPOPER_GETCHAR");

        function.getImportParameterList().setValue("INSPLOT", insplot);
        function.getImportParameterList().setValue("INSPOPER", operacion);
        if (function == null) {
            throw new RuntimeException("BAPI_INSPOPER_GETCHAR not found in SAP.");
        }
        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.err.println(e.getMessage());
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
        if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
            //throw new RuntimeException(returnStructure.getString("MESSAGE"));
            System.out.println("Sin caracteristicas");
        }

        JCoTable codes = function.getTableParameterList().getTable("INSPCHAR_LIST");
        for (int i = 0; i < codes.getNumRows(); i++) {
            codes.setRow(i);

            String inspchar = codes.getString("INSPCHAR"),
                    status = codes.getString("STATUS"),
                    char_desc = codes.getString("CHAR_DESCR"),
                    chartype = codes.getString("CHAR_TYPE");

            System.out.println("Caracteristica: " + inspchar
                    + '\t' + "Status: " + status
                    + '\t' + "Descripción: " + char_desc
                    + '\t' + "Tipo: " + chartype);

            //new DBCrud().insertOperCaract(q, insplot, operacion, inspchar, status, char_desc, chartype);
        }

        con.close();
        q.close();
    }

    public void Bapi_getIns(String insplot, String insoper, String inspchar) throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPCHAR_GETRESULT");

        function.getImportParameterList().setValue("INSPLOT", insplot);
        function.getImportParameterList().setValue("INSPOPER", insoper);
        function.getImportParameterList().setValue("INSPCHAR", inspchar);

        if (function == null) {
            throw new RuntimeException("BAPI_INSPCHAR_GETRESULT not found in SAP.");
        }
        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
            return;
        }

        JCoStructure resultados = function.getExportParameterList().getStructure("CHAR_RESULT");

        System.out.println(resultados.getString("INSPLOT"));
        System.out.println(resultados.getString("MEAN_VALUE"));     //si son decimales (Datos cuatitativos)
        System.out.println(resultados.getString("ORIGINAL_INPUT")); // si son decimales (Datos cuantitativos)
        System.out.println(resultados.getString("CODE1")); // resultado cuando solo es texto (solo codigo)
        System.out.println(resultados.getString("CODE_GRP1"));// Texto breve cuando es texto
        System.out.println("Cerrado: " + resultados.getString("CLOSED")); //X
        System.out.println("No valido: " + resultados.getString("CHAR_INVAL"));
        System.out.println("Resultado: " + resultados.getString("EVALUATION")); // A
        System.out.println("Valoración: " + resultados.getString("EVALUATED")); // X
        System.out.println(resultados.getString("START_DATE"));
        System.out.println(resultados.getString("START_TIME"));
        System.out.println(resultados.getString("INSPECTOR"));
        System.out.println(resultados.getString("REMARK"));
    }

    public void Bapi_getResult() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPCHAR_GETRESULT");

        function.getImportParameterList().setValue("INSPLOT", "30000000515");
        function.getImportParameterList().setValue("INSPOPER", "0020");
        function.getImportParameterList().setValue("INSPCHAR", "0010");
        function.getImportParameterList().setValue("INSPSAMPLE", "000001");

        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
            return;
        }
        JCoStructure resultados = function.getExportParameterList().getStructure("SAMPLE_RESULT");

        System.out.println("Lot. Inspección: " + resultados.getString("INSPLOT"));
        System.out.println("Operación: " + resultados.getString("INSPOPER"));
        System.out.println("Caracteristicas: " + resultados.getString("INSPCHAR"));
        System.out.println("Turno: " + resultados.getString("INSPSAMPLE"));
        //System.out.println("Último Insp: " + resultados.getString("LAST_SMPL"));
        System.out.println("Cerrar carac: " + resultados.getString("CLOSED"));
        System.out.println("Valoración: " + resultados.getString("EVALUATED"));
        System.out.println("Resultado Insp: " + resultados.getString("EVALUATION"));
        System.out.println("Resultados carac: " + resultados.getString("VALID_VALS"));
        System.out.println("# Unidades defec: " + resultados.getString("NONCONF"));
        System.out.println("Defectos: " + resultados.getString("DEFECTS"));
        System.out.println("Valores debajo: " + resultados.getString("VALS_ABOVE"));
        System.out.println("Valores arriba: " + resultados.getString("VALS_BELOW"));
        System.out.println("Media: " + resultados.getString("MEAN_VALUE"));
        System.out.println("Varianza: " + resultados.getString("VARIANCE"));
        System.out.println("Máximo: " + resultados.getString("MAXIMUM"));
        System.out.println("Mínimo: " + resultados.getString("MINIMUM"));
        System.out.println("Fecha inicio: " + resultados.getString("START_DATE"));
        System.out.println("Hora inicio: " + resultados.getString("START_TIME"));
        System.out.println("Fecha fin: " + resultados.getString("END_DATE"));
        System.out.println("Hora fin: " + resultados.getString("END_TIME"));
        System.out.println("Inspector: " + resultados.getString("INSPECTOR"));
        //System.out.println("Origen datos: " + resultados.getString("RES_ORG"));
        System.out.println("Comentario: " + resultados.getString("REMARK"));
        System.out.println("Codigo 1: " + resultados.getString("CODE1"));
        System.out.println("Grupo de codigo1: " + resultados.getString("CODE_GRP1"));
        //System.out.println("Código 2: "+resultados.getString("CODE2"));
        //System.out.println("Grupo de código2: "+resultados.getString("CODE_GRP2"));
        //System.out.println("Código 3: "+resultados.getString("CODE3"));
        //System.out.println("Grupo de código3: "+resultados.getString("CODE_GRP3"));
        //System.out.println("Código 4: "+resultados.getString("CODE4"));
        //System.out.println("Grupo de código4: "+resultados.getString("CODE_GRP4"));
        //System.out.println("Código 5: "+resultados.getString("CODE5"));
        //System.out.println("Grupo de código5: "+resultados.getString("CODE_GRP5"));
        System.out.println("Dato original: " + resultados.getString("ORIGINAL_INPUT"));
        //System.out.println("Procesada correctamente: "+resultados.getString("INPPROC_READY"));
        System.out.println("Cantidad alternativa: " + resultados.getString("DIFF_DEC_PLACES"));

    }

    public void Bapi_set2() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPCHAR_SETRESULT");

        function.getImportParameterList().setValue("INSPLOT", "30000000450");
        function.getImportParameterList().setValue("INSPOPER", "0020");
        function.getImportParameterList().setValue("INSPCHAR", "0010");
        function.getImportParameterList().setValue("INSPSAMPLE", "000001");
        if (function == null) {
            throw new RuntimeException("BAPI_INSPCHAR_SETRESULT not found in SAP.");
        }

        JCoParameterList importParam = function.getImportParameterList();
        JCoStructure structure = importParam.getStructure("SAMPLE_RESULT");
        //structure.setValue("MEAN_VALUE", "12");
        structure.setValue("CODE1", "0010");
        structure.setValue("CODE_GRP1", "CANT");
        structure.setValue("REMARK", "cantidad valida");
        structure.setValue("CLOSED", "X");
        structure.setValue("EVALUATION", "A");
        structure.setValue("EVALUATED", "X");
        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
        }
    }

    public String[] Bapi_getCaracteristicaView(String insplot, String operacion) throws JCoException, SQLException {

        db = new DBConnect();
        con = db.Connect();
        q = con.createStatement();
        String[] carac = new String[4];

        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPOPER_GETCHAR");

        function.getImportParameterList().setValue("INSPLOT", insplot);
        function.getImportParameterList().setValue("INSPOPER", operacion);
        if (function == null) {
            throw new RuntimeException("BAPI_INSPOPER_GETCHAR not found in SAP.");
        }
        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
        if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        JCoTable codes = function.getTableParameterList().getTable("INSPCHAR_LIST");
        for (int i = 0; i < codes.getNumRows(); i++) {
            codes.setRow(i);

            String inspchar = codes.getString("INSPCHAR");
            carac[i] = inspchar;
        }

        con.close();
        q.close();

        return carac;
    }

    public void Bapi_setForzar(String[] c, String insplot, String inspoper) throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPCHAR_SETRESULT");

        for (int j = 0; j < c.length; j++) {
            function.getImportParameterList().setValue("INSPLOT", insplot);
            function.getImportParameterList().setValue("INSPOPER", inspoper);
            function.getImportParameterList().setValue("INSPCHAR", c[j]);
            function.getImportParameterList().setValue("INSPSAMPLE", "000001");
            if (function == null) {
                throw new RuntimeException("BAPI_INSPCHAR_SETRESULT not found in SAP.");
            }

            JCoParameterList importParam = function.getImportParameterList();
            JCoStructure structure = importParam.getStructure("SAMPLE_RESULT");
            //structure.setValue("MEAN_VALUE", "12");
            //structure.setValue("CODE1", "0010");
            //structure.setValue("CODE_GRP1", "CANT");
            structure.setValue("REMARK", "cierre forzado");
            structure.setValue("CLOSED", "X");
            structure.setValue("EVALUATION", "A");
            structure.setValue("EVALUATED", "X");
            try {
                function.execute(destination);
            } catch (AbapException e) {
                System.out.println(e.toString());
            }
        }

    }

    public void Bapi_setResult(String[] c, String insplot, String inspoper) throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPCHAR_SETRESULT");

        String rc = "";

        for (int j = 0; j < c.length; j++) {
            function.getImportParameterList().setValue("INSPLOT", insplot);
            function.getImportParameterList().setValue("INSPOPER", inspoper);
            function.getImportParameterList().setValue("INSPCHAR", c[j]);
            function.getImportParameterList().setValue("INSPSAMPLE", "000001");
            if (function == null) {
                throw new RuntimeException("BAPI_INSPCHAR_SETRESULT not found in SAP.");
            }

            JCoParameterList importParam = function.getImportParameterList();
            JCoStructure structure = importParam.getStructure("SAMPLE_RESULT");

            switch (j) {
                case 0:
                    rc = "CANT";
                    break;
                case 1:
                    rc = "SELLO";
                    break;

                case 2:
                    rc = "APARPA";
                    break;
                case 3:
                    rc = "CODIFIC";
                    break;

            }
            //structure.setValue("MEAN_VALUE", "12");
            structure.setValue("CODE1", "0010");
            structure.setValue("CODE_GRP1", rc);
            structure.setValue("REMARK", "Caracteristica " + rc);
            structure.setValue("CLOSED", "X");
            structure.setValue("EVALUATION", "A");
            structure.setValue("EVALUATED", "X");

            try {
                function.execute(destination);
            } catch (AbapException e) {
                System.out.println(e.toString());
            }
        }

    }

    public void Bapi_getStatusLotInsp() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPLOT_GETSTATUS");

        function.getImportParameterList().setValue("NUMBER", "30000000470");
        //function.getImportParameterList().setValue("NUMBER", "30000000166");

        if (function == null) {
            throw new RuntimeException("BAPI_INSPOPER_GETCHAR not found in SAP.");
        }
        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
            return;
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
        if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        JCoTable codes = function.getTableParameterList().getTable("SYSTEM_STATUS");
        for (int i = 0; i < codes.getNumRows(); i++) {
            codes.setRow(i);

            String inspchar = codes.getString("SYS_STATUS"),
                    status = codes.getString("SY_ST_TEXT"),
                    char_desc = codes.getString("SY_ST_DSCR");

            System.out.println("status sistema: " + inspchar
                    + '\t' + "Status individual: " + status
                    + '\t' + "st ind objeto: " + char_desc);
        }
    }

    public void Bapi_getStatusLotInspDE() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPLOT_GETDETAIL");

        //function.getImportParameterList().setValue("NUMBER", "030000000493");
        function.getImportParameterList().setValue("NUMBER", "30000000501");

        if (function == null) {
            throw new RuntimeException("BAPI_INSPLOT_GETDETAIL not found in SAP.");
        }
        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
            return;
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
        if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        JCoStructure resultados = function.getExportParameterList().getStructure("USAGE_DECISION_DATA");

        System.out.println(resultados.getString("INSPLOT"));
        //System.out.println(resultados.getString("IND_AUTOMATIC_UD"));
        //System.out.println(resultados.getString("UD_MODE")); 
        //System.out.println(resultados.getString("UD_CATALOG_TYPE"));
        System.out.println(resultados.getString("UD_PLANT"));
        System.out.println(resultados.getString("UD_SELECTED_SET"));
        System.out.println(resultados.getString("UD_CODE_GROUP"));
        System.out.println(resultados.getString("UD_CODE"));
        System.out.println(resultados.getString("CODE_VALUATION"));
        //System.out.println(resultados.getString("IND_UD_LONG_TEXT"));
        System.out.println(resultados.getString("UD_RECORDED_BY_USER"));
        System.out.println(resultados.getString("UD_RECORDED_ON_DATE"));
        System.out.println(resultados.getString("UD_RECORDED_AT_TIME"));
    }

    public void Bapi_getStatusRecord() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPOPER_RECORDRESULTS");

        function.getImportParameterList().setValue("INSPLOT", "30000000528");
        function.getImportParameterList().setValue("INSPOPER", "0020");

        if (function == null) {
            throw new RuntimeException("BAPI_INSPOPER_RECORDRESULTS not found in SAP.");
        }
        try {
            function.execute(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
            return;
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
        if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        JCoTable codes = function.getTableParameterList().getTable("CHAR_RESULTS");
        for (int i = 0; i < codes.getNumRows(); i++) {
            codes.setRow(i);

            System.out.println(codes.getString("INSPCHAR"));
            System.out.println(codes.getString("INSPSAMPLE"));
            System.out.println(codes.getString("MEAN_VALUE"));
            System.out.println(codes.getString("CODE1"));
        }

    }

    public void Bapi_setDE() throws JCoException {
        String tid = null;

        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        tid = destination.createTID();
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPLOT_SETUSAGEDECISION");
        JCoContext.begin(destination);

        //function.getImportParameterList().setValue("NUMBER", "030000000158");
        function.getImportParameterList().setValue("NUMBER", "30000000158");

        if (function == null) {
            throw new RuntimeException("BAPI_INSPLOT_SETUSAGEDECISION not found in SAP.");
        }

        JCoParameterList importParam = function.getImportParameterList();
        JCoStructure structure = importParam.getStructure("UD_DATA");
        structure.setValue("INSPLOT", "30000000158");
        structure.setValue("UD_SELECTED_SET", "GDE-DMM");
        structure.setValue("UD_PLANT", "M006");
        structure.setValue("UD_CODE_GROUP", "GDE-DMM");
        structure.setValue("UD_CODE", "A");
        structure.setValue("UD_FORCE_COMPLETION", "X");

        importParam.setValue("UD_DATA", structure);

        try {
            function.execute(destination);
            //JCoContext.end(destination);
        } catch (AbapException e) {
            System.out.println(e.toString());
        }

        JCoFunction commit = destination.getRepository().getFunction("BAPI_TRANSACTION_COMMIT");
        commit.execute(destination);
        destination.confirmTID(tid);
    }

}
