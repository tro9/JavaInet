/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    boolean login( String password) throws RemoteException;
    void sendMessage(String message) throws RemoteException;
    String receiveMessage() throws RemoteException;
    void changePassword(String newPassword) throws RemoteException;
}