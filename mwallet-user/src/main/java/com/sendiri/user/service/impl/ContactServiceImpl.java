package com.sendiri.user.service.impl;

import com.sendiri.repo.entity.UserEntity;
import com.sendiri.repo.utils.RedisUtil;
import com.sendiri.user.repo.UserRepository;
import com.sendiri.user.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactServiceImpl implements ContactService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Object getListContact(String auth) {

        String phoneNo = redisUtil.get(auth, UserEntity.class).getPhoneNo();
        List<UserEntity> users = userRepository.findAll()
                .stream()
                .filter(us -> !us.getPhoneNo().equals(phoneNo)).toList();
        return users;

    }
}
