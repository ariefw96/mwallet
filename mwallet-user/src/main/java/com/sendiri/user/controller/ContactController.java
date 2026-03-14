package com.sendiri.user.controller;

import com.sendiri.user.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<Object> getListContact(
            @RequestHeader(name = "auth") String auth
    ){
        return new ResponseEntity<>(contactService.getListContact(auth), HttpStatus.OK);
    }

}
