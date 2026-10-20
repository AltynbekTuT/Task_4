package com.fulfillment.strategy.packing;

import com.fulfillment.strategy.model.OrderContext;

public class HeavyDutyReinforcedStrategy implements PackingStrategy {
    @Override
    public String pack(OrderContext order) {
        return "BOX-HEAVY: Double-Corrugated Wooden Container with Foam Fillers";
    }

    @Override
    public String getStrategyName() {
        return "Heavy-Duty Reinforced Packaging";
    }
}