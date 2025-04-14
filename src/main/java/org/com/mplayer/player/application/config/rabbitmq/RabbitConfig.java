package org.com.mplayer.player.application.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("RabbitConfigPlayer")
public class RabbitConfig {
    private static final String QUEUE_NAME = "player-queue";
    private static final String EXCHANGE_NAME = "player.exchange";
    private static final String ROUTING_KEY_NAME = "player.routing.key";

    @Bean("playerQueue")
    public Queue playerQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean("playerExchange")
    public Exchange playerExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("playerBinding")
    public Binding playerBinding() {
        return BindingBuilder.bind(playerQueue()).to(playerExchange()).with(ROUTING_KEY_NAME).noargs();
    }
}
