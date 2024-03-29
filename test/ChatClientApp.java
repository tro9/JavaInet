/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ChatClientApp {
    private JFrame loginFrame;
    private JFrame chatFrame;
    private JTextArea chatTextArea;
    private JTextField messageTextField;
    private ChatServer chatServer;
    
    private Timer inactivityTimer;
    private final int INACTIVITY_TIMEOUT = 60000; // Inactivity timeout in milliseconds

    public static void main(String[] args) {
        ChatClientApp clientApp = new ChatClientApp();
        clientApp.initialize();
    }

    public void initialize() {
        try {
            // Look up the remote object from the registry
            chatServer = (ChatServer) Naming.lookup("rmi://localhost/ChatServer");

            createLoginGUI();
        } catch (Exception e) {
            System.err.println("Chat client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createLoginGUI() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(2, 2));

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
//        JButton changePasswordButton = new JButton("Change Password");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                try {
                    if (chatServer.login(password)) {
                        loginFrame.dispose();
                        createChatGUI();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid password. Login failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatClientApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
//        changePasswordButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String newPassword = JOptionPane.showInputDialog(loginFrame, "Enter new password:");
//                if (newPassword != null && !newPassword.isEmpty()) {
//                    try {
//                        chatServer.changePassword(newPassword);
//                        JOptionPane.showMessageDialog(loginFrame, "Password changed successfully. Please login again.", "Success", JOptionPane.INFORMATION_MESSAGE);
//                    } catch (RemoteException ex) {
//                        JOptionPane.showMessageDialog(loginFrame, "Failed to change password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        });
        
        // Prompt the user to set the initial password
        String initialPassword = JOptionPane.showInputDialog(loginFrame, "Set your initial password:");

        if (initialPassword != null && !initialPassword.isEmpty()) {
            try {
                chatServer.changePassword(initialPassword);
                JOptionPane.showMessageDialog(loginFrame, "Initial password set successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(loginFrame, "Failed to set initial password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        
        


        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    }

    private void createChatGUI() {
        chatFrame = new JFrame("Chat");
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setSize(400, 300);
        chatFrame.setLocationRelativeTo(null);

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);

        messageTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageTextField.getText();
                resetInactivityTimer();
                try {
                    chatServer.sendMessage(message);
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatClientApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                messageTextField.setText("");
            }
        });
        
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = JOptionPane.showInputDialog(chatFrame, "Enter new password:");
                if (newPassword != null && !newPassword.isEmpty()) {
                    try {
                        chatServer.changePassword(newPassword);
                        JOptionPane.showMessageDialog(chatFrame, "Password changed successfully. Please login again.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        chatFrame.dispose();
                        createLoginGUI();
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(chatFrame, "Failed to change password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        chatPanel.add(changePasswordButton, BorderLayout.NORTH);

        chatPanel.add(scrollPane, BorderLayout.CENTER);
        chatPanel.add(messageTextField, BorderLayout.SOUTH);
        chatPanel.add(sendButton, BorderLayout.EAST);

        chatFrame.add(chatPanel);
        chatFrame.setVisible(true);
        startInactivityTimer();

        // Start a new thread to continuously receive messages
        Thread receivingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String receivedMessage = chatServer.receiveMessage();
                        if (receivedMessage != null) {
                            chatTextArea.append(receivedMessage + "\n");
                        }
                        Thread.sleep(100);  // Sleep for a short interval to avoid high CPU usage
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        receivingThread.start();
    }
    
    
    private void startInactivityTimer() {
    if (inactivityTimer != null && inactivityTimer.isRunning()) {
        inactivityTimer.stop();
    }

    inactivityTimer = new Timer(INACTIVITY_TIMEOUT, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            logout();
        }
    });

    inactivityTimer.setRepeats(false); // The timer should only fire once
    inactivityTimer.start();
}

    private void resetInactivityTimer() {
        if (inactivityTimer != null && inactivityTimer.isRunning()) {
            inactivityTimer.restart();
        }
    }

    private void logout() {
        // Perform any necessary cleanup or session management on the server-side
        // For example, notify the server to invalidate the user's session or update their online status
        
        JOptionPane.showMessageDialog(chatFrame, "You have been logged out due to inactivity.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        chatFrame.dispose();
        createLoginGUI();
    }

}