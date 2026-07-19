package backend.user.service;

import backend.common.enums.UserRole;
import backend.exception.BadRequestException;
import backend.exception.ResourceNotFoundException;
import backend.user.Model.User;
import backend.user.repository.UserRepository;
import backend.user.response.UserResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * Soo qaad dhammaan users-ka.
     */
    public List<UserResponse> findAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Soo qaad hal user iyadoo ID la adeegsanayo.
     */
    public UserResponse findUserById(Long userId) {

        User user = findUserEntityById(userId);

        return toResponse(user);
    }

    /**
     * Soo qaad User entity iyadoo ID la adeegsanayo.
     *
     * Method-kan modules-ka kale ayaa isticmaali kara.
     */
    public User findUserEntityById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );
    }

    /**
     * Soo qaad User entity iyadoo email la adeegsanayo.
     */
    public User findUserEntityByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }

    /**
     * Hubi in user-ku yahay donor/user caadi ah.
     */
    public void validateUserIsDonor(Long userId) {

        User user = findUserEntityById(userId);

        if (user.getRole() != UserRole.ROLE_USER) {
            throw new BadRequestException(
                    "User role must be ROLE_USER"
            );
        }
    }

    /**
     * User Entity u beddel UserResponse.
     *
     * Password-ka response-ka laguma darayo.
     */
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