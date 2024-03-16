/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuan8;

/**
 *
 * @author Admin
 */
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Tạo server socket
            ServerSocket serverSocket = new ServerSocket(12345);
            
            // Chờ và chấp nhận kết nối từ client
            Socket socket = serverSocket.accept();
            
            // Tạo ObjectInputStream để nhận đối tượng
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            
            // Đọc đối tượng từ luồng vào
            Person person = (Person) inputStream.readObject();
            
            // In thông tin đối tượng nhận được
            System.out.println("Received: " + person);
            
            // Đóng kết nối
            socket.close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
