package com.sendiri.mwallet_user.service;

public interface UserService {

    public void register (String phoneNo);
    public void verify (String phoneNo, String otp, String PIN);
    public Object login (String phoneNo, String PIN);
    public Object getProfile(String auth);

}
