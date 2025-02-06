package com.amangoes.inventory.service;

import com.amangoes.inventory.model.Inventory;
import com.amangoes.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        // we could also define a method named existsBySkuCodeAndQuantityGreaterThanEqual in the repository
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
        return inventory != null && inventory.getQuantity() >= quantity;
    }

    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    public Inventory findById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        inventoryRepository.deleteById(id);
    }
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
    public Inventory findBySkuCode(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode);
    }
    public Inventory updateQuantity(String skuCode, Integer quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
        inventory.setQuantity(inventory.getQuantity() + quantity);
        return inventoryRepository.save(inventory);
    }
}
