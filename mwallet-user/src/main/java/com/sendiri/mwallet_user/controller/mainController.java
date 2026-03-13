package com.sendiri.mwallet_user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class mainController {

    @GetMapping("/hello")
    public ResponseEntity<Object> hellow(){
        return new ResponseEntity<>("OK SOBAT USER", HttpStatus.OK);
    }
}
