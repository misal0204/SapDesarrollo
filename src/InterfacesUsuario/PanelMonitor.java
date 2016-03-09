package InterfacesUsuario;

import ProcessSap_Java.ProcessSap;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Misael Recinos
 */
public class PanelMonitor extends javax.swing.JFrame {

    /**
     * Creates new form PanelMonitor
     */
    private PrintStream standardOut;

    public PanelMonitor() {
        initComponents();
        TextArea.setEditable(false);
        PrintStream printStream = new PrintStream(new CustomOutputStream(TextArea));

        // keeps reference of standard output stream
        standardOut = System.out;

        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);

        // creates the GUI
        /*setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;

        //add(btnLoad_LotInsp, constraints);

        constraints.gridx = 1;

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;*/

        //add(new JScrollPane(TextArea), constraints);

        // adds event handler for button Start
        btnLoad_LotInsp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                printLog();
            }
        });
        
        btnCleanScreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    TextArea.getDocument().remove(0,
                            TextArea.getDocument().getLength());
                    standardOut.println("Text area cleared");
                } catch (BadLocationException ex) {
                    System.err.println("Error en limpiar consola: "+ex.getMessage());
                }
            }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize(480, 320);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPanel = new javax.swing.JScrollPane();
        TextArea = new javax.swing.JTextArea();
        btnLoad_LotInsp = new javax.swing.JButton();
        btnCleanScreen = new javax.swing.JButton();
        btnBack1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cargar Inspecciones desde SAP");
        setResizable(false);

        TextArea.setColumns(20);
        TextArea.setRows(5);
        scrollPanel.setViewportView(TextArea);

        btnLoad_LotInsp.setText("Cargar lotes de inspección");

        btnCleanScreen.setText("Limpiar Consola");

        btnBack1.setText("Atras");
        btnBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPanel)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnBack1)
                .addGap(26, 26, 26)
                .addComponent(btnLoad_LotInsp)
                .addGap(28, 28, 28)
                .addComponent(btnCleanScreen)
                .addContainerGap(395, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack1)
                    .addComponent(btnLoad_LotInsp)
                    .addComponent(btnCleanScreen))
                .addGap(18, 18, 18)
                .addComponent(scrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack1ActionPerformed
        Principal p=new Principal();
        p.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBack1ActionPerformed

    /**
     * @param args the command line arguments
     */
    private void printLog() {
        try {
            new ProcessSap().Bapi_getLotInspeccion();
        } catch (SQLException ex) {
            System.out.println("Error en Recuperar lote de inspección: " + ex.getMessage());
        }
    }

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
            java.util.logging.Logger.getLogger(PanelMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelMonitor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea TextArea;
    private javax.swing.JButton btnBack1;
    private javax.swing.JButton btnCleanScreen;
    private javax.swing.JButton btnLoad_LotInsp;
    private javax.swing.JScrollPane scrollPanel;
    // End of variables declaration//GEN-END:variables
}
