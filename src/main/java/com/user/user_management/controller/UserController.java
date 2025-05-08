package com.user.user_management.controller;

import com.user.user_management.model.User;
import com.user.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin("https://myusermanager.vercel.app/")
//@RequestMapping(path = "/api")
public class UserController {


    @Autowired
    private UserRepository userRepository;

    UserController(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @GetMapping("/users")
    List<User>getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    User saveUser(@RequestBody User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save user.");
        }
    }

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Data Found"));
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException(String.valueOf(id));
        }
        userRepository.deleteById(id);
        return  "User with id "+id+" has been deleted success.";
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User updatedUser , @PathVariable Long id){
        return userRepository.findById(id)
                .map(user-> {
                    user.setName(updatedUser.getName());
                    user.setProfession(updatedUser.getProfession());
                    user.setEmail(updatedUser.getEmail());
                    user.setDescription(updatedUser.getDescription());
                    return userRepository.save(user);
                }).orElseThrow(()->new RuntimeException(String.valueOf(id)));
    }
}
