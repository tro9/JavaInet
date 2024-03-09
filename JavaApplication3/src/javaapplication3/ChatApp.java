package javaapplication3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatApp extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedOutputStream fileOutputStream;


    public ChatApp(String clientName) {
        try {
            Socket socket = new Socket("localhost", 5555);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            // Create a separate output stream for file transfer
            fileOutputStream = new BufferedOutputStream(socket.getOutputStream());

            out.println(clientName);

            setTitle(clientName + "'s ChatApp");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Adjust the width proportion here
            int chatAreaWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 2.0 / 3.0);

            chatArea = new JTextArea();
            chatArea.setEditable(false);
            JScrollPane chatScrollPane = new JScrollPane(chatArea);
            chatScrollPane.setPreferredSize(new Dimension(chatAreaWidth, Toolkit.getDefaultToolkit().getScreenSize().height));
            add(chatScrollPane, BorderLayout.WEST);

            JPanel rightPanel = new JPanel(new BorderLayout());
            JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
            JButton sendMessageButton = new JButton("Send Message");
            JButton uploadImageButton = new JButton("Upload Image");
            JButton uploadFileButton = new JButton("Upload File");
            JButton createGroupButton = new JButton("Create Group");

            Dimension buttonSize = new Dimension(50, 30);
            sendMessageButton.setPreferredSize(buttonSize);
            uploadImageButton.setPreferredSize(buttonSize);
            uploadFileButton.setPreferredSize(buttonSize);
            createGroupButton.setPreferredSize(buttonSize);

            JPanel messagePanel = new JPanel(new BorderLayout());
            messageField = new JTextField();
            JButton sendMessageAction = new JButton("Send");

            sendMessageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    if (!message.isEmpty()) {
                        out.println(message);
                        messageField.setText(""); // Clear the message field
                    }
                }
            });

            sendMessageAction.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    if (!message.isEmpty()) {
                        out.println(message);
                        messageField.setText(""); // Clear the message field
                    }
                }
            });

            uploadFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(ChatApp.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getName();
                        out.println("FILE:" + fileName);

                        // Upload file content
                        try (FileInputStream fis = new FileInputStream(selectedFile);
                             BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {

                            byte[] buffer = new byte[1024];
                            int bytesRead;

                            while ((bytesRead = fis.read(buffer)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                            }

                            fileOutputStream.flush();
                            chatArea.append("You uploaded a file: " + fileName + "\n");

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            
            uploadImageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
                    fileChooser.setFileFilter(filter);

                    int result = fileChooser.showOpenDialog(ChatApp.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getName();
                        out.println("IMAGE:" + fileName);

                        // Upload image content
                        try (FileInputStream fis = new FileInputStream(selectedFile);
                             BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {

                            byte[] buffer = new byte[1024];
                            int bytesRead;

                            while ((bytesRead = fis.read(buffer)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                            }

                            bos.flush();
                            chatArea.append("You uploaded an image: " + fileName + "\n");

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });


            messagePanel.add(messageField, BorderLayout.CENTER);
            messagePanel.add(sendMessageAction, BorderLayout.EAST);

            buttonPanel.add(sendMessageButton);
            buttonPanel.add(uploadImageButton);
            buttonPanel.add(uploadFileButton);
            buttonPanel.add(createGroupButton);

            rightPanel.add(buttonPanel, BorderLayout.NORTH);
            rightPanel.add(messagePanel, BorderLayout.SOUTH);

            add(rightPanel, BorderLayout.CENTER);

            new Thread(new ReceiveMessages(in)).start();

            pack();
            setLocationRelativeTo(null);
            setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReceiveMessages implements Runnable {
        private BufferedReader in;

        public ReceiveMessages(BufferedReader in) {
            this.in = in;
        }

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
        SwingUtilities.invokeLater(() -> new ChatApp("Client1"));
        SwingUtilities.invokeLater(() -> new ChatApp("Client2"));
        SwingUtilities.invokeLater(() -> new ChatApp("Client3"));
    }
}
