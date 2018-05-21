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

        private static String SERVER_IP = "127.0.0.1";

        private static int SERVER_LISTEN_PORT = 10086;//动态生成

        public static String getServerIp() {
            return SERVER_IP;
        }

        public static void setServerPort(int serverPort) {
            SERVER_LISTEN_PORT = serverPort;
        }
        public static int getServerPort() {
            return SERVER_LISTEN_PORT;
        }
    }

    public static class ADBDConfig{


        private static String ADBD_IP = "127.0.0.1";

        //客户端调用reverse时，adbd监听的端口，这个端口需要pc端调用完reverse之后发送给我，并在actiivty中解析赋值
        private static int ADBD_LISTEN_PORT = 8000;//自动分配

        public static String getADBDIp() {
            return ADBD_IP;
        }


        public static int getADBDPort() {
            return ADBD_LISTEN_PORT;
        }

        public static void setADBDPort(int port) {
            ADBD_LISTEN_PORT = port;
        }

    }


}
