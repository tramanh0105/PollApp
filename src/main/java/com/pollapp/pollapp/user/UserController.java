package com.pollapp.pollapp.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/users")
public class UserController {
    private UserRepo userRepo;
    public UserController( UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/test")
    public String test() {
        return "Test successful";
    }
/*
    @GetMapping("")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = this.userRepo.findAll();
        System.out.println("ahihi");
        return new ResponseEntity<>(users, null, HttpStatus.CREATED);
    }

    public List<User> getUsers() {
        System.out.println("lol");
        return this.userRepo.findAll();
    }

    @PostMapping("")
    public User createUser(@RequestBody User user) {
        System.out.println("created");

        return this.userRepo.save(user);
    }*/
}

