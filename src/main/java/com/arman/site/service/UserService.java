package com.arman.site.service;

import com.arman.site.models.FileDB;
import com.arman.site.models.Post;
import com.arman.site.models.Role;
import com.arman.site.models.User;
import com.arman.site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Value("${upload.path.profile}")
    private String uploadPath;
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

    public boolean register(User user, MultipartFile file) throws IOException {
        User userFromBD = userRepository.findByUsername(user.getUsername());
        if (userFromBD != null)
            return false;

        savePhoto(user, file);

        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);

        return true;
    }

    private void savePhoto(User user, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String resultName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultName));
            user.setPhoto("img/" + resultName);
        }
        else
            user.setPhoto("uploads/photo-profile.jpg");
    }


    public User getUserById(Long user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    public void updateProfile(Long id, String username, String email, String about) {
        User user = userRepository.findById(id).orElse(null);
        if (username != null && username.length() > 0)
            user.setUsername(username);
        if (email != null && email.length() > 0)
            user.setEmail(email);
        if (about != null && about.length() > 0)
            user.setAbout(about);

        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void updateUser(Long user_id, String name, String email, Map<String, String> form) {
        User user = userRepository.findById(user_id).orElse(null);

        if(name != null && name.length() != 0)
            user.setUsername(name);
        if(email != null && email.length() != 0)
            user.setEmail(email);
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key))
                user.getRoles().add(Role.valueOf(key));
        }

        userRepository.save(user);
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
