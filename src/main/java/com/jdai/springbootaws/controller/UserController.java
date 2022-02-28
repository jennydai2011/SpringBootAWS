package com.jdai.springbootaws.controller;

import com.jdai.springbootaws.entity.User;
import com.jdai.springbootaws.exception.ResourceNotFoundException;
import com.jdai.springbootaws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //get users
    @GetMapping
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    //get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") long userId) {
        return this.userRepository.findById(userId).orElseThrow( () -> new ResourceNotFoundException("uesr not found with id:"+ userId));
    }

    //create user
    @PostMapping
    public User createUser(@RequestBody User user){
        return this.userRepository.save(user);

    }

    //update user
    @PutMapping("/{id}")
    public User udpateUser(@RequestBody User user, @PathVariable("id") long userId) {
        User existingUser = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("uesr not found with id:" + userId));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        return this.userRepository.save(existingUser);
    }


    //delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long userId){
        User existingUser = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("uesr not found with id:" + userId));
        this.userRepository.delete(existingUser);
        return ResponseEntity.ok().build();
    }

}
