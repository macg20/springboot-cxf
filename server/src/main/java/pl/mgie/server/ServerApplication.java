package pl.mgie.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import pl.mgie.server.endpoint.SimpleMessageEndpoint;

@EnableWs
@Configuration
@SpringBootApplication
public class ServerApplication {

    private SimpleMessageEndpoint endpoint;

    @Autowired
    public ServerApplication(SimpleMessageEndpoint endpoint) {
        this.endpoint = endpoint;
    }


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
