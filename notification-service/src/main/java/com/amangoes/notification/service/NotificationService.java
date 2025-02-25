package com.amangoes.notification.service;

import com.amangoes.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-placed")
    public void Listen(OrderPlacedEvent orderPlacedEvent) {
        log.info("Got message from order-placed topic {}", orderPlacedEvent);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("jai@amangoes.com");
            messageHelper.setTo(orderPlacedEvent.getEmail().toString());
            messageHelper.setSubject("Your Order Number %s has been placed".formatted(orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                    Hi %s,%s
                    
                    Your order with order number %s has been placed successfully.
                    
                    Best Regards
                    Amangoes Team
                    """,
                    orderPlacedEvent.getFirstName(),orderPlacedEvent.getLastName(),orderPlacedEvent.getOrderNumber()));
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            log.error("Error while sending email", e);
        }
    }
}
