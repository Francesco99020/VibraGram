package com.vibragram.backend.controller;

import com.vibragram.backend.model.AppUser;
import com.vibragram.backend.security.AppUserService;
import com.vibragram.backend.security.JwtConverter;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtConverter converter;
    @Autowired
    private final AppUserService appUserService;

    public AuthController(AuthenticationManager authenticationManager, JwtConverter converter, AppUserService appUserService) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.appUserService = appUserService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> credentials){
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));

        try{
            Authentication authentication = authenticationManager.authenticate(authToken);

            if(authentication.isAuthenticated()){
                String jwtToken = converter.getTokenFromUser((User) authentication.getPrincipal());

                HashMap<String, String> map = new HashMap<>();
                map.put("jwt_token", jwtToken);

                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (AuthenticationException ex){
            System.out.println(ex);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials){
        AppUser appUser = null;

        try{
            String username = credentials.get("username");
            String email = credentials.get("email");
            String password = credentials.get("password");

            appUser = appUserService.create(username, email, password).getPayload();

        } catch (ValidationException ex){
            return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("The provided username already exists"), HttpStatus.BAD_REQUEST);
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("appUserId", appUser.getUserId());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
