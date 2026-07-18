package backend.auth.service;

import backend.auth.request.LoginRequest;
import backend.auth.request.RegisterRequest;
import backend.auth.response.AuthResponse;
import backend.common.enums.UserRole;
import backend.common.enums.UserStatus;
import backend.exception.DuplicateResourceException;
import backend.security.JwtService;
import backend.user.Model.User;
import backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Diiwaangelinta user cusub.
     *
     * Public registration-ku wuxuu sameynayaa ROLE_USER oo keliya.
     * ROLE_ADMIN iyo ROLE_STAFF waxaa abuuri kara admin-ka.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String fullName = normalizeFullName(request.getFullName());
        String email = normalizeEmail(request.getEmail());

        /*
         * Hubi in email-kan aanu hore ugu jirin database-ka.
         */
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(

                    "An account with this email already exists"
            );
        }

        /*
         * Samee user cusub.
         *
         * Password-ka waxaa lagu kaydinayaa isagoo encrypted ah.
         * Public user-ku wuxuu automatic u noqonayaa ROLE_USER.
         * Status-kuna wuxuu automatic u noqonayaa ACTIVE.
         */
        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        /*
         * Samee JWT token kadib markii user-ka la kaydiyo.
         */
        String token = jwtService.generateToken(
                savedUser.getEmail(),
                savedUser.getRole()
        );

        return buildAuthResponse(savedUser, token);
    }

    /**
     * Login-ka user jira.
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        String email = normalizeEmail(request.getEmail());

        /*
         * User-ka email-kiisa ka raadi database-ka.
         *
         * Sababo amni awgood, lama sheegayo in email-ka
         * ama password-ku gaar ahaan khaldan yahay.
         */
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Email or password is incorrect"
                        )
                );

        /*
         * Hubi in account-ku ACTIVE yahay.
         */
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your account is not active"
            );
        }

        /*
         * Isbarbar dhig password-ka user-ku soo diray
         * iyo password-ka encrypted-ka ah ee database-ka ku jira.
         */
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new DuplicateResourceException(
                    "Email or password is incorrect"
            );
        }

        /*
         * Samee JWT token haddii email iyo password sax yihiin.
         */
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return buildAuthResponse(user, token);
    }

    /**
     * User entity-ga u beddel AuthResponse ammaan ah.
     *
     * Password-ka response-ka laguma darin.
     */
    private AuthResponse buildAuthResponse(
            User user,
            String token
    ) {
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    /**
     * Email-ka:
     * - spaces-ka hore iyo dambe ka saar
     * - lowercase ka dhig
     */
    private String normalizeEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email is required"
            );
        }

        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    /**
     * Full name-ka:
     * - spaces-ka hore iyo dambe ka saar
     * - spaces badan oo isku xiga hal space ka dhig
     */
    private String normalizeFullName(String fullName) {

        if (fullName == null || fullName.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Full name is required"
            );
        }

        return fullName
                .trim()
                .replaceAll("\\s+", " ");
    }
}