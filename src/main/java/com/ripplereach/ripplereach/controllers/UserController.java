package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.utilities.UsernameGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/generate-usernames")
    public ResponseEntity<List<String>> generateUsernames(@RequestParam(defaultValue = "10") int count) {
        List<String> usernames = UsernameGenerator.generateUsernames(count);
        return ResponseEntity.ok(usernames);
    }
}
