package com.fulfillment.subsystemC;

public class ShippingCarrier {
    public String generateTrackingNumber(String orderId) {
        return "TRACK-DHL-" + Math.abs(orderId.hashCode() % 10000);
    }
}