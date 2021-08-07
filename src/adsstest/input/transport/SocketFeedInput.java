/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsstest.input.transport;

import adsstest.input.AbstractFeedInput;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Implementation of feed input for reading from the network using socket
 * 
 * @author Chukwudi Alimele
 */
public class SocketFeedInput extends AbstractFeedInput {

    private Socket clientSocket;
    

    public SocketFeedInput(String host, int port) throws IOException {
        super(host, port);
    }
    
    @Override
    protected InputStream stream(Object ...args) throws IOException {
        clientSocket = new Socket(args[0].toString(), Integer.parseInt(args[1].toString()));
        return clientSocket.getInputStream();
    }

    @Override
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketFeedInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.close();
    }
    
    
}
