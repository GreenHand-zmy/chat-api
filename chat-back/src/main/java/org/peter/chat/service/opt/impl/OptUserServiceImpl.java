package org.peter.chat.service.opt.impl;

import org.peter.chat.domain.vo.common.UserCommonVO;
import org.peter.chat.entity.UserEntity;
import org.peter.chat.mapper.UserMapper;
import org.peter.chat.service.opt.OptUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptUserServiceImpl implements OptUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserCommonVO> queryAll() {
        List<UserEntity> userEntityList = userMapper.selectList(null);

        return userEntityList
                .stream()
                .map(UserCommonVO::transTo)
                .collect(Collectors.toList());
    }
}
