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

    public static class AndroidConfig{
        private static String IP = "";

        private static int PORT = 10086;//写死

        public static void setIp(String serverIp) {
            IP = serverIp;
        }
        public static String getIp() {
            return IP;
        }

        public static void setPort(int serverPort) {
            PORT = serverPort;
        }
        public static int getPort() {
            return PORT;
        }
    }

    public static class PCConfig{


        private static String IP = "127.0.0.1";


        private static int PORT = 12580;//自动分配
        public static void setIP(String IP) {
            PCConfig.IP = IP;
        }

        public static String getIp() {
            return IP;
        }


        public static int getPort() {
            return PORT;
        }

        public static void setPort(int port) {
            PORT = port;
        }

    }


}
