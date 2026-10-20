package com.fulfillment.strategy.packing;

import com.fulfillment.strategy.model.OrderContext;

public class EcoMinimalPackingStrategy implements PackingStrategy {
    @Override
    public String pack(OrderContext order) {
        return "BOX-ECO: 100% Recycled Kraft Envelope with Paper Padding";
    }

    @Override
    public String getStrategyName() {
        return "Eco-Friendly Minimalist Packing";
    }
}