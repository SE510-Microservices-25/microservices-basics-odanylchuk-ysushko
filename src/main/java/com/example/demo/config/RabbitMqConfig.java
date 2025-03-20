package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    // Define Exchange
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user.exchange");
    }

    // Define Queues
    @Bean
    public Queue userCreatedQueue() {
        return new Queue("user.created.queue");
    }

    @Bean
    public Queue userUpdatedQueue() {
        return new Queue("user.updated.queue");
    }

    @Bean
    public Queue userDeletedQueue() {
        return new Queue("user.deleted.queue");
    }

    // Add serialization to json instead of java odjects
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    // Bind Queues to Exchange with Routing Keys
    @Bean
    public Binding bindingUserCreated(Queue userCreatedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(userExchange).with("user.created");
    }

    @Bean
    public Binding bindingUserUpdated(Queue userUpdatedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userUpdatedQueue).to(userExchange).with("user.updated");
    }

    @Bean
    public Binding bindingUserDeleted(Queue userDeletedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userDeletedQueue).to(userExchange).with("user.deleted");
    }
}
