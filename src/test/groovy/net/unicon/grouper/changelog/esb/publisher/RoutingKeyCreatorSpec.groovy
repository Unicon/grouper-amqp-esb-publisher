package net.unicon.grouper.changelog.esb.publisher

import spock.lang.Specification

/**
 *
 * Unit tests for implementations of {@link AmqpRoutingKeyCreator}
 */
class RoutingKeyCreatorSpec extends Specification {

    def esbEventJsonPayload = '''
        {
            "encrypted":false,
            "esbEvent":[
            {
                "changeOccurred":false,
                "eventType":"MEMBERSHIP_ADD",
                "fieldName":"members",
                "groupId":"0fdffd19f5d546cc98e6fa23400378e3",
                "groupName":"hawaii.edu:auto:sis:registration:MAN:MATH:100:123456:201410:enrolled",
                "id":"174c967d62804f5fb5904416c2962e5e",
                "membershipType":"flattened",
                "sequenceNumber":"5615",
                "sourceId":"ldap",
                "subjectId":"handerson"
            }
        ]
    }'''

    def regex_routingKey_Without_ColonsReplaced = 'group.modify.auto:sis:registration:MAN:MATH:100:123456:201410'

    def regex_routingKey_With_ColonsReplaced = 'group.modify.auto.sis.registration.MAN.MATH.100.123456.201410'

    def groupName_routingKey_Without_ColonsReplaced = 'hawaii.edu:auto:sis:registration:MAN:MATH:100:123456:201410:enrolled'

    def groupName_routingKey_With_ColonsReplaced = 'hawaii.edu.auto.sis.registration.MAN.MATH.100.123456.201410.enrolled'

    def regexReplacementDefinition = '^hawaii.edu:->group.modify.___(:enrolled|:waitlisted|:withdrawn)$->@rm@'

    def 'regex replacement implementation without colons replacement'() {
        given:
        def routingKeyCreatorUnderTest = new RegexReplacementBasedRoutingKeyCreator(false, this.regexReplacementDefinition)

        expect:
        routingKeyCreatorUnderTest.createRoutingKey(this.esbEventJsonPayload) == regex_routingKey_Without_ColonsReplaced
    }

    def 'regex replacement implementation with colons replacement'() {
        given:
        def routingKeyCreatorUnderTest = new RegexReplacementBasedRoutingKeyCreator(true, this.regexReplacementDefinition)

        expect:
        routingKeyCreatorUnderTest.createRoutingKey(this.esbEventJsonPayload) == regex_routingKey_With_ColonsReplaced
    }

    def 'group name based implementation without colons replacement'() {
        given:
        def routingKeyCreatorUnderTest = new GroupNameBasedRoutingKeyCreator(false)

        expect:
        routingKeyCreatorUnderTest.createRoutingKey(this.esbEventJsonPayload) == groupName_routingKey_Without_ColonsReplaced
    }

    def 'group name based implementation with colons replacement'() {
        given:
        def routingKeyCreatorUnderTest = new GroupNameBasedRoutingKeyCreator(true)

        expect:
        routingKeyCreatorUnderTest.createRoutingKey(this.esbEventJsonPayload) == groupName_routingKey_With_ColonsReplaced
    }

}
