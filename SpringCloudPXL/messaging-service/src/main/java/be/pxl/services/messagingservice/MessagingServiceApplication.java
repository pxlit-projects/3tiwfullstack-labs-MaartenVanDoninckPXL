package be.pxl.services.messagingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessagingServiceApplication implements CommandLineRunner {

    @Autowired
    private QueueService queueService;

    public static void main(String[] args) {
        SpringApplication.run(MessagingServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        queueService.sendMessage();
    }
}
