package net.unicon.grouper.changelog.esb.publisher;

/**
 * A strategy interface for determining/creating routing key values for AMQP exchanges from
 * Grouper esb events payload.
 */
public interface AmqpRoutingKeyCreator {

    String createRoutingKey(String grouperEsbEventPayload) throws RuntimeException;
}
