package com.fulfillment.subsystemC;

import com.fulfillment.exception.FulfillmentException;

public class PaymentGateway {
    private final ShippingCarrier carrier = new ShippingCarrier();
    private final ShippingRepository repository = new ShippingRepository();

    public ShippingEntity processPaymentAndShip(String orderId, double amount, boolean forceFail) {
        if (forceFail) {
            throw new FulfillmentException("Subsystem C Failure: Payment declined by bank!");
        }
        String tracking = carrier.generateTrackingNumber(orderId);
        ShippingEntity entity = new ShippingEntity(orderId, tracking, amount);
        entity.setStatus(ShippingEntity.Status.CONFIRMED_PAID);
        repository.save(entity);
        return entity;
    }

    public void refundPayment(String orderId) {
        ShippingEntity entity = repository.findByOrderId(orderId);
        if (entity != null) {
            entity.setStatus(ShippingEntity.Status.REFUNDED);
            repository.save(entity);
        }
    }

    public ShippingEntity getShippingInfo(String orderId) {
        return repository.findByOrderId(orderId);
    }
}