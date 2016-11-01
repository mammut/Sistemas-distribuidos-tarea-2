package cl.utfsm;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server implements MoM {
    private HashMap<String, String> subscriptions;
    private HashMap<String, LinkedList<String>> clientQueues;
    private ArrayList<String> queues;

    @Override
    public void sendMessage(String clientUUID, String message) throws RemoteException {
        System.out.println("[Server] El cliente " + clientUUID + " ha enviado el siguiente mensaje a la cola \"" + subscriptions.get(clientUUID) + "\":");
        System.out.println("[Server] " + message);

        String queueName = subscriptions.get(clientUUID);
        for(Map.Entry<String, String> entry : subscriptions.entrySet()) {
            String client = entry.getKey();
            String queue = entry.getValue();
            if (queue.equals(queueName)) {
                clientQueues.get(client).add(message);
            }
        }
    }

    @Override
    public String receiveMessage(String clientUUID) throws RemoteException {
        if (clientQueues.get(clientUUID).isEmpty()) return null;
        return clientQueues.get(clientUUID).remove();
    }

    @Override
    public Boolean unsubscribe(String clientUUID) throws RemoteException {
        System.out.println("[Server] Desinscribiendo cliente " + clientUUID + " de la cola \"" + subscriptions.get(clientUUID) + "\"");
        subscriptions.remove(clientUUID);
        clientQueues.remove(clientUUID);
        return false;
    }

    @Override
    public Boolean subscribe(String clientUUID, String queueName) {
        System.out.println("[Server] Suscribiendo cliente " + clientUUID + " a la cola \"" + queueName + "\"");
        subscriptions.put(clientUUID, queueName);
        clientQueues.put(clientUUID, new LinkedList<String>());
        return true;
    }

    @Override
    public ArrayList<String> showQueues() throws RemoteException {
        return queues;
    }

    public Server() {
        queues = new ArrayList<>();
        queues.add("Cola 1");
        queues.add("Cola 2");
        queues.add("Cola 3");
        subscriptions = new HashMap<>();
        clientQueues = new HashMap<>();
    }

    public static void main(String[] args) {
        try {
            Server obj = new Server();
            MoM stub = (MoM) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("MoM", stub);

            System.out.println("[Server] Servidor esperando clientes...");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
