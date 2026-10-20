package com.fulfillment.subsystemA;

import com.fulfillment.exception.FulfillmentException;

public class OrderValidator {
    public void validateOrder(String itemId, int quantity) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new FulfillmentException("Subsystem A Error: Item ID cannot be empty!");
        }
        if (quantity <= 0) {
            throw new FulfillmentException("Subsystem A Error: Quantity must be greater than zero!");
        }
    }
}   