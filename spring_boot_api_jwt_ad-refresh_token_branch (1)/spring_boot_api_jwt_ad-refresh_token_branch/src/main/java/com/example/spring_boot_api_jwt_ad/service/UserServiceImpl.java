package com.example.spring_boot_api_jwt_ad.service;

import com.example.spring_boot_api_jwt_ad.authen.UserPrincipal;
import com.example.spring_boot_api_jwt_ad.entity.User;
import com.example.spring_boot_api_jwt_ad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public UserPrincipal findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal();

        if (null != user) {

            Set<String> authorities = new HashSet<>();

            if (null != user.getRoles())

                user.getRoles().forEach(r -> {
                authorities.add(r.getRoleKey());
                r.getPermissions().forEach(
                        p -> authorities.add(p.getPermissionKey()));
            });

            userPrincipal.setUserId(user.getId());
            userPrincipal.setUsername(user.getUsername());
            userPrincipal.setPassword(user.getPassword());
            userPrincipal.setAuthorities(authorities);

        }

        return userPrincipal;

    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User deleteUser(Long userId) {

        Optional<User> retrievedUser = userRepository.findById(userId);
        if(retrievedUser==null)
            try {
                throw new Exception("User not found");
            } catch (Exception e) {
                e.printStackTrace();
            }
        userRepository.deleteById(userId);
        return retrievedUser.get();
    }
}