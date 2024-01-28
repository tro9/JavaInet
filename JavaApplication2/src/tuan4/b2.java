/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuan4;

/**
 *
 * @author Admin
 */
import java.net.*;

public class b2 {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            if (address instanceof Inet4Address) {
                System.out.println("IPv4 Address");
            } else if (address instanceof Inet6Address) {
                System.out.println("IPv6 Address");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
