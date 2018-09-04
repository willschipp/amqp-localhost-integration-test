package com.example.code.endpoint;

import com.example.code.Application;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageEndpoint {

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    Application.Handler handler;

    @RequestMapping(method= RequestMethod.POST)
    public void send(@RequestBody String payload, @RequestParam("exchange") String exchange,@RequestParam("queueName") String queueName, HttpServletResponse response) throws Exception {
        AmqpTemplate template = new RabbitTemplate(connectionFactory);
        template.send(exchange,queueName, MessageBuilder.withBody(payload.getBytes()).build());
        response.setStatus(HttpStatus.OK.value());
    }

    @RequestMapping(method= RequestMethod.GET)
    public List<String> getMessages(@RequestParam("exchange") String exchange, @RequestParam("queueName") String queueName) {
        return handler.getMessages();
    }
}
