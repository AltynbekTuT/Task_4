package com.fulfillment.strategy.external;

public class SimulatedCarrierRateProvider implements ExternalRateProvider {
    @Override
    public double getFuelSurchargeMultiplier() {
        return 1.15; // 15% топливная надбавка
    }

    @Override
    public double getInsuranceRatePercentage() {
        return 0.02; // 2% страховка
    }
}