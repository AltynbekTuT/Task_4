package com.fulfillment.subsystemC;

public class ShippingEntity {
    public enum Status { INITIATED, CONFIRMED_PAID, REFUNDED }

    private String orderId;
    private String trackingNumber;
    private double amount;
    private Status status;

    public ShippingEntity(String orderId, String trackingNumber, double amount) {
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
        this.amount = amount;
        this.status = Status.INITIATED;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public String getTrackingNumber() { return trackingNumber; }
    public double getAmount() { return amount; }
    public Status getStatus() { return status; }

    @Override
    public String toString() {
        return orderId + ";" + trackingNumber + ";" + amount + ";" + status;
    }

    public static ShippingEntity parse(String line) {
        String[] parts = line.split(";");
        ShippingEntity entity = new ShippingEntity(parts[0], parts[1], Double.parseDouble(parts[2]));
        entity.status = Status.valueOf(parts[3]);
        return entity;
    }
}