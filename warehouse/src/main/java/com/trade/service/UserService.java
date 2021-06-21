package com.trade.service;

import com.trade.domain.User;
import com.trade.repository.UserRepository;
import com.trade.request.AuthenticationRequest;
import com.trade.request.RegistrationRequest;
import com.trade.service.exception.AuthenticationException;
import com.trade.service.exception.UserAlreadyExistsException;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private User user;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(@NonNull RegistrationRequest request) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setId(null);
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);

        this.user = userRepository.save(user);
    }

    @Transactional
    public void authenticate(@NonNull AuthenticationRequest request) throws AuthenticationException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException());

        boolean matched = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!matched) {
            throw new AuthenticationException();
        }

        this.user = user;
    }

    public User getCurrentUser() {
        return user;
    }

    public void logout() {
        user = null;
    }
}
