package cl.utfsm;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MoM extends Remote {
    void sendMessage(String clientUUID, String message) throws RemoteException;
    String receiveMessage(String clientUUID) throws RemoteException;
    ArrayList<String> showQueues() throws RemoteException;
    Boolean subscribe(String clientUUID, String queueName) throws RemoteException;
    Boolean unsubscribe(String clientUUID) throws RemoteException;
}
