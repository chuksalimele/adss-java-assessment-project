
package adsstest;

import adsstest.api.App;
import adsstest.api.MessageApi;
import adsstest.factory.InputFactory;
import adsstest.feed.IMessageService;
import adsstest.input.FeedInput;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Chukwudi Alimele
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        Injector injector = Guice.createInjector(new App());        
        IMessageService msgService = injector.getInstance(IMessageService.class);               
        InputFactory factory = injector.getInstance(InputFactory.class);       
        
        FeedInput in = factory.fileFeed("C:\\ADSS_FEED_TEST_DATA\\feed_data.txt");
        
        //FeedInput in = factory.socketFeed("localhost", 3030);

        
        MessageApi api = new MessageApi(msgService).start(in);  
        
       
        
        
        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(api::getAVals, 0, 400, TimeUnit.MILLISECONDS);

        
    }
    
}
