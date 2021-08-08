
package adsstest.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Chukwudi Alimele
 */
public abstract class AbstractFeedInput implements FeedInput {

    protected BufferedReader in = null;
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(AbstractFeedInput.class.getName());

    protected AbstractFeedInput(Object ...args) throws IOException {
        init(args);
    }


    private void init(Object ...args) throws IOException {
        InputStream ins = stream(args);    
        in = new BufferedReader(new InputStreamReader(ins, "UTF-8"));  
    }

    protected abstract InputStream stream(Object ...args) throws IOException;

    @Override
    public String receive() {
        String raw = null;
        try {
            raw = in.readLine();
        } catch (IOException ex) {
            LOGGER.error("An error occured while reading from feed!", ex);
        }

        return raw;
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(AbstractFeedInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
