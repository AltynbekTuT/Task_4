package com.fulfillment.main;

import com.fulfillment.facade.FulfillmentAdminFacade;
import com.fulfillment.facade.FulfillmentFacade;
import com.fulfillment.strategy.external.ExternalRateProvider;
import com.fulfillment.strategy.external.SimulatedCarrierRateProvider;
import com.fulfillment.strategy.model.OrderContext;
import com.fulfillment.strategy.packing.*;
import com.fulfillment.strategy.shipping.*;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FulfillmentFacade facade = new FulfillmentFacade();
        FulfillmentAdminFacade adminFacade = new FulfillmentAdminFacade();
        ExternalRateProvider externalRates = new SimulatedCarrierRateProvider();
        ShippingStrategySelector shippingSelector = new ShippingStrategySelector(externalRates);
        PackingStrategySelector packingSelector = new PackingStrategySelector();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==================================================");
            System.out.println("   INTEGRATED SYSTEM (ASSIGNMENT 4 & 5)           ");
            System.out.println("==================================================");
            System.out.println("--- ASSIGNMENT 4: FACADE & PERSISTENCE ---");
            System.out.println("1. Full Chain A->B->C (Happy Path)");
            System.out.println("2. Full Chain A->B->C (Fail at B)");
            System.out.println("3. Full Chain A->B->C (Fail at C - 2-step rollback)");
            System.out.println("4. Short Chain A->B (Pickup Order)");
            System.out.println("5. Admin Report from Disk");
            System.out.println("6. Clear Data Directory");
            System.out.println("--- ASSIGNMENT 5: STRATEGY PATTERN ---");
            System.out.println("7. Demo Strategy Families & Automatic Selector");
            System.out.println("8. Demo Runtime Strategy Switching");
            System.out.println("0. Exit");
            System.out.println("==================================================");
            System.out.print("Select option: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        facade.executeFulfillment("ORD-100", "LAPTOP", 1, 1200.0, false, false);
                        break;
                    case "2":
                        facade.executeFulfillment("ORD-200", "PHONE", 2, 800.0, true, false);
                        break;
                    case "3":
                        facade.executeFulfillment("ORD-300", "TABLET", 1, 500.0, false, true);
                        break;
                    case "4":
                        facade.executePickupFulfillment("ORD-PICKUP-1", "HEADPHONES", 1, false);
                        break;
                    case "5":
                        System.out.print("Enter Order ID: ");
                        adminFacade.printFullOrderReport(scanner.nextLine().trim());
                        break;
                    case "6":
                        clearData();
                        System.out.println("Data folder cleared!");
                        break;
                    case "7":
                        runStrategyDemo(shippingSelector, packingSelector);
                        break;
                    case "8":
                        runRuntimeSwitchDemo(externalRates);
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("[RESULT] Finished with error: " + e.getMessage());
            }
        }
    }

    private static void runStrategyDemo(ShippingStrategySelector shipSel, PackingStrategySelector packSel) {
        OrderContext ord1 = new OrderContext("O-ECO", 1.5, 50.0, 100.0, false, false);
        OrderContext ord2 = new OrderContext("O-HEAVY", 15.0, 1500.0, 500.0, false, true);
        OrderContext ord3 = new OrderContext("O-COLD", 3.0, 900.0, 1200.0, true, false);

        OrderContext[] list = { ord1, ord2, ord3 };
        for (OrderContext order : list) {
            ShippingCostStrategy shipStrat = shipSel.selectAutomatically(order, false);
            PackingStrategy packStrat = packSel.selectAutomatically(order);

            ShippingContext shipCtx = new ShippingContext(shipStrat);
            PackingContext packCtx = new PackingContext(packStrat);

            System.out.println("\nOrder " + order.getOrderId() + ":");
            System.out.println(" Shipping Strategy: " + shipCtx.getCurrentStrategyName() + " | Cost: $" + String.format("%.2f", shipCtx.executeCalculation(order)));
            System.out.println(" Packing Strategy : " + packCtx.getCurrentStrategyName() + " | Box: " + packCtx.executePacking(order));
        }
    }

    private static void runRuntimeSwitchDemo(ExternalRateProvider rates) {
        OrderContext order = new OrderContext("O-SWITCH", 5.0, 300.0, 200.0, false, false);
        ShippingContext context = new ShippingContext(new StandardGroundStrategy());

        System.out.println("\n[Initial Strategy] " + context.getCurrentStrategyName() + " -> $" + context.executeCalculation(order));

        context.setStrategy(new ExpressAirStrategy(rates));
        System.out.println("[Switched Strategy] " + context.getCurrentStrategyName() + " -> $" + context.executeCalculation(order));
    }

    private static void clearData() {
        File dir = new File("data");
        if (dir.exists()) {
            for (File f : dir.listFiles()) f.delete();
        }
    }
}