# Grouper ESB AMQP Publisher

This is an implementation of Grouper ESB changelog publisher which pushes Grouper ESB JSON messages over to a configured AMQP broker exchange.

## Current release

`1.0.0-M2`

## Prerequisites

* AMQP Broker server (such as RabbitMQ)
* AMQP exchange with appropriate queues, bindings and routing keys set up
* Latest Grouper server installation

## Installation and configuration

* Copy [AMQP Publisher jar](https://github.com/Unicon/grouper-amqp-esb-publisher/releases/download/1.0.0-M2/grouper-amqp-esb-publisher-all.jar) to
`GROUPER_HOME/lib/grouper`
* Copy sample log4j configuration to `GROUPER_HOME/conf/log4j.properties`

```bash
## Dedicated Grouper ESB logging
log4j.appender.grouper_esb                            = org.apache.log4j.DailyRollingFileAppender
log4j.appender.grouper_esb.File                       = ${grouper.home}logs/grouper_esb.log
log4j.appender.grouper_esb.DatePattern                = '.'yyyy-MM-dd
log4j.appender.grouper_esb.layout                     = org.apache.log4j.PatternLayout
log4j.appender.grouper_esb.layout.ConversionPattern   = %d{ISO8601}: [%t] %-5p %C{1}.%M(%L) - %x - %m%n

log4j.logger.net.unicon.grouper.changelog.esb.publisher = DEBUG, grouper_esb
```

* Copy sample configuration for AMQP publisher and change it according to your installation environment to
`GROUPER_HOME/conf/grouper-loader.properties`

```bash
# ESB changelog consumer
changeLog.consumer.esbAmqp.class = edu.internet2.middleware.grouper.changeLog.esb.consumer.EsbConsumer
#run every minute
changeLog.consumer.esbAmqp.quartzCron = 0 * * * * ?
changeLog.consumer.esbAmqp.elfilter = event.eventType eq 'GROUP_DELETE' || event.eventType eq 'GROUP_ADD' || event.eventType eq 'MEMBERSHIP_DELETE' || event.eventType eq 'MEMBERSHIP_ADD'

changeLog.consumer.esbAmqp.publisher.class = net.unicon.grouper.changelog.esb.publisher.EsbAmqpPublisher
changeLog.consumer.esbAmqp.rabbitMqHostName = rabbit.docker
changeLog.consumer.esbAmqp.rabbitMqDefaultExchange = amq.topic

# SpEL-based regex replacement definition. The chain of 'replaceFirst' method calls will be applied to an extracted group name (String) at runtime
# See: http://docs.oracle.com/javase/7/docs/api/java/lang/String.html#replaceFirst(java.lang.String,%20java.lang.String)
changeLog.consumer.esbAmqp.regexReplacementDefinition = replaceFirst('^hawaii.edu:', 'group.modify.').replaceFirst('(:enrolled|:waitlisted|:withdrawn)$', '')
changeLog.consumer.esbAmqp.replaceRoutingKeyColonsWithPeriods = true
```

Custom required properties are:

* `changeLog.consumer.esbAmqp.publisher.class = net.unicon.grouper.changelog.esb.publisher.EsbAmqpPublisher`
* `changeLog.consumer.esbAmqp.rabbitMqHostName = [PUT A HOSTNAME OF RabbitMq server here]`
* `changeLog.consumer.esbAmqp.rabbitMqDefaultExchange = [PUT designated AMQP exchange name for grouper messages here]`

Sample configuration files are [log4j.properties](https://github.com/Unicon/grouper-amqp-esb-publisher/releases/download/1.0.0-M2/log4j.properties)
and [groper-loader.properties](https://github.com/Unicon/grouper-amqp-esb-publisher/releases/download/1.0.0-M2/grouper-loader.properties)

## Acknowledgements
Unicon's work on the RabbitMQ ESB Publisher is funded through a project with the University of Hawaii.

These individuals have provided guidance through out the development process:

* Michael Hodges
* Julio Polo
