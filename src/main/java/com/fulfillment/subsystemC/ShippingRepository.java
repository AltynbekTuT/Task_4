package com.fulfillment.subsystemC;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ShippingRepository {
    private final String filePath = "data/shipping.txt";

    public ShippingRepository() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(ShippingEntity entity) {
        Map<String, ShippingEntity> all = findAll();
        all.put(entity.getOrderId(), entity);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (ShippingEntity e : all.values()) {
                writer.println(e.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving to Subsystem C storage: " + e.getMessage());
        }
    }

    public ShippingEntity findByOrderId(String orderId) {
        return findAll().get(orderId);
    }

    public Map<String, ShippingEntity> findAll() {
        Map<String, ShippingEntity> map = new HashMap<>();
        File file = new File(filePath);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    ShippingEntity entity = ShippingEntity.parse(line);
                    map.put(entity.getOrderId(), entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}