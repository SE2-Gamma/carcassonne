import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.commands.DisconnectCommand;
import at.aau.se2.gamma.core.utils.ServerResponseDecrypter;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.LinkedList;

public class TestSocket {
    public TestSocket(Socket socket, SecureObjectInputStream secureObjectInputStream, ObjectOutputStream objectOutputStream,boolean withConsumer) {
        this.socket = socket;
        this.secureObjectInputStream = secureObjectInputStream;
        this.objectOutputStream = objectOutputStream;
        if(withConsumer){
            responseConsumer=new ResponseConsumer();
            responseConsumer.start();
        }
        this.withConsumer=withConsumer;

    }
    public TestSocket(Socket socket, SecureObjectInputStream secureObjectInputStream, ObjectOutputStream objectOutputStream) {
        this.socket = socket;
        this.secureObjectInputStream = secureObjectInputStream;
        this.objectOutputStream = objectOutputStream;

    }
    public void disconnect(){
        try {
            if(withConsumer){
                responseConsumer.running=false;
                responseConsumer.interrupt();
            }

           objectOutputStream.writeObject(new DisconnectCommand(null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    String id;
    boolean withConsumer=false;
    ResponseConsumer responseConsumer;
    Socket socket;
    SecureObjectInputStream secureObjectInputStream;
    ObjectOutputStream objectOutputStream;
    public LinkedList<Object> returncommands=new LinkedList<>();

    public void setID(String id) {
        this.id=id;
    }

    public class ResponseConsumer extends Thread{

        boolean running=true;
        @Override
        public void run() {
            while(running){
                try {
                    synchronized (returncommands) {
                        Object object = ServerResponseDecrypter.payloadRetriever(secureObjectInputStream);
                        returncommands.add(object);
                        System.err.println("//Testsocket-----------------------------------------" + object + " added to responses--------------------------------------------/");

                    }

                }catch (StreamCorruptedException e){
                    running=false;
                }catch(NullPointerException | EOFException e){

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
