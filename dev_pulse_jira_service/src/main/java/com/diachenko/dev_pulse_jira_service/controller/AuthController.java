package com.diachenko.dev_pulse_jira_service.controller;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.config.security.JwtService;
import com.diachenko.dev_pulse_jira_service.dto.AppTokenResponse;
import com.diachenko.dev_pulse_jira_service.dto.AuthRequest;
import com.diachenko.dev_pulse_jira_service.model.AppUser;
import com.diachenko.dev_pulse_jira_service.service.AppUserService;
import com.diachenko.dev_pulse_jira_service.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AppUserService appUserService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(user, JwtService.TokenType.ACCESS);
            String refresh = jwtService.generateToken(user, JwtService.TokenType.REFRESH);

            return ResponseEntity.ok(new AppTokenResponse(token, refresh));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("User Not Found");
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken, JwtService.TokenType.REFRESH);
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(refreshToken, user, JwtService.TokenType.REFRESH)) {
                String newAccess = jwtService.generateToken(user, JwtService.TokenType.ACCESS);
                String newRefresh = jwtService.generateToken(user, JwtService.TokenType.REFRESH);

                return ResponseEntity.ok(new AppTokenResponse(newAccess, newRefresh));
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token invalid");
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@RequestBody AppUser user) {
        AppUser savedUser = appUserService.saveNewUser(user);
        return ResponseEntity.ok(savedUser);
    }

}
