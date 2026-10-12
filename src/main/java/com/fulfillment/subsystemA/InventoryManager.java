package com.fulfillment.subsystemA;

public class InventoryManager {
    private final OrderValidator validator = new OrderValidator();
    private final InventoryRepository repository = new InventoryRepository();

    public InventoryEntity reserveStock(String orderId, String itemId, int quantity) {
        validator.validateOrder(itemId, quantity);
        InventoryEntity entity = new InventoryEntity(orderId, itemId, quantity);
        entity.setStatus(InventoryEntity.Status.RESERVED);
        repository.save(entity);
        return entity;
    }

    public void cancelReservation(String orderId) {
        InventoryEntity entity = repository.findByOrderId(orderId);
        if (entity != null) {
            entity.setStatus(InventoryEntity.Status.CANCELLED);
            repository.save(entity);
        }
    }

    public InventoryEntity getStockInfo(String orderId) {
        return repository.findByOrderId(orderId);
    }
}