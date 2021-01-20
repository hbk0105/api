package com.rest.api.service;

import com.rest.api.domain.Role;
import com.rest.api.domain.User;
import com.rest.api.repository.RoleRepository;
import com.rest.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User singUp(User.Request userReqDto){
        User user = userRepository.findByEmail(userReqDto.getEmail());
        if(user == null){
            user = new User();
            user.setEmail(userReqDto.getEmail());
            user.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
            user.setFirstName(userReqDto.getFirstName());
            user.setLastName(userReqDto.getLastName());
            user.setEnabled(true);
            // https://java119.tistory.com/52
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime date  = startDate.plusMinutes(10);
            user.setMailCertificationtDate(date);

            Role role = roleRepository.findByName("ROLE_USER");
            user.setRoles(new ArrayList<>(Arrays.asList(role)));
            user = userRepository.save(user);

        }
        return user;
    }

    public Optional<User> findById(Long id){
        // Optional
        // https://advenoh.tistory.com/15

        return userRepository.findById(id);
    }

    public User findByEmail(String email){
        return  userRepository.findByEmail(email);
    }

    public void save(User user){
        userRepository.save(user);
    }

}
