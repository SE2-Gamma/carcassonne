package at.aau.se2.gamma.carcassonne.base;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import at.aau.se2.gamma.carcassonne.exceptions.NoServerInstanceException;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;

public class BaseActivity extends AppCompatActivity {
    protected ServerThread getServerThreadOrFail() throws NoServerInstanceException {
        if (ServerThread.instance == null) {
            throw new NoServerInstanceException();
        }

        return ServerThread.instance;
    }

    /**
     * send server command, and execute responseHandler on UIThread
     * @param command
     * @param responseHandler
     */
    protected void sendServerCommand(BaseCommand command, ServerThread.RequestResponseHandler responseHandler) {
        this.sendServerCommand(command, responseHandler, true);
    }

    /**
     * Send a server command, if there is a valid server instance.
     * @param command
     * @param responseHandler
     * @param shouldRunOnUIThread
     */
    protected void sendServerCommand(BaseCommand command, ServerThread.RequestResponseHandler responseHandler, boolean shouldRunOnUIThread) {
        try {
            // sendCommand, and execute responseHandler in uithread or not
            ServerThread serverThread = getServerThreadOrFail();
            serverThread.sendCommand(command, new ServerThread.RequestResponseHandler() {
                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    if (shouldRunOnUIThread) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseHandler.onResponse(response, payload, request);
                            }
                        });
                    } else {
                        responseHandler.onResponse(response, payload, request);
                    }
                }

                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    if (shouldRunOnUIThread) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseHandler.onFailure(response, payload, request);
                            }
                        });
                    } else {
                        responseHandler.onFailure(response, payload, request);
                    }
                }
            });
        } catch (NoServerInstanceException e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    // TODO: add method for group server responses
}
