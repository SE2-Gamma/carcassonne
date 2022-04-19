package at.aau.se2.gamma.carcassonne;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class UtilityKlasse {


    public static void saveData(Context context, String filename, String data){
        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            Log.d("Check","Saved: "+data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSavedData(Context context, String filename){

        FileInputStream fileInputStream;
        try{
            fileInputStream = context.openFileInput(filename);
            java.util.Scanner s = new Scanner(fileInputStream);
            s.useDelimiter("\\A");

            String streamString = s.hasNext()?s.next():"";

            s.close();
            Log.d("Check","Loaded: "+streamString);

            return streamString;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
