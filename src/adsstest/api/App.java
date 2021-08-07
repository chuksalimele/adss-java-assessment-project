/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsstest.api;

import adsstest.factory.FeedInputFactory;
import adsstest.factory.InputFactory;
import adsstest.feed.IMessageService;
import adsstest.feed.IStorage;
import adsstest.feed.service.MessageService;
import adsstest.feed.store.CacheStore;
import com.google.inject.AbstractModule;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 *
 * @author Chukwudi Alimele
 */
public class App extends AbstractModule {

    private void loggerConfig() throws URISyntaxException {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        URL url = this.getClass().getClassLoader().getResource("adsstest/resources/log4j2.xml");
        context.setConfigLocation(url.toURI());
    }

    @Override
    protected void configure() {

        try {

            loggerConfig();

            bind(IMessageService.class).to(MessageService.class);
            bind(InputFactory.class).to(FeedInputFactory.class);
            bind(IStorage.class).to(CacheStore.class);
        } catch (URISyntaxException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
