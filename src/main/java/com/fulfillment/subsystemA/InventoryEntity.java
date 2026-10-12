package com.fulfillment.subsystemA;

public class InventoryEntity {
    public enum Status { NEW, RESERVED, CANCELLED }

    private String orderId;
    private String itemId;
    private int quantity;
    private Status status;

    public InventoryEntity(String orderId, String itemId, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.status = Status.NEW;
    }

    public void setStatus(Status newStatus) {
        if (this.status == Status.CANCELLED) {
            throw new IllegalStateException("Cannot change status of an already cancelled reservation!");
        }
        this.status = newStatus;
    }

    public String getOrderId() { return orderId; }
    public String getItemId() { return itemId; }
    public int getQuantity() { return quantity; }
    public Status getStatus() { return status; }

    @Override
    public String toString() {
        return orderId + ";" + itemId + ";" + quantity + ";" + status;
    }

    public static InventoryEntity parse(String line) {
        String[] parts = line.split(";");
        InventoryEntity entity = new InventoryEntity(parts[0], parts[1], Integer.parseInt(parts[2]));
        entity.status = Status.valueOf(parts[3]);
        return entity;
    }
}