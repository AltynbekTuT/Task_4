package com.fulfillment.subsystemB;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WarehouseRepository {
    private final String filePath = "data/warehouse.txt";

    public WarehouseRepository() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(WarehouseEntity entity) {
        Map<String, WarehouseEntity> all = findAll();
        all.put(entity.getOrderId(), entity);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (WarehouseEntity e : all.values()) {
                writer.println(e.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving to Subsystem B storage: " + e.getMessage());
        }
    }

    public WarehouseEntity findByOrderId(String orderId) {
        return findAll().get(orderId);
    }

    public Map<String, WarehouseEntity> findAll() {
        Map<String, WarehouseEntity> map = new HashMap<>();
        File file = new File(filePath);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    WarehouseEntity entity = WarehouseEntity.parse(line);
                    map.put(entity.getOrderId(), entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}