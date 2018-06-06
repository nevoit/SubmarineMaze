package Model.Server;

import Model.algorithms.mazeGenerators.AMazeGenerator;
import Model.algorithms.mazeGenerators.Maze;
import Model.algorithms.search.*;

import java.io.*;

/**
 * This strategy receives mazes and solve them
 * Created by Nevo on 18-May-17.
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy {
    private String tempDirectoryPath = System.getProperty("java.io.tmpdir");

    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();
            Maze maze;
            String filePath;
            try {
                maze= (Maze) fromClient.readObject();
                filePath = tempDirectoryPath + "\\" + maze.hashCode() + ".maze";
                readersWriters(toClient, filePath, maze);
            }
            catch (Exception e) {
                AMazeGenerator mazeGenerator = ProjectProperties.getMazeGenerator();
                maze = mazeGenerator.generate(ProjectProperties.getMinRow(), ProjectProperties.getMinColumn()); //Generate new maze
                filePath = tempDirectoryPath + "\\" + maze.hashCode() + ".maze";
                readersWriters(toClient, filePath, maze);
            }
        }
        catch (IOException e) {
                e.printStackTrace();
        }

    }

    private synchronized void readersWriters(ObjectOutputStream toClient, String filePath, Maze maze) {
        try {
            File f = new File(filePath);
            if (f.isFile()) {
                ObjectInputStream fromFile = new ObjectInputStream(new FileInputStream(filePath));
                Solution solution = (Solution) fromFile.readObject();
                toClient.writeObject(solution);
                fromFile.close();
            } else {
                SearchableMaze domain = new SearchableMaze(maze);
                ISearchingAlgorithm searcher = ProjectProperties.getSearchingAlgorithm();

                //Solve a searching problem with a searcher
                Solution solution = searcher.solve(domain);
                ObjectOutputStream toFile = new ObjectOutputStream(new FileOutputStream(filePath));
                toFile.writeObject(solution);
                toFile.flush();
                toFile.close();
                toClient.writeObject(solution);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
