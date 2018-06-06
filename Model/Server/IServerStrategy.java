package Model.Server;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nevo on 18-May-17.
 */
public interface IServerStrategy {
    void serverStrategy(InputStream inFromClient, OutputStream outToClient);
}
