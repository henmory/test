package com.digiarty.phoneassistant.net;

/***
 *
 * Created on：24/04/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ServerConfig {



    private static String SERVER_IP = "";



    private static int SERVER_PORT = 10086;//自动分配

    public static void setServerIp(String serverIp) {
        SERVER_IP = serverIp;
    }
    public static String getServerIp() {
        return SERVER_IP;
    }

    public static void setServerPort(int serverPort) {
        SERVER_PORT = serverPort;
    }
    public static int getServerPort() {
        return SERVER_PORT;
    }

}
