package com.fulfillment.strategy.shipping;

import com.fulfillment.strategy.model.OrderContext;

public class MonolithicShippingCalculator {
    public double calculate(OrderContext order, String type) {
        switch (type) {
            case "GROUND":
                return 5.0 + (order.getWeightKg() * 1.5) + (order.getDistanceKm() * 0.05);
            case "AIR":
                return (25.0 + (order.getWeightKg() * 4.0) + (order.getDistanceKm() * 0.20)) * 1.15;
            case "COLD":
                return 40.0 + (order.getWeightKg() * 3.0) + (order.getDeclaredValue() * 0.02) + (order.isRequiresTemperatureControl() ? 30.0 : 0.0);
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }
}