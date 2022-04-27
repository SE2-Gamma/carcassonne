package at.aau.se2.gamma.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GlobalVariables {
    public static String getAdress() {

        return adress;
    }

    static String adress="localhost";

    public static String getDefaultname() {
        return defaultname;
    }

    private static String defaultname="XXXXXXXXXXXX";
    private static int port=1234;
    private static String defaultID="-1";
    public static String getDefaultID() {
        return defaultID;
    }

    public static int getPort() {
        return port;
    }
}
