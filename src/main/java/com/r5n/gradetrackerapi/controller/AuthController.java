package com.r5n.gradetrackerapi.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.r5n.gradetrackerapi.model.User;
import com.r5n.gradetrackerapi.repository.UserRepository;
import com.r5n.gradetrackerapi.security.JwtUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${gtapi.app.google-client-id}")
    private String GOOGLE_CLIENT_ID;

    /**
     * Authorizes a Google user, and save their information.
     * @param idTokenString ID token string provided by Google Identity service.
     * @return ResponseEntity Empty response with JWT cookie in the header.
     * @throws BadCredentialsException Thrown if the token is invalid.
     */
    @PostMapping("/google-signin")
    public ResponseEntity<?> googleSignIn(@RequestBody String idTokenString) {
        User user = getUserFromIdTokenString(idTokenString);

        if (user == null)
            throw new BadCredentialsException("Invalid token.");

        userRepository.save(user);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body("");
    }

    /**
     * Returns an User object generated from the id token string.
     * If invalid token, returns null.
     *
     * @param idTokenString Token string provided by the Google Identity service.
     * @return User An user object corresponding to the token.
     */
    public User getUserFromIdTokenString(String idTokenString){
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            ).setAudience(Collections.singletonList(this.GOOGLE_CLIENT_ID)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null)
                return null;

            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            String name = (String) payload.get("name");
            String email = payload.getEmail();

            return new User(userId, name, email);

        } catch (Exception ex) {
            return null;
        }
    }
}
