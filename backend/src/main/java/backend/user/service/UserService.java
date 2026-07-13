package backend.user.service;

import backend.common.enums.UserRole;
import backend.exception.BadRequestException;
import backend.exception.ResourceNotFoundException;
import backend.user.entity.User;
import backend.user.repository.UserRepository;
import backend.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse findUserById(Long userId) {
        User user = findUserEntityById(userId);
        return toResponse(user);
    }

    public User findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void validateUserIsDonor(Long userId) {
        User user = findUserEntityById(userId);

        if (user.getRole() != UserRole.ROLE_USER) {
            throw new BadRequestException("User role must be ROLE_USER");
        }
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}