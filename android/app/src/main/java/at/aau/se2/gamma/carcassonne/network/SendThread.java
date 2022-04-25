package at.aau.se2.gamma.carcassonne.network;

import at.aau.se2.gamma.core.commands.BaseCommand;

public class SendThread extends Thread{
    BaseCommand command;
    ServerThread.RequestResponseHandler handler;
    public SendThread(BaseCommand command, ServerThread.RequestResponseHandler handler){
        this.command=command;
        this.handler=handler;
    }
    @Override
    public void run(){
        ServerThread.instance.sendCommand(command,handler);
    }
}
