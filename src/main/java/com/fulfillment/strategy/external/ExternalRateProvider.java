package com.fulfillment.strategy.external;

public interface ExternalRateProvider {
    double getFuelSurchargeMultiplier();
    double getInsuranceRatePercentage();
}