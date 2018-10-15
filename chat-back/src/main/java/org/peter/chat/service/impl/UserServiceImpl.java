package org.peter.chat.service.impl;

import org.peter.chat.mapper.UserMapper;
import org.peter.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
}
