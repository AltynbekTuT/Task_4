# Assignment 4: Facade Pattern over E-Commerce Fulfillment

## 1. Principle of Least Knowledge (Law of Demeter)

### Before (Direct Subsystem Invocation):
The client would be required to manage interactions across three independent subsystems directly, keeping track of their states and error handling:

```java
InventoryManager inventory = new InventoryManager();
PackingStation packing = new PackingStation();
PaymentGateway payment = new PaymentGateway();

inventory.reserveStock("ORD-1", "ITEM-1", 2);
try {
    packing.packOrder("ORD-1", false);
    try {
        payment.processPaymentAndShip("ORD-1", 100.0, false);
    } catch (Exception e) {
        packing.rollbackPacking("ORD-1");
        inventory.cancelReservation("ORD-1");
    }
} catch (Exception e) {
    inventory.cancelReservation("ORD-1");
}