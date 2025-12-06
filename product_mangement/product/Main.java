import javax.swing.*;
import ui.MainMenuFrame;
import util.DBConnection;

public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting Look and Feel");
            e.printStackTrace();
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Dang dong ung dung...");
            DBConnection.closeConnection();
        }));
        
        SwingUtilities.invokeLater(() -> {
            new MainMenuFrame();
        });
    }
}
