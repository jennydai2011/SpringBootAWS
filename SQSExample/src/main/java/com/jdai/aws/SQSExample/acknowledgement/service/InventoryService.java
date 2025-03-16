package com.jdai.aws.SQSExample.acknowledgement.service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.jdai.aws.SQSExample.acknowledgement.configuration.ProductIdProperties;
import com.jdai.aws.SQSExample.acknowledgement.exception.OutOfStockException;
import com.jdai.aws.SQSExample.acknowledgement.exception.ProductNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
@Service
public class InventoryService implements InitializingBean {

    private final ProductIdProperties productIdProperties;

    // Using a Map to simulate storage
    private Map<UUID, Integer> inventory;

    public InventoryService(ProductIdProperties productIdProperties) {
        this.productIdProperties = productIdProperties;
    }

    @Override
    public void afterPropertiesSet() {
        this.inventory = new ConcurrentHashMap<>(Map.of(productIdProperties.getSmartphone(), 10,
                productIdProperties.getWirelessHeadphones(), 15,
                productIdProperties.getLaptop(), 5));
    }

    public void checkInventory(UUID productId, int quantity) {
        Integer stock = inventory.get(productId);
        if (stock == null) {
            throw new ProductNotFoundException("Product with id %s not found in Inventory".formatted(productId));
        }
        if (stock < quantity) {
            // Simulate Stock Replenishment for Retries
            inventory.put(productId, stock + (int) (Math.random() * 5));
            throw new OutOfStockException(
                    "Product with id %s is out of stock. Quantity requested: %s ".formatted(productId, quantity));
        }
        // Decrease inventory
        inventory.put(productId, stock - quantity);
    }

    public void slowCheckInventory(UUID productId, int quantity) {
        simulateBusyConnection();
        checkInventory(productId, quantity);
    }

    private void simulateBusyConnection() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}