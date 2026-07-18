package backend.category.Controller;

import backend.category.request.CategoryRequest;
import backend.category.response.CategoryResponse;
import backend.category.service.CategoryService;
import backend.common.enums.CategoryStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /*
     * ADMIN iyo STAFF:
     * Soo qaad dhammaan categories-ka.
     *
     * GET /api/categories
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<CategoryResponse>>
    getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    /*
     * User kasta oo login sameeyay:
     * Soo qaad active categories-ka.
     *
     * GET /api/categories/active
     */
    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoryResponse>>
    getActiveCategories() {

        return ResponseEntity.ok(
                categoryService.getActiveCategories()
        );
    }

    /*
     * ADMIN iyo STAFF:
     * Soo qaad category gaar ah.
     *
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CategoryResponse>
    getCategoryById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }

    /*
     * ADMIN:
     * Samee category cusub.
     *
     * POST /api/categories
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse>
    createCategory(
            @Valid
            @RequestBody CategoryRequest request
    ) {

        CategoryResponse response =
                categoryService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /*
     * ADMIN:
     * Wax ka beddel category.
     *
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse>
    updateCategory(
            @PathVariable Long id,
            @Valid
            @RequestBody CategoryRequest request
    ) {

        return ResponseEntity.ok(
                categoryService.updateCategory(
                        id,
                        request
                )
        );
    }

    /*
     * ADMIN:
     * Beddel status-ka category-ga.
     *
     * PATCH /api/categories/{id}/status
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse>
    updateCategoryStatus(
            @PathVariable Long id,
            @RequestBody CategoryStatus status
    ) {

        return ResponseEntity.ok(
                categoryService.updateCategoryStatus(
                        id,
                        status
                )
        );
    }
}