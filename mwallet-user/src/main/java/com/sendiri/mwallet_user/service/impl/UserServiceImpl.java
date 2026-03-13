package com.sendiri.mwallet_user.service.impl;

import com.sendiri.mwallet_repo.constant.GenericConstant;
import com.sendiri.mwallet_repo.entity.OtpEntity;
import com.sendiri.mwallet_repo.entity.UserEntity;
import com.sendiri.mwallet_repo.utils.RandomUtil;
import com.sendiri.mwallet_repo.utils.RedisUtil;
import com.sendiri.mwallet_user.repo.OTPRepository;
import com.sendiri.mwallet_user.repo.UserRepository;
import com.sendiri.mwallet_user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void register(String phoneNo) {
        userRepository.findByPhoneNo(phoneNo)
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nomor handphone telah terdaftar");
                });
        OtpEntity otp = new OtpEntity();
        otp.setOtpId(UUID.randomUUID());
        otp.setType(GenericConstant.OTP_REGISTER);
        otp.setPhoneNo(phoneNo);
        otp.setCode(RandomUtil.random6Digit());
        otpRepository.save(otp);

        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setPhoneNo(phoneNo);
        userRepository.save(user);
    }

    @Override
    public void verify(String phoneNo, String otp, String PIN) {
        var otpEntity = otpRepository.findFirstByPhoneNoAndIsUsedOrderByCreatedAtDesc(phoneNo, false).orElse(null);
        if(otpEntity != null){
            if(otpEntity.getCode().equals(otp)){
                otpEntity.setIsUsed(true);
                otpEntity.setUsedAt(new Date());
                otpRepository.save(otpEntity);

                UserEntity user = userRepository.findByPhoneNo(phoneNo)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan"));
                user.setIsVerified(true);
                user.setIsActive(true);
                String hashedPin = encryptPin(PIN);
                log.info("hashedPIN {}", hashedPin);
                user.setPin(hashedPin);
                user.setUpdatedAt(new Date());
                userRepository.save(user);
            }else{
                log.error("OTP tidak sesuai");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP tidak sesuai");
            }
        }else{
            log.error("OTP tidak ditemukan untuk nomor = {}", phoneNo);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP Tidak ditemukan");
        }

    }

    @Override
    public Object login(String phoneNo, String PIN) {
        UserEntity user = userRepository.findByPhoneNo(phoneNo).orElse(null);
        if(user == null){
            log.error("user tidak ditemukan");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User tidak ditemukan");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isMatch = encoder.matches(PIN, user.getPin());
        if(isMatch){
            if(!user.getIsActive()){
                log.error("user belum aktif");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User belum aktif");
            }
            if(!user.getIsVerified()){
                log.error("user belum verifikasi");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mohon verifikasi terlebih dahulu");
            }
            ObjectMapper mapper = new ObjectMapper();
            //set ke redis
            String auth = UUID.randomUUID().toString();
            redisUtil.set(auth, user, 3600);
            return Map.of("auth", auth);
        }
        log.error("nomor HP / pin tidak sesuai");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "USERNAME / PIN TIDAK SESUAI");
    }

    @Override
    public Object getProfile(String auth) {
        return redisUtil.get(auth, UserEntity.class);
    }


    private String encryptPin(String pin){
        log.info("raw PIN :: {}", pin);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pin);
    }
}
