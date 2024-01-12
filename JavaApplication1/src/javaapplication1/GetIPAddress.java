package javaapplication1;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIPAddress {

    public static void main(String[] args) {
        String hostname = "www.google.com";

        try {
            InetAddress address = InetAddress.getByName(hostname);

            // Lấy địa chỉ IP dạng chuỗi
            String ipAddress = address.getHostAddress();
            System.out.println("Địa chỉ IP của " + hostname + " là: " + ipAddress);
        } catch (UnknownHostException e) {
            System.err.println("Không thể tìm thấy địa chỉ IP cho " + hostname);
        }
    }
}
