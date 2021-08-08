package adsstest.api;

import adsstest.factory.FeedInputFactory;
import adsstest.factory.InputFactory;
import adsstest.feed.IMessageService;
import adsstest.feed.IStorage;
import adsstest.feed.service.MessageService;
import adsstest.feed.store.CacheStore;
import adsstest.util.LoggerHelper;
import com.google.inject.AbstractModule;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 *
 * @author Chukwudi Alimele
 */
public class App extends AbstractModule {

    static {
        try {
            LoggerHelper.loggerConfig();
        } catch (URISyntaxException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void configure() {

        bind(IMessageService.class).to(MessageService.class);
        bind(InputFactory.class).to(FeedInputFactory.class);
        bind(IStorage.class).to(CacheStore.class);

    }
}
