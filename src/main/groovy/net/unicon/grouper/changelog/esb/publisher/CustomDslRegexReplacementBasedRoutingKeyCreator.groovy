package net.unicon.grouper.changelog.esb.publisher

/**
 *
 * Implementation which replaces parts of the provided group name with values of configured regex replacement definition DSL.
 */
class CustomDslRegexReplacementBasedRoutingKeyCreator extends AbstractGroupNameExtractingRoutingKeyCreator {

    private replacementDefinition

    CustomDslRegexReplacementBasedRoutingKeyCreator(boolean replaceColonsWithPeriods, String replacementDefinitionString) {
        super(replaceColonsWithPeriods)
        this.replacementDefinition = replacementDefinitionString.tokenize('___').collect { it.tokenize('->').collect { it.replaceAll('@rm@', '') } }
    }

    @Override
    String createRoutingKeyFromGroupName(String groupName) {
        if(!groupName) {
            return null
        }
        def routingKey
        this.replacementDefinition.each {
            if (!routingKey) {
                routingKey = groupName.replaceFirst(it[0], it[1])
            }
            else {
                routingKey = routingKey.replaceFirst(it[0], it[1])
            }
        }
        routingKey
    }
}
