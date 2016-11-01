package cl.utfsm;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class Client {
    Boolean hasSuscription = false;
    String uuid;
    String host;
    MoM stub;

    public static void main(String[] args) {
        Client client = new Client();
        client.start((args.length < 1) ? null : args[0]);
    }

    void start(String host) {
        this.host = host;
        uuid = UUID.randomUUID().toString();
        Scanner scan = new Scanner(System.in);

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            this.stub = (MoM) registry.lookup("MoM");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.changeQueue();
        while(true) {
            this.showConsole();
            Integer s = scan.nextInt();

            if (this.hasSuscription) {
                     if (s == 1) this.sendMessage();
                else if (s == 2) this.getMessage();
                else if (s == 3) this.changeQueue();
                else if (s == 4) this.exit();
                else System.out.println("Opcion invalida");
            } else {
                if (s == 1) this.changeQueue();
                else System.out.println("Opcion invalida");
            }
        }
    }

    private void showConsole() {
        System.out.println("\n[Cliente] Seleccionar opción");
        if ( ! this.hasSuscription) {
            System.out.print("[Cliente] [1] Suscripción a cola:\n> ");
            return;
        }
        System.out.println("[Cliente] [1] Enviar Mensaje");
        System.out.println("[Cliente] [2] Recibir Mensaje");
        System.out.println("[Cliente] [3] Suscribir a otra cola");
        System.out.println("[Cliente] [4] Salir");
        System.out.print("> ");
    }

    void changeQueue() {
        try{
            ArrayList<String> queues = this.stub.showQueues();
            Integer i = 1;
            for (String s : queues) {
                System.out.println("[Cliente] [" + i++ + "] " + s);
            }
            System.out.print("> ");
            Scanner scan = new Scanner(System.in);
            Integer s = scan.nextInt();
            if (s < 1 || s > queues.size()){
                System.out.println("[Cliente] Error: opcion invalida");
                this.changeQueue();
                return;
            }

            if (this.hasSuscription) {
                this.hasSuscription = this.stub.unsubscribe(this.uuid);
            }
            this.hasSuscription = this.stub.subscribe(this.uuid, queues.get(s-1));
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        }
    }

    void sendMessage() {
        try{
            System.out.print("[Cliente] Escribir mensaje a enviar:\n> ");
            Scanner scan = new Scanner(System.in);
            String msg = scan.nextLine();
            this.stub.sendMessage(this.uuid, msg);
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        }
    }

    void getMessage() {
        try {
            String msg = this.stub.receiveMessage(this.uuid);
            if (msg != null){
                System.out.println("[Cliente] Mensaje recibido:");
                System.out.println("[Cliente] " + msg);
            }else {
                System.out.println("[Cliente] No quedan mensajes en la cola");
            }
        }catch(RemoteException e){
            System.out.println(e.getMessage());
        }
    }

    void exit() {
        try {
            this.hasSuscription = this.stub.unsubscribe(this.uuid);
            System.exit(0);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }
}
