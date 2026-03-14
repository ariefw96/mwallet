package com.sendiri.user.controller;


import com.sendiri.repo.dto.request.UserRequestDto;
import com.sendiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/customer")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @RequestBody UserRequestDto request
    ) {
        userService.register(request.getPhoneNo());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> verify(
            @RequestBody UserRequestDto request
    ) {
        userService.verify(request.getPhoneNo(), request.getOtp(), request.getPin());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody UserRequestDto request
    ) {

        return new ResponseEntity<>(userService.login(request.getPhoneNo(), request.getPin()), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(
            @RequestHeader String auth
    ) {
        return new ResponseEntity<>(userService.getProfile(auth), HttpStatus.OK);
    }


}
