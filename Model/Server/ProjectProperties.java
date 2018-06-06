package Model.Server;

import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.*;
import java.util.Properties;

/**
 * Created by User on 24-May-17.
 */
public final class ProjectProperties {
    private static Properties prop;
    private final static int MAX_THREADS_NUMBER = 10;
    private final static int MIN_ROW = 10;
    private final static int MIN_COLUMN = 10;

    private ProjectProperties() {
    }

    private static void setProperties() {

        prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("config.properties");

            // set the ProjectProperties value
            prop.setProperty("MazeGenerator", "MyMazeGenerator");
            prop.setProperty("NumberOfThreads", "10");
            prop.setProperty("SearchingAlgorithm", "BreadthFirstSearch");
            prop.setProperty("MinRow", "10");
            prop.setProperty("MinColumn", "10");

            // save ProjectProperties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public synchronized static String getProperties(String pProperties) {

        File f = new File("config.properties");
        if(!f.exists()) {setProperties();}

        prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");

            // load a ProjectProperties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty(pProperties);

        } catch (IOException ex) {
            //ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    public static int getNumberOfThreads() {
        String value = getProperties("NumberOfThreads");
        int threadNum;

        if(value=="")
            threadNum= MAX_THREADS_NUMBER;
        else
            threadNum = Integer.parseInt(value);

        return threadNum>0 ? threadNum : MAX_THREADS_NUMBER;
    }

    public static int getMinColumn() {
        String value = getProperties("MinColumn");
        int minCol = Integer.parseInt(value);

        return minCol>0 ? minCol : MIN_COLUMN;
    }

    public static int getMinRow() {
        String value = getProperties("MinRow");
        int minRow = Integer.parseInt(value);

        return minRow>0 ? minRow : MIN_ROW;
    }

    public static AMazeGenerator getMazeGenerator() {
        String value = getProperties("MazeGenerator");
       if (value == "SimpleMazeGenerator")
                return new SimpleMazeGenerator();
       else
                return new MyMazeGenerator();
    }

    public static ISearchingAlgorithm getSearchingAlgorithm() {
        String value = getProperties("SearchingAlgorithm");
       if(value=="DepthFirstSearch")
            return new DepthFirstSearch();
       else if(value=="BestFirstSearch")
            return new BestFirstSearch();
        else
            return new BreadthFirstSearch();
    }


}
