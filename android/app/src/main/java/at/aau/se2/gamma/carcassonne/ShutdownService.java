package at.aau.se2.gamma.carcassonne;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import at.aau.se2.gamma.carcassonne.exceptions.NoServerInstanceException;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.DisconnectCommand;

public class ShutdownService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.debug("Service Started");

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Logger.debug("Service Destroyed");

        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Logger.debug("END");

        try {
            if (ServerThread.instance == null) {
                throw new NoServerInstanceException();
            }

            ServerThread serverThread = ServerThread.instance;
            serverThread.sendCommand(new DisconnectCommand(null), new ServerThread.RequestResponseHandler() {
                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    Logger.debug("END_onResponse");
                }

                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    Logger.debug("END_onFailure");
                }
            });

        } catch (NoServerInstanceException e) {
            Logger.error(e.getMessage());
        }

        stopSelf();
    }
}