package cl.utfsm;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Hello extends Remote {
    void sendMessage(String clientUUID, String message) throws RemoteException;
    String receiveMessage(String clientUUID) throws RemoteException;
    ArrayList<String> showQueues() throws RemoteException;
    Boolean subscribe(String queueName, String clientUUID) throws RemoteException;
    Boolean unsubscribe(String clientUUID) throws RemoteException;
}