
package adsstest.input.transport;

import adsstest.input.AbstractFeedInput;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *Implementation of feed input for reading from file
 * 
 * @author Chukwudi Alimele
 */
public class FileFeedInput extends AbstractFeedInput{


    public FileFeedInput(String fileName) throws IOException{
        super(fileName);
    }
    
    @Override
    protected InputStream stream(Object ...args) throws FileNotFoundException {
        return new FileInputStream(args[0].toString());
    }        
}
