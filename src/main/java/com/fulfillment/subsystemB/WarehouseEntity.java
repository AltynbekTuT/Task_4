package com.fulfillment.subsystemB;

public class WarehouseEntity {
    public enum Status { NEW, PICKED_PACKED, ROLLED_BACK }

    private String orderId;
    private String boxId;
    private Status status;

    public WarehouseEntity(String orderId, String boxId) {
        this.orderId = orderId;
        this.boxId = boxId;
        this.status = Status.NEW;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public String getBoxId() { return boxId; }
    public Status getStatus() { return status; }

    @Override
    public String toString() {
        return orderId + ";" + boxId + ";" + status;
    }

    public static WarehouseEntity parse(String line) {
        String[] parts = line.split(";");
        WarehouseEntity entity = new WarehouseEntity(parts[0], parts[1]);
        entity.status = Status.valueOf(parts[2]);
        return entity;
    }
}