package com.fulfillment.subsystemB;

public class WarehousePicker {
    public String assignBox(String orderId) {
        return "BOX-" + Math.abs(orderId.hashCode() % 1000);
    }
}