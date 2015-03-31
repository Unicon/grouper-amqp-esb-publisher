package net.unicon.grouper.changelog.esb.publisher;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.StringUtils;

@Configuration
@PropertySource("classpath:grouper-loader.properties")
public class ApplicationConfig {

    @Bean
    public AmqpTemplate amqpTemplate(@Value("${changeLog.consumer.esbAmqp.rabbitMqHostName}")
                                     String rabbitMqHostName,
                                     @Value("${changeLog.consumer.esbAmqp.rabbitMqDefaultExchange}")
                                     String defaultExchange) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(new CachingConnectionFactory(rabbitMqHostName));
        rabbitTemplate.setExchange(defaultExchange);
        return rabbitTemplate;
    }

    @Bean
    public AmqpRoutingKeyCreator amqpRoutingKeyCreator(@Value("${changeLog.consumer.esbAmqp.regexReplacementDefinition:}")
                                                       String regexReplacementDefinition,
                                                       @Value("${changeLog.consumer.esbAmqp.replaceRoutingKeyColonsWithPeriods:}")
                                                       String replaceColonsWithPeriods) {

        final boolean replaceColons = (StringUtils.hasText(replaceColonsWithPeriods) && "true".equals(replaceColonsWithPeriods));
        if (StringUtils.hasText(regexReplacementDefinition)) {
            return new SpelRegexReplacementBasedRoutingKeyCreator(replaceColons, regexReplacementDefinition);
        }
        else {
            return new GroupNameBasedRoutingKeyCreator(replaceColons);
        }
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
