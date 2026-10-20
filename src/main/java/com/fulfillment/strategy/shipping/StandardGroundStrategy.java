package com.fulfillment.strategy.shipping;

import com.fulfillment.strategy.model.OrderContext;

public class StandardGroundStrategy implements ShippingCostStrategy {
    @Override
    public double calculateCost(OrderContext order) {
        double baseRate = 5.0;
        double weightFee = order.getWeightKg() * 1.5;
        double distanceFee = order.getDistanceKm() * 0.05;
        return baseRate + weightFee + distanceFee;
    }

    @Override
    public String getStrategyName() {
        return "Standard Ground Shipping";
    }
}