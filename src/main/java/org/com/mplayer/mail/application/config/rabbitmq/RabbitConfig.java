package org.com.mplayer.mail.application.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("RabbitConfigMail")
public class RabbitConfig {
    private static final String QUEUE_NAME = "mail-queue";
    private static final String EXCHANGE_NAME = "mail.exchange";
    private static final String ROUTING_KEY_NAME = "mail.routing.key";

    @Bean("mailQueue")
    public Queue mailQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean("mailExchange")
    public Exchange mailExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).build();
    }

    @Bean("mailBinding")
    public Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(ROUTING_KEY_NAME).noargs();
    }
}
