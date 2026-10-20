package com.fulfillment.strategy.packing;

import com.fulfillment.strategy.model.OrderContext;

public interface PackingStrategy {
    String pack(OrderContext order);
    String getStrategyName();
}