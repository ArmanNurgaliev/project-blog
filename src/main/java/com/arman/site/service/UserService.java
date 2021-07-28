package com.arman.site.service;

import com.arman.site.models.Role;
import com.arman.site.models.User;
import com.arman.site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found");
        return user;
    }

    public boolean register(User user) {
        User userFromBD = userRepository.findByUsername(user.getUsername());
        if (userFromBD != null)
            return false;

        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);

        return true;
    }


    public User getUserById(Long user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    public void updateProfile(Long id, String username, String email, String about) {
        User user = userRepository.findById(id).orElse(null);

        user.setUsername(username);
        user.setEmail(email);
        user.setAbout(about);

        userRepository.save(user);
    }
}
