/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServerApp {
    public static void main(String[] args) {
        try {
            // Create and start the RMI registry on the default port (1099)
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Create the ChatServerImpl instance
            ChatServer chatServer = new ChatServerImpl() {};
            
            // Bind the remote object to the registry
            Naming.rebind("ChatServer", chatServer);
            
            System.out.println("Chat server is running...");
        } catch (Exception e) {
            System.err.println("Chat server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}