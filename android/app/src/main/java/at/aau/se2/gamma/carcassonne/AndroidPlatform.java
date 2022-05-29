package at.aau.se2.gamma.carcassonne;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AndroidPlatform implements AndroidInterface{
    private Activity context;

    public AndroidPlatform(Activity context) {
        this.context = context;
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
    public void startMainActivity(String userName, String userID) {
        Intent prevIntent = context.getIntent();
        Intent intent = new Intent(context, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("UserName", userName);
        extras.putString("UserID", userID);
        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
