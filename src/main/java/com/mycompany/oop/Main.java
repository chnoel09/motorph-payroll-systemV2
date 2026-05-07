package com.mycompany.oop;

import javax.swing.SwingUtilities;

import com.mycompany.oop.view.LoginFrame;

public class Main {

    public static void main(String[] args) {

        // Launch GUI safely inside the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}

/*
GROUP CURRENT VERSION
• Repository Pattern
• Service Layer Pattern
• Template Method Pattern
• Polymorphism-ready Employee hierarchy
• Cutoff-based payroll cycle
• Duplicate prevention
• Demo reset support
• HR view + Employee self-service
*/