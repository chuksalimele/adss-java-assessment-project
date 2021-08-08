
package adsstest.input;

/**
 *
 * @author Chukwudi Alimele
 */
public interface FeedInput {
    
    /**
     * Receive incoming feed messages
     * 
     * @return 
     */
    String receive();
    
    /**
     * Close the input stream
     */
    void close();

}
