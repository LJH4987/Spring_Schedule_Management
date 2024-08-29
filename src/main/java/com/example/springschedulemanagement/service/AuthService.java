package com.example.springschedulemanagement.service;

import com.example.springschedulemanagement.dto.AuthUserDTO;


public interface AuthService {

    AuthUserDTO register(AuthUserDTO authUserDTO);

    String login(String email, String password);

}
