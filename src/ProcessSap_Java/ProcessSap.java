package ProcessSap_Java;

import ConnectionDB.DBConnect;
import ConnectionSAP.ConnectSap;
import DBOperations.DBCrud;
import com.sap.conn.jco.AbapException;
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

/**
 *
 * @author Misael Recinos
 */
public class ProcessSap {

    DBConnect db;
    Connection con;
    Statement q;
    ResultSet result;

    public void Bapi_getLotInspeccion() throws SQLException {
        db = new DBConnect();
        con = db.Connect();
        try {

            q = con.createStatement();

            JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
            JCoFunction function = destination.getRepository().getFunction("BAPI_INSPLOT_GETLIST");

            function.getImportParameterList().setValue("MAX_ROWS", "0");
            //function.getImportParameterList().setValue("MATERIAL", "000000000035000012");
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

                String insplot = codes.getString("INSPLOT"),plant = codes.getString("PLANT"),orden = codes.getString("ORDER_NO"),
                        inspoints = codes.getString("INSPPOINTS"),material = codes.getString("MATERIAL"),batch = codes.getString("BATCH"),
                        txtlot = codes.getString("TXT_LOT"),txtmaterial = codes.getString("TXT_MAT"),creatdat = codes.getString("CREAT_DAT");

                System.out.println("Lote de inspección: " + insplot
                        + '\t' + "Planta: " + plant
                        + '\t' + "Orden: " + orden
                        + '\t' + "Puntos insp: " + inspoints
                        + '\t' + "Material: " + material
                        + '\t' + "Lote: " + batch
                        + '\t' + "Texto Breve: " + txtlot
                        + '\t' + "Texto de material: " + txtmaterial
                        + '\t' + "Fecha creación: " + creatdat);

                new DBCrud().insertLotInsp(q, insplot, plant, orden, inspoints, material, batch, txtlot, txtmaterial, creatdat);
            }
        } catch (JCoException ex) {
            System.err.println("Error en lista de inspeccion: " + ex.getMessage());
        }
        con.close();
        q.close();
    }

    public static void Bapi_getOperacion(String lotInsp) throws JCoException {
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
            System.out.println("Operaciones de Lot. Inspección: " + codes.getString("INSPOPER")
                    + '\t' + "Planta: " + codes.getString("PLANT")
                    + '\t' + "Txt breve: " + codes.getString("TXT_OPER")
                    + '\t' + "Puesto de trabajo: " + codes.getString("WORKCENTER")
                    + '\t' + "Txt puesto de trab.: " + codes.getString("TXT_WORKC"));
        }
    }

    public static void Bapi_getCaracteristica(String insplot, String operacion) throws JCoException {
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
            return;
        }

        JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
        if (!(returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S"))) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        JCoTable codes = function.getTableParameterList().getTable("INSPCHAR_LIST");
        for (int i = 0; i < codes.getNumRows(); i++) {
            codes.setRow(i);
            System.out.println("Caracteristica: " + codes.getString("INSPCHAR")
                    + '\t' + "Status: " + codes.getString("STATUS")
                    + '\t' + "Descripción: " + codes.getString("CHAR_DESCR")
                    + '\t' + "Tipo: " + codes.getString("CHAR_TYPE"));

        }
    }

    public static void Bapi_getIns(String insplot, String insoper, String inspchar) throws JCoException {
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

    public static void Bapi_getResult() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPCHAR_GETRESULT");

        function.getImportParameterList().setValue("INSPLOT", "030000000508");
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
        System.out.println("Último Insp: " + resultados.getString("LAST_SMPL"));
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
        System.out.println("Origen datos: " + resultados.getString("RES_ORG"));
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

    public static void Bapi_getStatusLotInsp() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPLOT_GETLIST");

        function.getImportParameterList().setValue("STATUS_UD", "ED");
        function.getImportParameterList().setValue("MATERIAL", "000000000036000025");

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

        JCoTable codes = function.getTableParameterList().getTable("INSPLOT_LIST");
        for (int i = 0; i < codes.getNumRows(); i++) {
            codes.setRow(i);
            System.out.println("Lote de inspección: " + codes.getString("INSPLOT")
                    + '\t' + "Material: " + codes.getString("MATERIAL")
                    + '\t' + "Puntos de inspección: " + codes.getString("INSPPOINTS"));
        }
    }

    public static void Bapi_set2() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(ConnectSap.ABAP_AS_POOLED);
        JCoFunction function = destination.getRepository().getFunction("BAPI_INSPCHAR_SETRESULT");

        function.getImportParameterList().setValue("INSPLOT", "030000000508");
        function.getImportParameterList().setValue("INSPOPER", "0020");
        function.getImportParameterList().setValue("INSPCHAR", "0040");
        function.getImportParameterList().setValue("INSPSAMPLE", "000001");
        if (function == null) {
            throw new RuntimeException("BAPI_INSPCHAR_SETRESULT not found in SAP.");
        }

        JCoParameterList importParam = function.getImportParameterList();
        JCoStructure structure = importParam.getStructure("SAMPLE_RESULT");
        //structure.setValue("MEAN_VALUE", "10");
        //structure.setValue("CODE1", "0010");
        //structure.setValue("CODE_GRP1", "CANT");
        //structure.setValue("REMARK", "Valores correctos");
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
