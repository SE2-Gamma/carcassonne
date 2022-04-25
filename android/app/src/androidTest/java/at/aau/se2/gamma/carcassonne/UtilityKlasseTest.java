package at.aau.se2.gamma.carcassonne;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UtilityKlasseTest {
    String testfile;
    Context context;
    String testdata;

    @Test
    public void saveLoadTest(){
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testfile = "testfile.txt";
        double randata = Math.random();
        testdata = Double.toString(randata);

        UtilityKlasse.saveData(context,testfile,testdata);
        String recieved = UtilityKlasse.getSavedData(context,testfile);
        Assert.assertEquals(testdata,recieved);
    }
}