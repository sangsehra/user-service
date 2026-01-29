package com.todoapp.todoapp.Controller;

import com.entities.common.common_entities.UserService.UserDetails;
import com.todoapp.todoapp.service.UsersService;
import com.todoapp.todoapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public String createUser(@RequestBody UserDetails user){
      usersService.createUser(user);
      return JwtUtil.generateToken(user.getName());
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody UserDetails user){
        return usersService.userlogin(user);
    }

    @GetMapping("/users")
    public List<UserDetails> getUserNames(){
        return usersService.getUsers();
    }

}
