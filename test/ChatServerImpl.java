/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<String> messages;
    private String password;
    private boolean isPasswordSet;

    protected ChatServerImpl() throws RemoteException {
        super();
        messages = new ArrayList<>();
        isPasswordSet = false;
    }

    @Override
    public boolean login(String password) throws RemoteException {
        if (!isPasswordSet) {
            throw new RemoteException("Please set the initial password first.");
        }

        return password.equals(this.password);
    }

    @Override
    public void changePassword(String newPassword) throws RemoteException {
        if (!isPasswordSet) {
            isPasswordSet = true;
        }
        password = newPassword;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        messages.add(message);
    }

    @Override
    public String receiveMessage() throws RemoteException {
        if (messages.isEmpty()) {
            return null;
        } else {
            String receivedMessage = messages.get(0);
            messages.remove(0);
            return receivedMessage;
        }
    }

//    @Override
//    public boolean isInitialPasswordSet() throws RemoteException {
//        return isPasswordSet;
//    }
}