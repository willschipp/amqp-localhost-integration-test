package com.example.code;


import com.rabbitmq.client.LongString;
import com.rabbitmq.client.SaslConfig;
import com.rabbitmq.client.SaslMechanism;
import com.rabbitmq.client.impl.LongStringHelper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    @Profile("embedded")
    public ConnectionFactory connectionFactory() {

        com.rabbitmq.client.ConnectionFactory client = new com.rabbitmq.client.ConnectionFactory();

        client.setHost("localhost");
        client.setPort(5672);
        client.setSaslConfig(new SaslConfig() {
            public SaslMechanism getSaslMechanism(String[] mechanisms) {
                return new SaslMechanism() {
                    public String getName() {
                        return "ANONYMOUS";
                    }

                    public LongString handleChallenge(LongString challenge, String username, String password) {
                        return LongStringHelper.asLongString("");
                    }
                };
            }
        });
        return new CachingConnectionFactory(client);
    }

    @Bean
    public String queueName() {
        return "testQueue";
    }

    @Bean
    @Profile("embedded")
    public Queue queue() {
        return new Queue(queueName(),false);
    }

    @Bean
    @Profile("embedded")
    public TopicExchange exchange() {
        return new TopicExchange("default");
    }

    @Bean
    @Profile("embedded")
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName());
    }

    @Bean
    @Profile("embedded")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @Profile("embedded")
    MessageListenerAdapter listenerAdapter(Handler handler) {
        return new MessageListenerAdapter(handler,"receivedMessage");
    }


    @Component
    @Profile("embedded")
    public class Handler {

        private java.util.Queue<String> messages;

        Handler() {
            messages = new ArrayDeque<>();
        }

        public void receivedMessage(byte[] message) {
            messages.add(new String(message));
        }

        public List<String> getMessages() {
            List<String> messageList = new ArrayList<String>();
            for (String message : messages) {
                messageList.add(messages.poll());
            }//end for
            //return
            return messageList;
        }
    }
}
