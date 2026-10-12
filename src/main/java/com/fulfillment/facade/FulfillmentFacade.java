package com.fulfillment.facade;

import com.fulfillment.exception.FulfillmentException;
import com.fulfillment.subsystemA.InventoryManager;
import com.fulfillment.subsystemB.PackingStation;
import com.fulfillment.subsystemC.PaymentGateway;

import java.io.*;
import java.nio.file.*;

public class FulfillmentFacade {
    private final InventoryManager inventoryManager = new InventoryManager();
    private final PackingStation packingStation = new PackingStation();
    private final PaymentGateway paymentGateway = new PaymentGateway();
    private final String chainLogPath = "data/chain_status.txt";

    public FulfillmentFacade() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean executeFulfillment(String orderId, String itemId, int quantity, double amount, 
                                      boolean forceFailB, boolean forceFailC) {
        
        System.out.println("\n=== EXECUTING CHAIN FOR ORDER: " + orderId + " ===");

        // Step 1: Subsystem A (Inventory Reservation)
        try {
            inventoryManager.reserveStock(orderId, itemId, quantity);
            saveChainStatus(orderId, "STEP_A_COMMITTED");
            System.out.println("-> Step A Passed: Inventory reserved.");
        } catch (Exception e) {
            saveChainStatus(orderId, "FAILED_AT_A");
            System.out.println("❌ Step A Failure: " + e.getMessage());
            throw e;
        }

        // Step 2: Subsystem B (Warehouse Packing)
        try {
            packingStation.packOrder(orderId, forceFailB);
            saveChainStatus(orderId, "STEP_B_COMMITTED");
            System.out.println("-> Step B Passed: Order packed.");
        } catch (Exception e) {
            System.out.println("❌ Step B Failure: " + e.getMessage());
            System.out.println("⚠️  COMPENSATION: Rolling back Subsystem A...");
            
            // 1-Step Rollback: Cancel A
            inventoryManager.cancelReservation(orderId);
            saveChainStatus(orderId, "COMPENSATED_A_ONLY");
            throw new FulfillmentException("Rollback executed (Inventory reservation in A cancelled). Cause: " + e.getMessage());
        }

        // Step 3: Subsystem C (Payment & Shipping)
        try {
            paymentGateway.processPaymentAndShip(orderId, amount, forceFailC);
            saveChainStatus(orderId, "COMPLETED_SUCCESSFULLY");
            System.out.println("-> Step C Passed: Payment and shipping confirmed.");
            System.out.println("✅ CHAIN COMPLETED SUCCESSFULLY!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Step C Failure: " + e.getMessage());
            System.out.println("⚠️  TWO-STEP COMPENSATION: Rolling back B, then A...");

            // 2-Step Rollback: Cancel B, then Cancel A
            packingStation.rollbackPacking(orderId);
            inventoryManager.cancelReservation(orderId);
            
            saveChainStatus(orderId, "COMPENSATED_B_THEN_A");
            throw new FulfillmentException("Two-step rollback executed (B and A compensated). Cause: " + e.getMessage());
        }
    }

    private void saveChainStatus(String orderId, String status) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(chainLogPath, true))) {
            writer.println(orderId + "=" + status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}