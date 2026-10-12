package com.fulfillment.subsystemB;

import com.fulfillment.exception.FulfillmentException;

public class PackingStation {
    private final WarehousePicker picker = new WarehousePicker();
    private final WarehouseRepository repository = new WarehouseRepository();

    public WarehouseEntity packOrder(String orderId, boolean forceFail) {
        if (forceFail) {
            throw new FulfillmentException("Subsystem B Failure: Conveyor belt jammed!");
        }
        String boxId = picker.assignBox(orderId);
        WarehouseEntity entity = new WarehouseEntity(orderId, boxId);
        entity.setStatus(WarehouseEntity.Status.PICKED_PACKED);
        repository.save(entity);
        return entity;
    }

    public void rollbackPacking(String orderId) {
        WarehouseEntity entity = repository.findByOrderId(orderId);
        if (entity != null) {
            entity.setStatus(WarehouseEntity.Status.ROLLED_BACK);
            repository.save(entity);
        }
    }

    public WarehouseEntity getPackingInfo(String orderId) {
        return repository.findByOrderId(orderId);
    }
}