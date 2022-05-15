package at.aau.se2.gamma.carcassonne.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import at.aau.se2.gamma.carcassonne.exceptions.NoServerInstanceException;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
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
        Log.d("SOUT", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SOUT", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("SOUT", "END");

        try {
            if (ServerThread.instance == null) {
                throw new NoServerInstanceException();
            }
            ServerThread serverThread = ServerThread.instance;
            serverThread.sendCommand(new DisconnectCommand(null), new ServerThread.RequestResponseHandler() {
                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    Log.e("SOUT", "END_onResponse");
                }

                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    Log.e("SOUT", "END_onFailure");
                }
            });
        } catch (NoServerInstanceException e) {
            Logger.error(e.getMessage());
        }
        stopSelf();
    }
}