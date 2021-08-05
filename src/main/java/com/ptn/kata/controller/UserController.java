package com.ptn.kata.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.ptn.kata.exception.ResourceNotFoundException;
import com.ptn.kata.model.User;
import com.ptn.kata.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found, id :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
 
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
        
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetial) {
        User user = userRepository.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User not found, id :: " + userId));
        user.setEmailId(userDetial.getEmailId());
        user.setFirstName(userDetial.getFirstName());
        user.setLastName(userDetial.getLastName());
        userRepository.save(user);
        return ResponseEntity.ok().build(); 
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUserById(@PathVariable(value = "id") Long userId) {

        Map<String, Boolean> response = new HashMap<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found, id :: " + userId));
        userRepository.delete(user);
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
