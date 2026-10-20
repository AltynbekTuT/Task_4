package com.fulfillment.strategy.packing;

import com.fulfillment.strategy.model.OrderContext;

public class InsulatedThermalStrategy implements PackingStrategy {
    @Override
    public String pack(OrderContext order) {
        return "BOX-THERMO: Styrofoam Insulated Box with Dry Ice Gel Packs";
    }

    @Override
    public String getStrategyName() {
        return "Insulated Cold-Chain Packaging";
    }
}