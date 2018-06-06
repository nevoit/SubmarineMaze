package Model.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Nevo on 18-May-17.
 */
public class Client {

    private InetAddress mIP;
    private int mPort;
    private IClientStrategy mIClientStrategy;

    public Client(InetAddress IP, int port, IClientStrategy clientStrategy) {
        this.mIP = IP;
        this.mPort = port;
        this.mIClientStrategy = clientStrategy;
    }

    public void communicateWithServer(){
        try {
            Socket theServer = new Socket(mIP, mPort);
            //System.out.println("Connected to server!");
            mIClientStrategy.clientStrategy(theServer.getInputStream(),theServer.getOutputStream());
            theServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
