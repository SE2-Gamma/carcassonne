package at.aau.se2.gamma.core.network.Testing;

import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.core.network.ClientRemote;
import at.aau.se2.gamma.core.network.ClientRemoteImpl;
import at.aau.se2.gamma.core.network.SessionCommunicationInterface;
import jdk.jpackage.internal.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PlayingGroundClient extends Thread{
    PrintWriter out=null;
    BufferedReader in=null;
    String input=null;

    @Override
    public void run() {
        SessionCommunicationInterface communication=null;
        try {
            System.out.println("test");
            Registry registry = LocateRegistry.getRegistry(adress,5000);

            Socket socket=new Socket(adress,port);
            out=new PrintWriter(socket.getOutputStream(),true);
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            input=in.readLine();
            System.out.println(input);
            if(!input.equals("Server up, Registry up")){
                System.out.println("fehler nach verbinden");
            }
            // Registry clientRegistry= LocateRegistry.createRegistry(1212);
            ClientRemoteImpl clientRemote=new ClientRemoteImpl();
            ClientRemote stub = (ClientRemote) UnicastRemoteObject.exportObject(clientRemote, 1212);
            registry.bind("ClientRemote", stub);

            out.println("Registry up, ClientRemote bound\n");


            communication = (SessionCommunicationInterface) registry.lookup("Communication");
            System.out.println("123");
        } catch (IOException | AlreadyBoundException | NotBoundException e) {
            e.printStackTrace();
        }


    }

    public PlayingGroundClient() {

    }

    String adress="192.168.0.170";
    int port=4999;

    public static void main(String[] args) {
        System.out.println("test");
        PlayingGroundClient client=new PlayingGroundClient();
        client.start();
    }




}
