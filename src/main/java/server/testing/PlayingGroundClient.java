package server.testing;

import models.impl.Session;
import server.ClientRemote;
import server.ClientRemoteImpl;
import server.SessionCommunication;
import server.SessionCommunicationInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PlayingGroundClient {
    PrintWriter out=null;
    BufferedReader in=null;
    String input=null;

    public PlayingGroundClient() {
        SessionCommunicationInterface communication=null;
        try {
            Registry registry = LocateRegistry.getRegistry(5000);
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

    String adress="localhost";
    int port=4999;

    public static void main(String[] args) {
        PlayingGroundClient client=new PlayingGroundClient();
    }




}
