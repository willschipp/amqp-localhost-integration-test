package integration;

import com.example.code.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes= Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ApplicationIT {

    @Autowired
    ApplicationContext context;

    @Test
    public void testStartup() throws Exception {
        assertTrue(context.getBeanDefinitionCount() > 0);
    }
}
