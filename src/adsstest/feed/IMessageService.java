
package adsstest.feed;

import adsstest.input.FeedInput;
import adsstest.listeners.MessageListener;

/**
 * Service that will declare the contract for service implementation
 * 
 * @author Chukwudi Alimele
 */
public interface IMessageService extends Feed{
 
    /**
     * This is use to start the message service
     * 
     * @param input
     * @return 
     */
    IMessageService start(FeedInput input);
 
    /**
     * This is used to stop the message service and clean up
     * 
     */
    void stop();
    
    /**
     * Get the store for storing the feed messages
     * 
     * @return 
     */
    IStorage getStore();
    
    /**
     * Register listeners for incoming messages 
     * 
     * @param listener 
     */
    void addMessageListener(MessageListener listener);
    
    /**
     * Remove listeners for messages
     * 
     * @param listener 
     */
    void removeMessageListener(MessageListener listener);
    
    /**
     * Clear all message listeners
     */
    void clearMessageListeners();

}
