package com.strategy;

import com.fulfillment.strategy.external.ExternalRateProvider;
import com.fulfillment.strategy.model.OrderContext;
import com.fulfillment.strategy.packing.*;
import com.fulfillment.strategy.shipping.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyPatternTest {

    // --- Fake External Data Source for Isolation (Clean Code Ch. 10) ---
    private static class FakeExternalRateProvider implements ExternalRateProvider {
        @Override
        public double getFuelSurchargeMultiplier() { return 1.10; }
        @Override
        public double getInsuranceRatePercentage() { return 0.05; }
    }

    private ExternalRateProvider fakeRateProvider;
    private ShippingStrategySelector shippingSelector;
    private PackingStrategySelector packingSelector;

    @BeforeEach
    void setUp() {
        fakeRateProvider = new FakeExternalRateProvider();
        shippingSelector = new ShippingStrategySelector(fakeRateProvider);
        packingSelector = new PackingStrategySelector();
    }

    // --- Individual Strategy Tests: Family 1 (Shipping) ---

    @Test
    @DisplayName("StandardGroundStrategy calculates correct base cost")
    void testStandardGroundCalculation() {
        OrderContext order = new OrderContext("O1", 10.0, 100.0, 100.0, false, false);
        ShippingCostStrategy strategy = new StandardGroundStrategy();
        assertEquals(25.0, strategy.calculateCost(order), 0.001);
    }

    @Test
    @DisplayName("ExpressAirStrategy incorporates fuel surcharge multiplier")
    void testExpressAirCalculation() {
        OrderContext order = new OrderContext("O2", 5.0, 200.0, 100.0, false, false);
        ShippingCostStrategy strategy = new ExpressAirStrategy(fakeRateProvider);
        assertEquals(71.5, strategy.calculateCost(order), 0.001);
    }

    @Test
    @DisplayName("FragileColdChainStrategy incorporates insurance and climate handling")
    void testFragileColdChainCalculation() {
        OrderContext order = new OrderContext("O3", 2.0, 1000.0, 50.0, true, true);
        ShippingCostStrategy strategy = new FragileColdChainStrategy(fakeRateProvider);
        assertEquals(126.0, strategy.calculateCost(order), 0.001);
    }

    // --- Individual Strategy Tests: Family 2 (Packing) ---

    @Test
    @DisplayName("EcoMinimalPackingStrategy returns eco box specification")
    void testEcoMinimalPacking() {
        OrderContext order = new OrderContext("O-ECO", 1.0, 20.0, 10.0, false, false);
        PackingStrategy strategy = new EcoMinimalPackingStrategy();
        assertTrue(strategy.pack(order).contains("BOX-ECO"));
    }

    @Test
    @DisplayName("HeavyDutyReinforcedStrategy returns heavy container specification")
    void testHeavyDutyPacking() {
        OrderContext order = new OrderContext("O-HEAVY", 15.0, 500.0, 10.0, false, false);
        PackingStrategy strategy = new HeavyDutyReinforcedStrategy();
        assertTrue(strategy.pack(order).contains("BOX-HEAVY"));
    }

    @Test
    @DisplayName("InsulatedThermalStrategy returns thermal insulated box specification")
    void testInsulatedThermalPacking() {
        OrderContext order = new OrderContext("O-COLD", 2.0, 100.0, 10.0, true, false);
        PackingStrategy strategy = new InsulatedThermalStrategy();
        assertTrue(strategy.pack(order).contains("BOX-THERMO"));
    }

    // --- Selector Logic Tests ---

    @Test
    @DisplayName("ShippingSelector rule: Temperature/Fragile order selects FragileColdChainStrategy")
    void testShippingSelectorFragileRule() {
        OrderContext order = new OrderContext("O4", 1.0, 50.0, 10.0, false, true);
        ShippingCostStrategy selected = shippingSelector.selectAutomatically(order, false);
        assertInstanceOf(FragileColdChainStrategy.class, selected);
    }

    @Test
    @DisplayName("ShippingSelector rule: Express requested or long distance selects ExpressAirStrategy")
    void testShippingSelectorExpressRule() {
        OrderContext order = new OrderContext("O-DIST", 1.0, 50.0, 1500.0, false, false);
        ShippingCostStrategy selected = shippingSelector.selectAutomatically(order, false);
        assertInstanceOf(ExpressAirStrategy.class, selected);
    }

    @Test
    @DisplayName("PackingSelector rule: Heavy order selects HeavyDutyReinforcedStrategy")
    void testPackingSelectorHeavyRule() {
        OrderContext order = new OrderContext("O5", 12.0, 50.0, 10.0, false, false);
        PackingStrategy selected = packingSelector.selectAutomatically(order);
        assertInstanceOf(HeavyDutyReinforcedStrategy.class, selected);
    }

    // --- Parameterized Test for Invariant Checking ---

    static Stream<ShippingCostStrategy> shippingStrategyProvider() {
        ExternalRateProvider fake = new FakeExternalRateProvider();
        return Stream.of(
            new StandardGroundStrategy(),
            new ExpressAirStrategy(fake),
            new FragileColdChainStrategy(fake)
        );
    }

    @ParameterizedTest
    @MethodSource("shippingStrategyProvider")
    @DisplayName("Invariant: Shipping cost is always strictly positive for valid orders across all strategies")
    void testShippingCostIsAlwaysPositive(ShippingCostStrategy strategy) {
        OrderContext order = new OrderContext("O-TEST", 5.0, 200.0, 300.0, false, false);
        double cost = strategy.calculateCost(order);
        assertTrue(cost > 0.0, "Cost must be positive for strategy: " + strategy.getStrategyName());
    }
}