package com.fulfillment;

import com.fulfillment.exception.FulfillmentException;
import com.fulfillment.facade.FulfillmentFacade;
import com.fulfillment.subsystemA.InventoryEntity;
import com.fulfillment.subsystemA.InventoryRepository;
import com.fulfillment.subsystemB.WarehouseEntity;
import com.fulfillment.subsystemB.WarehouseRepository;
import com.fulfillment.subsystemC.ShippingEntity;
import com.fulfillment.subsystemC.ShippingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FulfillmentFacadeTest {
    private FulfillmentFacade facade;
    private InventoryRepository repoA;
    private WarehouseRepository repoB;
    private ShippingRepository repoC;

    @BeforeEach
    void setUp() {
        new File("data/inventory.txt").delete();
        new File("data/warehouse.txt").delete();
        new File("data/shipping.txt").delete();
        new File("data/chain_status.txt").delete();

        facade = new FulfillmentFacade();
        repoA = new InventoryRepository();
        repoB = new WarehouseRepository();
        repoC = new ShippingRepository();
    }

    // --- DSL Helper (Domain-Specific Testing Language) ---
    private void assertOrderStatusOnDisk(String orderId, InventoryEntity.Status expectedA, 
                                        WarehouseEntity.Status expectedB, ShippingEntity.Status expectedC) {
        InventoryEntity a = repoA.findByOrderId(orderId);
        WarehouseEntity b = repoB.findByOrderId(orderId);
        ShippingEntity c = repoC.findByOrderId(orderId);

        if (expectedA != null) assertEquals(expectedA, a.getStatus());
        if (expectedB != null) assertEquals(expectedB, b.getStatus());
        if (expectedC != null) assertEquals(expectedC, c.getStatus());
    }

    @Test
    @DisplayName("Successfully execute full chain (A -> B -> C)")
    void testSuccessfulChain() {
        facade.executeFulfillment("ORD-1", "ITEM-1", 2, 100.0, false, false);
        assertOrderStatusOnDisk("ORD-1", InventoryEntity.Status.RESERVED, 
                                        WarehouseEntity.Status.PICKED_PACKED, 
                                        ShippingEntity.Status.CONFIRMED_PAID);
    }

    @Test
    @DisplayName("Failure in Subsystem B -> Rollback Subsystem A")
    void testCompensationWhenBFails() {
        assertThrows(FulfillmentException.class, () -> 
            facade.executeFulfillment("ORD-2", "ITEM-1", 2, 100.0, true, false)
        );

        assertOrderStatusOnDisk("ORD-2", InventoryEntity.Status.CANCELLED, null, null);
    }

    @Test
    @DisplayName("Failure in Subsystem C -> Two-step rollback (B, then A)")
    void testCompensationWhenCFails() {
        assertThrows(FulfillmentException.class, () -> 
            facade.executeFulfillment("ORD-3", "ITEM-1", 2, 100.0, false, true)
        );

        assertOrderStatusOnDisk("ORD-3", InventoryEntity.Status.CANCELLED, 
                                        WarehouseEntity.Status.ROLLED_BACK, null);
    }

    @Test
    @DisplayName("Test state reload from disk after application restart")
    void testPersistenceReloadAfterRestart() {
        facade.executeFulfillment("ORD-4", "ITEM-99", 1, 50.0, false, false);

        InventoryRepository newRepoA = new InventoryRepository();
        assertNotNull(newRepoA.findByOrderId("ORD-4"));
        assertEquals(InventoryEntity.Status.RESERVED, newRepoA.findByOrderId("ORD-4").getStatus());
    }
}