package Model.Client;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nevo on 18-May-17.
 */
public interface IClientStrategy {
    void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}
