package com.fulfillment.strategy.shipping;

import com.fulfillment.strategy.external.ExternalRateProvider;
import com.fulfillment.strategy.model.OrderContext;

public class ExpressAirStrategy implements ShippingCostStrategy {
    private final ExternalRateProvider rateProvider;

    public ExpressAirStrategy(ExternalRateProvider rateProvider) {
        this.rateProvider = rateProvider;
    }

    @Override
    public double calculateCost(OrderContext order) {
        double subtotal = 25.0 + (order.getWeightKg() * 4.0) + (order.getDistanceKm() * 0.20);
        return subtotal * rateProvider.getFuelSurchargeMultiplier();
    }

    @Override
    public String getStrategyName() {
        return "Express Air Courier";
    }
}