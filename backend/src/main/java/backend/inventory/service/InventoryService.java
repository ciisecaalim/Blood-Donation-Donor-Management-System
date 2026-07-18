package backend.inventory.service;

import backend.category.entity.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.common.enums.InventoryStatus;
import backend.inventory.Model.Inventory;
import backend.inventory.repository.InventoryRepository;
import backend.inventory.request.InventoryRequest;
import backend.inventory.request.InventoryUpdateRequest;
import backend.inventory.response.InventoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final CategoryRepository categoryRepository;


    // Constructor Injection
    public InventoryService(
            InventoryRepository inventoryRepository,
            CategoryRepository categoryRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.categoryRepository = categoryRepository;
    }


    // Get all inventory
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // Get inventory by ID
    public InventoryResponse getInventoryById(Long id) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found")
                );

        return mapToResponse(inventory);
    }


    // Create inventory
    public InventoryResponse createInventory(InventoryRequest request) {

        BloodCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Blood category not found")
                );


        if (inventoryRepository.existsByCategory(category)) {
            throw new RuntimeException(
                    "Inventory already exists for this blood category"
            );
        }


        Inventory inventory = Inventory.builder()
                .category(category)
                .quantity(request.getQuantity())
                .notes(request.getNotes())
                .status(calculateStatus(request.getQuantity()))
                .build();


        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToResponse(savedInventory);
    }



    // Update inventory
    public InventoryResponse updateInventory(
            Long id,
            InventoryUpdateRequest request
    ) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found")
                );


        inventory.setQuantity(request.getQuantity());
        inventory.setNotes(request.getNotes());

        // Status is automatically updated
        inventory.setStatus(
                calculateStatus(request.getQuantity())
        );


        Inventory updatedInventory = inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }



    // Delete inventory
    public void deleteInventory(Long id) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found")
                );

        inventoryRepository.delete(inventory);
    }



    // Calculate inventory status
    private InventoryStatus calculateStatus(Integer quantity) {

        if (quantity == null || quantity == 0) {
            return InventoryStatus.OUT_OF_STOCK;
        }

        if (quantity < 10) {
            return InventoryStatus.LOW_STOCK;
        }

        return InventoryStatus.AVAILABLE;
    }



    // Convert Entity to Response DTO
    private InventoryResponse mapToResponse(Inventory inventory) {

        return new InventoryResponse(
                inventory.getId(),
                inventory.getCategory().getBloodGroup(),
                inventory.getQuantity(),
                inventory.getStatus(),
                inventory.getNotes()
        );
    }
}