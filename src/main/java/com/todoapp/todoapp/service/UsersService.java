package com.todoapp.todoapp.service;

import com.entities.common.common_entities.UserService.UserDetails;
import com.todoapp.todoapp.Entities.Users;
import com.todoapp.todoapp.Repository.UserRepository;
import com.todoapp.todoapp.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    public void createUser(UserDetails user){
        Users userEntity = createUserEntity(user);
        userRepository.save(userEntity);
        log.info("user created");
    }

    public Users createUserEntity(UserDetails user){
        Users userRequest = new Users();
        userRequest.setName(user.getName());
        userRequest.setPassword(passwordUtil.encryptPassword(user.getPassword()));
        return userRequest;
    }

    public String userlogin(UserDetails user){
        Optional<Users> optionalUser = userRepository.findByName(user.getName());
        Users existingUser = null;
        if(optionalUser.isPresent()){
            existingUser =optionalUser.get();
        }
        if(passwordUtil.passwordChecker(existingUser.getPassword(),user.getPassword())){
            return "user Logged-in";
        }
        return "Log-in failed";
    }

    public Users findByUsername(String username) {
        return userRepository.findByName(username).get();
    }

    public List<UserDetails> getUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDetails(user.getName(), null))
                .toList();
    }
}
