package com.pollapp.pollapp.authentication;

import com.pollapp.pollapp.exception.AppException;
import com.pollapp.pollapp.payload.response.ApiResponse;
import com.pollapp.pollapp.payload.response.JwtAuthenticationResponse;
import com.pollapp.pollapp.payload.request.LoginRequest;
import com.pollapp.pollapp.payload.request.SignUpRequest;
import com.pollapp.pollapp.role.Role;
import com.pollapp.pollapp.role.RoleName;
import com.pollapp.pollapp.role.RoleRepo;
import com.pollapp.pollapp.security.JwtTokenProvider;
import com.pollapp.pollapp.user.User;
import com.pollapp.pollapp.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("clgt Nghiem", HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken"), HttpStatus.BAD_REQUEST);
        }
        User newUser = new User(signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword());
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);

        Role role = roleRepo.findByRoleName(RoleName.ROLE_USER);
        if (role == null) {
            throw new AppException("User role is not set");
        }
        newUser.setRoles(Collections.singleton(role));
        User result = userRepo.save(newUser);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("users/{username}").buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "User created sucessfully"));
    }
}
