package integration;

import com.example.code.Application;
import com.example.code.endpoint.MessageEndpoint;
import com.rabbitmq.client.LongString;
import com.rabbitmq.client.SaslConfig;
import com.rabbitmq.client.SaslMechanism;
import com.rabbitmq.client.impl.LongStringHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes=Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("embedded")
public class MessageEndpointIT {

    @Autowired
    MessageEndpoint endpoint;

    static EmbeddedBroker broker;

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    Queue queue;


    @BeforeClass
    public static void startup() throws Exception {
        broker = new EmbeddedBroker();
        broker.start();
    }

    @AfterClass
    public static void stop() throws Exception {
        broker.stop();
    }

    @Test
    public void testSend() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        endpoint.send("hello world","default","testQueue",response);
        assertTrue(response.getStatus() == HttpStatus.OK.value());
    }

    @Test
    public void testGet() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        endpoint.send("hello world","default","testQueue",response);
        assertTrue(response.getStatus() == HttpStatus.OK.value());
        Thread.sleep(1 * 1000);
        List<String> messages = endpoint.getMessages("default","testQueue");
        assertNotNull(messages);
        assertTrue(messages.size() > 0);
    }
}
