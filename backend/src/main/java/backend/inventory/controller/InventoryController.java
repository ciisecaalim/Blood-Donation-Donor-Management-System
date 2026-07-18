package backend.inventory.controller;

import backend.inventory.request.InventoryRequest;
import backend.inventory.request.InventoryUpdateRequest;
import backend.inventory.response.InventoryResponse;
import backend.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/inventory")
public class InventoryController {


    private final InventoryService inventoryService;


    // Constructor Injection
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }



    // GET ALL INVENTORY
    // URL: GET /api/inventory
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {

        return ResponseEntity.ok(
                inventoryService.getAllInventory()
        );
    }




    // GET INVENTORY BY ID
    // URL: GET /api/inventory/{id}
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(id)
        );
    }





    // CREATE INVENTORY
    // URL: POST /api/inventory
    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request
    ) {

        InventoryResponse response =
                inventoryService.createInventory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }





    // UPDATE INVENTORY
    // URL: PUT /api/inventory/{id}
    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryUpdateRequest request
    ) {

        InventoryResponse response =
                inventoryService.updateInventory(id, request);

        return ResponseEntity.ok(response);
    }





    // DELETE INVENTORY
    // URL: DELETE /api/inventory/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(
            @PathVariable Long id
    ) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.ok(
                "Inventory deleted successfully"
        );
    }

}