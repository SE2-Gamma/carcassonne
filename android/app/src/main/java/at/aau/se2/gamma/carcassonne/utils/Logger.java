package at.aau.se2.gamma.carcassonne.utils;

import android.util.Log;

public class Logger {

    public static String TAG = "GAMMA-LOG";

    public static void debug(String message) {
        Log.d(TAG, message);
    }
    public static void error(String message) {
        Log.e(TAG, message);
    }
}
