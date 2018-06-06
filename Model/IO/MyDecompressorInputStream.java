package Model.IO;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by User on 02-May-17.
 */
public class MyDecompressorInputStream extends InputStream {

    InputStream in;
    private static byte END_OF_TYPE = 127;
    int mDelimiter;
    int numOfRepetitions;
    boolean mValue;


    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
        this.mDelimiter=0;
        this.numOfRepetitions = 0;
        mValue = true; //initializes with true since we immediately flip it
    }

    /**
     * Row,127,Column,127,startRow,127,startCol,127,endRow,127,endCol,127,dataStartWithZero
     */

    @Override
    public int read() throws IOException {
        if(mDelimiter < 6)
        {
            int i=in.read();
            if(toUnsignedByte(i) == END_OF_TYPE) {
                mDelimiter++;
            }
            return i;
        }
        else{
            if(numOfRepetitions == 0){
                numOfRepetitions = in.read();
                mValue = !mValue;
            }
            numOfRepetitions--;
            int returnValue = 0;
            if(mValue)
                returnValue = 1;
            if(numOfRepetitions == 0) { //We finished printing all of the 0's or 1's and are reading the next value.
                numOfRepetitions = in.read();
                mValue = !mValue;
            }
            return returnValue;
        }
    }

    /**
     * toUnsignedByte:
     * converts int to byte
     * @param pInt
     * @return
     */
    private byte toUnsignedByte(int pInt)
    {
        return (byte) (pInt-(Byte.MAX_VALUE+1));
    }
}
