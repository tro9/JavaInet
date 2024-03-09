package javaapplication3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    private static Set<PrintWriter> clients = new HashSet<>();
    private static Set<String> clientNames = new HashSet<>();
    private static Set<String> availableFiles = new HashSet<>();

    public static void main(String[] args) {
        int portNumber = 5555;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Chat server is listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String clientName = in.readLine();
                clients.add(out);
                clientNames.add(clientName);

                broadcast(clientName + " has joined the chat.");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("FILE:")) {
                        handleFileTransfer(inputLine.substring(5));
                    } else if (inputLine.startsWith("DOWNLOAD:")) {
                        handleFileDownload(inputLine.substring(9));
                    } else {
                        broadcast(clientName + ": " + inputLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        clients.remove(out);
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) {
            for (PrintWriter client : clients) {
                client.println(message);
            }
        }

        private void handleFileTransfer(String fileName) throws IOException {
            availableFiles.add(fileName);

            try (FileOutputStream fos = new FileOutputStream(fileName);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            broadcast("File '" + fileName + "' uploaded by " + in.readLine() + " has been received.");
        }
        
        private void handleImageTransfer(String fileName) throws IOException {
            availableFiles.add(fileName);

            try (FileOutputStream fos = new FileOutputStream(fileName);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            broadcast("Image '" + fileName + "' uploaded by " + in.readLine() + " has been received.");
        }

        private void handleFileDownload(String fileName) throws IOException {
            if (availableFiles.contains(fileName)) {
                byte[] fileBytes = Files.readAllBytes(new File(fileName).toPath());
                OutputStream os = clientSocket.getOutputStream();
                os.write(fileBytes, 0, fileBytes.length);
                os.flush();
            }
        }
    }


}

