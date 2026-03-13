package com.sendiri.mwallet_user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/contact")
public class ContactController {

    @GetMapping
    public ResponseEntity<Object> getListContact(
            @RequestHeader String auth
    ){

    }

}
