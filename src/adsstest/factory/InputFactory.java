
package adsstest.factory;

import adsstest.input.FeedInput;
import java.io.IOException;

/**
 *
 * @author Chukwudi Alimele
 */
public interface InputFactory {
    
    public FeedInput socketFeed(String host, int port)  throws IOException;
    
    public FeedInput fileFeed(String  fileName)  throws IOException;
    
}
