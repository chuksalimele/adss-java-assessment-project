/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
