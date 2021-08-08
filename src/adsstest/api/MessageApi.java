
package adsstest.api;

import adsstest.feed.IMessageService;
import adsstest.feed.Message;
import adsstest.feed.MsgType;
import adsstest.feed.service.MessageService;
import adsstest.func.Callback;
import adsstest.input.FeedInput;
import adsstest.listeners.MessageListener;
import com.google.inject.Inject;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Chukwudi Alimele
 */
public class MessageApi {

    /**
     * Unique id for every launch of this application.
     * This will be used to track only messages received since the start
     * of the application.
     */
    private final static String PROCESS_UUID = UUID.randomUUID().toString();
    
    private static final Logger LOGGER = LogManager.getLogger(MessageApi.class.getName());
    
    private final IMessageService msgService;

    private ScheduledFuture<?> executor;

    @Inject
    public MessageApi(IMessageService msgService) {
        this.msgService = msgService;
        this.msgService.addMessageListener(new MessageAdapterTypeA());
    }

    public MessageApi start(FeedInput input) {

        int initiaDelay = 0; //start immediately
        int msIterval = 500;//run every 500 ms

        this.executor = Executors
                .newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(msgService.start(input)::getMessage, initiaDelay, msIterval, TimeUnit.MILLISECONDS);

        return this;
    }

    void close() {
        msgService.stop();
        this.executor.cancel(false);//wait for background tasks to complete before shutting down
    }

    /**
     * Returns a string that summarizes the values from the messages of type ‘A’
     * that have been received so far, with each message separated by “%%”. For
     * example. If the following messages have been received:
     *
     * A,123A,1.0,2.91….. A,124A,8.3,2.981…..
     *
     * Then the following should be output from this method:
     *
     * unique id = ‘123A’,field2 = ‘1.0’, field3=’2.91’ %% unique id = ‘124A’,
     * field2 = ‘8.3’, field3=’2.981’ etc..
     *
     * The order of the messages is not important here, as long as all messages
     * are outputted
     * @return 
     */
    public String getAVals() {

        StringBuilder aVals = new StringBuilder();

        this.msgService
                .getStore()
                .eachInGroup(PROCESS_UUID, (Callback) (Object msg) -> {
                    String[] fields = ((Message) msg).getFields();
                    
                    if (aVals.length() > 0) {
                        aVals.append("%%");
                    }
                    
                    aVals.append("unique id=")
                            .append(fields[1]);
                    
                    for (int k = 2; k < fields.length; k++) {
                        Object value = fields[k];
                        if (k == 2 || k == 3) {
                            try {
                                value = Double.parseDouble(fields[k]);
                            } catch (NumberFormatException e) {
                                LOGGER.warn(String.format("Invalid type A message - value of field%s must be of type double but found %s", k, fields[k]));
                                continue;
                            }
                        }
                        
                        aVals.append(",")
                                .append("field")
                                .append(k)
                                .append("=")
                                .append(value);
                    }
        });

        return aVals.toString();
    }

    /**
     * Clears the store of values obtained from type A messages
     */
    public void clearAVals() {
        this.msgService.getStore().clear();
    }

    final private class MessageAdapterTypeA implements MessageListener {

        public MessageAdapterTypeA() {
        }

        @Override
        public void onMessage(Message msg) {

            if (msg.getType().equals(MsgType.MSG_TYPE_A)) {
                //store these fields in memory using the value from field 1 as a unique id.
                msgService.getStore().store(msg.getId(), PROCESS_UUID, msg);
            }
        }

    }
}
