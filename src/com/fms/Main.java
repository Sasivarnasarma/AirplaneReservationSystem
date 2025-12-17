package com.fms;

import com.fms.ui.LoginFrame;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {

        try {
            FlatLightLaf.setup();

            java.awt.Font globalFont = new java.awt.Font(com.fms.ui.StyleTheme.FONT_FAMILY, java.awt.Font.PLAIN, 13);
            UIManager.put("defaultFont", globalFont);

            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
