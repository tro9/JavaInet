/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuan4;
import java.net.*;
/**
 *
 * @author Admin
 */
public class b3 {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://classroom.google.com/u/1/c/NjUxMTU5NDEzNzU0/a/NjU3OTY0Nzg3NzU1/details");
            System.out.println("Protocol: " + url.getProtocol());
            System.out.println("Host: " + url.getHost());
            System.out.println("Path: " + url.getPath());
            System.out.println("Query: " + url.getQuery());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
