package com.amangoes.order.service;

import com.amangoes.order.client.InventoryClient;
import com.amangoes.order.dto.OrderRequest;
import com.amangoes.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import com.amangoes.order.model.Order;
import com.amangoes.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate; // it uses key (name of the topic to send the event to),  value (is the event to be sent)

    public void placeOrder(OrderRequest orderRequest){

        var inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if(inStock){
            // map OrderRequest to Order object
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());

            // save Order object to OrderRepository
            orderRepository.save(order);

            //send message to Kafka Topic
            // requires orderNumber and email
            OrderPlacedEvent orderPlacedEvent=new OrderPlacedEvent(order.getOrderNumber(),orderRequest.userDetails().email());
            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed",orderPlacedEvent);
            kafkaTemplate.send("order-placed",orderPlacedEvent);
            log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed",orderPlacedEvent);
        }else{
            throw new RuntimeException("Product with skuCode :"+ orderRequest.skuCode() +" out of stock");
        }
    }
}
