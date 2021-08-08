
package adsstest.feed.service;

import adsstest.exceptions.MessageException;
import adsstest.feed.IMessageService;
import adsstest.feed.IStorage;
import adsstest.feed.Message;
import adsstest.input.FeedInput;
import adsstest.listeners.MessageListener;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Feed service implementation
 *
 * @author Chukwudi Alimele
 */
public class MessageService implements IMessageService {

    IStorage msgStore;
    List<MessageListener> msgListeners = new ArrayList();
    FeedInput feedInput;
    private static final Logger LOGGER = LogManager.getLogger(MessageService.class.getName());

    @Inject
    public MessageService(IStorage storage) {
        this.msgStore = storage;
    }

    @Override
    public MessageService start(FeedInput input) {
        feedInput = input;
        msgStore.init();
        return this;
    }

    @Override
    public IStorage getStore() {
        return msgStore;
    }

    /**
     * Stop the service and do some cleanup
     */
    @Override
    public void stop() {
        msgListeners.clear();
        feedInput.close();
        msgStore.shutdown();
    }

    @Override
    public String getMessage() {

        String raw = feedInput.receive();

        if (raw != null && !raw.isEmpty()) {
            try {
                Message msg = new Message(raw);
                msgListeners.forEach((MessageListener msgListener) -> {
                    msgListener.onMessage(msg);
                });
            } catch (MessageException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        return raw;
    }

    @Override
    public void addMessageListener(MessageListener listener) {
        msgListeners.add(listener);
    }

    @Override
    public void removeMessageListener(MessageListener listener) {
        msgListeners.remove(listener);
    }

    @Override
    public void clearMessageListeners() {
        msgListeners.clear();
    }
}
