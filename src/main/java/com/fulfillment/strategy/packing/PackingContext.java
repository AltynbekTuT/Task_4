package com.fulfillment.strategy.packing;

import com.fulfillment.strategy.model.OrderContext;

public class PackingContext {
    private PackingStrategy strategy;

    public PackingContext(PackingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PackingStrategy strategy) {
        this.strategy = strategy;
    }

    public String executePacking(OrderContext order) {
        if (strategy == null) {
            throw new IllegalStateException("Packing strategy is not set.");
        }
        return strategy.pack(order);
    }

    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }
}