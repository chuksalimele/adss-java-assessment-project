/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
