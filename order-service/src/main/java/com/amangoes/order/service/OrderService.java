package com.amangoes.order.service;

import com.amangoes.order.client.InventoryClient;
import com.amangoes.order.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import com.amangoes.order.model.Order;
import com.amangoes.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

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
        }else{
            throw new RuntimeException("Product with skuCode :"+ orderRequest.skuCode() +" not of stock");
        }
    }
}
