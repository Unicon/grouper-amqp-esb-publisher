package net.unicon.grouper.changelog.esb.publisher

/**
 *
 * Simply passes back a group name as a routing key
 */
class GroupNameBasedRoutingKeyCreator extends AbstractGroupNameExtractingRoutingKeyCreator {

    GroupNameBasedRoutingKeyCreator(boolean replaceColonsWithPeriods) {
        super(replaceColonsWithPeriods)
    }

    @Override
    String createRoutingKeyFromGroupName(String groupName) {
        groupName
    }
}
