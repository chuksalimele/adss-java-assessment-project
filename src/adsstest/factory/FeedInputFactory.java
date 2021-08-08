
package adsstest.factory;

import adsstest.input.FeedInput;
import adsstest.input.transport.FileFeedInput;
import adsstest.input.transport.SocketFeedInput;
import java.io.IOException;

/**
 *
 * @author Chukwudi Alimele
 */
public class FeedInputFactory implements InputFactory{

    @Override
    public FeedInput socketFeed(String host, int port) throws IOException {
        return new SocketFeedInput(host, port);
    }

    @Override
    public FeedInput fileFeed(String fileName) throws IOException {
        return new FileFeedInput(fileName);    
    }
    
}
