/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misael Recinos
 */
public class DBConnect {

    //Instancia o servicio a la que se conectará.

    private final String INSTANCIA = "srvdbha";
    //Puerto utilizado para la comunicación entre la aplicación y la instancia
    private final String PUERTO = "1521";
    //Base de datos
    private final String DB = "molinos";
    //Usuario de base de datos
    private final String USER = "datalabr2";
    //Password de usuario
    private final String PASS = "datalabr2";

    /* Método que realiza la conexión a la base de datos
     @return Devuelve una conexión con la base de datos, en estado open. 
     */
    public Connection Connect() {
        try {
            Connection conn;
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            conn = DriverManager.getConnection("jdbc:oracle:thin:@" + INSTANCIA + ":" + PUERTO + ":" + DB, USER, PASS);
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Coneccion fallida: " + ex.getMessage());
            return null;
        } catch (NoClassDefFoundError e) {
            System.err.println("Error en clases: " + e.getMessage());
            return null;
        } catch (NullPointerException ne) {
            System.err.println("Erro null pointer in DBConnection:" + ne.getMessage());
            return null;
        }
    } //Cierre de método Connect
}
