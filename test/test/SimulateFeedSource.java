package test;

import adsstest.api.App;
import adsstest.api.MessageApi;
import adsstest.exceptions.MessageException;
import adsstest.factory.InputFactory;
import adsstest.feed.IMessageService;
import adsstest.input.FeedInput;
import adsstest.util.LoggerHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 *
 * @author Chukwudi Alimele
 */
public class SimulateFeedSource {

    static String[] CHARS = {"A", "B", "C", "D", "E"};
    static String[] chars = {"a", "b", "c", "c", "e"};
    private static String aVals;
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(SimulateFeedSource.class.getName());

    static{
        try {
            LoggerHelper.loggerConfig();
        } catch (URISyntaxException ex) {
            Logger.getLogger(SimulateFeedSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static String rpt(int n) {
        String str = "";
        for (int i = 0; i < n; i++) {
            str += CHARS[(int) (Math.random() * CHARS.length)];
            str += chars[(int) (Math.random() * chars.length)];
        }

        return str;
    }

    static String generatedCSVRecordWithInvalidMessageOfTypeA() {

        String record = "";

        double rand = Math.random();
        record += CHARS[(int) (rand * CHARS.length)] + ",";
        record += CHARS[(int) (rand * CHARS.length)] + (int) (1000 * rand) + ",";
        record += (int) (1000 * Math.random()) + ",";
        record += rpt(3) + ",";//this field is invalid for type A message
        record += rpt(3) + ",";
        record += rpt(3) + ",";
        record += rpt(4) + ",";
        record += rpt(2) + ",";
        record += rpt(5) + ",";
        record += rpt(7) + "\n";//end with new line

        return record;
    }

    static String generatedCSVRecord() {

        String record = "";

        double rand = Math.random();
        record += CHARS[(int) (rand * CHARS.length)] + ",";
        record += CHARS[(int) (rand * CHARS.length)] + (int) (1000 * rand) + ",";
        record += (int) (1000 * Math.random()) + ",";
        record += (int) (1000 * Math.random()) + ",";
        record += rpt(3) + ",";
        record += rpt(3) + ",";
        record += rpt(4) + ",";
        record += rpt(2) + ",";
        record += rpt(5) + ",";
        record += rpt(7) + "\n";//end with new line

        return record;
    }

    static void SimulateFeedFile() throws IOException {

        Injector injector = Guice.createInjector(new App());
        IMessageService msgService = injector.getInstance(IMessageService.class);
        InputFactory factory = injector.getInstance(InputFactory.class);

        FeedInput in = factory.fileFeed("C:\\ADSS_FEED_TEST_DATA\\feed_data.txt");

        MessageApi api = new MessageApi(msgService).start(in);

        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(() -> {

                    String pre_vals = api.getAVals();

                    if (!pre_vals.equals(aVals)) {
                        System.out.println(pre_vals);
                        aVals = pre_vals;
                    }

                }, 0, 400, TimeUnit.MILLISECONDS);

    }

    static void SimulateFeedSocketClient() throws IOException {

        Injector injector = Guice.createInjector(new App());
        IMessageService msgService = injector.getInstance(IMessageService.class);
        InputFactory factory = injector.getInstance(InputFactory.class);

        //FeedInput in = factory.fileFeed("C:\\ADSS_FEED_TEST_DATA\\feed_data.txt");
        FeedInput in = factory.socketFeed("localhost", 5000);

        MessageApi api = new MessageApi(msgService).start(in);

        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(() -> {

                    String pre_vals = api.getAVals();

                    if (!pre_vals.equals(aVals) && !pre_vals.isEmpty()) {
                        System.out.println(pre_vals);
                        aVals = pre_vals;
                    }

                }, 0, 400, TimeUnit.MILLISECONDS);

    }

    static void SimulateFeedSocketServer() throws IOException, Exception {

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                ServerSocket server = new ServerSocket(5000);
                server.setSoTimeout(100);

                while (true) {
                    try {
                        Socket sock = server.accept();
                        if (sock.isConnected()) {
                            Executors.newSingleThreadExecutor().submit(() -> {
                                OutputStream in = null;
                                try {
                                    while (true) {
                                        in = sock.getOutputStream();

                                        //valid type A message
                                        byte[] data = generatedCSVRecord().getBytes();

                                        //invalid type A message 
                                        //byte[] data = generatedCSVRecordWithInvalidMessageOfTypeA().getBytes();
                                        
                                        in.write(data);
                                        in.flush();

                                    }
                                } catch (IOException ex) {
                                    LOGGER.debug(ex.getMessage(), ex);
                                } finally {
                                    try {
                                        in.close();
                                    } catch (IOException ex) {
                                        LOGGER.debug(ex.getMessage(), ex);
                                    }
                                }
                            });

                        }
                    } catch (SocketTimeoutException ex) {/*Do nothing*/
                    }

                }

            } catch (IOException ex) {
                LOGGER.debug(ex.getMessage(), ex);
            }

        });

    }

    static void SimuateFeedSocketCommunication() throws Exception {

        SimulateFeedSocketServer();

        int CLIENT_COUNT = 3; //number of client connnections to the server
        for (int i = 0; i < CLIENT_COUNT; i++) {
            SimulateFeedSocketClient();
        }
    }

    public static void main(String args[]) throws IOException, Exception {

        SimuateFeedSocketCommunication(); //where the feed is coming from remote server using TCP protocal for communication

        //SimulateFeedFile();//where the feed is coming from local file
    }
}
