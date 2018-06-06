package Model.IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by User on 02-May-17.
 */
public class MyCompressorOutputStream extends OutputStream {
    private static byte END_OF_TYPE = 127;
    private static int MAX_UNSIGNED_MAZE_BYTE = 254;
    OutputStream out;
    int mLastOne;
    int mCounter;
    int mDelimiter;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
        mLastOne = 0;
        mCounter = 0;
        mDelimiter = 0;
    }

    /**
     * Row,127,Column,127,startRow,127,startCol,127,endRow,127,endCol,127,dataStartWithZero
     */
    @Override
    public void write(int b) throws IOException {
        if(mDelimiter < 6) //changed from 5 to 6 because I added a delimiter for column.
        {
            out.write(b);
            if(toUnsignedByte(b) == END_OF_TYPE) {
                mDelimiter++;
            }
        }
        else {
            if (mLastOne == b)
                mCounter++;
            else {
                int numOfMaxValue = mCounter/MAX_UNSIGNED_MAZE_BYTE;
                int remainder = mCounter%MAX_UNSIGNED_MAZE_BYTE;

                for (int i = 0; i < numOfMaxValue; i++)
                    out.write(toUnsignedByte(MAX_UNSIGNED_MAZE_BYTE));
                if (remainder > 0)
                    out.write(toUnsignedByte(remainder));
                }
                out.write(mCounter);
                mLastOne = b;
                mCounter = 1;
            }
        }

    private byte toUnsignedByte(int pInt)
    {
        return (byte) (pInt-(Byte.MAX_VALUE+1));
    }
}
;
