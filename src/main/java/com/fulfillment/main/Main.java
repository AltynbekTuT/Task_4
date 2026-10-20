package com.fulfillment.main;

import com.fulfillment.facade.FulfillmentAdminFacade;
import com.fulfillment.facade.FulfillmentFacade;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FulfillmentFacade facade = new FulfillmentFacade();
        FulfillmentAdminFacade adminFacade = new FulfillmentAdminFacade();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("   E-COMMERCE FULFILLMENT SYSTEM MENU     ");
            System.out.println("--- Full Chain (A -> B -> C: Delivery) ---");
            System.out.println("1. Execute Full Order (Happy Path)");
            System.out.println("2. Execute Full Order (Fail at Step B)");
            System.out.println("3. Execute Full Order (Fail at Step C - 2-step rollback)");
            System.out.println("--- Short Chain (A -> B: Pickup) ---");
            System.out.println("4. Execute Pickup Order (Happy Path: A -> B)");
            System.out.println("5. Execute Pickup Order (Fail at Step B)");
            System.out.println("--- Admin & Persistence ---");
            System.out.println("6. Print Full Order Report from Disk");
            System.out.println("7. Reset / Clear All Persisted Files");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter Order ID (e.g. ORD-100): ");
                        String id1 = scanner.nextLine().trim();
                        facade.executeFulfillment(id1, "LAPTOP", 1, 1200.0, false, false);
                        break;

                    case "2":
                        System.out.print("Enter Order ID (e.g. ORD-200): ");
                        String id2 = scanner.nextLine().trim();
                        facade.executeFulfillment(id2, "PHONE", 2, 800.0, true, false);
                        break;

                    case "3":
                        System.out.print("Enter Order ID (e.g. ORD-300): ");
                        String id3 = scanner.nextLine().trim();
                        facade.executeFulfillment(id3, "TABLET", 1, 500.0, false, true);
                        break;

                    case "4":
                        System.out.print("Enter Pickup Order ID (e.g. ORD-PICKUP-1): ");
                        String id4 = scanner.nextLine().trim();
                        facade.executePickupFulfillment(id4, "HEADPHONES", 1, false);
                        break;

                    case "5":
                        System.out.print("Enter Pickup Order ID (e.g. ORD-PICKUP-2): ");
                        String id5 = scanner.nextLine().trim();
                        facade.executePickupFulfillment(id5, "KEYBOARD", 1, true);
                        break;

                    case "6":
                        System.out.print("Enter Order ID to inspect: ");
                        String inspectId = scanner.nextLine().trim();
                        adminFacade.printFullOrderReport(inspectId);
                        break;

                    case "7":
                        resetDataDirectory();
                        System.out.println("All data files in /data directory cleared successfully!");
                        break;

                    case "0":
                        System.out.println("Exiting application. Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option. Please enter a number between 0 and 7.");
                }
            } catch (Exception e) {
                System.out.println("\n[SYSTEM RESULT] Execution finished with exception: " + e.getMessage());
            }
        }
    }

    private static void resetDataDirectory() {
        File dataDir = new File("data");
        if (dataDir.exists() && dataDir.isDirectory()) {
            File[] files = dataDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }
}