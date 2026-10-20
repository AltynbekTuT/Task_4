package com.fulfillment.strategy.shipping;

import com.fulfillment.strategy.model.OrderContext;

public interface ShippingCostStrategy {
    double calculateCost(OrderContext order);
    String getStrategyName();
