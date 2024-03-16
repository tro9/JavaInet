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

public class Client {
    public static void main(String[] args) {
        try {
            // Kết nối tới server
            Socket socket = new Socket("localhost", 12345);
            
            // Tạo ObjectOutputStream để gửi đối tượng
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            
            // Tạo một đối tượng cần tuần tự hóa
            Person person = new Person("John", 30);
            
            // Gửi đối tượng
            outputStream.writeObject(person);
            
            // Đóng kết nối
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
