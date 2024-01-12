package javaapplication1;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class InternetConnectionTest {

    public static void main(String[] args) {
        testInternetConnection();
    }

    public static void testInternetConnection() {
        // Using a combination of socket and InetAddress checks for IPv4
        try {
            // Socket check
            Socket socket = new Socket("www.google.com", 80);
            System.out.println("Socket connection successful.");

            // InetAddress check for IPv4
            InetAddress address = InetAddress.getByName("www.google.com");
            if (address.isReachable(5000)) {
                System.out.println("connection is available.");
            } else {
                System.out.println("connection is not available.");
            }

            // Close the socket
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (java.io.IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
