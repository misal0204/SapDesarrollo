/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacesUsuario;

import ConnectionDB.DBConnect;
import DBOperations.DBCrud;
import ProcessSap_Java.ProcessSap;
import com.sap.conn.jco.JCoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hajfec12
 */
public class LiberarLotes extends javax.swing.JFrame {

    DBConnect db;
    Connection con;
    Statement q;
    ResultSet result;

    /**
     * Creates new form LiberarLotes
     */
    public LiberarLotes() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        edtLotInsp = new javax.swing.JTextField();
        btnFindLot = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtResultCierre = new javax.swing.JLabel();
        txtOperaciones = new javax.swing.JLabel();
        txtCaracteristicas = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Lot. Inspección: ");

        btnFindLot.setText("Buscar");
        btnFindLot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindLotActionPerformed(evt);
            }
        });

        jLabel2.setText("Operación default 0020");

        jLabel3.setText("Operaciones: ");

        jLabel4.setText("Caracteristicas:");

        jButton1.setText("Cerra con resultados");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Forzar cierre");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txtResultCierre.setText("Resultado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(70, 70, 70)
                        .addComponent(jButton2)
                        .addGap(142, 142, 142))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtResultCierre)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(edtLotInsp, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnFindLot)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCaracteristicas)
                                    .addComponent(txtOperaciones))))
                        .addGap(28, 28, 28))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edtLotInsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFindLot)
                    .addComponent(jLabel2))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtOperaciones))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCaracteristicas))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(txtResultCierre)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindLotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindLotActionPerformed
        //txtOperaciones.setText(edtLotInsp.getText());
        //txtCaracteristicas.setText("Caracterisiticas");
        String query = "SELECT * FROM SAP_LOTINSP_OPE WHERE INSPLOT='" + edtLotInsp.getText() + "'";
        String query2 = "SELECT * FROM SAP_OPER_CARACTERISTICAS WHERE INSPLOT='" + edtLotInsp.getText()
                + "' AND INSPOPER='0020'";
        String operaciones = "";
        String caracteristica = "";

        db = new DBConnect();
        con = db.Connect();
        try {
            q = con.createStatement();
            result = q.executeQuery(query);
            while (result.next()) {
                String oper = result.getString("INSPOPER");
                operaciones += oper + ",";
            }

            result = q.executeQuery(query2);
            while (result.next()) {
                String carac = result.getString("INSPCHAR");
                caracteristica += carac + ",";
            }

            if (operaciones.isEmpty()) {
                txtOperaciones.setText("Sin operaciones");
            } else {
                txtOperaciones.setText(operaciones);
            }

            if (caracteristica.isEmpty()) {
                txtCaracteristicas.setText("Sin caracteristica");
            } else {
                txtCaracteristicas.setText(caracteristica);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProcessSap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnFindLotActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String lotInspec = edtLotInsp.getText();
        try {

            String[] carac = new ProcessSap().Bapi_getCaracteristicaView(lotInspec, "0020");
            new ProcessSap().Bapi_setResult(carac, lotInspec, "0020");
            txtResultCierre.setText("Realizado con exíto");
        } catch (JCoException ex) {
            Logger.getLogger(LiberarLotes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LiberarLotes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String lotInspec = edtLotInsp.getText();
        try {

            String[] carac = new ProcessSap().Bapi_getCaracteristicaView(lotInspec, "0020");
            new ProcessSap().Bapi_setForzar(carac, lotInspec, "0020");
            txtResultCierre.setText("Realizado con exíto");
        } catch (JCoException ex) {
            Logger.getLogger(LiberarLotes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LiberarLotes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LiberarLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LiberarLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LiberarLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LiberarLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LiberarLotes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFindLot;
    private javax.swing.JTextField edtLotInsp;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel txtCaracteristicas;
    private javax.swing.JLabel txtOperaciones;
    private javax.swing.JLabel txtResultCierre;
    // End of variables declaration//GEN-END:variables
}
