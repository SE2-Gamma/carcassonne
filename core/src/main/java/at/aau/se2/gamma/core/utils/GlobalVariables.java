package at.aau.se2.gamma.core.utils;

public class GlobalVariables {
    public static String getAdress() {
        return adress;
    }


    public static void setAdress(String adress) {
        GlobalVariables.adress = adress;
    }

    static String adress="192.168.178.23";


    public static String getDefaultname() {
        return defaultname;
    }

    private static String defaultname="XXXXXXXXXXXX";
    private static int port=53212;
    private static String defaultID="-1";
    public static String getDefaultID() {
        return defaultID;
    }

    public static int getPort() {
        return port;
    }
}
