package backend.security;

import backend.common.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    /**
     * Register ama login kadib JWT token ayuu sameeyaa.
     */
    public String generateToken(
            String email,
            UserRole role
    ) {
        Date issuedAt = new Date();

        Date expirationDate = new Date(
                issuedAt.getTime() + jwtExpirationMs
        );

        return Jwts.builder()
                .subject(email)
                .claim("role", role.name())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Token-ka wuxuu kasoo saarayaa email-ka user-ka.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Wuxuu hubinayaa token-ka iyo user-ka.
     */
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        String email = extractUsername(token);

        return email.equalsIgnoreCase(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Wuxuu hubinayaa in token-ku dhacay iyo in kale.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Token-ka wuxuu kasoo saarayaa expiration date-ka.
     */
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Wuxuu akhrinayaa dhammaan xogta token-ka.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Secret key-ga JWT.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}