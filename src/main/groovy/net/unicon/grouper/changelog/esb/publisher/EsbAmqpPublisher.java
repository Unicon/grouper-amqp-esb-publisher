package net.unicon.grouper.changelog.esb.publisher;

import edu.internet2.middleware.grouper.esb.listener.EsbListenerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Publishes Grouper events to AMQP broker, formatted as JSON strings.
 * The implementation assumes that this class is used in a thread-safe manner within Grouper changelog notification framework.
 */
public class EsbAmqpPublisher extends EsbListenerBase {

    private static final Logger logger = LoggerFactory.getLogger(EsbAmqpPublisher.class);

    private AmqpTemplate amqpTemplate;

    private AmqpRoutingKeyCreator amqpRoutingKeyCreator;

    public EsbAmqpPublisher() {
        final ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        this.amqpTemplate = ctx.getBean(AmqpTemplate.class);
        this.amqpRoutingKeyCreator = ctx.getBean(AmqpRoutingKeyCreator.class);
    }

    @Override
    public boolean dispatchEvent(String eventJsonString, String consumerName) {
        logger.debug("Consumer: [{}] got event: {}", consumerName, eventJsonString);
        final String routingKey = this.amqpRoutingKeyCreator.createRoutingKey(eventJsonString);
        if (routingKey != null) {
            logger.info("Publishing this event to AMQP exchange with [{}] routing key...", routingKey);
            this.amqpTemplate.convertAndSend(routingKey, eventJsonString);
            return true;
        }
        logger.warn("Unable to determine AMQP routingKey for the exchange to send the data to. Event message was not dispatched.");
        return false;
    }

    @Override
    public void disconnect() {
        //Noop
    }
}
