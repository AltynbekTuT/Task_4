package com.fulfillment.strategy.shipping;

import com.fulfillment.strategy.external.ExternalRateProvider;
import com.fulfillment.strategy.model.OrderContext;

public class ShippingStrategySelector {
    private final ExternalRateProvider rateProvider;

    public ShippingStrategySelector(ExternalRateProvider rateProvider) {
        this.rateProvider = rateProvider;
    }

    public ShippingCostStrategy selectAutomatically(OrderContext order, boolean customerExpressRequested) {
        if (order.isRequiresTemperatureControl() || order.isFragile()) {
            return new FragileColdChainStrategy(rateProvider);
        }
        if (customerExpressRequested || order.getDistanceKm() > 1000.0) {
            return new ExpressAirStrategy(rateProvider);
        }
        return new StandardGroundStrategy();
    }
}