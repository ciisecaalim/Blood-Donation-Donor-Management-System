package backend.user.Controller;

import backend.user.response.UserResponse;
import backend.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Soo qaad dhammaan users-ka.
     *
     * Admin oo keliya ayaa geli kara.
     *
     * GET /api/users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users =
                userService.findAllUsers();

        return ResponseEntity.ok(users);
    }

    /**
     * Soo qaad hal user iyadoo ID la adeegsanayo.
     *
     * Admin oo keliya ayaa geli kara.
     *
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {

        UserResponse user =
                userService.findUserById(id);

        return ResponseEntity.ok(user);
    }
}