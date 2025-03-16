package com.jdai.aws.SQSExample.acknowledgement.service;

import com.jdai.aws.SQSExample.acknowledgement.model.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    Map<UUID, OrderStatus> ORDER_STATUS_STORAGE = new ConcurrentHashMap<>();

    public void updateOrderStatus(UUID orderId, OrderStatus status) {
        ORDER_STATUS_STORAGE.put(orderId, status);
    }

    public OrderStatus getOrderStatus(UUID orderId) {
        return ORDER_STATUS_STORAGE.getOrDefault(orderId, OrderStatus.UNKNOWN);
    }

}