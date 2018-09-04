package integration;

import org.apache.qpid.server.SystemLauncher;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EmbeddedBroker
{
    private static final String INITIAL_CONFIGURATION = "test-config.json";

    private SystemLauncher systemLauncher;



    public void start() throws Exception {

        systemLauncher = new SystemLauncher();
//        try {
            systemLauncher.startup(createSystemConfig());
            // performMessagingOperations();
//        } finally {
//            systemLauncher.shutdown();
//        }
    }

    public void stop() throws Exception {
        systemLauncher.shutdown();
    }

    private Map<String, Object> createSystemConfig() {
        Map<String, Object> attributes = new HashMap<>();
        URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(INITIAL_CONFIGURATION);
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfig.toExternalForm());
        attributes.put("startupLoggedToSystemOut", true);
        attributes.put("port",20179);
        return attributes;
    }
}
