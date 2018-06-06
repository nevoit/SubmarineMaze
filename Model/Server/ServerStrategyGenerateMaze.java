package Model.Server;

import Model.IO.MyCompressorOutputStream;
import Model.algorithms.mazeGenerators.AMazeGenerator;
import Model.algorithms.mazeGenerators.Maze;
import java.io.*;
/**
 * Server class
 *
 * @author Nevo
 * @since 19-may-17
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();

            int[] mazeDimensions = (int[]) fromClient.readObject();
            int row,column;
            try
            {
                row = mazeDimensions[0];
                column = mazeDimensions[1];

            }
            catch (NullPointerException e)
            {
                row= ProjectProperties.getMinRow();
                column= ProjectProperties.getMinRow();
            }

            AMazeGenerator mazeGenerator = ProjectProperties.getMazeGenerator();
            Maze maze = mazeGenerator.generate(row, column); //Generate new maze

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try {
                OutputStream out = new MyCompressorOutputStream(byteOut);
                out.write(maze.toByteArray());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            toClient.writeObject(byteOut.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
