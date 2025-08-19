package com.vibragram.backend.security;

import com.vibragram.backend.model.AppUser;
import com.vibragram.backend.repository.UserRepository;
import com.vibragram.backend.service.Result;
import com.vibragram.backend.service.ResultType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final Validator validator;

    public AppUserService(UserRepository repository,
                          PasswordEncoder encoder, Validator validator) {
        this.repository = repository;
        this.encoder = encoder;
        this.validator = validator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return appUser;
    }

    public AppUser findByUsername(String username){
        return repository.findByUsername(username);
    }

    public AppUser findByEmail(String userEmail){
        return repository.findByEmail(userEmail);
    }

    public AppUser findById(int id){
        return repository.findById(id);
    }

    public Result<AppUser> create(String username, String email, String password) {

        validate(username);
        validatePassword(password);

        String hashedPassword = encoder.encode(password);


        email = (email == null || email.isBlank()) ? "null@null.com" : email;

        LocalDateTime createdAt = LocalDateTime.now();
        boolean isAdmin = false;
        boolean enabled = true;

        AppUser appUserToCreate = new AppUser(
                0,
                username,
                email,
                hashedPassword,
                createdAt,
                isAdmin,
                enabled);


        Result<AppUser> result = new Result<>();

        Set<ConstraintViolation<AppUser>> violations = validator.validate(appUserToCreate);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AppUser> violation : violations) {
                result.addMessage(violation.getMessage(),ResultType.INVALID);
            }
            return result;
        }

        result = validateUser(appUserToCreate);

        if(!result.isSuccess()){
            return result;
        }

        result.setPayload(repository.add(appUserToCreate));

        return result;
    }

    public Result<AppUser> create(AppUser appUser) {

        Result<AppUser> result = new Result<>();

        try {
            validate(appUser.getUsername());
            validatePassword(appUser.getPassword());
        } catch (ValidationException e) {
            result.addMessage(e.getMessage(), ResultType.INVALID);
            return result;
        }

        String hashedPassword = encoder.encode(appUser.getPassword());

        LocalDateTime createdAt = LocalDateTime.now();
        boolean isAdmin = false;
        boolean enabled = true;

        AppUser appUserToCreate = new AppUser(
                0,
                appUser.getUsername(),
                appUser.getEmail(),
                hashedPassword,
                createdAt,
                isAdmin,
                enabled);


        Set<ConstraintViolation<AppUser>> violations = validator.validate(appUserToCreate);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AppUser> violation : violations) {
                result.addMessage(violation.getMessage(),ResultType.INVALID);
            }
            return result;
        }

        result = validateUser(appUserToCreate);

        if(!result.isSuccess()){
            return result;
        }

        result.setPayload(repository.add(appUserToCreate));

        return result;
    }

    public Result<AppUser> update (AppUser appUser){
        Result<AppUser> result = new Result<>();

        Set<ConstraintViolation<AppUser>> violations = validator.validate(appUser);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AppUser> violation : violations) {
                result.addMessage(violation.getMessage(),ResultType.INVALID);
            }
            return result;
        }

        AppUser originalOpt = repository.findById(appUser.getUserId());
        if (originalOpt == null) {
            result.addMessage("User not found", ResultType.NOT_FOUND);
            return result;
        }
        if (!originalOpt.getUsername().equals(appUser.getUsername())) {
            if (repository.findByUsername(appUser.getUsername()) != null ) {
                result.addMessage("Username already exists", ResultType.INVALID);
                return result;
            }
        }
        if (!originalOpt.getEmail().equals(appUser.getEmail())) {
            if (repository.findByEmail(appUser.getEmail())  != null ) {
                result.addMessage("Email already exists", ResultType.INVALID);
                return result;
            }
        }

        if (appUser.getUserId() <= 0) {
            result.addMessage("User id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(appUser)) {
            String msg = String.format("UserId: %s, not found", appUser.getUserId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int userId) {
        return repository.deleteById(userId);
    }

    private Result<AppUser> validateUser(AppUser appUser){
        Result<AppUser> result = new Result<>();

        if (repository.findById(appUser.getUserId()) != null){
            result.addMessage("User already exists" , ResultType.INVALID);
            return result;
        }
        if (repository.findByEmail(appUser.getEmail()) != null){
            result.addMessage("This email already exists" , ResultType.INVALID);
            return result;
        }
        if (repository.findByUsername(appUser.getUsername()) != null){
            result.addMessage("This user name already exists" , ResultType.INVALID);
            return result;
        }
        return result;
    }

    private void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }

        if (username.length() > 50) {
            throw new ValidationException("username must be less than 50 characters");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("password must be at least 8 characters");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("password must contain a digit, a letter, and a non-digit/non-letter");
        }
    }
}