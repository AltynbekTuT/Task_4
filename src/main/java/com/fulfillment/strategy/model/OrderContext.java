package com.fulfillment.strategy.model;

public class OrderContext {
    private final String orderId;
    private final double weightKg;
    private final double declaredValue;
    private final double distanceKm;
    private final boolean requiresTemperatureControl;
    private final boolean isFragile;

    public OrderContext(String orderId, double weightKg, double declaredValue, 
                        double distanceKm, boolean requiresTemperatureControl, boolean isFragile) {
        this.orderId = orderId;
        this.weightKg = weightKg;
        this.declaredValue = declaredValue;
        this.distanceKm = distanceKm;
        this.requiresTemperatureControl = requiresTemperatureControl;
        this.isFragile = isFragile;
    }

    public String getOrderId() { return orderId; }
    public double getWeightKg() { return weightKg; }
    public double getDeclaredValue() { return declaredValue; }
    public double getDistanceKm() { return distanceKm; }
    public boolean isRequiresTemperatureControl() { return requiresTemperatureControl; }
    public boolean isFragile() { return isFragile; }
}