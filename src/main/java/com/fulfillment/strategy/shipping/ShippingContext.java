package com.fulfillment.strategy.shipping;

import com.fulfillment.strategy.model.OrderContext;

public class ShippingContext {
    private ShippingCostStrategy strategy;

    public ShippingContext(ShippingCostStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ShippingCostStrategy strategy) {
        this.strategy = strategy;
    }

    public double executeCalculation(OrderContext order) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy is not set.");
        }
        return strategy.calculateCost(order);
    }

    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }
}