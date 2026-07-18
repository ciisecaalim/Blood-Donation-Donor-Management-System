package backend.auth.response;

import backend.common.enums.UserRole;
import backend.common.enums.UserStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private String tokenType;

    private Long userId;

    private String fullName;

    private String email;

    private UserRole role;

    private UserStatus status;
}