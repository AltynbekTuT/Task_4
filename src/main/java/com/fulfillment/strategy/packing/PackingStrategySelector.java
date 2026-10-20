package com.fulfillment.strategy.packing;

import com.fulfillment.strategy.model.OrderContext;

public class PackingStrategySelector {
    public PackingStrategy selectAutomatically(OrderContext order) {
        if (order.isRequiresTemperatureControl()) {
            return new InsulatedThermalStrategy();
        }
        if (order.getWeightKg() >= 10.0 || order.isFragile()) {
            return new HeavyDutyReinforcedStrategy();
        }
        return new EcoMinimalPackingStrategy();
    }
}