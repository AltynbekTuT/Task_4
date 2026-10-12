package com.fulfillment.subsystemA;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InventoryRepository {
    private final String filePath = "data/inventory.txt";

    public InventoryRepository() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(InventoryEntity entity) {
        Map<String, InventoryEntity> all = findAll();
        all.put(entity.getOrderId(), entity);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (InventoryEntity e : all.values()) {
                writer.println(e.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving to Subsystem A storage: " + e.getMessage());
        }
    }

    public InventoryEntity findByOrderId(String orderId) {
        return findAll().get(orderId);
    }

    public Map<String, InventoryEntity> findAll() {
        Map<String, InventoryEntity> map = new HashMap<>();
        File file = new File(filePath);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    InventoryEntity entity = InventoryEntity.parse(line);
                    map.put(entity.getOrderId(), entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}