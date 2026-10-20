package com.fulfillment.strategy.shipping;

import com.fulfillment.strategy.external.ExternalRateProvider;
import com.fulfillment.strategy.model.OrderContext;

public class FragileColdChainStrategy implements ShippingCostStrategy {
    private final ExternalRateProvider rateProvider;

    public FragileColdChainStrategy(ExternalRateProvider rateProvider) {
        this.rateProvider = rateProvider;
    }

    @Override
    public double calculateCost(OrderContext order) {
        double insurance = order.getDeclaredValue() * rateProvider.getInsuranceRatePercentage();
        double climateFee = order.isRequiresTemperatureControl() ? 30.0 : 0.0;
        return 40.0 + (order.getWeightKg() * 3.0) + insurance + climateFee;
    }

    @Override
    public String getStrategyName() {
        return "Fragile Cold-Chain Express";
    }
}