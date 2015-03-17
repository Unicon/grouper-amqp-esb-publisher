package net.unicon.grouper.changelog.esb.publisher

import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

/**
 *
 * Abstract template-method based implementation that defines core logic of extracting group name
 * from JSON even payload and passes that name to subclasses to actually decide what routing key value
 * to return for it.
 */
@Slf4j
abstract class AbstractGroupeNameExtractingRoutingKeyCreator implements AmqpRoutingKeyCreator {

    private boolean replaceColonsWithPeriods

    AbstractGroupeNameExtractingRoutingKeyCreator(boolean replaceColonsWithPeriods) {
        this.replaceColonsWithPeriods = replaceColonsWithPeriods
    }

    @Override
    final String createRoutingKey(String grouperEsbEventPayload) throws RuntimeException {
        def jsonSlurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
        def parsedJson = jsonSlurper.parseText(grouperEsbEventPayload)
        log.debug('Parsing esb event payload: {}', grouperEsbEventPayload)
        def groupName = parsedJson.esbEvent[0].groupName ?: parsedJson.esbEvent[0].name
        def routingKey = createRoutingKeyFromGroupName(groupName)
        if(routingKey) {
            if(replaceColonsWithPeriods) {
                routingKey = routingKey.replaceAll(':', '.')
            }
            log.debug('Created AMQP routing key: [{}]', routingKey)
            return routingKey
        }
        else {
            log.warn('Unable to create routing key. Returning [null]...')
            return null
        }
    }

    abstract String createRoutingKeyFromGroupName(String groupName)
}
