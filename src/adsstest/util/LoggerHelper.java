/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adsstest.util;

import java.net.URISyntaxException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 *
 * @author USER
 */
public class LoggerHelper {

    
    public static void loggerConfig() throws URISyntaxException {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        URL url = new LoggerHelper().getClass().getClassLoader().getResource("adsstest/resources/log4j2.xml");
        context.setConfigLocation(url.toURI());
    }

}
