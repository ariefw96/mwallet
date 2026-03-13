package com.sendiri.mwallet_user.service.impl;

import com.sendiri.mwallet_repo.entity.UserEntity;
import com.sendiri.mwallet_repo.utils.RedisUtil;
import com.sendiri.mwallet_user.repo.UserRepository;
import com.sendiri.mwallet_user.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactServiceImpl implements ContactService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Object getListContact(String auth) {

        String phoneNo = redisUtil.get(auth, UserEntity.class).getPhoneNo();


    }
}
