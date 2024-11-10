package be.pxl.services.messagingservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public QueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "myQueue")
    public void listen(String in) {
        System.out.println("Message read from myQueue: " + in);
    }

    public void sendMessage() {
        rabbitTemplate.convertAndSend("myQueue", "Hello, world!");
        System.out.println("Message sent to myQueue: Hello, world!");
    }
}
