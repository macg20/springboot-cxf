package pl.mgie.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import pl.mgie.services.MessageServiceClient;


@SpringBootApplication
@ComponentScan(basePackages = "pl.mgie")
public class ClientApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    private MessageServiceClient serviceClient;

    @Autowired
    public ClientApplication(MessageServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String message = serviceClient.callGetMessage("John123", "Smith");
        logger.info(message);
    }
}
