package Model.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 *
 * @author Nevo
 * @since 19-may-17
 */
public class Server {
    private int mPort; //The port we listen to
    private int mListeningInterval; //Listen Interval (ms)
    private IServerStrategy mIServerStrategy; //Server strategy we got in the CTOR
    private volatile boolean mStop; //Used to indicate that a variable's value will be modified by different threads.
    private ExecutorService executor;
    private volatile boolean mCreated;

    public Server(int mPort, int mListeningInterval, IServerStrategy mIServerStrategy) {
        this.mPort = mPort;
        this.mListeningInterval = mListeningInterval;
        this.mIServerStrategy = mIServerStrategy;
        this.mCreated=false;
        executor = Executors.newFixedThreadPool(ProjectProperties.getNumberOfThreads()); //We want only up to x threads
    }

    public void start() {
        boolean result = mCreated; //Expensive
        if(!result) {
            synchronized (this) {
                result=mCreated;
                if(!result) {
                    //System.out.println("Create a new Thread to runServer()");
                    mCreated = result = true;
                    //Lambda expression that use Runnable with the local function runServer()
                    executor.execute(() -> runServer());
                }
            }
        }
        else
            System.out.println("You already created this server!");
    }

    private void runServer() {
        try {
            ServerSocket server = new ServerSocket(mPort);
            server.setSoTimeout(mListeningInterval);

            while (!mStop)
            {
                try {
                    Socket aClient = server.accept(); // blocking call
                    //Lambda expression that use Runnable with the local function handleClient(_____)
                    executor.execute(() -> handleClient(aClient));
                } catch (SocketTimeoutException e) {
                    System.out.println("SocketTimeout!");
                }
            }
            executor.shutdown();
            server.close();
        } catch (IOException e){
            //System.err.println(e.toString());
        }
    }

    private void handleClient(Socket aClient) {
        try {
            //System.out.println("Client excepted!");
            //System.out.println((String.format("Handling client with socket: %s", aClient.toString())));
            mIServerStrategy.serverStrategy(aClient.getInputStream(), aClient.getOutputStream());
            aClient.getInputStream().close();
            aClient.getOutputStream().close();
            aClient.close();
        } catch (IOException e) {
            //System.err.println(e.toString());
        }
    }

    public void stop() {
        //System.out.println("Stopping the Server!");
        mStop = true;
    }

}
