package com.fulfillment.facade;

import com.fulfillment.subsystemA.InventoryEntity;
import com.fulfillment.subsystemA.InventoryManager;
import com.fulfillment.subsystemB.PackingStation;
import com.fulfillment.subsystemB.WarehouseEntity;
import com.fulfillment.subsystemC.PaymentGateway;
import com.fulfillment.subsystemC.ShippingEntity;

public class FulfillmentAdminFacade {
    private final InventoryManager inventoryManager = new InventoryManager();
    private final PackingStation packingStation = new PackingStation();
    private final PaymentGateway paymentGateway = new PaymentGateway();

    public void printFullOrderReport(String orderId) {
        System.out.println("\n--- ORDER INSPECTION REPORT: " + orderId + " ---");
        
        InventoryEntity entityA = inventoryManager.getStockInfo(orderId);
        WarehouseEntity entityB = packingStation.getPackingInfo(orderId);
        ShippingEntity entityC = paymentGateway.getShippingInfo(orderId);

        System.out.println("Subsystem A (Inventory): " + (entityA != null ? entityA.getStatus() : "NO DATA"));
        System.out.println("Subsystem B (Warehouse): " + (entityB != null ? entityB.getStatus() : "NO DATA"));
        System.out.println("Subsystem C (Shipping):  " + (entityC != null ? entityC.getStatus() : "NO DATA"));
    }
}