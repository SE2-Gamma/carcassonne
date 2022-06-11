package at.aau.se2.gamma.carcassonne;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AndroidPlatform implements AndroidInterface{
    private Activity context;
    private String userName;
    private String userID;

    public AndroidPlatform(Activity context, String userName, String userID) {
        this.context = context;
        this.userName = userName;
        this.userID = userID;
    }

    public Activity getContext() {
        return context;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public void makeToast(String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("UserName", userName);
        extras.putString("UserID", userID);
        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
