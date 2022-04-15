package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static class realServer extends Thread{
        int port=4999;
        PrintWriter out=null;
        BufferedReader in=null;
        String input=null;
        @Override
        public void run() {

            try {
                Registry registry= LocateRegistry.createRegistry(5000);
                SessionCommunication sessionCommunication=new SessionCommunication();
                SessionCommunicationInterface stub = (SessionCommunicationInterface) UnicastRemoteObject.exportObject(sessionCommunication, 5000);
                registry.bind("Communication", stub);

                ServerSocket serverSocket=new ServerSocket(port);
                Socket socket=serverSocket.accept();
                out=new PrintWriter(socket.getOutputStream(),true);
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("Server up, Registry up");

                while(input==null){
                    input=  in.readLine();
                }

                System.out.println(input);
                if(!input.equals("Registry up, ClientRemote bound")){
                    System.out.println("fehler bei verbinden mit clientremote");
                }

                //Registry clientRegistry = LocateRegistry.getRegistry(1212);
                ClientRemote clientRemote = (ClientRemote) registry.lookup("ClientRemote");
                clientRemote.notify("notify Client");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AlreadyBoundException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
    public Server(){

    }
    public static void main(String[] args) {

            realServer server=new realServer();
            server.start();

            System.err.println("Server Ready");

    }
}
