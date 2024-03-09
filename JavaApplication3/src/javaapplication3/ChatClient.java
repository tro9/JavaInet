package javaapplication3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;

    public ChatClient(String name) {
        try {
            Socket socket = new Socket("localhost", 5555);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(name);

            frame = new JFrame(name + "'s ChatApp");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            chatArea = new JTextArea();
            chatArea.setEditable(false);
            JScrollPane chatScrollPane = new JScrollPane(chatArea);
            frame.add(chatScrollPane, BorderLayout.CENTER);

            JPanel messagePanel = new JPanel(new BorderLayout());
            messageField = new JTextField();
            JButton sendMessageButton = new JButton("Send");

            sendMessageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    out.println(name + ": " + message);
                    messageField.setText("");
                }
            });

            messagePanel.add(messageField, BorderLayout.CENTER);
            messagePanel.add(sendMessageButton, BorderLayout.EAST);
            frame.add(messagePanel, BorderLayout.SOUTH);

            new Thread(new ReceiveMessages()).start();

            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReceiveMessages implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClient("Client1"));
        SwingUtilities.invokeLater(() -> new ChatClient("Client2"));
        SwingUtilities.invokeLater(() -> new ChatClient("Client3"));
    }
}
